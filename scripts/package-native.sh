#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

VERSION="${1:-}"
if [[ -z "$VERSION" ]]; then
  VERSION="$(python3 - <<'PY'
import re
from pathlib import Path
text = re.sub(r"<parent>.*?</parent>", "", Path("pom.xml").read_text(), flags=re.S)
match = re.search(r"<version>([^<]+)</version>", text)
print(match.group(1) if match else "")
PY
)"
fi

if ! command -v jpackage >/dev/null 2>&1; then
  echo "需要 JDK 21+ 的 jpackage（当前 PATH 没有）" >&2
  exit 1
fi

if [[ ! -f dist/utils.jar ]]; then
  "$ROOT/scripts/package.sh" "$VERSION"
fi

python3 "$ROOT/scripts/gen-icon.py"
ICON="$ROOT/packaging/icon.png"

OS="$(uname -s | tr '[:upper:]' '[:lower:]')"
TYPE="${2:-}"
if [[ -z "$TYPE" ]]; then
  case "$OS" in
    linux*) TYPE="app-image" ;;
    darwin*) TYPE="dmg" ;;
    mingw*|msys*|cygwin*) TYPE="exe" ;;
    *) TYPE="app-image" ;;
  esac
fi

STAGE="$ROOT/dist/jpackage-input"
rm -rf "$STAGE" "$ROOT/dist/native"
mkdir -p "$STAGE" "$ROOT/dist/native"
cp "$ROOT/dist/utils.jar" "$STAGE/utils.jar"

MAIN_CLASS="org.springframework.boot.loader.launch.JarLauncher"
if unzip -p "$STAGE/utils.jar" META-INF/MANIFEST.MF | grep -q 'org.springframework.boot.loader.JarLauncher'; then
  MAIN_CLASS="org.springframework.boot.loader.JarLauncher"
fi

ARGS=(
  --name Utils
  --app-version "${VERSION%%-*}"
  --dest "$ROOT/dist/native"
  --input "$STAGE"
  --main-jar utils.jar
  --main-class "$MAIN_CLASS"
  --java-options "-Dutils.desktop=true"
  --arguments "--desktop"
  --description "Java / 前端 / 日常办公工具台"
  --vendor "mengzhihua"
  --type "$TYPE"
)
if [[ -f "$ICON" ]]; then
  ARGS+=(--icon "$ICON")
fi

echo "==> jpackage $TYPE ($OS)"
jpackage "${ARGS[@]}"

echo
echo "原生包已生成：$ROOT/dist/native"
ls -la "$ROOT/dist/native"
echo
echo "Windows：在 Windows + JDK 21 上执行  scripts\\package-native.bat"
echo "macOS：在 Mac + JDK 21 上执行  ./scripts/package-native.sh ${VERSION} dmg"
echo "Linux：本脚本可生成 app-image 或 deb（./scripts/package-native.sh ${VERSION} deb）"

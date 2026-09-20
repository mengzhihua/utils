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
ICON_PNG="$ROOT/packaging/icon.png"
ICON_ICNS="$ROOT/packaging/icon.icns"

OS="$(uname -s | tr '[:upper:]' '[:lower:]')"
ARCH_RAW="$(uname -m | tr '[:upper:]' '[:lower:]')"
case "$ARCH_RAW" in
  arm64|aarch64) ARCH="arm64" ;;
  x86_64|amd64) ARCH="x64" ;;
  *) ARCH="$ARCH_RAW" ;;
esac

TYPE="${2:-}"
if [[ -z "$TYPE" ]]; then
  case "$OS" in
    linux*) TYPE="app-image" ;;
    darwin*) TYPE="dmg" ;;
    mingw*|msys*|cygwin*) TYPE="exe" ;;
    *) TYPE="app-image" ;;
  esac
fi

case "$OS" in
  darwin*) PLATFORM="macos" ;;
  linux*) PLATFORM="linux" ;;
  mingw*|msys*|cygwin*) PLATFORM="windows" ;;
  *) PLATFORM="$OS" ;;
esac

make_icns() {
  local png="$1" icns="$2"
  command -v sips >/dev/null 2>&1 || return 1
  command -v iconutil >/dev/null 2>&1 || return 1
  local set="${icns%.icns}.iconset"
  rm -rf "$set"
  mkdir -p "$set"
  local sz
  for sz in 16 32 128 256 512; do
    sips -z "$sz" "$sz" "$png" --out "$set/icon_${sz}x${sz}.png" >/dev/null
    sips -z "$((sz * 2))" "$((sz * 2))" "$png" --out "$set/icon_${sz}x${sz}@2x.png" >/dev/null
  done
  iconutil -c icns "$set" -o "$icns"
  rm -rf "$set"
}

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

if [[ "$PLATFORM" == "macos" ]]; then
  ARGS+=(--mac-package-identifier com.mengzhihua.utils --mac-package-name Utils)
  if make_icns "$ICON_PNG" "$ICON_ICNS" && [[ -f "$ICON_ICNS" ]]; then
    ARGS+=(--icon "$ICON_ICNS")
  elif [[ -f "$ICON_PNG" ]]; then
    ARGS+=(--icon "$ICON_PNG")
  fi
elif [[ -f "$ICON_PNG" ]]; then
  ARGS+=(--icon "$ICON_PNG")
fi

echo "==> jpackage $TYPE ($PLATFORM-$ARCH)"
jpackage "${ARGS[@]}"

python3 "$ROOT/scripts/stage-native.py" "$ROOT/dist/native" "$VERSION" "$PLATFORM" "$ARCH"

echo
echo "原生包已生成：$ROOT/dist/native"
ls -la "$ROOT/dist/native"
echo
echo "产物按芯片命名，例如："
echo "  utils-${VERSION}-macos-arm64.dmg   # Apple Silicon"
echo "  utils-${VERSION}-macos-x64.dmg     # Intel Mac"
echo "  utils-${VERSION}-windows-x64.zip"
echo "  utils-${VERSION}-linux-x64.zip"
echo
echo "Windows：在 Windows + JDK 21 上执行  scripts\\package-native.bat"
echo "macOS Apple Silicon：./scripts/package-native.sh ${VERSION} dmg"
echo "macOS Intel：同一脚本在 x86_64 机器上会生成 macos-x64"
echo "Linux：本脚本可生成 app-image 或 deb（./scripts/package-native.sh ${VERSION} deb）"

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
if [[ -z "$VERSION" ]]; then
  echo "无法读取版本号，请传入：./scripts/package.sh 1.0.0" >&2
  exit 1
fi

if [[ ! -x ./mvnw ]]; then
  chmod +x ./mvnw
fi

echo "==> 构建前端控制台"
if [[ ! -d frontend/node_modules ]]; then
  (cd frontend && npm ci)
else
  (cd frontend && npm install --no-fund --no-audit)
fi
(cd frontend && npm run build)

echo "==> 打包可执行 JAR"
./mvnw -B -DskipTests package

OUT_DIR="$ROOT/dist"
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"
JAR_NAME="utils-${VERSION}.jar"
cp "$ROOT/target/utils.jar" "$OUT_DIR/$JAR_NAME"
cp "$ROOT/target/utils.jar" "$OUT_DIR/utils.jar"
(
  cd "$OUT_DIR"
  if command -v sha256sum >/dev/null 2>&1; then
    sha256sum "$JAR_NAME" > "${JAR_NAME}.sha256"
  else
    shasum -a 256 "$JAR_NAME" > "${JAR_NAME}.sha256"
  fi
)

echo
echo "成品已生成："
echo "  $OUT_DIR/$JAR_NAME"
echo "  $OUT_DIR/${JAR_NAME}.sha256"
echo
echo "运行："
echo "  java -jar $OUT_DIR/$JAR_NAME"
echo "  浏览器打开 http://localhost:8080/"

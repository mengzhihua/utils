#!/usr/bin/env bash
set -euo pipefail
DIR="$(cd "$(dirname "$0")" && pwd)"
if [[ -f "$DIR/utils.jar" ]]; then
  JAR="$DIR/utils.jar"
elif [[ -f "$DIR/../dist/utils.jar" ]]; then
  JAR="$DIR/../dist/utils.jar"
elif [[ -f "$DIR/../target/utils.jar" ]]; then
  JAR="$DIR/../target/utils.jar"
else
  echo "找不到 utils.jar，请先运行 ./scripts/package.sh" >&2
  exit 1
fi
echo "启动桌面模式（办公工作台）…"
exec java -Dutils.desktop=true -jar "$JAR" --desktop

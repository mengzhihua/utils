#!/usr/bin/env bash
# Resolve the next release version.
# Usage: next-version.sh [auto|patch|minor|major|x.y.z]
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

MODE="${1:-auto}"

pom_version() {
  python3 - <<'PY'
import re
from pathlib import Path
text = re.sub(r"<parent>.*?</parent>", "", Path("pom.xml").read_text(), flags=re.S)
match = re.search(r"<version>([^<]+)</version>", text)
print((match.group(1) if match else "").removesuffix("-SNAPSHOT"))
PY
}

latest_tag() {
  git tag -l 'v*' --sort=-v:refname | head -1 | sed 's/^v//'
}

parse() {
  python3 - "$1" "$2" <<'PY'
import sys
raw, part = sys.argv[1], sys.argv[2]
nums = raw.split(".")
while len(nums) < 3:
    nums.append("0")
major, minor, patch = (int(nums[0]), int(nums[1]), int(nums[2].split("-")[0]))
if part == "major":
    major, minor, patch = major + 1, 0, 0
elif part == "minor":
    minor, patch = minor + 1, 0
elif part == "patch":
    patch += 1
print(f"{major}.{minor}.{patch}")
PY
}

newer_than() {
  python3 - "$1" "$2" <<'PY'
import sys
def key(v):
    p = v.split(".")
    while len(p) < 3:
        p.append("0")
    return tuple(int(x.split("-")[0]) for x in p[:3])
print("yes" if key(sys.argv[1]) > key(sys.argv[2]) else "no")
PY
}

POM="$(pom_version)"
LAST="$(latest_tag)"
if [[ -z "$LAST" ]]; then
  LAST="0.0.0"
fi

case "$MODE" in
  ""|auto)
    if [[ -n "$POM" && "$(newer_than "$POM" "$LAST")" == "yes" ]]; then
      echo "$POM"
    else
      parse "$LAST" patch
    fi
    ;;
  patch|minor|major)
    parse "$LAST" "$MODE"
    ;;
  [0-9]*.[0-9]*.[0-9]*)
    echo "$MODE"
    ;;
  *)
    echo "unsupported version mode: $MODE" >&2
    exit 1
    ;;
esac

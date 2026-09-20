#!/usr/bin/env bash
# Rewrite this project's <version> in pom.xml (does not touch the parent).
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
VERSION="${1:-}"
if [[ -z "$VERSION" ]]; then
  echo "usage: set-version.sh 1.1.0" >&2
  exit 1
fi
python3 - "$ROOT/pom.xml" "$VERSION" <<'PY'
import re
import sys
from pathlib import Path
path = Path(sys.argv[1])
version = sys.argv[2]
text = path.read_text()
def repl(match):
    return match.group(1) + version + match.group(3)
updated, count = re.subn(
    r"(<parent>.*?</parent>.*?<version>)([^<]+)(</version>)",
    repl,
    text,
    count=1,
    flags=re.S,
)
if count != 1:
    raise SystemExit("failed to update project version in pom.xml")
path.write_text(updated)
print(version)
PY

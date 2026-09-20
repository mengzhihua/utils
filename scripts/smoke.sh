#!/usr/bin/env bash
set -euo pipefail

BASE="${1:-http://127.0.0.1:8080}"

check() {
  local name="$1" url="$2" needle="$3"
  local body
  body="$(curl -fsS -L -m 15 "$url")"
  if ! grep -Fq "$needle" <<<"$body"; then
    echo "FAIL $name expected=$needle" >&2
    echo "$body" | head -c 240 >&2
    echo >&2
    return 1
  fi
  echo "OK $name"
}

check health "$BASE/actuator/health" '"status":"UP"'
check home "$BASE/" 'Java Utils'
check mask "$BASE/api/utils/string/mask-phone?phone=13812345678" '138****5678'
check snowflake "$BASE/api/utils/id/snowflake" '"idStr"'
check pit "$BASE/api/utils/pit?income=30000&month=1" '750'
check invoice "$BASE/api/utils/invoice-vat?amount=113&rate=13" '100'
check desktop "$BASE/api/utils/desktop-url?port=18765" '/#/office'
check docs "$BASE/v3/api-docs" 'openapi'

echo SMOKE_OK

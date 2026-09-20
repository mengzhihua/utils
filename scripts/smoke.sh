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
check info "$BASE/actuator/info" '"artifact":"utils"'
check home "$BASE/" 'Java Utils'
check mask "$BASE/api/utils/string/mask-phone?phone=13812345678" '138****5678'
check snowflake "$BASE/api/utils/id/snowflake" '"idStr"'
check idcard "$BASE/api/utils/idcard/parse?idNo=110101199003078937" '"valid":true'
check jwt "$BASE/api/utils/jwt?subject=ada" '"token"'
check gbvat "$BASE/api/utils/gb-vat?value=GB%20980%207806%2084" '"valid":true'
check iso11649 "$BASE/api/utils/iso11649?value=RF18%205390%200754%207034" 'RF18539007547034'
check crc "$BASE/api/utils/crc16-x25?text=123456789" '906e'
check pit "$BASE/api/utils/pit?income=30000&month=1" '750'
check invoice "$BASE/api/utils/invoice-vat?amount=113&rate=13" '100'
check desktop "$BASE/api/utils/desktop-url?port=18765" '/#/office'
check docs "$BASE/v3/api-docs" 'openapi'
check swagger "$BASE/swagger-ui/index.html" 'Swagger'

echo SMOKE_OK

#!/usr/bin/env bash
set -euo pipefail

base_url="${1:-http://localhost:8080}"
large_payload="$(mktemp)"
small_payload="$(mktemp)"
trap 'rm -f "$large_payload" "$small_payload"' EXIT

jq -n '
  {
    dataset_name: "BenchmarkSmall",
    jurisdictions: ["EU"],
    business_role: "controller",
    processing_purposes: ["analytics"],
    processing_activities: ["storage"],
    fields: [{name: "email", type: "string"}]
  }
' > "$small_payload"

jq -n '
  {
    dataset_name: "BenchmarkDataset",
    jurisdictions: ["EU"],
    business_role: "controller",
    processing_purposes: ["analytics"],
    processing_activities: ["storage", "analytics"],
    fields: [range(0; 500) | {name: ("field_" + tostring), type: "string"}]
  }
' > "$large_payload"

echo "Target: $base_url/v1/datasets/analyze"
echo "Single small request:"
/usr/bin/time -p curl --fail-with-body -sS -o /dev/null \
  -X POST "$base_url/v1/datasets/analyze" \
  -H 'Content-Type: application/json' \
  -H 'X-Correlation-ID: benchmark-small' \
  --data-binary "@$small_payload"

echo "Single 500-field request:"
/usr/bin/time -p curl --fail-with-body -sS -o /dev/null \
  -X POST "$base_url/v1/datasets/analyze" \
  -H 'Content-Type: application/json' \
  -H 'X-Correlation-ID: benchmark-large' \
  --data-binary "@$large_payload"

echo "Twenty concurrent 500-field requests:"
/usr/bin/time -p bash -c '
  for i in $(seq 1 20); do
    curl --fail-with-body -sS -o /dev/null \
      -X POST "$0/v1/datasets/analyze" \
      -H "Content-Type: application/json" \
      -H "X-Correlation-ID: benchmark-concurrent-$i" \
      --data-binary "@$1" &
  done
  wait
' "$base_url" "$large_payload"

scan_count="${SCAN_COUNT:-10000}"
concurrency="${BENCHMARK_CONCURRENCY:-16}"
echo "$scan_count sequentially numbered scan requests at concurrency $concurrency:"
/usr/bin/time -p bash -c '
  seq "$0" | xargs -P "$3" -n 1 sh -c '\''
    curl --fail-with-body -sS -o /dev/null \
      -X POST "$0/v1/datasets/analyze" \
      -H "Content-Type: application/json" \
      -H "X-Correlation-ID: benchmark-10k" \
      --data-binary "@$1"
  '\'' "$1" "$2"
' "$scan_count" "$base_url" "$small_payload" "$concurrency"

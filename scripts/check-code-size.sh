#!/usr/bin/env bash
set -euo pipefail

limit_fail=300
failed=0

while IFS= read -r file; do
  case "$file" in
    */generated/*|*/target/*)
      continue
      ;;
  esac

  lines=$(wc -l < "$file")
  if (( lines >= limit_fail )); then
    printf 'ERROR: %s has %d lines; non-generated code must stay under %d lines.\n' "$file" "$lines" "$limit_fail" >&2
    failed=1
  fi
done < <(find src -type f -name '*.java' 2>/dev/null | sort)

exit "$failed"

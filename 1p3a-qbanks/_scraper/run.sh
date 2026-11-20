#!/bin/bash
# run.sh <jsdir> <outdir> [delaySeconds]
# Runs each JS worker in the matching Chrome tab, paced so the server is not hammered.
# Resumable: skips any output file already present and non-empty.
JS="$1"
OUT="$2"
DELAY="${3:-0}"
mkdir -p "$OUT"
FAIL=0
for f in "$JS"/*.js; do
  [ -e "$f" ] || continue
  base=$(basename "$f" .js)
  if [ -s "$OUT/$base.json" ]; then continue; fi
  osascript /tmp/runjs.applescript "$f" "1point3acres.com/interview/problems" \
    </dev/null 2>/dev/null > "$OUT/$base.json"
  if [ ! -s "$OUT/$base.json" ]; then
    echo "EMPTY -> $base"
    FAIL=$((FAIL+1))
  fi
  if [ "$DELAY" != "0" ]; then osascript -e "delay $DELAY" </dev/null 2>/dev/null; fi
done
echo "FINISHED $JS (empty: $FAIL)"

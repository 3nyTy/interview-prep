#!/bin/bash
# Full scrape: initial batches, then retry rounds until nothing is missing.
# Results accumulate (one directory per round) so a 403 in a later round never
# destroys a record captured earlier. Stops when three rounds make no progress.
cd /tmp/qb || exit 1

DELAY=3
MAXROUND=16
STALE=0
PREV=-1

echo "=== ROUND 0: initial batches ==="
./run.sh /tmp/qb/detail /tmp/qb/res "$DELAY"

ROUND=0
while [ "$ROUND" -lt "$MAXROUND" ]; do
  ROUND=$((ROUND + 1))
  python3 gen.py merge
  GOOD=$(python3 -c "import json;print(len(json.load(open('/tmp/qb/records.json'))))")
  MISSING=$(python3 -c "import json;print(len(json.load(open('/tmp/qb/missing.json'))))")
  echo "=== ROUND $ROUND: good=$GOOD missing=$MISSING ==="

  if [ "$MISSING" -eq 0 ]; then
    echo "ALL QUESTIONS CAPTURED"
    break
  fi
  if [ "$GOOD" -le "$PREV" ]; then
    STALE=$((STALE + 1))
    echo "  no progress ($STALE consecutive rounds)"
    if [ "$STALE" -ge 3 ]; then
      echo "STOPPING: no progress across 3 rounds; remaining are still blocked"
      break
    fi
  else
    STALE=0
  fi
  PREV=$GOOD

  python3 gen.py retry
  ./run.sh /tmp/qb/retry "/tmp/qb/res/round_$ROUND" "$DELAY"
done

python3 gen.py merge
echo "=== SCRAPE DONE ==="

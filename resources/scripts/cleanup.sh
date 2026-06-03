#!/usr/bin/env bash
set -euo pipefail

WORKSPACE="${WORKSPACE:-.}"
KEEP_DAYS="${KEEP_DAYS:-7}"

echo "Cleaning workspace: $WORKSPACE"
find "$WORKSPACE" -type f \( -name "*.log" -o -name "*.tmp" \) -mtime +"$KEEP_DAYS" -delete 2>/dev/null || true
docker system prune -f --filter "until=24h" 2>/dev/null || true
echo "Cleanup complete"

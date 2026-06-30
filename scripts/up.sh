#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

if ! command -v docker >/dev/null 2>&1; then
  echo "docker is not installed or not in PATH"
  exit 1
fi

if ! docker compose version >/dev/null 2>&1; then
  echo "docker compose plugin is required"
  exit 1
fi

if [ ! -f ".env" ]; then
  cp .env.example .env
  echo "Created .env from .env.example"
fi

set -a
source .env
set +a

docker compose up --build -d

cat <<EOF

Application stack is starting:
- AUTH:         http://localhost:${AUTH_PORT:-8080}
- AUTH Swagger: http://localhost:${AUTH_PORT:-8080}/swagger-ui
- NOTIFICATION: http://localhost:${NOTIFICATION_PORT:-8081}
- Mailpit UI:   http://localhost:8025

Useful commands:
- Stop stack: docker compose down
- Logs:       docker compose logs -f
EOF

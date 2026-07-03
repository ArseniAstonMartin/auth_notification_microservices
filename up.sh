#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT_DIR"

if ! command -v docker >/dev/null 2>&1; then
  echo "Error: docker is not installed or is not available in PATH." >&2
  exit 1
fi

if ! docker compose version >/dev/null 2>&1; then
  echo "Error: Docker Compose plugin is not available." >&2
  exit 1
fi

if [[ ! -f .env ]]; then
  if [[ ! -f .env.example ]]; then
    echo "Error: .env.example is missing, cannot create .env." >&2
    exit 1
  fi

  cp .env.example .env
  echo "Created .env from .env.example"
fi

echo "Building and starting services..."
docker compose up -d --build --force-recreate --remove-orphans

echo
echo "Application is starting."
echo "Useful URLs:"
echo "  Auth API:           http://localhost:8080"
echo "  Swagger UI:         http://localhost:8080/swagger-ui"
echo "  Notification API:    http://localhost:8081"
echo "  Mailpit UI:          http://localhost:8025"
echo
echo "Current container status:"
docker compose ps

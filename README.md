# Auth + Notification

Two Spring Boot services connected through Kafka:

- `auth` handles registration, login, JWT, and users
- `notification` listens for user events and sends email to admins through Mailpit

## Start

```bash
./up.sh
```

The script:

1. creates `.env` from `.env.example` if needed
2. builds both services
3. starts the full stack

## Environment

If you create `.env` manually, set only:

- `JWT_KEY` - JWT secret key, 32+ characters

Everything else has defaults in the compose file and `.env.example`.

## Open

- Auth API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui`
- Mailpit: `http://localhost:8025`

## Quick check

1. Open Swagger UI: `http://localhost:8080/swagger-ui`
2. Call `POST /api/auth/register/user`
3. Open Mailpit: `http://localhost:8025`
4. You should see a new email to the admin addresses

## What to verify

- `auth` should return `200` on `GET /api/admin-emails`
- `notification` should log `Received user event`
- `notification` should log `Sent notification email`
- Mailpit should show the message in the inbox

## Stop

```bash
docker compose down
```

Remove data too:

```bash
docker compose down -v
```

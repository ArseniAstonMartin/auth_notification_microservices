# Микросервисы Auth + Notification

Два микросервиса на Spring Boot, общающиеся через Kafka:

- `auth` — регистрация, логин, JWT-аутентификация, CRUD пользователей
- `notification` — получает события пользователей и отправляет Email админам

Запуск через Docker Compose: PostgreSQL, Kafka, Mailpit и Auth & Notification микросервисы.

## требования

- Docker Desktop
- Bash-оболочка 

## Команда для запука

```bash
./scripts/up.sh
```

1. создаёт `.env` из `.env.example`
2. собирает образы обоих микросервисов
3. запускает все контейнеры 

## Остановка приложения

```bash
docker compose down
```

Чтобы удалить сохранённые данные в БД:

```bash
docker compose down -v
```

## Переменные окружения

Переменные задаются в файле `.env`.

Стартовая команда:

```bash
cp .env.example .env
```

### Обязательные

- `JWT_KEY` — ключ JWT (32+ символов) 
- `INTERNAL_API_KEY` — общий ключ для внутреннего запроса Notification -> Auth

### БД

- `POSTGRES_USER`
- `POSTGRES_PASSWORD`
- `POSTGRES_DB`
- `POSTGRES_PORT` 

### Kafka

- `USER_TOPIC`
- `NOTIFICATION_GROUP_ID`
- `KAFKA_PORT`

### Порты приложений & почта

- `AUTH_PORT`
- `NOTIFICATION_PORT`
- `MAIL_FROM`

## Где используются переменные

- `docker-compose.yaml` читает значения из `.env`
- `auth` получает переменные окружения через Compose:
  - `DB_URL`, `PG_USER`, `PG_PASSWORD`, `JWT_KEY`, `INTERNAL_API_KEY`, `USER_TOPIC`
- `notification` получает переменные окружения через Compose:
  - `KAFKA_BOOTSTRAP_SERVERS`, `USER_TOPIC`, `NOTIFICATION_GROUP_ID`, `AUTH_BASE_URL`, `INTERNAL_API_KEY`, `MAIL_HOST`, `MAIL_PORT`, `MAIL_FROM`

## URL для тестирования

- API AUTH: `http://localhost:8080`
- Swagger UI AUTH: `http://localhost:8080/swagger-ui`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Сервис Notification: `http://localhost:8081`
- Mailpit UI (входящие письма): `http://localhost:8025`

## Быстрая проверка работы

1. Откройте Swagger UI: `http://localhost:8080/swagger-ui`
2. Зарегистрируйтесь с ролью USER: `POST /api/auth/register/user`
3. Залогинтесь: `POST /api/auth/login`
4. Обновите/удалите пользователя от имени
5. Откройте Mailpit: `http://localhost:8025`
6. Письмо-уведомление должно быть доставлено для админа

## Полезные команды

```bash
docker compose logs -f
docker compose logs -f auth
docker compose logs -f notification
docker compose ps
```

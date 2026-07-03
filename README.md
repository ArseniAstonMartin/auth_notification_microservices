# Auth + Notification

Два Spring Boot микросервиса:

- `auth` - регистрация, логин, JWT и управление пользователями
- `notification` - получает события о пользователях и отправляет письма админам через Mailpit

## Как запустить

```bash
./up.sh
```

Скрипт сам:

1. создаёт `.env` из `.env.example`, если файла нет
2. собирает оба сервиса
3. запускает весь стек

## Что открыть

- API `auth`: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui`
- Mailpit: `http://localhost:8025`

## Как проверить

1. Откройте Swagger UI: `http://localhost:8080/swagger-ui`
2. Выполните `POST /api/auth/register/user`
3. Откройте Mailpit: `http://localhost:8025`
4. В Mailpit должно появиться новое письмо

Если письмо не появилось:

- проверьте, что `auth` и `notification` контейнеры запущены
- откройте `GET /api/admin-emails` в `auth`
- посмотрите логи `notification`

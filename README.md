# Auth + Notification

Два Spring Boot микросервиса:

- `auth` - регистрация, логин, JWT и управление юзерами
- `notification` - получает ивенты о пользователях и отправляет письма админам через Mailpit

## Команда для запуска

```bash
./up.sh
```

1. создаёт `.env` из `.env.example`
2. собирает оба сервиса
3. запускает все контейнеры

## Что проверять

- API `auth`: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui`
- Mailpit: `http://localhost:8025`

Примерный алгоритм проверки базового сценария:

1. Выполните `POST /api/auth/register/user`  (С помощью Postman или Swagger UI: `http://localhost:8080/swagger-ui`)
2. Открыть Mailpit: `http://localhost:8025`
3. Т.к. в БД уже есть тестовый админ, письмо должно отправиться и быть видно в Mailpit 


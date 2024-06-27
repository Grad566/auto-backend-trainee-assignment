# auto-backend-trainee-assignment
[![Maintainability](https://api.codeclimate.com/v1/badges/e86c4429a58955cda69a/maintainability)](https://codeclimate.com/github/Grad566/auto-backend-trainee-assignment/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/e86c4429a58955cda69a/test_coverage)](https://codeclimate.com/github/Grad566/auto-backend-trainee-assignment/test_coverage)

## Тестовое задание от Авито

Требования - [здесь](https://github.com/avito-tech/auto-backend-trainee-assignment?tab=readme-ov-file)

Приложение представляет собой HTTP сервис для сокращения URL наподобие Bitly.

## Использования

Развернутое приложение: https://short-link-lbgq.onrender.com

Помимо работы через web-версию, приложение может также работать как JSON API сервис.

Пример запроса:
```
curl -X POST https://short-link-lbgq.onrender.com/ -H "Content-Type: application/json" -d '{ "url":"https://www.example.com", "readablePart":"jack" }'
```
Ответ:
```
{"shortUrl":"https://short-link-lbgq.onrender.com/jack","originUrl":"https://www.example.com","message":"Ссылка успешно создана"}
```

Ключ readablePart можно не указывать, если ссылку не нужно кастомизировать.

## Локальный запуск

1) Если есть Docker и docker-compose.

```
docker-compose up
```
После чего приложение будет доступно по http://localhost:7070/

2) Запуск без докера.

Требования:
 - jdk 21
 - gradle 8.7

```
make dev
```

После чего приложение будет доступно по http://localhost:7070/

В качестве бд будет H2.

Дополнительные команды:
```
// запуск checkStyle
make lint 
```


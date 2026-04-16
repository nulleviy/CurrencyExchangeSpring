 # Currency Exchange REST API

REST API для управления валютами и обменными курсами с поддержкой конвертации через прямой, обратный и кросс-курс (через USD).

## Технологии

- **Java 17+**
- **Spring Boot** (MVC, Data JPA)
- **PostgreSQL** (через Liquibase для миграций)
- **Maven** (сборка)
- **Lombok** (для DTO/моделей)

## Функциональность

### Валюты (`/currencies`)
- `GET /currencies` – список всех валют
- `GET /currency/{code}` – получить валюту по коду (USD, EUR, RUB)
- `POST /currencies` – добавить новую валюту (form-data: `name`, `code`, `sign`)

### Обменные курсы (`/exchangeRates`)
- `GET /exchangeRates` – список всех курсов
- `GET /exchangeRate/{pair}` – курс для пары (например, `USDRUB`)
- `POST /exchangeRates` – добавить курс (form-data: `baseCurrencyCode`, `targetCurrencyCode`, `rate`)
- `PATCH /exchangeRate/{pair}` – обновить курс (form-data: `rate`)

### Конвертация (`/exchange`)
- `GET /exchange?from=USD&to=RUB&amount=100` – конвертация суммы с автоматическим определением курса:
    1. Прямой курс (USD→RUB)
    2. Обратный курс (RUB→USD → 1/rate)
    3. Кросс-курс через USD (USD→A и USD→B)

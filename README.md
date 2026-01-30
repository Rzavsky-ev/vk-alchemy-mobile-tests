# API Automation Tests

## Структура проекта

- `src/test/java/aqa/api/base/` - базовые тестовые классы
- `src/test/java/aqa/api/tests/` - тестовые классы
- `src/test/java/aqa/api/utils/` - утилиты
- `src/test/java/aqa/api/exceptions/` - кастомные исключения

## Запуск тестов

```bash
# Все тесты
mvn clean test

# Конкретный тестовый класс
mvn clean test -Dtest=LoginTest

# Сразу с генерацией Allure отчета
mvn clean test allure:serve
```

# Mobile UI Automation Tests

## Структура проекта

- `src/main/java/org/example/exceptions/` - кастомные исключения

- `src/main/java/org/example/tests/base/` - базовые тестовые классы

- `src/main/java/org/example/tests/alchemy/` - тесты приложения Алхимия

- `src/main/java/org/example/tests/vkVideo/` - тесты приложения VK Video

- `src/main/java/org/example/utils/` - утилиты для работы с приложениями

## Поддерживаемые приложения

### Alchemy (Алхимия)

Тип устройства: Физическое Android устройство

#### Тестируемый функционал:

- Получение подсказок через просмотр рекламы

- Взаимодействие с игровым интерфейсом

- Проверка доступности разделов

### VK Video

Тип устройства: Android эмулятор

#### Тестируемый функционал:

- Воспроизведение видео с обработкой ошибок

- Работа поиска видео (с известными проблемами)

- Обработка невалидных Deeplink ссылок

- Пропуск логина при необходимости

### Настройка окружения

#### Требования

- Java 11+

- Maven 3.6+

- Appium Server 2.0+

- Android SDK

- Для Alchemy: физическое устройство Android

- Для VK Video: Android эмулятор или устройство

### Запуск тестов

- Все тесты

```bash

mvn clean test
```

- Тесты для конкретного приложения

```bash
# Только тесты Alchemy
mvn clean test -Dtest=AlchemyTest
```

```bash
# Только тесты VK Video
mvn clean test -Dtest=VKVideoTest
```



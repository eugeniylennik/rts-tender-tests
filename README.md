## Requirements

1. Selenium (Selenide) v4.10
2. TestNG v6.14
3. Gradle
4. Chromedriver 2.40 (osx)

## Flow run tests

1. Выполнить таску gradle для запуска тестов

> ./gradlew clean test

* Внутри проекта находится файл date.properties где указываются dateFrom и dateTo Дата начала публикации
* Для логирования используется log4j, отображение в консоль и добавление в файл logging.log
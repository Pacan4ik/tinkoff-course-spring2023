![Bot](https://github.com/Pacan4ik/tinkoff-course-spring2023/actions/workflows/bot.yml/badge.svg)
![Scrapper](https://github.com/Pacan4ik/tinkoff-course-spring2023/actions/workflows/scrapper.yml/badge.svg)

# Link Tracker

ФИО: Потехин Андрей Валерьевич

Приложение для отслеживания обновлений контента по ссылкам.
При появлении новых событий отправляется уведомление в Telegram.

Проект написан на `Java 21` с использованием `Spring Boot 3`.

Проект состоит из 2-х приложений:
* Bot
* Scrapper

Для работы требуется БД `PostgreSQL`. Присутствует опциональная зависимость на `Kafka`.

### Запуск "из коробки"
Compose файл (PostgreSQL, Kafka, Zookeeper, Prometheus, Grafana):
``` shell
curl https://raw.githubusercontent.com/Pacan4ik/tinkoff-course-spring2023/main/compose.yml | docker compose -f - up -d 
```
Telegram бот:
``` shell
docker run -d -p 8090:8090 --name bot --network link_tracker_backend -e TELEGRAM_TOKEN=token ghcr.io/pacan4ik/bot
```
Scrapper сервис:
``` shell
docker run -d -p 8080:8080 --name scrapper --network link_tracker_backend ghcr.io/pacan4ik/scrapper
```

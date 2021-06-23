# Changelog

## 6.3.0 (unreleased)

- ...

## 6.2.1 (2021-06-22)

- Compatible with Taiga 6.2.1

## 6.2.0 (2021-06-09)

- Compatible with Taiga 6.2.0

## 6.1.1 (2021-05-18)

- Compatible with Taiga 6.1.1

## 6.1.0 (2021-05-04)

- Update github templates

## 6.0.4 (2021-04-06)

- Add named volumes for rabbit services
- Improve docker configuration with `POSTGRES_PORT`.

## 6.0.3 (2021-02-22)

- Improve docker configuration with `EVENTS_PUSH_BACKEND_URL` and `CELERY_BROKER_URL` variables. Thanks to @ginuerzh.
- Simplify and improve docker configuration

## 6.0.2 (2021-02-15)

Add `ENABLE_SLACK`, `ENABLE_GITHUB_AUTH` and `ENABLE_GITLAB_AUTH` environment variables


## 6.0.1 (2021-02-08)

Adatp to latest


## 6.0.0 (2021-02-02)

### Features

- main `docker-compose.yml` for Taiga installation

- `docker-compose-inits.yml` to run `python manage.py` with docker-compose
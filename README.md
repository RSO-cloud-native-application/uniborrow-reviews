# Uniborrow - Reviews Service

Reviews service for Uniborrow application. It handles reviews for
users and items.

## Prerequesites

A database is needed for the service to work. Docker command:

```bash
docker run -d --name uniborrow-reviews-db -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=reviews -p 5432:5432 postgres:13
```

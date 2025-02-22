#!/bin/bash

DUMP_FILE="/dump/GameData_db-dump.sql"
# Ожидание запуска сервера PostgreSQL
until pg_isready -U $POSTGRES_USER; do
  echo "Ожидание запуска PostgreSQL"
  sleep 1
done

# Проверка существования бд
DB_EXISTS=$(psql -U $POSTGRES_USER -tc "SELECT 1 FROM pg_database WHERE datname = '$POSTGRES_NAME';")
if [[ $DB_EXISTS != 1 ]]; then
  echo "База данных $POSTGRES_NAME не существует. Создание"
  createdb -U $POSTGRES_USER $POSTGRES_NAME
  echo "База данных $POSTGRES_NAME успешно создана"
else
  echo "База данных $POSTGRES_NAME уже существует"
fi

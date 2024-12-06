#!/bin/bash
DUMP_FILE="/docker-entrypoint-initdb.d/dump/GameData_db-dump.sql"
# Ожидание запуска сервера PostgreSQL
until pg_isready -U $POSTGRES_USER; do
  echo "Ожидание запуска PostgreSQL"
  sleep 1
done
# Проверка, существует ли база данных
DB_EXISTS=$(psql -U $POSTGRES_USER -tc "SELECT 1 FROM pg_database WHERE datname = '$POSTGRES_NAME';")
if [[ $DB_EXISTS != 1 ]]; then
  echo "База данных $POSTGRES_NAME не существует. Создание"
  createdb -U $POSTGRES_USER $POSTGRES_NAME
  echo "База данных $POSTGRES_NAME успешно создана"
else
  echo "База данных $POSTGRES_NAME уже существует"
fi
# Восстановление базы данных из дампа
echo "Восстанавливаю базу данных из файла $DUMP_FILE"
psql -U $POSTGRES_USER -d $POSTGRES_NAME -f ./dump/GameData_db-dump.sql
echo "База данных успешно восстановлена"
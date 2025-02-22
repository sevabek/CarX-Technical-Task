#!/bin/bash

# Получаем IP контейнера
CONTAINER_IP=$(hostname -I | awk '{print $1}')

# Заменяем параметры в /etc/cassandra/cassandra.yaml
# Указываем ip, который Cassandra будет слушать для внешних запросов. 0.0.0.0 - слушать все адреса
sed -i "s/^rpc_address:.*/rpc_address: 0.0.0.0/" /etc/cassandra/cassandra.yaml
# Указываем другим узлам в кластере, какой адрес нужно использовать для подключения к данному узлу через RPC
sed -i "s/^# broadcast_rpc_address:.*/broadcast_rpc_address: ${CONTAINER_IP}/" /etc/cassandra/cassandra.yaml

# Запускаем Cassandra
exec cassandra -f &

# Ждём, пока Cassandra запустится
echo "Ожидание запуска Cassandra..."
until cqlsh -e "DESCRIBE KEYSPACES;" > /dev/null 2>&1; do
    sleep 5
    echo "Cassandra ещё не готова, повторяем попытку..."
done

# Создаем схему
cqlsh -f /docker-entrypoint-initdb.d/schema.cql

# Поддерживаем контейнер в активном состоянии
tail -f /dev/null


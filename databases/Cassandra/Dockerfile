FROM cassandra:4.0
RUN apt-get update && apt-get install -y sed
COPY /dump /docker-entrypoint-initdb.d
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
USER cassandra
EXPOSE 9042
ENTRYPOINT ["/entrypoint.sh"]

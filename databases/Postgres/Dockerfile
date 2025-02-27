FROM postgres:17
ARG POSTGRES_USER
ARG POSTGRES_PASSWORD
ARG POSTGRES_DB_NAME
COPY restore.sh ./docker-entrypoint-initdb.d/restore.sh
RUN chmod +x ./docker-entrypoint-initdb.d/restore.sh
EXPOSE 5432
ENTRYPOINT ["sh", "-c", "docker-entrypoint.sh postgres"]

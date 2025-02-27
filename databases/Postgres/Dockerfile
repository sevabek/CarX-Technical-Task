FROM postgres:17
ENV POSTGRES_USER=postgres
ARG POSTGRES_PASSWORD
ENV POSTGRES_NAME=GameData_db
COPY restore.sh ./docker-entrypoint-initdb.d/restore.sh
RUN chmod +x ./docker-entrypoint-initdb.d/restore.sh
EXPOSE 5432
ENTRYPOINT ["sh", "-c", "docker-entrypoint.sh postgres"]

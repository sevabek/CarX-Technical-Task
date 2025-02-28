FROM maven:3.8.1-openjdk-8-slim AS build
WORKDIR /app
RUN ls
# Копируем исходники и скачиваем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:8-jre-slim
WORKDIR /app
COPY --from=build /app/target/TechnicalTask_Boot-1.0-SNAPSHOT.jar app.jar
ENV POSTGRES_HOST=postgres
ENV CASSANDRA_HOST=cassandra
ENV TZ=Europe/Moscow
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

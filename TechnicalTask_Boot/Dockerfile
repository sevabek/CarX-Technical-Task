FROM openjdk:8-slim
ENV MAVEN_VERSION=3.8.1

RUN apt-get update && apt-get install -y wget tar \
    && wget -O /tmp/apache-maven.tar.gz https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
    && tar xzvf /tmp/apache-maven.tar.gz -C /opt \
    && ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven \
    && rm /tmp/apache-maven.tar.gz

ENV MAVEN_HOME=/opt/maven
ENV PATH=${MAVEN_HOME}/bin:${PATH}

RUN mvn -version

WORKDIR /app
COPY . .

RUN apt-get install -y sed
RUN sed -i 's/localhost/postgres/' /app/src/main/resources/application.properties

RUN mvn clean package

ENTRYPOINT [ "java", "-jar", "/app/target/TechnicalTask_Boot-1.0-SNAPSHOT.jar" ]
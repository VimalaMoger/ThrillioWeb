FROM maven:3.8.4-openjdk-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY target/Book-0.0.1-SNAPSHOT.jar /spring.jar
RUN chmod 755 /spring.jar
ENTRYPOINT ["java", "-jar", "/spring.jar"]





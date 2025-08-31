FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/Book-0.0.1-SNAPSHOT.jar /spring.jar
RUN chmod 755 /spring.jar
ENTRYPOINT ["java", "-jar", "/spring.jar"]





FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/query.jar /app/query.jar
ENTRYPOINT ["java", "-jar", "query.jar"]
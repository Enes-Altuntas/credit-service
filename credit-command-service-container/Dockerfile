FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/command.jar /app/command.jar
ENTRYPOINT ["java", "-jar", "command.jar"]
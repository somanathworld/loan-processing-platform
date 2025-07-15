FROM openjdk:21-jdk-slim
LABEL maintainer="your_email@example.com"
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
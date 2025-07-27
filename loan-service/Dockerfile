# -------- STAGE 1: Build the application --------
FROM maven:3.9.10-eclipse-temurin-21 AS builder

# Set working directory inside container
WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY src ./src

# Build the application and skip tests for faster build
RUN mvn clean package -DskipTests

# -------- STAGE 2: Run the application --------
FROM eclipse-temurin:21-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy only the built jar file from the builder image
COPY --from=builder /app/target/*.jar app.jar

# Expose application port (optional, adjust if different)
EXPOSE 8083

# Set environment variable for the application (optional)
ENV EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://discovery-server:8761/eureka
ENV ZIPKIN_BASE_URL=http://zipkin:9411/api/v2/spans
ENV SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]

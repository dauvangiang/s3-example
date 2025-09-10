# Sử dụng OpenJDK 21 slim để giảm dung lượng image
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy file jar đã build từ Maven/Gradle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Chạy Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]

# Etapa 1: Build com Gradle
FROM gradle:8.5-jdk21 AS builder
LABEL authors="welbermacedo"
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Etapa 2: Executar o jar
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

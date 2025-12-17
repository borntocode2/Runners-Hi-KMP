# 1) Build stage
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app

COPY gradlew .
COPY gradlew.bat .
COPY gradle gradle
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY gradle.properties .
COPY gradle/libs.versions.toml gradle/libs.versions.toml

COPY build-logic build-logic
COPY shared shared
COPY server server

RUN chmod +x ./gradlew
RUN ./gradlew :server:build -x test

# 2) Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
ENV TZ=Asia/Seoul
ENV LANG=C.UTF-8

COPY --from=builder /app/server/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

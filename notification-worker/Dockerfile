FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /workspace

COPY . .

RUN sed -i 's/\r$//' mvnw \
    && chmod +x mvnw \
    && ./mvnw -pl notification-worker -am clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

ARG APP_VERSION=dev

LABEL org.opencontainers.image.title="Industrial Support Hub Notification Worker"
LABEL org.opencontainers.image.version="${APP_VERSION}"

ENV APP_VERSION="${APP_VERSION}"

WORKDIR /app

RUN groupadd --system spring \
    && useradd --system --gid spring --no-create-home spring

COPY --from=builder \
    /workspace/notification-worker/target/notification-worker-*.jar \
    app.jar

USER spring:spring

ENTRYPOINT ["java", "-jar", "app.jar"]
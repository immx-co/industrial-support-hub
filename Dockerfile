FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /workspace

COPY . .

RUN sed -i 's/\r$//' mvnw \
    && chmod +x mvnw \
    && ./mvnw -pl support-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

ARG APP_VERSION=dev

LABEL org.opencontainers.image.title="Industrial Support Hub Support Service"
LABEL org.opencontainers.image.version="${APP_VERSION}"

ENV APP_VERSION="${APP_VERSION}"

WORKDIR /app

RUN groupadd --system spring \
    && useradd --system --gid spring --no-create-home spring

COPY --from=builder \
    /workspace/support-service/target/support-service-*.jar \
    app.jar

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
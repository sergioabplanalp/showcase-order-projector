FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build
COPY . ./
RUN mvn clean package

FROM eclipse-temurin:21-alpine

COPY --from=builder /build/target/order-projector-*.jar /app.jar
COPY --from=ghcr.io/ufoscout/docker-compose-wait:latest /wait /wait

EXPOSE 8080

ENTRYPOINT /wait && java -jar -Dspring.profiles.active=prod app.jar

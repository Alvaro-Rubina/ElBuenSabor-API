FROM alpine:latest AS build

RUN apk update
RUN apk add openjdk17

COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-alpine
EXPOSE 8080
COPY --from=build target/elbuensabor-api-0.0.1-SNAPSHOT.jar ./app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
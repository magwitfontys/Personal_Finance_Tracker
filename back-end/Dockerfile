# syntax=docker/dockerfile:1

FROM gradle:8.7-jdk21 AS build
WORKDIR /app
COPY . .
# build jar but skip unit tests (they will run in the workflow)
RUN gradle --no-daemon clean bootJar -x test

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

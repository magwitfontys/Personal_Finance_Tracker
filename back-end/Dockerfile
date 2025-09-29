# syntax=docker/dockerfile:1

##### Build stage (Gradle) #####
FROM gradle:8.7-jdk21 AS build
WORKDIR /app
COPY . .
# runs tests and builds the runnable jar
RUN gradle --no-daemon clean test bootJar

##### Run stage #####
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

# syntax=docker/dockerfile:1

##### Build stage (Gradle or Maven — Gradle shown) #####
FROM gradle:8.7-jdk21 AS build
WORKDIR /app
COPY . .
# run tests and build the runnable jar
RUN gradle --no-daemon clean test bootJar

##### Run stage #####
FROM eclipse-temurin:21-jre
WORKDIR /app
# copy the jar produced in the build stage
COPY --from=build /app/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

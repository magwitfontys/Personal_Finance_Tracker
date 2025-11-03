# syntax=docker/dockerfile:1
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# copy the Spring Boot jar built by Gradle
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# container port (change if your app uses another)
EXPOSE 8081

# optional JVM flags through JAVA_OPTS
ENV JAVA_OPTS=""

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]

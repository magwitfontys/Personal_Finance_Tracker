# syntax=docker/dockerfile:1

# ---- build stage ----
FROM gradle:8.7-jdk21 AS build
WORKDIR /app

# Copy only files needed to cache deps first
COPY gradlew gradlew.bat /app/
COPY gradle /app/gradle
COPY settings.gradle build.gradle /app/

# Ensure wrapper is executable and warm Gradle cache
RUN chmod +x ./gradlew && ./gradlew --no-daemon help

# Now copy sources (invalidates cache only when src changes)
COPY src /app/src

# Build jar (tests already run in CI, so skip here)
RUN ./gradlew --no-daemon clean bootJar -x test

# ---- runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

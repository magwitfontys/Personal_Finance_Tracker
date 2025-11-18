# syntax=docker/dockerfile:1

########################
# Build stage
########################
FROM eclipse-temurin:21-jdk AS build

# Work in the app root (matches your repo layout)
WORKDIR /app

# Copy everything into the image
COPY . .

# Normalize Windows line endings for the Gradle wrapper and make it executable
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# Build the Spring Boot fat jar with Gradle
RUN ./gradlew --no-daemon clean bootJar

########################
# Runtime stage
########################
FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", " -jar", "app.jar"]

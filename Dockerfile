# ---- build ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
WORKDIR /app/back-end
# normalize line endings (Windows) + make wrapper executable
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
RUN ./gradlew --no-daemon clean bootJar

# ---- run ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/back-end/build/libs/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=h2
VOLUME ["/app/data"]
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]

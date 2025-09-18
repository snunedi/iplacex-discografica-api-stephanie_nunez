# Stage 1
FROM gradle:jdk21 as builder
WORKDIR /app
COPY .build.gradle .
COPY ./settings.gradle .
COPY src ./src
RUN gradle bootJar --no-daemon

# Stage 2
FROM openjdk:21-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar discografia-1.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
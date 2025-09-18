# Stage 1
FROM gradle:jdk21 as builder
WORKDIR /app
COPY build.gradle .
COPY settings.gradle .
COPY src ./src
RUN gradle build

# Stage 2
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/discografia-1.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
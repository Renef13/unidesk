FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew build --no-daemon

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/uni-desk-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p /app/uploads
RUN chmod -R 777 /app/uploads
ENTRYPOINT ["java", "-jar", "app.jar"]
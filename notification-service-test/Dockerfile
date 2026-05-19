FROM eclipse-temurin:21-jdk-alpine
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY . .
RUN mvn -pl notification-service-test -am -DskipTests package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/notification-service-test/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
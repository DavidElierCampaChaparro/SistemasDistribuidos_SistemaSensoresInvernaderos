FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY . .
RUN mvn -pl sensor-service-test -am -DskipTests package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/sensor-service-test/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
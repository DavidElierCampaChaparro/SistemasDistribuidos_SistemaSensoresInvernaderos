FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
RUN mvn -f grpc-contracts/pom.xml -DskipTests install \
 && mvn -f greenhouse-common/pom.xml -DskipTests install \
 && mvn -f sensor-service-test/pom.xml -DskipTests package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /workspace/sensor-service-test/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
FROM maven:3.9.9-eclipse-temurin-23 AS build
WORKDIR /workspace

COPY pom.xml pom.xml
COPY inventory-service/pom.xml inventory-service/pom.xml
COPY inventory-service/src inventory-service/src

RUN mvn -q -pl inventory-service -am package -DskipTests

FROM eclipse-temurin:23-jre
WORKDIR /app

COPY --from=build /workspace/inventory-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

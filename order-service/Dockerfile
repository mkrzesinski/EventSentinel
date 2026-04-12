FROM maven:3.9.9-eclipse-temurin-23 AS build
WORKDIR /workspace

COPY pom.xml pom.xml
COPY checkstyle.xml checkstyle.xml
COPY user-service/pom.xml user-service/pom.xml
COPY order-service/pom.xml order-service/pom.xml
COPY inventory-service/pom.xml inventory-service/pom.xml
COPY order-service/src order-service/src

RUN mvn -q -pl order-service -am package -DskipTests

FROM eclipse-temurin:23-jre
WORKDIR /app

COPY --from=build /workspace/order-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

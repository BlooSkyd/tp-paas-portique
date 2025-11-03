# ---- Étape 1 : build ----
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

WORKDIR /app/target
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "core-operational-backend-0.0.1-SNAPSHOT.jar"]
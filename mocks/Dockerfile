FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/mocks-1.0-SNAPSHOT.jar mocks.jar
EXPOSE 8080

CMD ["java", "-jar", "mocks.jar"]
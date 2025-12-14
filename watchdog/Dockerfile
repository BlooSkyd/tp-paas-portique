FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/watchdog-0.0.1-SNAPSHOT.jar watchdog.jar
EXPOSE 8080

## Allow overriding Redis hosts/ports via environment variables
ENV REDIS_MASTER_HOST=192.168.248.108
ENV REDIS_MASTER_PORT=6379
ENV REDIS_SLAVE_HOST=192.168.248.109
ENV REDIS_SLAVE_PORT=6379

## Path to shared YAML config file (core_status.yml)
## Mount or copy the file at runtime; default expects /app/core_status.yml
ENV shared_file_path=/app/core_status.yml

CMD ["java", "-jar", "watchdog.jar"]
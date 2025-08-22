FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src/

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

COPY --from=builder /app/target/*.jar historico-service.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "historico-service.jar"]

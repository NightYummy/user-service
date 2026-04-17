FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY ../pom.xml /app/pom.xml
COPY ../user-service/pom.xml /app/user-service/pom.xml
COPY ../config-server/pom.xml /app/config-server/pom.xml
COPY ../eureka-server/pom.xml /app/eureka-server/pom.xml
COPY ../gateway-server/pom.xml /app/gateway-server/pom.xml

RUN mvn -B dependency:go-offline -pl eureka-server -am

COPY ../eureka-server/src /app/eureka-server/src

RUN mvn clean package -pl eureka-server -am -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/eureka-server/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
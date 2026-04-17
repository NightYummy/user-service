FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY ../pom.xml /app/pom.xml
COPY ../user-service/pom.xml /app/user-service/pom.xml
COPY ../config-server/pom.xml /app/config-server/pom.xml
COPY ../eureka-server/pom.xml /app/eureka-server/pom.xml
COPY ../gateway-server/pom.xml /app/gateway-server/pom.xml

RUN mvn -B dependency:go-offline -pl config-server -am

COPY ../config-server/src /app/config-server/src

RUN mvn clean package -pl config-server -am -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/config-server/target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
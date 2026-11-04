# Estágio 1: compilação da aplicação
FROM maven:3.10.0-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package

# Estágio 2: execução da aplicação
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/gestao-empresas-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]


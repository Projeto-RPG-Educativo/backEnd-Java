FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -Dmaven.javadoc.skip=true

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:default}","-jar","/app/app.jar"]

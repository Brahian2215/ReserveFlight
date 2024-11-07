FROM maven:3.8.1-openjdk-17 AS build
COPY pom.xml /app/
COPY src /app/src/
WORKDIR /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
COPY --from=build /app/target/reserveFlight-0.0.1-SNAPSHOT.jar /usr/local/lib/reserveFlight.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/reserveFlight.jar"]
FROM openjdk:17
EXPOSE 8080
ADD target/reserveFlight-0.0.1-SNAPSHOT.jar reserveFlight-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/reserveFlight-0.0.1-SNAPSHOT.jar"]
FROM openjdk:22-jdk-slim-bullseye
COPY target/engfinity-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
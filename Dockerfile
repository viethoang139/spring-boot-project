FROM openjdk:17-jdk-slim

MAINTAINER lvh.com

COPY target/spring-boot-project-0.0.1-SNAPSHOT.jar spring-boot-project-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "spring-boot-project-0.0.1-SNAPSHOT.jar"]


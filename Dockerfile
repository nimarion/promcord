FROM maven:3.8.1-adoptopenjdk-16 AS build  
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:16-jdk-slim
COPY --from=build /usr/src/app/target/promcord-*-SNAPSHOT-shaded.jar promcord.jar

ENTRYPOINT ["java", "-jar", "promcord.jar"]
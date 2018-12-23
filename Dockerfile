FROM openjdk:10-jdk-slim

LABEL maintainer = "biosphere.dev@gmx.de"

COPY target/promcord-1.0-SNAPSHOT-shaded.jar promcord.jar

ENTRYPOINT ["java", "-jar", "-Xmx128m", "promcord.jar"]
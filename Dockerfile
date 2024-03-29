FROM openjdk:11-jdk-slim

LABEL maintainer = "biosphere.dev@gmx.de"

ADD src/main/resources/static ./src/main/resources/static

COPY target/WheresTheParty-0.1-shaded.jar discordParty.jar

ENTRYPOINT ["java", "-jar", "-Xmx128m", "discordParty.jar"]

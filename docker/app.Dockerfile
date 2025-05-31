FROM eclipse-temurin:22-jre-alpine
WORKDIR /home
COPY target/battle-buds-server-0.0.1-jar-with-dependencies.jar ./
ENTRYPOINT ["java", "-jar", "battle-buds-server-0.0.1-jar-with-dependencies.jar"]
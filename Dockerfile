FROM eclipse-temurin:25
LABEL authors="Thin Nadi Oo"
COPY ./target/semApp.jar /tmp/semApp.jar
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "semApp.jar"]
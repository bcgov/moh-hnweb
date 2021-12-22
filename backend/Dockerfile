# Add code for maven build
FROM maven:3.8.3-openjdk-11-slim as build-stage
COPY src /home/app/src
COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean install

FROM adoptopenjdk:11-jre-hotspot

WORKDIR /tmp
#Creating new directory in tmp
RUN mkdir -p keystore

COPY --from=build-stage /home/app/target/*.jar /tmp/app.jar
ENTRYPOINT ["java","-cp","/tmp","-jar","/tmp/app.jar"]
EXPOSE 9090
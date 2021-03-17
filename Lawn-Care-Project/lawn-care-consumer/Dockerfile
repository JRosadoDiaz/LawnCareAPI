#Using java 11
FROM openjdk:11

#Setting an argument variable for the project JAR file
ARG JAR_FILE=target/lawn-care-consumer-0.0.1-SNAPSHOT.jar

#The actual command. Copying the JAR file to create a Docker image
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
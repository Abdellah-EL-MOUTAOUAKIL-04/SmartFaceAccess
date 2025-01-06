# Use JDK 21 base image for building the app
FROM openjdk:21-jdk-slim as build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file and the source code into the container
COPY pom.xml .
COPY src ./src

# Install Maven and build the application
RUN apt-get update && apt-get install -y maven
RUN mvn clean install

# Create a new stage for running the application with JDK 21
FROM openjdk:21-jdk-slim

# Set the working directory for running the app
WORKDIR /app

# Copy the jar file from the build stage to the running stage
COPY --from=build /app/target/SmartAccessFace.jar ./SmartAccessFace.jar

# Expose the port the app will run on (optional, depends on your app)
EXPOSE 8080

# Command to run the JavaFX application
ENTRYPOINT ["java", "-jar", "SmartAccessFace.jar"]

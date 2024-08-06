# Base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file
COPY target/*.jar /app/app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]

# Base image with Maven and OpenJDK
FROM maven:3.8.5-openjdk-17-slim as build

# Set the working directory
WORKDIR /app

# Copy the source code
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Second stage: create a minimal runtime image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/corso-0.0.1-SNAPSHOT.jar ./corso-0.0.1-SNAPSHOT.jar

# Expose the port that the Spring Boot app runs on
EXPOSE 8080

# Run the Spring Boot app
CMD ["java", "-jar", "corso-0.0.1-SNAPSHOT.jar"]

# Use OpenJDK 17 with Alpine as the base image
FROM openjdk:17-jdk-alpine as build

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and its configurations
COPY mvnw ./
COPY .mvn .mvn/

# Copy the pom.xml file to cache dependencies
COPY pom.xml ./

# Install Node.js and Yarn (ensure permissions)
RUN chmod +x /app/frontend/node/node
RUN chmod -R +x /app/frontend/node/

# Copy the source code
COPY src ./src/

# Package the application without running tests
RUN ./mvnw package -DskipTests

# Start a new stage for the final image
FROM openjdk:17-jdk-alpine

# Copy the packaged JAR file from the build stage
COPY --from=build /app/target/app-0.0.1-SNAPSHOT.jar /app/app.jar

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

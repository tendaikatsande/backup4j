# Use Eclipse Temurin as an alternative to OpenJDK (better maintenance)
FROM eclipse-temurin:17-jdk-alpine AS build

# Set the working directory
WORKDIR /app

# Copy Maven wrapper and configuration
COPY mvnw ./
COPY .mvn .mvn/
RUN chmod +x mvnw

# Copy Maven configuration for dependency caching
COPY pom.xml ./
RUN ./mvnw dependency:resolve

# Copy source code
COPY src ./src/

# Package the application without running tests
RUN ./mvnw package -DskipTests

# Start a new stage for the final image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the packaged JAR file from the build stage
COPY --from=build /app/target/app-0.0.1-SNAPSHOT.jar app.jar

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

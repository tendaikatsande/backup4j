# Use OpenJDK 17 base image
FROM openjdk:24-ea-17-slim-bullseye AS build

# Set the working directory
WORKDIR /app

# Copy Maven wrapper and configuration
COPY mvnw ./
COPY .mvn .mvn/
RUN chmod +x mvnw

# Copy Maven configuration for dependency caching
COPY pom.xml ./
RUN ./mvnw dependency:resolve

# Copy the application source code
COPY src ./src/

# Build the application without running tests
RUN ./mvnw package -DskipTests -Dnative-image.enabled=true -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn \
  --enable-native-access=ALL-UNNAMED


# Runtime stage
FROM openjdk:24-ea-17-slim-bullseye

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/app-0.0.1-SNAPSHOT.jar app.jar

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

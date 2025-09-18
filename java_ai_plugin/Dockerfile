FROM openjdk:17-jdk-slim

WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Copy the project files
COPY java_ai_plugin/ .

# Build the application
RUN mvn clean compile package -DskipTests

# Create logs directory
RUN mkdir -p logs

EXPOSE 8080

# Run the demo application
CMD ["java", "-jar", "target/ai-plugin-1.0.0.jar"]
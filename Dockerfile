# Use the OpenJDK 17 base image
FROM openjdk:17

# Define the build argument with the default value
ARG JAR_FILE=/build/libs/*.jar

# Print the JAR_FILE variable value for debugging
RUN echo "JAR_FILE is set to ${JAR_FILE}"

# List files in the current directory for debugging
RUN ls -l .

# Copy the JAR file to the container
COPY ${JAR_FILE} app.jar

# Specify the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-oracle

# Set the working directory in the container
WORKDIR /app

# Copy the packaged jar file into the container
COPY target/order-0.0.1-SNAPSHOT.jar order-app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "order-app.jar"]
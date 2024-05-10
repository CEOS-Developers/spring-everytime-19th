# Dockerfile
FROM openjdk:17

WORKDIR /app

COPY . .

RUN sed -i 's/\r$//' gradlew

RUN chmod +x ./gradlew
RUN ./gradlew clean build

ENV JAR_PATH=/app/build/libs
RUN mv ${JAR_PATH}/*.jar /app/app.jar

CMD ["java", "-jar", "/app.jar"]
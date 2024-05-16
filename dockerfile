FROM openjdk:18
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar
LABEL authors="imhyeongun"
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]

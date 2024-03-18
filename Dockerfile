FROM amazoncorretto:17
EXPOSE 8080
ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} app.jar
ADD src/main/resources/*.csv src/main/resources/
ENTRYPOINT ["java", "-jar", "/app.jar"]
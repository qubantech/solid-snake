FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.14.1_1-slim
EXPOSE 8080
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
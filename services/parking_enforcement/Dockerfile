FROM amazoncorretto:21-alpine

RUN mkdir /data
VOLUME /data
EXPOSE 9080

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker","/app.jar"]

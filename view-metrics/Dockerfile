FROM  openjdk:8-jdk-alpine

ARG JAR_FILE=./view-metrics/build/libs/view-metrics-*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

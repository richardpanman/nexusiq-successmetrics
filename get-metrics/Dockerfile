FROM  openjdk:8-jdk-alpine

ARG JAR_FILE=./get-metrics/build/libs/get-metrics-*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

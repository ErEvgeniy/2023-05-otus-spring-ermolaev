FROM bellsoft/liberica-openjdk-alpine:17.0.2

COPY target/homework-17-1.0.0.jar /usr/local/apps/homework.jar

WORKDIR /usr/local/apps/

CMD ["java", "-jar", "homework.jar"]
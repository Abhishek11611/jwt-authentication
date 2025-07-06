
FROM openjdk:21

COPY target/authapp.jar /usr/app/

WORKDIR /usr/app

ENTRYPOINT ["java", "-jar", "authapp.jar"]

EXPOSE 8080


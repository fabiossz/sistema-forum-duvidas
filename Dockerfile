FROM openjdk:8

WORKDIR /app

EXPOSE 8080

ADD target/forum-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "forum-0.0.1-SNAPSHOT.jar"]
FROM openjdk:13-alpine
ADD target/spring-kafka.jar spring-kafka.jar
ENTRYPOINT ["java", "-jar", "spring-kafka.jar"]



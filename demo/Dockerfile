FROM openjdk:17
ARG JAR_FILE= target/CollaVore.jar
COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-Dspring.profiles.active=linux" ,"-jar", "app.jar"]
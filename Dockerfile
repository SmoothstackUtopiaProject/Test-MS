FROM openjdk:8-jdk-alpine
ARG DBUrl
ARG DBUsername
ARG DBPassword
ENV DB_URL=DBUrl 
ENV DB_PASSWORD=DBPassword 
ENV DB_USERNAME=DB_USERNAME
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
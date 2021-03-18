FROM openjdk:8-jdk-alpine
ENV DB_URL=${DB_URL} 
ENV DB_PASSWORD=${DB_PASSWORD} 
ENV DB_USERNAME=${DB_USERNAME}
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
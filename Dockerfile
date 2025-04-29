FROM --platform=linux/amd64 eclipse-temurin:21-jdk

VOLUME /temp
COPY build/libs/*.jar /app.jar
#RUN java -jar /app.jar --logging.level.root=ERROR --logging.file.name=/tem/app.log
ENTRYPOINT ["java","-jar","/app.jar","--logging.level.root=ERROR","--logging.file.name=/tem/app.log"]


#docker build --platform linux/amd64 -t jinyoung0924/spring-app:0.0.0 .
#docker buildx build --platform linux/amd64,linux/arm64 -t jinyoung0924/spring-app:0.0.1 .
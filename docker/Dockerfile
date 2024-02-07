FROM amazoncorretto:17-alpine-jdk
RUN mkdir /app
EXPOSE 8080
COPY build/libs/*-all.jar /app/rinhadebackend.jar
ENTRYPOINT ["java","-jar","/app/rinhadebackend.jar"]
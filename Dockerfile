
FROM openjdk:11

COPY ./target/CartoonizePhoto-1.0.0.jar /app.jar

CMD ["--server.port=8080"]

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]
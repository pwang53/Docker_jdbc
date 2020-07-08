FROM openjdk:8

COPY ./jar-folder/JDBCProject-0.0.1-jar-with-dependencies.jar /demo.jar

EXPOSE 8000

CMD ["java", "-jar", "/demo.jar"]

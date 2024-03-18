FROM openjdk:21
WORKDIR /my-project
CMD ["./gradlew", "clean", "bootJar"]
COPY build/libs/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/app.jar"]

#FROM openjdk:21
#WORKDIR /productSyncApp
#COPY . .
#RUN ./gradlew clean build
#CMD ./gradlew bootRun
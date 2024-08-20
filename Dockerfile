FROM openjdk:17-jdk
LABEL authors="LovelyCat"
WORKDIR /app
COPY  build/libs/shadowcat-ai-server-1.0.0-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]

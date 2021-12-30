FROM adoptopenjdk:11-jre-hotspot
RUN mkdir /app
WORKDIR /app
ADD ./api/target/services-api-1.0.0-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java", "-jar", "services-api-1.0.0-SNAPSHOT.jar"]

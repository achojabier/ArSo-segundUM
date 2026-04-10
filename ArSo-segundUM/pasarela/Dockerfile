FROM maven:3.8.6-amazoncorretto-11 AS builder
WORKDIR /app
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
COPY . .
RUN mvn clean package -DskipTests -Dfile.encoding=UTF-8

FROM amazoncorretto:11-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
EXPOSE 8090
CMD ["java", "-Djava.net.preferIPv4Stack=true", "-jar", "/app/app.jar"]
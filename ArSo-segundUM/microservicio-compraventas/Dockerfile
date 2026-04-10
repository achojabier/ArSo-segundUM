# ETAPA 1: builder
FROM maven:3.8.6-amazoncorretto-11 AS builder
WORKDIR /app
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
COPY . .
RUN mvn clean package -DskipTests -Dfile.encoding=UTF-8

# ETAPA 2: ejecución
FROM amazoncorretto:11-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-Djava.net.preferIPv4Stack=true", "-jar", "/app/app.jar"]
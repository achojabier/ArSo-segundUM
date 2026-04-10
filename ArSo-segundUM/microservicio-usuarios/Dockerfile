FROM maven:3.8.6-amazoncorretto-11
WORKDIR /app
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
COPY . .
RUN mvn clean package -DskipTests -Dfile.encoding=UTF-8
EXPOSE 8082
CMD ["mvn", "-Djava.net.preferIPv4Stack=true", "jetty:run"]
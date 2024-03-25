FROM amazoncorretto:17

WORKDIR /source

COPY target/*.jar app.jar
COPY deploy/entrypoint.sh entrypoint.sh

EXPOSE 8080

RUN chmod +x entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]

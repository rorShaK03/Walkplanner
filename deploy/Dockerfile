FROM amazoncorretto:17

WORKDIR /source

COPY build/libs/*.jar app.jar
COPY deploy/entrypoint.sh entrypoint.sh

EXPOSE 8080

RUN chmod +x entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]

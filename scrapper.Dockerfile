FROM openjdk:21
ADD scrapper/target/*.jar scrapper.jar

EXPOSE 8080 8081
ENV SENDING_METHOD queue
ENV BOT_ADDRESS bot:8090
ENV KAFKA_ADDRESS kafka:19092
ENV POSTGRES_ADDRESS postgres:5432
ENV DATABASE_NAME scrapper
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD postgres

ENTRYPOINT java -jar scrapper.jar --spring.profiles.active=docker-backend-network

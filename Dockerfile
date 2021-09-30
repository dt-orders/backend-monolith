FROM openjdk:11
COPY target/*.jar .
COPY ./MANIFEST /

# used in the response time pattern
ARG SLEEP_TIME=1000
ENV SLEEP_TIME=$SLEEP_TIME

# used to set the problem pattern
ARG APP_VERSION=1
ENV APP_VERSION=$APP_VERSION

CMD ["sh", "-c", "/usr/local/openjdk-11/bin/java -Xmx400m -Xms400m -jar *.jar"]
EXPOSE 8080
FROM openjdk:17

EXPOSE 8090

ENV APP_HOME /usr/src/app

COPY target/webflux-api-demo-0.0.1-SNAPSHOT.jar $APP_HOME/reactive-api.jar

WORKDIR $APP_HOME

ENTRYPOINT exec java -jar reactive-api.jar
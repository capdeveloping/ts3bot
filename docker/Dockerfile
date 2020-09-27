FROM openjdk:13-jdk-slim-buster

MAINTAINER captain

COPY ts3bot.jar ts3bot.jar
COPY entrypoint.sh /etc/entrypoint.sh

VOLUME /data

ENTRYPOINT ["sh", "/etc/entrypoint.sh"]

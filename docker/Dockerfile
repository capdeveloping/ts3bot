FROM openjdk:13-jdk-slim-buster

MAINTAINER Capdeveloping

# Install apache, PHP, and supplimentary programs. openssh-server, curl, and lynx-cur are for debugging the container.
RUN apt-get update && apt install wget -y && \
    echo "deb https://packages.sury.org/php/ buster main" > /etc/apt/sources.list.d/php.list && \
    wget -O /etc/apt/trusted.gpg.d/php.gpg https://packages.sury.org/php/apt.gpg && \
    apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get -y install apache2 php8.0 libapache2-mod-php8.0 php8.0-sqlite3 sqlite3

# Enable apache mods.
RUN a2enmod php8.0
RUN a2enmod rewrite

# Update the PHP.ini file, enable <? ?> tags and quieten logging.
RUN sed -i "s/short_open_tag = Off/short_open_tag = On/" /etc/php/8.0/apache2/php.ini
RUN sed -i "s/error_reporting = .*$/error_reporting = E_ERROR | E_WARNING | E_PARSE/" /etc/php/8.0/apache2/php.ini

# Manually set up the apache environment variables
ENV APACHE_RUN_USER www-data
ENV APACHE_RUN_GROUP www-data
ENV APACHE_LOG_DIR /var/log/apache2
ENV APACHE_LOCK_DIR /var/lock/apache2
ENV APACHE_PID_FILE /var/run/apache2.pid

RUN ln -sf /proc/self/fd/1 $APACHE_LOG_DIR/access.log && \
    ln -sf /proc/self/fd/2 $APACHE_LOG_DIR/error.log && \
    sed -i '/DocumentRoot /a DirectoryIndex index.php' /etc/apache2/sites-enabled/000-default.conf


# By default start up apache in the foreground, override with /bin/bash for interative.

COPY ts3bot.jar /ts3bot.jar
COPY control-bot.sh /control-bot.sh
COPY entrypoint.sh /etc/entrypoint.sh
COPY html/ /var/www/html/
COPY serverconfig.template /serverconfig.template

RUN chmod gu+x /control-bot.sh && \
    chown www-data:www-data -R /control-bot.sh /ts3bot.jar /var/www/html && \
    usermod --shell /bin/bash www-data

VOLUME /data

# Expose apache.
EXPOSE 80

ENTRYPOINT ["/bin/bash", "/etc/entrypoint.sh"]
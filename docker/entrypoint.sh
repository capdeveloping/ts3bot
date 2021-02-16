#!/bin/bash

echo "Europe/Berlin" > /etc/timezone
ln -sf /usr/share/zoneinfo/Europe/Berlin /etc/localtime
ln -sf /data/ts3_icons/ /
mkdir -p /data/configs /data/logs
chown www-data:www-data -R /data/

if [ ! -e "/data/serverconfig.template" ]; then
  cp /serverconfig.template /data/serverconfig.template
  chown www-data:www-data /data/serverconfig.template
fi

if [ ! -e "/data/db.sqlite3" ]; then
  echo "CREATE TABLE users(username text,password text);" > /data/db.schema
  sqlite3 /data/db.sqlite3 < /data/db.schema
  rm /data/db.schema
  sqlite3 /data/db.sqlite3 'insert into users (username, password) VALUES("admin", "$2y$11$D4OxW1TABL4T81ioPD2CC.5OHmm0/ONd1mZ2WhKMAIRydjb8M3XEq");'
  chown www-data:www-data /data/db.sqlite3
fi

su www-data -c "/control-bot.sh start" &

/usr/sbin/apache2ctl -D FOREGROUND

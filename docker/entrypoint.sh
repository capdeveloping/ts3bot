#!/usr/bin/env bash

ln -sf /dev/stdout /data/logs/bot.log

echo "Europe/Berlin" > /etc/timezone
ln -sf /usr/share/zoneinfo/Europe/Berlin /etc/localtime

ln -s /data/ts3_icons/ /

java -Duser.timezone=Europe/Berlin -jar ./ts3bot.jar
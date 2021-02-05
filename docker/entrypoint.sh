#!/bin/bash

echo "Europe/Berlin" > /etc/timezone
ln -sf /usr/share/zoneinfo/Europe/Berlin /etc/localtime
ln -sf /data/ts3_icons/ /

/usr/sbin/apache2ctl -D FOREGROUND
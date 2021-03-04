#!/bin/bash

export LANG="C.UTF-8"

case $1 in
        start)
                # shellcheck disable=SC2028
                echo "################################" >> /data/logs/bot.log
                # shellcheck disable=SC2028
                echo "#         Bot gestartet!       #" >> /data/logs/bot.log
                # shellcheck disable=SC2028
                echo "################################" >> /data/logs/bot.log
                java -Duser.timezone=Europe/Berlin -jar /ts3bot.jar 2&> /data/logs/start.log &
                ;;
        stop)
                pkill -f 'java'
                # shellcheck disable=SC2028
                echo "################################" >> /data/logs/bot.log
                # shellcheck disable=SC2028
                echo "#         Bot getoppt!         #" >> /data/logs/bot.log
                # shellcheck disable=SC2028
                echo "################################" >> /data/logs/bot.log
                ;;
        restart)
                pkill -f 'java'
                sleep 5
                # shellcheck disable=SC2028
                echo "################################" >> /data/logs/bot.log
                # shellcheck disable=SC2028
                echo "#       Bot neugestartet!      #" >> /data/logs/bot.log
                # shellcheck disable=SC2028
                echo "################################" >> /data/logs/bot.log
                java -Duser.timezone=Europe/Berlin -jar /ts3bot.jar 2&> /data/logs/start.log &
                ;;
        createInstance)
                mkdir /data/configs/"$2"/
                cp /data/serverconfig.template /data/configs/"$2"/serverconfig.cfg
                sqlite3 /data/db.sqlite3 'CREATE TABLE "'$2'_groups"(id INTEGER,name text);'
                sqlite3 /data/db.sqlite3 'CREATE TABLE "'$2'_users"(uid text, name text);'
                sqlite3 /data/db.sqlite3 'CREATE TABLE "'$2'_channels"(id INTEGER,name text);'
                ;;
        renameInstance)
                mv /data/configs/"$2" /data/configs/"$3"
                sqlite3 /data/db.sqlite3 'ALTER TABLE "'$2'_channels" RENAME TO '$3'_channels;'
                sqlite3 /data/db.sqlite3 'ALTER TABLE "'$2'_users" RENAME TO '$3'_users;'
                sqlite3 /data/db.sqlite3 'ALTER TABLE "'$2'_groups" RENAME TO '$3'_groups;'
                ;;
        removeInstance)
                rm /data/configs/"$2"/ -r
                sqlite3 /data/db.sqlite3 'DROP TABLE "'$2'_groups";'
                sqlite3 /data/db.sqlite3 'DROP TABLE "'$2'_users";'
                sqlite3 /data/db.sqlite3 'DROP TABLE "'$2'_channels";'
                ;;
        *)
                echo "Sorry, I don't understand"
                ;;
esac

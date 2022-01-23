#!/bin/bash
logfile="/data/logs/bot.log"
export LANG="C.UTF-8"

case $1 in
        start)
                # shellcheck disable=SC2028
                echo "[INFO ] ################################" >> $logfile
                # shellcheck disable=SC2028
                echo "[INFO ] #         Bot gestartet!       #" >> $logfile
                # shellcheck disable=SC2028
                echo "[INFO ] ################################" >> $logfile
                java -Duser.timezone=Europe/Berlin -jar /ts3bot.jar configPath=/data/configs/ instanceFile=/data/configs/instancemanager.cfg 2&>> $logfile &
                                ;;
        stop)
                pkill -f 'java'
                # shellcheck disable=SC2028
                echo "[INFO ] ################################" >> $logfile
                # shellcheck disable=SC2028
                echo "[INFO ] #         Bot gestoppt!         #" >> $logfile
                # shellcheck disable=SC2028
                echo "[INFO ] ################################" >> $logfile
                ;;
        restart)
                pkill -f 'java'
                sleep 5
                # shellcheck disable=SC2028
                echo "[INFO ] ################################" >> $logfile
                # shellcheck disable=SC2028
                echo "[INFO ] #       Bot neugestartet!      #" >> $logfile
                # shellcheck disable=SC2028
                echo "[INFO ] ################################" >> $logfile
                java -Duser.timezone=Europe/Berlin -jar /ts3bot.jar configPath=/data/configs/ instanceFile=/data/configs/instancemanager.cfg 2&>> $logfile &
                ;;
        createInstance)
                mkdir /data/configs/"$2"/
                cp /data/serverconfig.template /data/configs/"$2"/serverconfig.cfg
                sqlite3 /data/db.sqlite3 'CREATE TABLE "'$2'_groups"(id INTEGER,name text);'
                sqlite3 /data/db.sqlite3 'CREATE TABLE "'$2'_users"(uid text, name text, ip text, groups text, online INTEGER);'
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
                echo "[INFO ] Sorry, I don't understand"
                ;;
esac

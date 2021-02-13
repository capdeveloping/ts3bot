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
                ;;
        renameInstance)
                mv /data/configs/"$2" /data/configs/"$2"
                ;;
        removeInstance)
                rm /data/configs/"$2"/ -r
                ;;
        *)
                echo "Sorry, I don't understand"
                ;;
esac
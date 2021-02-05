#!/bin/bash

export LANG="C.UTF-8"

case $1 in
        start)
                java -Duser.timezone=Europe/Berlin -jar /ts3bot.jar
                ;;
        stop)
                pkill -f 'java'
                break
                ;;
        restart)
                pkill -f 'java'
                sleep 5
                java -Duser.timezone=Europe/Berlin -jar /ts3bot.jar
                ;;
        createInstance)
                mkdir /data/configs/$2/
                cp /data/serverconfig.template /data/configs/$2/serverconfig.cfg
                ;;
        renameInstance)
                mv /data/configs/$2 /data/configs/$3
                ;;
        removeInstance)
                rm /data/configs/$2/ -r
                ;;
        *)
                echo "Sorry, I don't understand"
                ;;
esac
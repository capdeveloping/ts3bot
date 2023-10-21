#!/bin/bash

# -------------------------------------------------- (FUNCTIONS)

createSQLTables(){
  firstServer=true
  sqlite3 /data/db.sqlite3 "PRAGMA writable_schema = 1; DELETE FROM sqlite_master WHERE type = 'table' AND name NOT IN ('users', 'status'); PRAGMA writable_schema = 0; VACUUM;"
  sqlite3 /data/db.sqlite3 'CREATE TABLE IF NOT EXISTS status(channel_create_count integer, channel_delete_count integer, client_moved_count integer, welcome_message_count integer, twitch_live_count integer);'
  sqlite3 /data/db.sqlite3 'INSERT INTO status(channel_create_count, channel_delete_count, client_moved_count, welcome_message_count, twitch_live_count) SELECT "0", "0", "0", "0", "0" WHERE NOT EXISTS (select * from status);'
  for folder in `find /data/configs/ -maxdepth 1 -type d`; do
    folder="${folder##*/}"
    if [ -n "$folder" ]; then
      sqlite3 /data/db.sqlite3 'CREATE TABLE IF NOT EXISTS "'$folder'_users"(uid text, name text, ip text, groups text, online INTEGER);'
      sqlite3 /data/db.sqlite3 'CREATE TABLE IF NOT EXISTS "'$folder'_groups"(id INTEGER, name text);'
      sqlite3 /data/db.sqlite3 'CREATE TABLE IF NOT EXISTS "'$folder'_channels"(id INTEGER, name text);'
      firstServer=false
    fi
  done

  if [ "$firstServer" = true ]; then
      sqlite3 /data/db.sqlite3 'CREATE TABLE IF NOT EXISTS server1_users(uid text, name text, ip text, groups text, online INTEGER);'
      sqlite3 /data/db.sqlite3 'CREATE TABLE IF NOT EXISTS server1_groups(id INTEGER, name text);'
      sqlite3 /data/db.sqlite3 'CREATE TABLE IF NOT EXISTS server1_channels(id INTEGER, name text);'
  fi
}

# -------------------------------------------------- (MAIN)

# -------------------------- (prepare start)

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
  sqlite3 /data/db.sqlite3 'CREATE TABLE users(username text, password text, isadmin text, instances text);'
  sqlite3 /data/db.sqlite3 'insert into users (username, password, isadmin, instances) VALUES("admin", "$2y$11$D4OxW1TABL4T81ioPD2CC.5OHmm0/ONd1mZ2WhKMAIRydjb8M3XEq", "true", "server1");'
  chown www-data:www-data /data/db.sqlite3
fi

createSQLTables

# -------------------------- (start)

su www-data -c "/control-bot.sh start" &

/usr/sbin/apache2ctl -D FOREGROUND

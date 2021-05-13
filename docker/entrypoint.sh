#!/bin/bash

# -------------------------------------------------- (FUNCTIONS)

createSQLTables(){
  firstServer=true
  sqlite3 /data/db.sqlite3 "PRAGMA writable_schema = 1; DELETE FROM sqlite_master WHERE type = 'table' AND name NOT IN ('users'); PRAGMA writable_schema = 0; VACUUM;"
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
  sqlite3 /data/db.sqlite3 'CREATE TABLE users(username text,password text);'
  sqlite3 /data/db.sqlite3 'insert into users (username, password) VALUES("admin", "$2y$11$D4OxW1TABL4T81ioPD2CC.5OHmm0/ONd1mZ2WhKMAIRydjb8M3XEq");'
  chown www-data:www-data /data/db.sqlite3
fi

createSQLTables

# -------------------------- (start)

su www-data -c "/control-bot.sh start" &

/usr/sbin/apache2ctl -D FOREGROUND

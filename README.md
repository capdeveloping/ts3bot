# Teamspeak 3 Bot

Dieser Bot kann frei verwendet werden. 

## ACHTUNG!
Ich empfehle im 1.0.1 Release erstmal mit einem Basic Auth zu arbeiten. Sorry. Weblogin wird nachgezogen.

## Funktionen

Können mehrmals in der Config verwendet werden 
-------------------------------------------------------------------------------------------------------------

#### ClientMove
```
# Client joint den channel
FUNKTIONSNAME_client_moved_channel =

# Clients dieser Server Groupen sollen angestupst/angeschrieben werden.
# Eine mit Komma getrennte Liste (ohne Leerzeichen) mit Gruppen IDs.
FUNKTIONSNAME_client_moved_group_notify =

# Gruppen die ignoriert werden oder auf die nur geachtet werden sollen.
# Eine mit Komma getrennte Liste (ohne Leerzeichen) mit Gruppen IDs.
FUNKTIONSNAME_client_moved_group_ids = 

# Gruppen sollen ignoriert oder nur diese sollen gesehen werden. -> ignore/only
FUNKTIONSNAME_client_moved_group_action = ignore
```

#### ClientAFK
```
# Zeit bis der Client engültig gemoved wird
# Angaben in Sekunden
FUNKTIONSNAME_client_afk_time = 

# Channel wo die AFK Clients hin gemoved werden
FUNKTIONSNAME_client_afk_channel = 

# Channel die ignoriert werden sollen vom Bot oder auf denen nur geachtet werden sollen
# Eine mit Komma getrennte Liste (ohne Leerzeichen) mit Channel IDs.
FUNKTIONSNAME_client_afk_channel_io = 

# Channel sollen ignoriert oder nur diese sollen gesehen werden. -> ignore/only
FUNKTIONSNAME_client_afk_channel_watch = ignore

# Gruppen die ignoriert werden sollen vom Bot oder auf denen nur geachtet werden sollen
# Eine mit Komma getrennte Liste (ohne Leerzeichen) mit Gruppen IDs.
FUNKTIONSNAME_client_afk_group_ids = 

# Gruppen sollen ignoriert oder nur diese sollen gesehen werden. -> ignore/only
FUNKTIONSNAME_client_afk_group_watch = ignore
```

#### WelcomeMessage
```
# Willkommensnachricht die der Client erhalten soll.
FUNKTIONSNAME_welcome_message =

# Poke Willkommensnachricht die der Client erhalten soll.
# true/false
FUNKTIONSNAME_welcome_poke_client = false

# Willkommensnachricht die der Client als Poke Nachricht erhalten soll.
# Maximal 100 Zeichen lang. Sollten mehr zeichen vorhanden sein werden diese auf 100 Zeichen gekürzt!
FUNKTIONSNAME_welcome_poke_message =

# Bis wann soll die Nachricht gesendet werden?
# Format dd.MM.yy HH:mm
# empty - ohne Ablaufdatum
FUNKTIONSNAME_welcome_date = empty

# Wie hoft soll die Nachricht gesendet werden? daily/always
# always = Jedes mal wenn der Client auf dem TS joint.
# daily = Nur einmal Pro Tag.
FUNKTIONSNAME_welcome_repeat = always

# Welche Gruppe soll die Nachricht bekommen
# Eine mit Komma getrennte Liste (ohne Leerzeichen) mit Gruppen IDs.
FUNKTIONSNAME_welcome_group_ids =
```

---

Kann nur einmal verwendet werden 
-------------------------------------------------------------------------------------------------------------

#### Ts3Viewer
```
# Hintergrundfarbe vom Ts3Viewer
# Entweder die HTML Farbcodes(#3829FF) oder der Farbenname(black) auf Englisch
FUNKTIONSNAME_ts3_viewer_background_color =

# Textfarbe von den Channeln sowie deren Clients
# Entweder die HTML Farbcodes(#3829FF) oder der Farbenname(black) auf Englisch
FUNKTIONSNAME_ts3_viewer_text_color =

# Es muss ein Ort festgelegt werden wo die html Datei abgelegt werden soll
FUNKTIONSNAME_ts3_viewer_file_location = 

# only important if ts3_server_ip is localhost
FUNKTIONSNAME_ts3_viewer_server_ip = 
```

#### Broadcast
```
# Clients die eine Broadcast Nachricht schicken dÃ¼rfen
# Eine mit Komma getrennte Liste (ohne Leerzeichen) mit der Einzigartigen ID
# Beispiel +z7a/exrm6PqPWXmh+47eJxaCcA=,hXuT3tgCmIF+oeq3RELL9xZaYK8=
FUNKTIONSNAME_broadcast_clients = 
```

#### Friendlist
```
# Join Power die der Channel hÃ¶chstens haben darf um Client zu moven
FUNKTIONSNAME_move_to_friend_needed_join_power = 
```


#### ChannelAutoCreate
```
# Eine mit Komma getrennte Liste (ohne Leerzeichen) mit Parent Channel IDs.
FUNKTIONSNAME_channel_check_subchannel =

# Datei mit Einträgen für Channel Passwörter
FUNKTIONSNAME_channel_check_password_file_path = 
```

#### VersionChecker
```
# Angaben in Stunden
FUNKTIONSNAME_version_check_time =
```

#### AutoRemove
```
# Welche Gruppe soll die Nachricht bekommen
# Eine mit Komma getrennte Liste (ohne Leerzeichen) mit Gruppen IDs.
FUNKTIONSNAME_auto_remove_group_ids =
```

#### AcceptRules
```
# Gast Gruppe bei der ersten Verbindung
FUNKTIONSNAME_accept_rules_first_group =

# Gast Gruppe die nach dem Akzeptieren vergeben werden soll
FUNKTIONSNAME_accept_rules_accepted_group =

# TeamSpeaknamen Trenner
# Eine mit Komma getrennte Liste (ohne Leerzeichen) mit Trennern
FUNKTIONSNAME_accept_rules_name_seperators =

# Nachricht die der Client als Poke Nachricht erhalten soll.
# Maximal 100 Zeichen lang. Sollten mehr zeichen vorhanden sein werden diese auf 100 Zeichen gekürzt!
FUNKTIONSNAME_accept_rules_poke_message =

# Nachricht die der Client erhalten soll.
FUNKTIONSNAME_accept_rules_message_file_path =

# Datei mit Einträgen als Regex Werte
FUNKTIONSNAME_accept_rules_forbidden_file_path = 
```

#### Twitch
```
# Twitch client id
FUNKTIONSNAME_twitch_api_client_id =

# Twitch client secret
FUNKTIONSNAME_twitch_api_client_oauth_token =

# Twitch TS3 Servergruppe ID
FUNKTIONSNAME_twitch_server_group = 

# Pfad ab der serverconfig.cfg
# Config mit der Verbidnung von Twitch Channelnamen zu TS3 UiDs
# Bsp. Config:
#    evillionslive #=# bKBZLHN0/KlgiZRA6FD18ESP/8k=
FUNKTIONSNAME_twitch_config_name = 
```

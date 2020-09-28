# docker-compose example
## for teamspeak server as container on same host
```
version: "2"

services:
ts3bot:
  image: ts3bot:latest
  container_name: ts3bot
  restart: always
  networks:
    front:
      ipv4_address: specific ip for whitelist
  volumes:
    - /path/to/folder/ts3bot:/data

networks:
 front:
     external:
         name: ts3server
```

## for teamspeak server on different host
```
version: "2"

services:
 ts3bot:
   image: ts3bot:latest
   container_name: ts3bot
   restart: always
   volumes:
     - /path/to/folder/ts3bot:/data
```



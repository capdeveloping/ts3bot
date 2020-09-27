package de.ts3bot.app.features;

import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import de.ts3bot.app.models.TS3ServerConfig;
import de.ts3bot.app.models.data.GameServerData;
import de.ts3bot.app.models.functions.FunctionGameServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class UpdateGameServerChannel {
    private final Logger log = LogManager.getLogger(UpdateGameServerChannel.class.getName());
    private TS3Api api;
    private TS3ServerConfig serverConfig;

    public UpdateGameServerChannel( TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void setApi(TS3Api api) {
        this.api = api;
    }

    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void updateChannel(){
        Map<String, FunctionGameServer> gameServers = serverConfig.getFunctionUpdateGameChannelHashMap();
        for (Map.Entry<String, FunctionGameServer> entry : gameServers.entrySet()) {
            String key = entry.getKey();
            if (gameServers.get(key).getGame_type().equals("csgo")){
                updateCSGOChannel(gameServers.get(key));
            }
        }
    }

    private void updateCSGOChannel(FunctionGameServer gameServer){
            /*
            hostname: BDR - CSGO Server Sponsored by Captain
            version : 1.36.9.3/13693 912/7478 secure  [G:1:2708257]
            udp/ip  : 0.0.0.0:27015  (public ip: 195.201.11.104)
            os      :  Linux
            type    :  community dedicated
            map     : de_cache
            players : 0 humans, 0 bots (16/0 max) (hibernating)
            */
        GameServerData gameServerInfo = new GameServerData();
        try{
            SourceServer server = new SourceServer(gameServer.getGame_server_ip(),gameServer.getGame_server_port());
            server.rconAuth(gameServer.getGame_server_rcon_password());
            HashMap<String, Object> status = server.getServerInfo();
            gameServerInfo.setHostname(String.valueOf(status.get("serverName")));
            gameServerInfo.setHumanClients(String.valueOf(status.get("numberOfPlayers")));
            gameServerInfo.setMaxCliente(String.valueOf(status.get("maxPlayers")));
            gameServerInfo.setServerMap(String.valueOf(status.get("mapName")));
            gameServerInfo.setServerIp(gameServer.getGame_server_ip() + ":" + gameServer.getGame_server_port());
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }
        updateChannelById(gameServer, gameServerInfo);
    }

    private void updateChannelById(FunctionGameServer gameServer, GameServerData gameServerData){
        if( ! api.getChannelInfo(gameServer.getGame_server_channel_id()).getName().equals(gameServer.getGame_server_channel_name() + " (" + gameServerData.getHumanClients() + "/" + gameServerData.getMaxCliente() + ")")){
            api.editChannel(gameServer.getGame_server_channel_id(), ChannelProperty.CHANNEL_NAME, gameServer.getGame_server_channel_name() + " (" + gameServerData.getHumanClients() + "/" + gameServerData.getMaxCliente() + ")");
        }
        String urlPassword = "";
        String viewPassword = "";
        if( ! gameServer.getGame_server_password().isEmpty() ){
            urlPassword = ";password/" + gameServer.getGame_server_password();
            viewPassword = "; password " + gameServer.getGame_server_password();
        }

        String desc = "Servername: " + gameServerData.getHostname() + "\n" +
                "Map: " + gameServerData.getServerMap() + "\n" +
                "connect link: [url=steam://connect/" + gameServerData.getServerIp().trim() + urlPassword.trim() + "]" +
                    "connect " + gameServerData.getServerIp() + viewPassword + "[/url]";

        if( ! api.getChannelInfo(gameServer.getGame_server_channel_id()).getDescription().equals(desc)){
            api.editChannel(gameServer.getGame_server_channel_id(), ChannelProperty.CHANNEL_DESCRIPTION, desc);
        }
    }
}

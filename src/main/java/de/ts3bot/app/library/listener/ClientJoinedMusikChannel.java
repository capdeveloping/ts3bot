package de.ts3bot.app.library.listener;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import de.ts3bot.app.models.TS3ServerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientJoinedMusikChannel extends TS3EventAdapter {
    private final Logger log = LogManager.getLogger(ClientJoinedMusikChannel.class.getName());
    private TS3Api api;
    private TS3ServerConfig serverConfig;

    public ClientJoinedMusikChannel(TS3Api api, TS3ServerConfig serverConfig) {
        this.api = api;
        this.serverConfig = serverConfig;
    }

    //region setter
    public void setApi(TS3Api api) {
        this.api = api;
    }

    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }
    //endregion


    @Override
    public void onTextMessage(TextMessageEvent e) {
        super.onTextMessage(e);
    }

    @Override
    public void onClientMoved(ClientMovedEvent e) {
        if ( e.getTargetChannelId() ==  3638 ) {
            
        }
    }
}

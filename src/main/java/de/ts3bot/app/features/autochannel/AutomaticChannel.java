package de.ts3bot.app.features.autochannel;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import de.ts3bot.app.models.AutomaticChannelProperty;
import de.ts3bot.app.models.TS3ServerConfig;

import java.util.*;

public class AutomaticChannel extends TS3EventAdapter{
    private TS3Api api;
    private TS3ServerConfig serverConfig;
    private HashMap<Integer, AutomaticChannelProperty> channels;
    private CreateAutomaticChannel createAutomaticChannel;
    private DeleteAutomaticChannel deleteAutomaticChannel;

    public AutomaticChannel(TS3ServerConfig serverConfig){
        this.serverConfig = serverConfig;
        channels = new HashMap<>();
        deleteAutomaticChannel = new DeleteAutomaticChannel(channels);
        createAutomaticChannel = new CreateAutomaticChannel(channels);
        reinitializeACP();
    }

//region setter
    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        channels.clear();
        reinitializeACP();
        getAllSubChannelsForPCIDs();
    }

    public void setApi(TS3Api api) {
        this.api = api;
        deleteAutomaticChannel.setApi(api);
        createAutomaticChannel.setApi(api);
        getAllSubChannelsForPCIDs();
    }

    public void setStopFunction(boolean stopTask){
        channels.clear();
        if (stopTask){
            createAutomaticChannel.setChannels(channels);
            deleteAutomaticChannel.setChannels(channels);
        }else{
            reinitializeACP();
            getAllSubChannelsForPCIDs();
            createAutomaticChannel.setChannels(channels);
            deleteAutomaticChannel.setChannels(channels);
        }
        createAutomaticChannel.setStopFunction(stopTask);
        deleteAutomaticChannel.setStopFunction(stopTask);
    }
//endregion

    public void regisListener(){
        api.addTS3Listeners(deleteAutomaticChannel);
        api.addTS3Listeners(createAutomaticChannel);
    }

    private void reinitializeACP(){
        for (int id : serverConfig.getChannelidlist()){
            if (channels.isEmpty()){
                channels.put(id, new AutomaticChannelProperty(""));
            }else {
                if ( !channels.containsKey(id))
                    channels.put(id, new AutomaticChannelProperty(""));
            }
        }
    }

    private void getAllSubChannelsForPCIDs(){
        for (Channel chan : api.getChannels()){
            if ( channels.containsKey(chan.getParentChannelId())){
                if (channels.get(chan.getParentChannelId()).getBaseChannelName().equals("")) {
                    String channelName = chan.getName().substring( 0 , chan.getName().lastIndexOf(' ') );
                    channels.get(chan.getParentChannelId()).setBaseChannelName(channelName);
                }
                if(channels.get(chan.getParentChannelId()).getCidList().size() != 0){
                    channels.get(chan.getParentChannelId()).addCidTolist(chan.getId(), chan.getName());
                    continue;
                }
                channels.get(chan.getParentChannelId()).addCidTolist(chan.getId(), chan.getName());
                channels.get(chan.getParentChannelId()).setChannelPermissions(api.getChannelPermissions(chan.getId()));
                channels.get(chan.getParentChannelId()).setMaxChannelClients( String.valueOf(chan.getMaxClients()) );

                boolean hasUnlimitedCLients = api.getChannelInfo(chan.getId()).hasUnlimitedClients();
                if(hasUnlimitedCLients){
                    channels.get(chan.getParentChannelId()).setHasUnlimitedClients("1");
                }else{
                    channels.get(chan.getParentChannelId()).setHasUnlimitedClients("0");
                }

                channels.get(chan.getParentChannelId()).setChannelDescription(api.getChannelInfo(chan.getId()).getDescription());
            }
        }
    }
}

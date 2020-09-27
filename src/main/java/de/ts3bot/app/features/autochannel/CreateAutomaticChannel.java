package de.ts3bot.app.features.autochannel;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.Permission;
import de.ts3bot.app.manager.FormatManager;
import de.ts3bot.app.models.AutomaticChannelProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.nio.ch.ChannelInputStream;

import java.util.*;

public class CreateAutomaticChannel extends TS3EventAdapter {
    private TS3Api api;
    private Map<Integer, AutomaticChannelProperty> channels;
    private boolean stopFunction;
    private final Logger log = LogManager.getLogger(CreateAutomaticChannel.class);

    CreateAutomaticChannel(Map<Integer, AutomaticChannelProperty> channels){
        this.channels = channels;
        stopFunction = false;
    }

//region setter
    public void setApi(TS3Api api) {
        this.api = api;
    }

    public void setChannels(Map<Integer, AutomaticChannelProperty> channels) {
        this.channels = channels;
    }

    public void setStopFunction(boolean stopFunction) {
        this.stopFunction = stopFunction;
    }
//endregion


    @Override
    public void onClientJoin(ClientJoinEvent e) {
        if (stopFunction)
            return;

        createChannelAutomatic( api.getChannelInfo(e.getClientTargetId()) );
    }

    @Override
    public void onClientMoved(ClientMovedEvent e) {
        if (stopFunction)
            return;

        createChannelAutomatic( api.getChannelInfo(e.getTargetChannelId()) );
    }

    private void createChannelAutomatic(ChannelInfo channelinfo){
        int cpid = channelinfo.getParentChannelId();
        if (channels.containsKey(cpid)){
            createChannel(cpid);
        }
    }

    @Override
    public void onChannelCreate(ChannelCreateEvent e){
        if (stopFunction)
            return;

        int parentChannelId = Integer.parseInt(e.getMap().get("cpid"));
        if (channels.containsKey(parentChannelId)){
            channels.get(parentChannelId).addCidTolist(Integer.parseInt(e.getMap().get("cid")), e.getMap().get("channel_name"));
            try {
                setChannelPermissions(Integer.parseInt(e.getMap().get("cid")), parentChannelId);
            }catch(Exception ex){
                log.error("Couldn't set Channel Permission for cid: '{}'. Message: {}", e.getMap().get("cid"), FormatManager.StackTraceToString(ex));
            }
        }
    }

    private void setChannelPermissions(int channelId, int parentChannelId){
        for (Permission perm : channels.get(parentChannelId).getChannelPermissions()) {
            api.addChannelPermission(channelId, perm.getName(), perm.getValue());
        }
    }

    private void createChannel(int parentChannelId){
        // Let's customize our channel
        final Map<ChannelProperty, String> properties = new HashMap<>();
        // Make it a permanent channel
        properties.put(ChannelProperty.CHANNEL_FLAG_PERMANENT, "1");
        // Make it a subchannel
        properties.put(ChannelProperty.CPID, String.valueOf(parentChannelId));

        if(channels.get(parentChannelId).hasUnlimitedClients().equals("0")){
            properties.put(ChannelProperty.CHANNEL_FLAG_MAXCLIENTS_UNLIMITED, channels.get(parentChannelId).hasUnlimitedClients() );

            properties.put(ChannelProperty.CHANNEL_MAXCLIENTS, channels.get(parentChannelId).getMaxChannelClients() );
        }

        // Let's also set a channel topic
        properties.put(ChannelProperty.CHANNEL_DESCRIPTION, channels.get(parentChannelId).getChannelDescription());

        // Done customizing, let's create the channel with our properties
        //
        int empty = 0;
        int exist = 0;
        for (Channel channel : api.getChannels()){
            if ( channel.getParentChannelId() == parentChannelId ) {
                exist++;
            }
            if ( channel.getParentChannelId() == parentChannelId && channel.getTotalClients() == 0 ) {
                empty++;
            }
            if (channel.getParentChannelId() == parentChannelId && channels.get(parentChannelId).getNextChannelName().equals(channel.getName())){
                channels.get(parentChannelId).manageNextChannelName("");
            }
        }
        if (empty < 1 || (exist > empty && empty < 1)){
            api.createChannel(channels.get(parentChannelId).getNextChannelName(), properties);
            channels.get(parentChannelId).manageNextChannelName("");
        }
    }
}
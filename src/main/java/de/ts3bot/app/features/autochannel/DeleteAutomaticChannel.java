package de.ts3bot.app.features.autochannel;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import de.ts3bot.app.models.AutomaticChannelProperty;
import de.ts3bot.app.models.CollectData;

import java.util.HashMap;
import java.util.Map;

public class DeleteAutomaticChannel extends TS3EventAdapter {
    private TS3Api api;
    private Map<Integer, AutomaticChannelProperty> channels;
    private boolean stopFunction;
    private CollectData collectData;

    DeleteAutomaticChannel(Map<Integer, AutomaticChannelProperty> channels, CollectData collectData){
        this.channels = channels;
        this.collectData = collectData;
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
    public void onClientMoved(ClientMovedEvent e) {
        if (stopFunction)
            return;

        searchAndDeleteChannel();
    }

    @Override
    public void onClientLeave(ClientLeaveEvent e) {
        if (stopFunction)
            return;

        searchAndDeleteChannel();
    }

    private void searchAndDeleteChannel(){
        // parentChannelID  | Anzahl der leeren Channels
        HashMap<Integer, Integer> emptychannels = new HashMap<>();
        boolean lowestNameFound = false;
        String lastChannelName = "";
        for (Channel channel : api.getChannels()) {
            int parentChannelId = channel.getParentChannelId();
            if (channels.containsKey(parentChannelId) && channel.getTotalClients() == 0){
                if ( ! emptychannels.containsKey(parentChannelId) ) {
                    emptychannels.put(parentChannelId, 1);
                }else{
                    emptychannels.replace(parentChannelId, 2);
                }
                if (lastChannelName.equals("")) {
                    lastChannelName = channel.getName();
                }
            }
            if (channels.containsKey(parentChannelId) && channel.getTotalClients() == 0 && emptychannels.get(parentChannelId) > 1){
                channels.get(parentChannelId).removeCidFromList(channel.getId());
                api.deleteChannel(channel.getId());
                collectData.increaseChannelDeleteCounter();
                if ( ! lowestNameFound) {
                    channels.get(parentChannelId).manageNextChannelName(lastChannelName);
                    lowestNameFound = true;
                }
            }
        }
    }
}

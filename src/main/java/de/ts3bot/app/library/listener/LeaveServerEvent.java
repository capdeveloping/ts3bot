package de.ts3bot.app.library.listener;

import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import de.ts3bot.app.features.ClientAfkMode;

import java.util.List;

/**
 * Created by Patrice on 05.11.2016.
 *
 */
public class LeaveServerEvent extends TS3EventAdapter {
    private List<ClientAfkMode> clientAfkMode;

    public void setClientAfkMode(List<ClientAfkMode> clientAfkMode) {
        this.clientAfkMode = clientAfkMode;
    }

    @Override
    public void onClientLeave(ClientLeaveEvent e){
        if(clientAfkMode != null){
            for (ClientAfkMode afkMode : clientAfkMode){
                afkMode.deleteLeftAfkClients();
            }
        }
    }
}

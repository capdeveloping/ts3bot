package de.ts3bot.app.features;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.ts3bot.app.models.functions.FunctionModel;

import java.util.Timer;
import java.util.TimerTask;

public class AutoRemove {
    private boolean stopFunction;
    private FunctionModel autoRemove;
    private Timer timer;
    private TS3Api api;

    public AutoRemove(FunctionModel autoRemove) {
        this.stopFunction = false;
        this.autoRemove = autoRemove;
        timer = new Timer();
    }

    public void setStopFunction(boolean stopFunction) {
        this.stopFunction = stopFunction;
    }

    public void setAutoRemove(FunctionModel autoRemove) {
        this.autoRemove = autoRemove;
    }

    public void setApi(TS3Api api) {
        this.api = api;
    }

    private void checkForGroupsToRemove(){
        for (Client client : api.getClients()){
            int[]groups = client.getServerGroups();
            for (int group : groups){
                if(autoRemove.getGruppenIDs().contains(group) && groups.length >= 2){
                    api.removeClientFromServerGroup(group, client.getDatabaseId());
                }
            }
        }
    }

    public void checkForGroupsToRemoveTimer(){
        int delaySeconds = 5 * 1000;
        int periodSeconds = 1000;
        if( ! stopFunction ){
            timer.schedule(new TimerTask() {
                public void run() {
                    checkForGroupsToRemove();
                }
            }, delaySeconds, periodSeconds);
        } else{
            timer.cancel();
            timer.purge();
        }
    }
}

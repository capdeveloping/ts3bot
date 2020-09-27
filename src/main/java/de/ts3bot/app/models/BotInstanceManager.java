package de.ts3bot.app.models;

import java.util.HashMap;
import java.util.Map;

public class BotInstanceManager {

    private Map<String, InstanceThread > instanceList;

    public BotInstanceManager() {
        if (instanceList == null)
            instanceList = new HashMap<>();
    }

    public void setNewThread(String botname, String configpath){
        instanceList.put(botname, getNewThread(configpath, botname, this));
    }

    public void stopThread(String instanceName){
        instanceList.get(instanceName).stopBot();
    }

    public void startThread(String instanceName){
        instanceList.get(instanceName).startBot();
    }

    public Map<String, InstanceThread> getInstanceList() {
        return instanceList;
    }

    private InstanceThread getNewThread(String configpath, String botname, BotInstanceManager botInstanceManager){
        return new InstanceThread(configpath,botname,botInstanceManager);
    }
}

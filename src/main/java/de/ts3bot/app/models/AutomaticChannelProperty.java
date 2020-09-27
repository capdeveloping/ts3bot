package de.ts3bot.app.models;


import com.github.theholywaffle.teamspeak3.api.wrapper.Permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.ts3bot.app.manager.RomanConverter.toNumerical;
import static de.ts3bot.app.manager.RomanConverter.toRoman;

public class AutomaticChannelProperty {
    private Map<Integer, String> cidList;
    private String baseChannelName;
    private String nextChannelName;
    private int channelnumber;
    private List<Permission> channelPermissions;
    private String channelDescription;
    private String maxChannelClients;
    private String hasUnlimitedClients; // boolean with 0 and 1

    public AutomaticChannelProperty(String channelName){
        cidList = new HashMap<>();
        channelnumber = 2;
        this.baseChannelName = channelName;
}

    public void removeCidFromList(int cid){
        cidList.remove(cid);
    }

    public void manageNextChannelName(String channelName){
        if (channelName.equals(""))
            channelName = this.nextChannelName;
        channelnumber = toNumerical(channelName.replace(this.baseChannelName + " ","")) + 1;
        checkAndSetNextChannelName(toRoman(channelnumber));
    }

    private void checkAndSetNextChannelName(String nextChannelNumber){
        boolean nameexist;
        setNextChannelName(nextChannelNumber);
        do {
            nameexist = false;
            for (Map.Entry<Integer, String> entry : cidList.entrySet()) {
                Integer key = entry.getKey();
                if (nextChannelName.equals(cidList.get(key))){
                    nameexist = true;
                    channelnumber++;
                    setNextChannelName(toRoman(channelnumber));
                    break;
                }
            }
        }while(nameexist);
    }

//region getter
    public String hasUnlimitedClients() {
        return hasUnlimitedClients;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public String getMaxChannelClients() {
        return maxChannelClients;
    }

    public String getBaseChannelName() {
        return baseChannelName;
    }

    public String getNextChannelName() {
        return nextChannelName;
    }

    public Map<Integer, String> getCidList() {
        return cidList;
    }

    public List<Permission> getChannelPermissions() {
        return channelPermissions;
    }

    //endregion

//region setter
    public void setHasUnlimitedClients(String hasUnlimitedClients) {
        this.hasUnlimitedClients = hasUnlimitedClients;
    }

    public void setChannelDescription(String channelDescription) {
        this.channelDescription = channelDescription;
    }

    public void setMaxChannelClients(String maxChannelClients) {
        this.maxChannelClients = maxChannelClients;
    }

    public void addCidTolist(int cid, String channelName){
        cidList.put(cid, channelName);
    }

    private void setNextChannelName(String nextChannelNumber) {
        this.nextChannelName = baseChannelName + " " + nextChannelNumber;
    }

    public void setBaseChannelName(String channelName) {
        this.baseChannelName = channelName;
        setNextChannelName( toRoman(channelnumber));
    }

    public void setChannelPermissions(List<Permission> channelPermissions) {
        this.channelPermissions = channelPermissions;
    }
    //endregion
}

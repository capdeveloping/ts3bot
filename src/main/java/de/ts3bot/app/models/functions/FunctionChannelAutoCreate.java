package de.ts3bot.app.models.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionChannelAutoCreate {
    private List<Integer> channelidlist;
    private String channelPasswordFilePath;
    private Map<String, String> channelPasswordList;

    public FunctionChannelAutoCreate() {
        this.channelidlist = new ArrayList<>();
        this.channelPasswordList = new HashMap<>();
    }

    public String getChannelPasswordFilePath() {
        return channelPasswordFilePath;
    }

    public void setChannelPasswordFilePath(String channelPasswordFilePath) {
        this.channelPasswordFilePath = channelPasswordFilePath;
    }

    public List<Integer> getChannelidlist() {
        return channelidlist;
    }

    public void setChannelidlist(List<Integer> channelidlist) {
        this.channelidlist = channelidlist;
    }

    public Map<String, String> getChannelPasswordList() {
        return channelPasswordList;
    }

    public void setChannelPasswordList(Map<String, String> channelPasswordList) {
        this.channelPasswordList = channelPasswordList;
    }
}

package de.ts3bot.app.models;

import com.github.theholywaffle.teamspeak3.TS3Query;
import de.ts3bot.app.models.functions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Captain on 12.11.2015.
 * Lade die Ts3 Bot Config
 */
public class TS3ServerConfig {
    private int ts3ServerPort;
    private int ts3BotChannelId;
    private int moveToFriendNeededJoinPower;

    private String configFolder;
    private String configPath;
    private String botName;
    private String language;
    private TS3Query.FloodRate ts3ServerFloodrate;
    private String ts3ServerIp;
    private int ts3ServerQueryPort;
    private String ts3ServerQueryLoginName;
    private String ts3ServerQueryLoginPassword;
    private String ts3BotNickname;
    private String ts3BotNickname2;
    private List<String> botAdmin;
    private List<String> botFullAdmin;

    private String broadcastClients;
    private int ts3ViewerUpdateTime;
    private int versionCheckTime;

    //region TS3 Viewer Variables
    private String ts3ViewerFileLocation;
    private String ts3ViewerBackgroundColor;
    private String ts3ViewerTextColor;
    private String ts3ViewerServerIp;
    //endregion

    //region Twitch Variables
    private String twitchApiClientID;
    private String twitchApiClientOauthToken;
    private String twitchConfigName;
    private int twitchLiveGruppe;
    //endregion

    //region Automatic Channel Variables
    private List<Integer> channelidlist;
    //endregion

    private FunctionAcceptRules functionAcceptRules;
    private FunctionModel functionAutoRemove;
    
    private Map<String, String> functionNames;
    private Map<String, FunctionMove> functionMoveHashMap;
    private Map<String, FunctionAFK> functionAFKHashMap;
    private Map<String, FunctionGameServer> functionGameServerHashMap;
    private Map<String, FunctionWelcomeMessage> functionWelcomeMessageHashMap;

//region methods
    public void addFunctionMove(String key, FunctionMove move){
        if(functionMoveHashMap == null){
            functionMoveHashMap = new HashMap<>();
        }
        functionMoveHashMap.put(key, move);
    }

    public void addFunctionAFK(String key, FunctionAFK afk){
        if(functionAFKHashMap == null){
            functionAFKHashMap = new HashMap<>();
        }
        functionAFKHashMap.put(key, afk);
    }

    public void addFunctionUpdateGameChannel(String key, FunctionGameServer functionGameServer){
        if(functionGameServerHashMap == null){
            functionGameServerHashMap = new HashMap<>();
        }
        functionGameServerHashMap.put(key, functionGameServer);
    }

    public void addFunctionWelcomeMessage(String key, FunctionWelcomeMessage functionWelcomeMessage){
        if(functionWelcomeMessageHashMap == null){
            functionWelcomeMessageHashMap = new HashMap<>();
        }
        functionWelcomeMessageHashMap.put(key, functionWelcomeMessage);
    }
//endregion

//region getter
    public FunctionModel getFunctionAutoRemove() {
        return functionAutoRemove;
    }

    public FunctionAcceptRules getFunctionAcceptRules() {
        return functionAcceptRules;
    }

    public int getTwitchLiveGruppe() {
        return twitchLiveGruppe;
    }

    public String getTwitchApiClientID() {
        return twitchApiClientID;
    }

    public String getTwitchApiClientOauthToken() {
        return twitchApiClientOauthToken;
    }

    public String getTwitchConfigName() {
        return twitchConfigName;
    }

    public int getTs3ViewerUpdateTime() {
        return ts3ViewerUpdateTime;
    }

    public String getConfigPath() {
        return configPath;
    }

    public String getConfigFolder() {
        return configFolder;
    }

    public String getBotName() {
        return botName;
    }

    public int getTs3ServerQueryPort() {
        return ts3ServerQueryPort;
    }

    public TS3Query.FloodRate getTs3ServerFloodrate() {
        return ts3ServerFloodrate;
    }

    public Map<String, FunctionGameServer> getFunctionUpdateGameChannelHashMap() {
        return functionGameServerHashMap;
    }

    public int getVersionCheckTime() {
        return versionCheckTime;
    }

    public List<Integer> getChannelidlist() {
        return channelidlist;
    }

    public String getTs3ViewerFileLocation() {
        return ts3ViewerFileLocation;
    }

    public Map<String, String> getFunctionNames() {
        return functionNames;
    }

    public Map<String, FunctionMove> getFunctionMoveHashMap() {
        return functionMoveHashMap;
    }

    public Map<String, FunctionAFK> getFunctionAFKHashMap() {
        return functionAFKHashMap;
    }

    public Map<String, FunctionGameServer> getFunctionGameServerHashMap() {
        return functionGameServerHashMap;
    }

    public Map<String, FunctionWelcomeMessage> getFunctionWelcomeMessageHashMap() {
        return functionWelcomeMessageHashMap;
    }

    public String getTs3ViewerBackgroundColor() {
        return ts3ViewerBackgroundColor;
    }

    public String getTs3ViewerTextColor() {
        return ts3ViewerTextColor;
    }

    public int getMoveToFriendNeededJoinPower() {return moveToFriendNeededJoinPower;}

    public int getTs3BotChannelId() {
        return ts3BotChannelId;
    }

    public int getTs3ServerPort() {
        return ts3ServerPort;
    }

    public List<String> getBotAdmin() {
        return botAdmin;
    }

    public List<String> getBotFullAdmin() {
        return botFullAdmin;
    }

    public String getTs3BotNickname() {
        return ts3BotNickname;
    }

    public String getTs3BotNickname2() {
        return ts3BotNickname2;
    }

    public String getTs3ServerIp() {
        return ts3ServerIp;
    }

    public String getTs3ServerQueryLoginName() {
        return ts3ServerQueryLoginName;
    }

    public String getTs3ServerQueryLoginPassword() {
        return ts3ServerQueryLoginPassword;
    }

    public String getBroadcastClients() {
        return broadcastClients;
    }

    public String getLanguage() {
        return language;
    }

    public String getTs3ViewerServerIp() {
        return ts3ViewerServerIp;
    }

    //endregion

//region setter
    public void setFunctionAutoRemove(FunctionModel functionAutoRemove) {
        this.functionAutoRemove = functionAutoRemove;
    }

    public void setFunctionAcceptRules(FunctionAcceptRules functionAcceptRules) {
        this.functionAcceptRules = functionAcceptRules;
    }

    public void setTwitchLiveGruppe(int twitchLiveGruppe) {
        this.twitchLiveGruppe = twitchLiveGruppe;
    }

    public void setTwitchApiClientID(String twitchApiClientID) {
        this.twitchApiClientID = twitchApiClientID;
    }

    public void setTwitchApiClientOauthToken(String twitchApiClientOauthToken) {
        this.twitchApiClientOauthToken = twitchApiClientOauthToken;
    }

    public void setTwitchConfigName(String twitchConfigName) {
        this.twitchConfigName = twitchConfigName;
    }

    public void setTs3ViewerUpdateTime(int ts3ViewerUpdateTime) {
        this.ts3ViewerUpdateTime = ts3ViewerUpdateTime;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public void setConfigFolder(String configFolder) {
        this.configFolder = configFolder;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public void setTs3ServerQueryPort(int ts3ServerQueryPort) {
        this.ts3ServerQueryPort = ts3ServerQueryPort;
    }

    public void setTs3ServerFloodrate(TS3Query.FloodRate ts3ServerFloodrate) {
        this.ts3ServerFloodrate = ts3ServerFloodrate;
    }

    public void setFunctionGameServerHashMap(Map<String, FunctionGameServer> functionGameServerHashMap) {
        this.functionGameServerHashMap = functionGameServerHashMap;
    }

    public void setFunctionWelcomeMessageHashMap(Map<String, FunctionWelcomeMessage> functionWelcomeMessageHashMap) {
        this.functionWelcomeMessageHashMap = functionWelcomeMessageHashMap;
    }

    public void setVersionCheckTime(int versionCheckTime) {
        this.versionCheckTime = versionCheckTime;
    }

    public void setChannelidlist(List<Integer> channelidlist) {
        this.channelidlist = channelidlist;
    }

    public void setTs3ViewerServerIp(String ts3ViewerServerIp) {
        this.ts3ViewerServerIp = ts3ViewerServerIp;
    }

    public void setTs3ViewerFileLocation(String ts3ViewerFileLocation) {
        this.ts3ViewerFileLocation = ts3ViewerFileLocation;
    }

    public void setFunctionAFKHashMap(Map<String, FunctionAFK> functionAFKHashMap) {
        this.functionAFKHashMap = functionAFKHashMap;
    }

    public void setFunctionMoveHashMap(Map<String, FunctionMove> functionMoveHashMap) {
        this.functionMoveHashMap = functionMoveHashMap;
    }

    public void setFunctionNames(Map<String, String> functionNames) {
        this.functionNames = functionNames;
    }

    public void setTs3ViewerTextColor(String ts3ViewerTextColor) {
        this.ts3ViewerTextColor = ts3ViewerTextColor;
    }

    public void setTs3ViewerBackgroundColor(String ts3ViewerBackgroundColor) {
        this.ts3ViewerBackgroundColor = ts3ViewerBackgroundColor;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setMoveToFriendNeededJoinPower(int moveToFriendNeededJoinPower) {
        this.moveToFriendNeededJoinPower = moveToFriendNeededJoinPower;
    }

    public void setBotAdmin(List<String> botAdmin) {
        this.botAdmin = botAdmin;
    }

    public void setTs3BotChannelId(int ts3BotChannelId) {
        this.ts3BotChannelId = ts3BotChannelId;
    }

    public void setBotFullAdmin(List<String> botFullAdmin) {
        this.botFullAdmin = botFullAdmin;
    }

    public void setTs3BotNickname(String ts3BotNickname) {
        this.ts3BotNickname = ts3BotNickname;
    }

    public void setTs3BotNickname2(String ts3BotNickname2) {
        this.ts3BotNickname2 = ts3BotNickname2;
    }

    public void setTs3ServerIp(String ts3ServerIp) {
        this.ts3ServerIp = ts3ServerIp;
    }

    public void setTs3ServerPort(int ts3ServerPort) {
        this.ts3ServerPort = ts3ServerPort;
    }

    public void setTs3ServerQueryLoginName(String ts3ServerQueryLoginName) {
        this.ts3ServerQueryLoginName = ts3ServerQueryLoginName;
    }

    public void setTs3ServerQueryLoginPassword(String ts3ServerQueryLoginPassword) {
        this.ts3ServerQueryLoginPassword = ts3ServerQueryLoginPassword;
    }

    public void setBroadcastClients(String broadcastClients) {
        this.broadcastClients = broadcastClients;
    }
    //endregion
}
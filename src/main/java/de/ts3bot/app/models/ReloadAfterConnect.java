package de.ts3bot.app.models;

import com.github.theholywaffle.teamspeak3.TS3Api;
import de.ts3bot.app.TaskTimer;
import de.ts3bot.app.features.*;
import de.ts3bot.app.features.autochannel.AutomaticChannel;
import de.ts3bot.app.library.configload.TS3TextLoad;
import de.ts3bot.app.library.listener.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PAPA-TS-NB on 30.01.2016.
 * Aktualisiere alles was sich auf der Variable bezieht
 */
public class ReloadAfterConnect {
    private List<String> deactivatedFuntions;
    private TS3Api api;
    private TS3ServerConfig serverConfig;
    private TS3TextLoad ts3TextLoad;
    private TS3Viewer ts3Viewer;
    private MoveToClient moveToClient;
    private List<ClientAfkMode> clientAfkMode;
    private List<ClientJoinedChannelEvent> clientMovedEvent;
    private TaskTimer taskTimer;
    private BroadcastMessage broadcastMessage;
    private AdminMessageEvent adminMessageEvent;
    private UserMessageEvent userMessageEvent;
    private NewVersionChecker versionChecker;
    private AutomaticChannel automaticChannel;
    private UpdateGameServerChannel updateGameServerChannel;
    private WelcomeMessageEvent welcomeMessageEvent;
    private TwitchController twitchController;
    private AcceptRulesEvent acceptRulesEvent;
    private AutoRemove autoRemove;
    private CollectData collectData;

    ReloadAfterConnect(TS3ServerConfig serverConfig, TS3TextLoad ts3TextLoad, MoveToClient moveToClient,
                       List<ClientAfkMode> clientAfkMode, TaskTimer taskTimer, BroadcastMessage broadcastMessage, UserMessageEvent userMessageEvent,
                       AdminMessageEvent adminMessageEvent, NewVersionChecker versionChecker, TS3Viewer ts3Viewer, AutomaticChannel automaticChannel,
                       UpdateGameServerChannel updateGameServerChannel, List<ClientJoinedChannelEvent> clientMovedEvent,
                       WelcomeMessageEvent welcomeMessageEvent, TwitchController twitchController, AcceptRulesEvent acceptRulesEvent,
                       AutoRemove autoRemove, CollectData collectData) {
        this.serverConfig = serverConfig;
        this.ts3TextLoad = ts3TextLoad;
        this.moveToClient = moveToClient;
        this.clientAfkMode = clientAfkMode;
        this.taskTimer = taskTimer;
        this.adminMessageEvent = adminMessageEvent;
        this.broadcastMessage = broadcastMessage;
        this.userMessageEvent = userMessageEvent;
        this.ts3Viewer = ts3Viewer;
        this.versionChecker = versionChecker;
        this.automaticChannel = automaticChannel;
        this.clientMovedEvent = clientMovedEvent;
        deactivatedFuntions = new ArrayList<>();
        this.updateGameServerChannel = updateGameServerChannel;
        this.welcomeMessageEvent = welcomeMessageEvent;
        this.twitchController = twitchController;
        this.acceptRulesEvent = acceptRulesEvent;
        this.autoRemove = autoRemove;
        this.collectData = collectData;
    }

    void updateAll(){
        serverConfigUpdated();
    }

    void setMessanger(BroadcastMessage broadcastMessage, AdminMessageEvent adminMessageEvent,
                      UserMessageEvent userMessageEvent, WelcomeMessageEvent welcomeMessageEvent,
                      AcceptRulesEvent acceptRulesEvent){
        if(broadcastMessage != null) {
            this.broadcastMessage = broadcastMessage;
        }
        if(welcomeMessageEvent != null) {
            this.welcomeMessageEvent = welcomeMessageEvent;
        }
        if(acceptRulesEvent != null) {
            this.acceptRulesEvent = acceptRulesEvent;
        }
        this.adminMessageEvent = adminMessageEvent;
        this.userMessageEvent = userMessageEvent;
    }

    void apiUpdated(TS3Api api){
        this.api = api;
        adminMessageEvent.setApi(api);
        userMessageEvent.setApi(api);
        collectData.setApi(api);
        if(versionChecker != null) {
            versionChecker.setApi(api);
        }
        if(automaticChannel != null) {
            automaticChannel.setApi(api);
        }
        if(moveToClient != null) {
            moveToClient.setApi(api);
        }
        if(broadcastMessage != null) {
            broadcastMessage.setApi(api);
        }
        if(acceptRulesEvent != null) {
            acceptRulesEvent.setApi(api);
        }
        if(updateGameServerChannel != null) {
            updateGameServerChannel.setApi(api);
        }
        if(welcomeMessageEvent != null) {
            welcomeMessageEvent.setApi(api);
        }
        if(twitchController != null) {
            twitchController.setApi(api);
        }
        if(autoRemove != null) {
            autoRemove.setApi(api);
        }
        if(clientAfkMode != null) {
            for (ClientAfkMode afkMode : clientAfkMode) {
                afkMode.setApi(api);
            }
        }
        if(clientMovedEvent != null) {
            for (ClientJoinedChannelEvent clientMoved : clientMovedEvent) {
                clientMoved.setApi(api);
            }
        }
        if(ts3Viewer != null){
            ts3Viewer.setApi(api);
        }
    }

    public void serverConfigUpdated(){
        taskTimer.setServerConfig(serverConfig);
        adminMessageEvent.setServerConfig(serverConfig);
        ts3TextLoad.setTs3ServerConfig(serverConfig);
        collectData.setServerConfig(serverConfig);
        if(automaticChannel != null) {
            automaticChannel.setServerConfig(serverConfig);
        }
        if(acceptRulesEvent != null) {
            acceptRulesEvent.setServerConfig(serverConfig);
        }
        if(moveToClient != null) {
            moveToClient.setServerConfig(serverConfig);
        }
        if(welcomeMessageEvent != null) {
            welcomeMessageEvent.setServerConfig(serverConfig);
        }
        if(ts3Viewer != null){
            ts3Viewer.setServerConfig(serverConfig);
        }
        if(broadcastMessage != null){
            broadcastMessage.setServerConfig(serverConfig);
        }
        if(twitchController != null){
            twitchController.setServerConfig(serverConfig);
        }
        if(clientMovedEvent != null) {
            for (ClientJoinedChannelEvent clientMoved : clientMovedEvent) {
                clientMoved.setServerConfig(serverConfig);
                clientMoved.setFunctionMove(serverConfig.getFunctionMoveHashMap().get(clientMoved.getClientMovedKey()));
            }
        }
        if(autoRemove != null) {
            autoRemove.setAutoRemove(serverConfig.getFunctionAutoRemove());
        }
        if(clientAfkMode != null) {
            for (ClientAfkMode afkMode : clientAfkMode) {
                afkMode.setFunctionAFK( serverConfig.getFunctionAFKHashMap().get(afkMode.getClientAfkModeKey()) );
                afkMode.setServerConfig(serverConfig);
            }
        }
        if(updateGameServerChannel != null) {
            updateGameServerChannel.setServerConfig(serverConfig);
        }
        if(versionChecker != null){
            versionChecker.setBotFullAdminList(serverConfig.getBotFullAdmin());
        }
    }
//region setter/getter
    public TS3Api getApi() {
        return api;
    }

    public void setApi(TS3Api api) {
        this.api = api;
    }

    public TS3ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

//endregion
}

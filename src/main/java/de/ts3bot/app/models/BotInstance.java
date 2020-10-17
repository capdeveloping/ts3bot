package de.ts3bot.app.models;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.ts3bot.app.TaskTimer;
import de.ts3bot.app.features.*;
import de.ts3bot.app.features.autochannel.AutomaticChannel;
import de.ts3bot.app.library.configload.TS3Controller;
import de.ts3bot.app.library.configload.TS3ConfigWrite;
import de.ts3bot.app.library.configload.TS3TextLoad;
import de.ts3bot.app.library.listener.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Captain on 19.11.2015.
 * Bots starten
 */
public class BotInstance {
    private final Logger log = LogManager.getLogger(BotInstance.class.getName());
    private TS3Controller ts3Instance;
    private TaskTimer taskTimer;
    private ReloadAfterConnect reloadAfterConnect;
    private BroadcastMessage broadcastMessage;
    private AdminMessageEvent adminMessageEvent;
    private UserMessageEvent userMessageEvent;
    private List<ClientAfkMode> clientAfkMode;


    //region Einzelne Inhalte müssen erneuert werden
    private TS3Viewer ts3viewer;
    private AutomaticChannel automaticChannel;
    private TS3Api api;
    private TS3ServerConfig serverConfig;
    private MoveToClient moveToClient;
    private NewVersionChecker versionChecker;
    private LeaveServerEvent leaveEvent;
    private UpdateGameServerChannel updateGameChannel;
    private List<ClientJoinedChannelEvent> clientMoveEvent;
    private WelcomeMessageEvent welcomeMessageEvent;
    private TwitchController twitchController;
    private AcceptRulesEvent acceptRulesEvent;
    private AutoRemove autoRemove;
    //endregion

    public BotInstance(TS3Controller ts3Instance, TS3ServerConfig serverConfig, BotInstanceManager botInstanceManager) {
        this.ts3Instance = ts3Instance;
        this.serverConfig = serverConfig;

        TS3TextLoad ts3TextLoad = new TS3TextLoad(serverConfig);
        TS3ConfigWrite configWrite = new TS3ConfigWrite(serverConfig, reloadAfterConnect);
        leaveEvent = new LeaveServerEvent();
        functionsOnOff();
        taskTimer = new TaskTimer(clientAfkMode, serverConfig, serverConfig.getBotName(), versionChecker, ts3viewer, updateGameChannel);
        userMessageEvent = new UserMessageEvent(moveToClient);
        adminMessageEvent = new AdminMessageEvent(serverConfig, ts3TextLoad, configWrite, botInstanceManager, twitchController);
        reloadAfterConnect = new ReloadAfterConnect(serverConfig,
                ts3TextLoad,
                configWrite,
                moveToClient,
                clientAfkMode,
                taskTimer,
                broadcastMessage,
                userMessageEvent,
                adminMessageEvent,
                versionChecker,
                ts3viewer,
                automaticChannel,
                updateGameChannel,
                clientMoveEvent,
                welcomeMessageEvent,
                twitchController,
                acceptRulesEvent,
                autoRemove);
        adminMessageEvent.setAfterConnect(reloadAfterConnect, taskTimer, serverConfig.getBotName());
        configWrite.setAfterConnect(reloadAfterConnect);
    }

    private void functionsOnOff(){
        for (String key : serverConfig.getFunctionNames().keySet()) {
            switch (serverConfig.getFunctionNames().get(key)){
                case "ClientAFK":
                    if(clientAfkMode == null) clientAfkMode = new ArrayList<>();
                    clientAfkMode.add(new ClientAfkMode( serverConfig.getFunctionAFKHashMap().get(key), key, serverConfig ));
                    leaveEvent.setClientAfkMode(clientAfkMode);
                    break;
                case "ClientMove":
                    if(clientMoveEvent == null) clientMoveEvent = new ArrayList<>();
                    clientMoveEvent.add(new ClientJoinedChannelEvent( serverConfig.getFunctionMoveHashMap().get(key), key ));
                    break;
                case "Friendlist":
                    moveToClient = new MoveToClient(serverConfig);
                    break;
                case "AcceptRules":
                    acceptRulesEvent = new AcceptRulesEvent(serverConfig);
                    acceptRulesEvent.userChangedNicknameTimer();
                    break;
                case "AutoRemove":
                    autoRemove = new AutoRemove(serverConfig.getFunctionAutoRemove());
                    autoRemove.checkForGroupsToRemoveTimer();
                    break;
                case "ChannelAutoCreate":
                    automaticChannel = new AutomaticChannel(serverConfig);
                    break;
                case "Broadcast":
                    broadcastMessage = new BroadcastMessage(serverConfig);
                    break;
                case "Viewer":
                    ts3viewer = new TS3Viewer(serverConfig);
                    break;
                case "VersionChecker":
                    versionChecker = new NewVersionChecker();
                    break;
                case "UpdateGameChannel":
                    updateGameChannel = new UpdateGameServerChannel(serverConfig);
                    break;
                case "WelcomeMessage":
                    welcomeMessageEvent = new WelcomeMessageEvent(serverConfig);
                    welcomeMessageEvent.deleteUserListTimer();
                    break;
                case "Twitch":
                    twitchController = new TwitchController(serverConfig);
                    break;
                default:
                    break;
            }
        }
    }

    public void stopConfig() {
        startTimer(true);
    }

    public void startConfig() {
        reloadAfterConnect.apiUpdated(ts3Instance.getApi());
        api = ts3Instance.getApi();
        reloadAfterConnect.updateAll();
        log.info( "{}: Switching to standard channel {}", serverConfig.getBotName(), serverConfig.getTs3BotChannelId());
        if (serverConfig.getTs3BotChannelId() != 0) {
            api.moveQuery(serverConfig.getTs3BotChannelId());
        }
        setBotNickname();
        registerListener();
        startTimer(false);
    }

    private void setBotNickname(){
        boolean done = false;
        String botNickname = serverConfig.getTs3BotNickname();
        int counter = 1;
        if (api.whoAmI().getNickname().equals(botNickname)){
            return;
        }

        while( ! done ) {
            boolean found = false;
            for (Client client : api.getClients()) {
                if ( client.getNickname().equals(botNickname)) {
                    found = true;
                    break;
                }
            }
            if( ! found ){
                log.info("{}: Bot nickname free changing to '{}'", serverConfig.getBotName(), botNickname);
                api.setNickname(botNickname);
                done = true;
            }else if (counter < 2){
                log.info("{}: Bot nickname '{}' in use trying '{}'", serverConfig.getBotName(), botNickname, serverConfig.getTs3BotNickname2());
                botNickname = serverConfig.getTs3BotNickname2();
                counter++;
            }else{
                botNickname = RandomStringUtils.random(10, true, true);
                log.info("{}: All Nicknames used, setting a random nickname '{}'", serverConfig.getBotName(), botNickname);
                api.setNickname(botNickname);
                done = true;
            }
        }
    }

    private void startTimer(boolean stopTasks) {
        if( versionChecker != null) {
            log.info( "{}: Starting CheckVersion Timer -> updating every day.", serverConfig.getBotName());
            log.info("{}: Checking for new Bot Version", serverConfig.getBotName());
            taskTimer.checkNewVersion(stopTasks);
        }
        if(clientAfkMode != null) {
            log.info("{}: Starting ClientAfk listener.", serverConfig.getBotName());
            taskTimer.controlClientAfkTimer(stopTasks);
        }
        if(updateGameChannel != null) {
            log.info("{}: Starting updateGameChannel in 15 seconds -> update every 1 minute.", serverConfig.getBotName());
            taskTimer.updateGameChannel(stopTasks);
        }
    }

    private void registerListener() {
        log.info("{}: Register all events.", serverConfig.getBotName());
        api.registerAllEvents();
        log.info("{}: Starting user message listener.", serverConfig.getBotName());
        api.addTS3Listeners(userMessageEvent);
        log.info("{}: Starting admin message listener.", serverConfig.getBotName());
        api.addTS3Listeners(adminMessageEvent);
        log.info("{}: Starting leave server listener.", serverConfig.getBotName());
        api.addTS3Listeners(leaveEvent);
        if(automaticChannel != null) {
            log.info("{}: Starting automatic channel creation listener.", serverConfig.getBotName());
            automaticChannel.regisListener();
        }
        if(welcomeMessageEvent != null) {
            log.info("{}: Starting welcome message listener.", serverConfig.getBotName());
            api.addTS3Listeners(welcomeMessageEvent);
        }
        if(broadcastMessage != null) {
            log.info("{}: Starting broadcast message listener.", serverConfig.getBotName());
            api.addTS3Listeners(broadcastMessage);
        }
        if(acceptRulesEvent != null) {
            log.info("{}: Starting accept rules listener.", serverConfig.getBotName());
            api.addTS3Listeners(acceptRulesEvent);
        }
        if(versionChecker != null) {
            log.info("{}: Starting version checker listener.", serverConfig.getBotName());
            api.addTS3Listeners(versionChecker);
        }
        if(clientMoveEvent != null) {
            for(ClientJoinedChannelEvent moveEvent : clientMoveEvent){
                log.info("{}: Starting client move event listener.", serverConfig.getBotName());
                api.addTS3Listeners(moveEvent);
            }
        }
        reloadAfterConnect.setMessanger(broadcastMessage, adminMessageEvent, userMessageEvent, welcomeMessageEvent, acceptRulesEvent);
    }
}
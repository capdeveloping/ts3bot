package de.ts3bot.app.features;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.events.ChannelGoOfflineEvent;
import de.ts3bot.app.manager.FormatManager;
import de.ts3bot.app.manager.ListManager;
import de.ts3bot.app.models.TS3ServerConfig;
import de.ts3bot.app.models.Twitchuser;
import de.ts3bot.app.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TwitchController {
    private final Logger log = LogManager.getLogger(TwitchController.class.getName());
    private TwitchClient client;
    private List<Twitchuser> users;
    private TS3ServerConfig serverConfig;
    private TS3Api api;

    public TwitchController(TS3ServerConfig serverConfig){
        this.serverConfig = serverConfig;
        users = new ArrayList<>();
        readConfigFile();
        twitchConnect();
    }

    private void readConfigFile(){
        if( ! ListManager.fileExist(serverConfig.getTwitchConfigName())){
            createTwitchChannelNameToUidConfig();
            return;
        }
        try {
            users.clear();
            List<String> userList = new ArrayList<>();
            userList.addAll(ListManager.readLists(serverConfig.getTwitchConfigName()));
            for (String userStr : userList) {
                if ( ! userStr.startsWith("#") && ! userStr.equals("")) {
                    String[] clientData = userStr.split(" #=# ");
                    users.add(new Twitchuser(clientData[0], clientData[1], Boolean.parseBoolean(clientData[2])));
                }
            }
        }
        catch (Exception ex){
            log.error( "{}: Fehler beim lesen des Backupfiles: {}", serverConfig.getBotName(), serverConfig.getTwitchConfigName());
            log.error( FormatManager.StackTraceToString(ex) );
        }
    }

    private void createTwitchChannelNameToUidConfig(){
        log.info("create twitch channel name to uid file");
        try (FileWriter fileWriter = new FileWriter(serverConfig.getTwitchConfigName());
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("");
        }
        catch(IOException ex) {
            log.error("writing to file: {}", ex.getMessage());
        }
    }

    private void twitchConnect(){
        try {
            client = TwitchClientBuilder.builder()
                    .withClientId(serverConfig.getTwitchApiClientID())
                    .withDefaultAuthToken(new OAuth2Credential("twitch", serverConfig.getTwitchApiClientOauthToken()))
                    .withEnableHelix(true)
                    .withEnableKraken(false)
                    .build();
            client.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelGoLiveEvent.class, this::channelGoLive);
            client.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelGoOfflineEvent.class, this::channelGoOffline);
        }catch(Exception ex){
            log.error( "{}: Fehler beim connecten to twitch: {}", serverConfig.getBotName(), serverConfig.getTwitchConfigName());
            log.error( "{}: Cause: {}", serverConfig.getBotName(), serverConfig.getTwitchConfigName());
            log.error( FormatManager.StackTraceToString(ex) );
        }
    }

    private void registerChannelEvents(){
        log.info( "{}: Hinzufuegen der User aus der Config", serverConfig.getBotName());
        for(User user : users){
            try {
                client.getClientHelper().enableStreamEventListener(user.getName());
                log.info( "{}: '{}' wurde zum Listener hinzugefuegt", serverConfig.getBotName(), user.getName());
            }catch (Exception ex){
                log.error( "{}: Fehler beim add StreamEventListener: {}", serverConfig.getBotName(), user.getName());
                log.error( FormatManager.StackTraceToString(ex) );
            }
        }
    }

    private void channelGoLive(ChannelGoLiveEvent event){
        for(Twitchuser user : users){
            if( user.getName().equalsIgnoreCase(event.getStream().getUserName()) && ! checkIfUserHasGroup(user.getUid())) {
                api.addClientToServerGroup(serverConfig.getTwitchLiveGruppe(), api.getDatabaseClientByUId(user.getUid()).getDatabaseId());
                if(serverConfig.isTwitchSendServerMessage() && user.isServerpost()){
                    String customMessage = serverConfig.getTwitchServerMessage().replace("%URL%","https://www.twitch.tv/" + user.getName());
                    customMessage = customMessage.replace("%USER%",user.getName());
                    api.sendServerMessage(customMessage);
                }
            }
        }
    }

    private void channelGoOffline(ChannelGoOfflineEvent event){
        for(User user : users){
            if( user.getName().equalsIgnoreCase(event.getChannel().getName()) && checkIfUserHasGroup(user.getUid()) ){
                api.removeClientFromServerGroup(serverConfig.getTwitchLiveGruppe(), api.getDatabaseClientByUId(user.getUid()).getDatabaseId());
            }
        }
    }

    private boolean checkIfUserHasGroup(String uid){
        for(ServerGroup serverGroup : api.getServerGroupsByClientId(api.getDatabaseClientByUId(uid).getDatabaseId())){
            if(serverGroup.getId() == serverConfig.getTwitchLiveGruppe()){
                return true;
            }
        }
        return false;
    }

    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        readConfigFile();
        registerChannelEvents();
    }

    public void setApi(TS3Api api) {
        this.api = api;
    }
}
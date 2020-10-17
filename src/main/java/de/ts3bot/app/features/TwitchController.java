package de.ts3bot.app.features;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.common.events.channel.ChannelGoLiveEvent;
import com.github.twitch4j.common.events.channel.ChannelGoOfflineEvent;
import de.ts3bot.app.manager.FormatManager;
import de.ts3bot.app.manager.ListManager;
import de.ts3bot.app.models.TS3ServerConfig;
import de.ts3bot.app.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TwitchController extends TS3EventAdapter {
    private final Logger log = LogManager.getLogger(TwitchController.class.getName());
    private TwitchClient client;
    private List<User> users;
    private TS3ServerConfig serverConfig;
    private TS3Api api;

    public TwitchController(TS3ServerConfig serverConfig){
        this.serverConfig = serverConfig;
        users = new ArrayList<>();
        readConfigFile();
        twitchConnect();
    }

    private void writeConfigFile(){
        List<String> userList = new ArrayList<>();
        userList.add("# Config mit der Verbidnung von Twitch Channelnamen zu TS3 UiDs");
        userList.add("# Bsp. Config:");
        userList.add("#    evillionslive #=# bKBZLHN0/KlgiZRA6FD18ESP/8k=");
        for(User twitchUser : users) {
            String user = twitchUser.getName() + " #=# "
                    + twitchUser.getUid();
            userList.add(user);
        }
        ListManager.writeLists(userList, serverConfig.getTwitchConfigName());
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
                    users.add(new User(clientData[0], clientData[1]));
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
            bufferedWriter.write("# Config mit der Verbidnung von Twitch Channelnamen zu TS3 UiDs\n");
            bufferedWriter.write("# Bsp. Config:\n");
            bufferedWriter.write("#    evillionslive #=# bKBZLHN0/KlgiZRA6FD18ESP/8k=");
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
                    .withEnableKraken(true)
                    .build();
            client.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelGoLiveEvent.class, this::channelGoLive);
            client.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelGoOfflineEvent.class, this::channelGoOffline);

            log.info( "{}: Hinzufuegen der User aus der Config", serverConfig.getBotName());
            registerChannelEvents();
        }catch(Exception ex){
            log.error( "{}: Fehler beim connecten to twitch: {}", serverConfig.getBotName(), serverConfig.getTwitchConfigName());
            log.error( FormatManager.StackTraceToString(ex) );
        }
    }

    private void registerChannelEvents(){
        for(User user : users){
            try {
                client.getClientHelper().enableStreamEventListener(user.getName());
            }catch (Exception ex){
                log.error( "{}: Fehler beim add StreamEventListener: {}", serverConfig.getBotName(), user.getName());
                log.error( FormatManager.StackTraceToString(ex) );
            }
        }
    }

    private void channelGoLive(ChannelGoLiveEvent event){
        for(User user : users){
            if( user.getName().equals(event.getChannel().getName().toLowerCase()) && ! checkIfUserHasGroup(user.getUid())) {
                api.addClientToServerGroup(serverConfig.getTwitchLiveGruppe(), api.getDatabaseClientByUId(user.getUid()).getDatabaseId());
            }
        }
    }

    private void channelGoOffline(ChannelGoOfflineEvent event){
        for(User user : users){
            if( user.getName().equals(event.getChannel().getName().toLowerCase()) && checkIfUserHasGroup(user.getUid()) ){
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

    public boolean addTwitchUser(String[] twitchUser){
        try{
            users.add(new User(twitchUser[0], twitchUser[1]));
            client.getClientHelper().enableStreamEventListener(twitchUser[0]);
            writeConfigFile();
            return true;
        }
        catch (Exception ex){
            log.error( FormatManager.StackTraceToString(ex) );
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
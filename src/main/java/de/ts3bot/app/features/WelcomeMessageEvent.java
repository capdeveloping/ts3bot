package de.ts3bot.app.features;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import de.ts3bot.app.manager.GeneralManager;
import de.ts3bot.app.manager.ListManager;
import de.ts3bot.app.models.TS3ServerConfig;
import de.ts3bot.app.models.functions.FunctionWelcomeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

public class WelcomeMessageEvent extends TS3EventAdapter {
    private final Logger log = LogManager.getLogger(WelcomeMessageEvent.class);
    private TS3Api api;
    private TS3ServerConfig serverConfig;
    private Timer timer;
    private HashMap<String, List<String>> userLists;
    private HashMap<String, String> backupFiles;
    private HashMap<String, Date> removeDates;
    private long plusday = 86400000;
    private String daily = "daily";

    public WelcomeMessageEvent(TS3ServerConfig serverConfig) {
        this.api = null;
        this.serverConfig = serverConfig;
        timer = new Timer();
        userLists = new HashMap<>();
        backupFiles = new HashMap<>();
        removeDates = new HashMap<>();
        addFunctions();
    }

    private void addFunctions(){
        for (FunctionWelcomeMessage function : serverConfig.getFunctionWelcomeMessageHashMap().values()) {
            addBackupFile( function.getFunctionKey() );
            removeDates.put( function.getFunctionKey(), new Date(new Date().getTime() + plusday) );
            userLists.put( function.getFunctionKey(), new ArrayList<>() );
            readBackupFile(function.getFunctionKey());
        }
    }

    private void addBackupFile(String key){
        backupFiles.put(key, serverConfig.getConfigFolder() + "/backup/WelcomeMessageEvent_" + key + ".cfg");
    }

    private void readBackupFile(String key){
        if (!ListManager.fileExist(backupFiles.get(key))) {
            return;
        }

        userLists.get(key).addAll(ListManager.readLists(backupFiles.get(key)));
        return;
    }

    private void writeBackupFile(String key){
        ListManager.writeLists(userLists.get(key), backupFiles.get(key));
    }

//region setter
    public void setApi(TS3Api api) {
        this.api = api;
    }

    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

//endregion

    @Override
    public void onClientJoin(ClientJoinEvent e) {
        for (FunctionWelcomeMessage welcomeObj : serverConfig.getFunctionWelcomeMessageHashMap().values()){
            Date today = new Date();
            if( ( welcomeObj.isEndDate() && today.after(welcomeObj.getDateUntil()) )
                    || welcomeObj.getWelcome_message().isEmpty()
                    || ( welcomeObj.isEndDate() && welcomeObj.getDateUntil() == null )
                    || ( userLists.get(welcomeObj.getFunctionKey()).contains(e.getUniqueClientIdentifier()) && welcomeObj.getWelcome_repeat().equals(daily) ) ) {
                continue;
            }
            searchAndSendMessage(e, welcomeObj);
        }
    }

    private void searchAndSendMessage(ClientJoinEvent e, FunctionWelcomeMessage welcomeObj){
        String serverGroups = e.getClientServerGroups();
        for (int group : welcomeObj.getWelcome_group_ids()){
            if(serverGroups.contains(String.valueOf(group))){
                try{
                    sendPokeMessage(e, welcomeObj);
                    sendMessage(welcomeObj, e);
                }catch (Exception ex){
                    log.error( "{}: Es ist ein unerwartet Fehler aufgetreten in searchAndSendMessatge '{}'.", serverConfig.getBotName(), ex.getMessage());
                    log.error( "{}: Stacktrace '{}'.", serverConfig.getBotName(), ex.getStackTrace());
                }
                break;
            }
        }
    }

    private void sendPokeMessage(ClientJoinEvent e, FunctionWelcomeMessage welcomeObj){
        if( ! welcomeObj.isPokeClient() || welcomeObj.getPokeMessage().isEmpty() ){
            return;
        }

        api.pokeClient(e.getClientId(), welcomeObj.getPokeMessage());
    }

    private void sendMessage(FunctionWelcomeMessage welcomeObj, ClientJoinEvent e){
        String message = welcomeObj.getWelcome_message().replace("%user%", e.getClientNickname());
        if (message.contains("%date%") && welcomeObj.getDateUntil() != null){
            message = message.replace("%date%", formatDate("member"));
        }
        if( api.isClientOnline(e.getClientId()) ){
            api.sendPrivateMessage(e.getClientId(), message);
        }
        if(welcomeObj.getWelcome_repeat().equals(daily)){
            userLists.get(welcomeObj.getFunctionKey()).add(e.getUniqueClientIdentifier());
            writeBackupFile(welcomeObj.getFunctionKey());
        }
    }

    private String formatDate(String key){
        Format formatter = new SimpleDateFormat("dd.MM.yyyy 'um' HH : mm");
        Date date = serverConfig.getFunctionWelcomeMessageHashMap().get(key).getDateUntil();
        return formatter.format(date);
    }

    public void deleteUserListTimer(){
        int delaySeconds = 5 * 1000;
        int periodSeconds = 1800 * 1000; // 30 minuten
        timer.schedule(new TimerTask() {
            public void run() {
                for(String id : serverConfig.getFunctionWelcomeMessageHashMap().keySet()){
                    deleteUserList(id);
                }
            }
        }, delaySeconds, periodSeconds);
    }

    private void deleteUserList(String id){
        DateFormat format = new SimpleDateFormat("dd.MM.yy", Locale.GERMAN);
        Date today = new Date();
        if(format.format(today).equals(format.format(removeDates.get(id)))){
            userLists.get(id).clear();
            log.info( "{}: Lösche Willkommensnachricht Userliste für '{}'.", serverConfig.getBotName(), id);
            removeDates.put( id, new Date(today.getTime() + plusday) );
            writeBackupFile(id);
        }
    }
}
package de.ts3bot.app.features;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.ts3bot.app.manager.FormatManager;
import de.ts3bot.app.models.TS3ServerConfig;
import de.ts3bot.app.models.functions.FunctionMove;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class ClientJoinedChannelEvent extends TS3EventAdapter {
    private final Logger log = LogManager.getLogger(ClientJoinedChannelEvent.class.getName());
    private TS3Api api;
    private TS3ServerConfig serverConfig;
    private FunctionMove functionMove;
    private String clientMovedKey;
    private boolean stopFunction;
    private int clientsPoked;
    private HashMap<String, Date> preventSpamList;
    private Timer removeUser;
    private boolean timerStarted;

    public ClientJoinedChannelEvent(FunctionMove functionMove, String clientMovedKey) {
        this.functionMove = functionMove;
        this.clientMovedKey = clientMovedKey;
        stopFunction = false;
        preventSpamList = new HashMap<>();
        removeUser = new Timer();
        manageTimer(true);
    }

    //region getter
    public String getClientMovedKey() {
        return clientMovedKey;
    }
    //endregion

    //region setter
    public void setApi(TS3Api api) {
        this.api = api;
    }

    public void setFunctionMove(FunctionMove functionMove) {
        this.functionMove = functionMove;
    }

    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void setStopFunction(boolean stopFunction) {
        this.stopFunction = stopFunction;
        if(stopFunction && timerStarted ){
            manageTimer(false);
            log.info("{}: Stop spam timer", serverConfig.getBotName());
        }else if( ! timerStarted ){
            manageTimer(true);
            log.info("{}: Start spam timer", serverConfig.getBotName());
        }
    }
    //endregion

    @Override
    public void onClientMoved(ClientMovedEvent e) {
        if (e.getTargetChannelId() == functionMove.getClient_moved_channel() && !stopFunction) {
            String uid = api.getClientInfo(e.getClientId()).getUniqueIdentifier();
            if( preventSpamList.containsKey(uid) ){
                FormatManager.checkAndSendLanguage(api,e.getClientId(), serverConfig.getLanguage(), "Bitte habe etwas Geduld.",
                        "Please wait some minutes.");
                return;
            }

            boolean validUser = findClients(e);
            if (validUser) {
                addUserToSpamList(uid);
                if(clientsPoked >= 2){
                    FormatManager.checkAndSendLanguage(api,e.getClientId(), serverConfig.getLanguage(), "Ich habe " + clientsPoked + " Ansprechpartner gefunden und angeschrieben.",
                            "There are " + clientsPoked + " contact persons online I have informed them.");
                }else if(clientsPoked == 0){
                    FormatManager.checkAndSendLanguage(api,e.getClientId(), serverConfig.getLanguage(), "Zurzeit ist kein Ansprechpartner online.",
                            "At this moment there is no contact person online.");
                }
                else{
                    FormatManager.checkAndSendLanguage(api,e.getClientId(), serverConfig.getLanguage(), "Ich habe " + clientsPoked + " Ansprechpartner gefunden und angeschrieben.",
                            "There is " + clientsPoked + " contact person online I have informed him.");
                }
            }
        }
    }

    //region Spam
    private void manageTimer(boolean startTimer){
        long delaySeconds = 10;
        long periodSeconds = 60;
        if( startTimer ){
            timerStarted = true;
            removeUser.schedule(new TimerTask() {
                public void run() {
                    Date currentTime = new Date(System.currentTimeMillis());
                    for (Map.Entry<String,Date> entry : preventSpamList.entrySet()) {
                        String key = entry.getKey();
                        if(currentTime.after(preventSpamList.get( key ))){
                            preventSpamList.remove( key );
                        }
                    }
                }
            }, delaySeconds * 1000, periodSeconds * 1000);
        } else{
            timerStarted = false;
            removeUser.cancel();
            removeUser.purge();
            removeUser = new Timer();
        }
    }

    private void addUserToSpamList(String uid){
        Date targetTime = new Date(System.currentTimeMillis());
        targetTime = DateUtils.addMinutes(targetTime, 1);
        preventSpamList.put(uid, targetTime);
    }
    //endregion

    private boolean findClients(ClientMovedEvent e) {
        boolean validUser = isValidUser(e);

        if( ! validUser ){
            return false;
        }

        List<Integer> pokeClientsIds = findPokeIds();

        clientsPoked = 0;

        String invokerName = api.getClientInfo(e.getClientId()).getNickname();
        for(int clientId : pokeClientsIds){
            FormatManager.checkAndPokeLanguage(api,clientId, serverConfig.getLanguage(), invokerName + " benötigt Hilfe und ist im '" + api.getChannelInfo(e.getTargetChannelId()).getName() + "' Channel.",
                    invokerName + " need your help and waiting in '" + api.getChannelInfo(e.getTargetChannelId()).getName() + "' channel.");
            clientsPoked++;
        }
        return true;
    }

    private boolean isValidUser(ClientMovedEvent e){
        boolean validUser = true;
        List<Integer> clientGroups = Arrays.stream(api.getClientInfo(e.getClientId()).getServerGroups()).boxed().collect(Collectors.toList());
        for(int groupId : functionMove.getClient_moved_group_ids()){
            if(functionMove.getClient_moved_group_action().equals("ignore") && clientGroups.contains(groupId)){
                return false;
            }
            if(functionMove.getClient_moved_group_action().equals("only")){
                if(clientGroups.contains(groupId)){
                    return true;
                }else{
                    validUser = false;
                }
            }
        }
        return validUser;
    }

    private List<Integer> findPokeIds(){
        List<Integer> pokeClientsIds = new ArrayList<>();
        for (Client client : api.getClients()){
            for(int groupId : functionMove.getClient_moved_group_notify()) {
                if ( Arrays.binarySearch(client.getServerGroups(), groupId) >= 0 && !pokeClientsIds.contains(client.getId()) ) {
                    pokeClientsIds.add(client.getId());
                }
            }
        }
        return pokeClientsIds;
    }
}

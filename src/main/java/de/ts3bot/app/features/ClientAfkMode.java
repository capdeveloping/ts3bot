package de.ts3bot.app.features;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.ts3bot.app.manager.FormatManager;
import de.ts3bot.app.manager.ListManager;
import de.ts3bot.app.models.CollectData;
import de.ts3bot.app.models.TS3ServerConfig;
import de.ts3bot.app.models.data.ClientAfkUserData;
import de.ts3bot.app.models.functions.FunctionAFK;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Captain on 19.11.2015.
 *
 */
public class ClientAfkMode {
    private final Logger log = LogManager.getLogger(ClientAfkMode.class.getName());

    private TS3ServerConfig serverConfig;
    private ArrayList<ClientAfkUserData> clients;
    private TS3Api api;
    private FunctionAFK functionAFK;
    private String clientAfkModeKey;
    private String backupFile;
    private CollectData collectData;

    public ClientAfkMode(FunctionAFK functionAFK, String clientAfkModeKey, TS3ServerConfig serverConfig, CollectData collectData) {
        this.functionAFK = functionAFK;
        this.collectData = collectData;
        clients = new ArrayList<>();
        this.clientAfkModeKey = clientAfkModeKey;
        this.serverConfig = serverConfig;
        backupFile = serverConfig.getConfigFolder() + "/backup/ClientAfkMode_" + clientAfkModeKey + ".cfg";
        readBackupFile();
    }

    private void readBackupFile(){
        if( ! ListManager.fileExist(backupFile)){
            return;
        }
        try {
            List<String> userList = new ArrayList<>();
            userList.addAll(ListManager.readLists(backupFile));
            for (String userStr : userList) {
                if ( ! userStr.equals("") ) {
                    String[] clientData = userStr.split(" #=# ");
                    String[] clientInfo = clientData[1].split(",");
                    clients.add(new ClientAfkUserData(clientData[0], Integer.parseInt(clientInfo[0]), Integer.parseInt(clientInfo[1])));
                }
            }
        }catch (Exception ex){
            log.error( "{}: Fehler beim lesen des Backupfiles: {}", serverConfig.getBotName(), backupFile);
            log.error( FormatManager.StackTraceToString(ex) );
        }
    }

    private void writeBackupFile(){
        List<String> userList = new ArrayList<>();
        for(ClientAfkUserData userData : clients) {
            String user = userData.getUid() + " #=# "
                    + userData.getLastChannelId() + ","
                    + userData.getAfkChannelId();
            userList.add(user);
        }
        ListManager.writeLists(userList, backupFile);
    }

    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void setApi(TS3Api api) {
        this.api = api;
    }

    public void setFunctionAFK(FunctionAFK functionAFK) {
        this.functionAFK = functionAFK;
    }

    public String getClientAfkModeKey() {
        return clientAfkModeKey;
    }

    public FunctionAFK getFunctionAFK() {
        return functionAFK;
    }

    public void clientAfk() {
        List<Client> clientList = api.getClients();
        if (clientList != null) {
            for (Client client : clientList) {
                boolean muted = client.isOutputMuted();
                int clientIdle = (int) client.getIdleTime() / 1000;
                if (clientIdle >= functionAFK.getClient_afk_time() && muted) {
                    checkGroupAndMove(client);
                } else {
                    removeUser(client);
                }
            }
        }
    }

    private void checkGroupAndMove(Client client){
        if ( functionAFK.getAfk_client_afk_group_watch().equals("ignore")
                && ! FormatManager.compareArrayToIntList(client.getServerGroups() ,functionAFK.getAfk_client_afk_group_ids()) ) {
            moveClient(client);
        }
        if ( functionAFK.getAfk_client_afk_group_watch().equals("only")
                && FormatManager.compareArrayToIntList(client.getServerGroups() ,functionAFK.getAfk_client_afk_group_ids()) ) {
            moveClient(client);
        }
    }

    private void moveClient(Client client){
        boolean move = false;
        if(clients.isEmpty() && ! functionAFK.getClient_afk_channel_io().contains(client.getChannelId()) ){
            addUser(client.getUniqueIdentifier(), client.getChannelId(), functionAFK.getClient_afk_channel());
            move = true;
        }else if ( ! functionAFK.getClient_afk_channel_io().contains(client.getChannelId()) ) {
            for (int z = 0; z < clients.size(); z++) {
                if (!getClientAtIndex(z).getUid().equals(client.getUniqueIdentifier()) && client.getChannelId() != functionAFK.getClient_afk_channel()) {
                    addUser(client.getUniqueIdentifier(), client.getChannelId(), functionAFK.getClient_afk_channel());
                    move = true;
                    break;
                } else if (z == clients.size() - 1 && client.getChannelId() != functionAFK.getClient_afk_channel()) {
                    move = true;
                }
            }
        }
        if(move){
            api.moveClient(client.getId(), functionAFK.getClient_afk_channel());
            collectData.increaseClientMovedCounter();
        }
    }

    private void addUser(String clientUID, int lastChannelId, int afkChannelId) {
        clients.add(new ClientAfkUserData(clientUID, lastChannelId, afkChannelId));
        writeBackupFile();
    }

    private void removeUser(Client client){
        for (int z = 0; z < clients.size(); z++) {
            if (getClientAtIndex(z).getUid().equals(client.getUniqueIdentifier())
                    && client.getChannelId() == getClientAtIndex(z).getAfkChannelId()
                    && ! client.isOutputMuted() ) {
                try{
                    api.moveClient(client.getId(), getClientAtIndex(z).getLastChannelId());
                }catch (Exception e){
                    api.pokeClient(client.getId(), "I'm sorry but your Channel doesn't exist anymore! :(");
                }
                clients.remove(z);
            } else if ( getClientAtIndex(z).getUid().equals(client.getUniqueIdentifier()) && client.getChannelId() != getClientAtIndex(z).getAfkChannelId() ){
                clients.remove(z);
            }
        }
        writeBackupFile();
    }

    public void deleteLeftAfkClients(){
        for (int z = 0; z < clients.size(); z++) {
            boolean removeObsoleteUser = true;
            for(Client client : api.getClients()){
                if (getClientAtIndex(z).getUid().equals(client.getUniqueIdentifier())){
                    removeObsoleteUser = false;
                    break;
                }
            }
            if (removeObsoleteUser){
                clients.remove(z);
            }
        }
    }

    private ClientAfkUserData getClientAtIndex(int index) {
        return clients.get(index);
    }
}
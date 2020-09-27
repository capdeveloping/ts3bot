package de.ts3bot.app.features;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.Permission;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import de.ts3bot.app.manager.FormatManager;
import de.ts3bot.app.models.data.ServerGroupData;
import de.ts3bot.app.manager.ListManager;
import de.ts3bot.app.models.data.ClientFriendData;
import de.ts3bot.app.models.MoveToRequest;
import de.ts3bot.app.models.data.MoveToUserData;
import de.ts3bot.app.models.TS3ServerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Captain on 06.01.2016.
 * Ziehe einen Spieler zu einem gewünschten Spieler
 */

public class MoveToClient {
    private ArrayList<MoveToUserData> clients;
    private ArrayList<MoveToRequest> clientRequest;
    private ArrayList<String> botAdminList;
    private TS3Api api;
    private TS3ServerConfig serverConfig;
    private String backupFile;
    private ArrayList<ServerGroupData> serverGroupList;

    public MoveToClient(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        clients = new ArrayList<>();
        clientRequest = new ArrayList<>();
        this.backupFile = serverConfig.getConfigFolder() + "/backup/MoveToClient.cfg";
        readList();
    }

//region setter
    public void setBotAdminList(ArrayList<String> botAdminList) {
        if (this.botAdminList != null) this.botAdminList.clear();
        this.botAdminList = botAdminList;
    }

    public void setApi(TS3Api api) {
        this.api = api;
    }

    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    //endregion

    private void groupCollector(){
        List<ServerGroup> groupList = api.getServerGroups();
        for(ServerGroup group : groupList) {
            List<Permission> permission = api.getServerGroupPermissions(group);
            for(int x = 0; x < permission.size(); x++){
                if(permission.get(x).getName().equals("i_channel_join_power")){
                    addGroup(group.getId(), permission.get(x).getValue(), permission.get(x).isSkipped());
                    break;
                }
            }
        }
        boolean getauscht = true;
        while (getauscht) {
            getauscht = false;
            for (int x = 0; x < serverGroupList.size(); x++) {
                if(serverGroupList.get(x).getGroupJoinPower() < serverGroupList.get(x+1).getGroupJoinPower()){
                    int help = serverGroupList.get(x).getGroupJoinPower();
                    serverGroupList.get(x).setGroupJoinPower(serverGroupList.get(x+1).getGroupJoinPower());
                    serverGroupList.get(x+1).setGroupJoinPower(help);
                    getauscht = true;
                }
            }
        }
    }

//region Suche/Anfrage
    public void showFriendlist(String invokerUid, int invokerID) {
        String antwort;
        if(serverConfig.getLanguage().equals("german")) {
            antwort = "Du hast zurzeit keine Freunde in der Freundesliste.\n" +
                    "Um Freunde hinzuzufügen verwende !addfriend [Name] = [aktueller TS3 Nickname]  <- ohne die [] Klammern und der TS3 Nickname muss exakt";
        }else{
            antwort = "You don't have friends in your friendlist.\n" +
                "To add friends use !addfriend [name] = [currently ts3 nickname]  <- without [] these brackets and ts3 nickname have to be the same as the client" +
                " in teamspeak";
        }
        if (clients.size() > 0) {
            boolean gefunden = false;
            for (MoveToUserData client : clients) {
                if (client.getClientUID().equals(invokerUid)) {
                    String freunde;
                    if(serverConfig.getLanguage().equals("german")) {
                        freunde = "Deine Freunde: \n";
                    }else{
                        freunde = "Your friends: \n";
                    }
                    List<Client> serverClients = api.getClients();
                    int round = 0;
                    do {
                        round++;
                        for (ClientFriendData friend : client.getClientFriends()) {
                            String friendName = "[URL=client://0/" + friend.getUid() + "]" + friend.getName() + "[/URL]";
                            if(round == 1) {
                                for (Client serverClient : serverClients) {
                                    if (serverClient.getUniqueIdentifier().equals(friend.getUid()) && !freunde.contains(friend.getName())) {
                                        if (serverConfig.getLanguage().equals("german")) {
                                            // [URL=client://0/" + friend.getUid() + "]" + friend.getName() + "[/URL]
                                            freunde = freunde + friendName + " :  [COLOR=#00aa00]online[/COLOR] in diesem Channel \"" + api.getChannelInfo(serverClient.getChannelId()).getName() + "\"\n";
                                        } else {
                                            freunde = freunde + friendName + " :  [COLOR=#00aa00]online[/COLOR] in this channel \"" + api.getChannelInfo(serverClient.getChannelId()).getName() + "\"\n";
                                        }
                                    }
                                }
                            }else{
                                if (!freunde.contains(friend.getName())) {
                                    if (serverConfig.getLanguage().equals("german")) {
                                        freunde = freunde + friendName + " :  [COLOR=red]offline[/COLOR]\n";
                                    } else {
                                        freunde = freunde + friendName + " :  [COLOR=red]offline[/COLOR]\n";
                                    }
                                }
                            }
                        }
                    }while(round != 2);
                    api.sendPrivateMessage(invokerID, freunde);
                    gefunden = true;
                    break;
                }
            }
            if (!gefunden) {
                api.sendPrivateMessage(invokerID, antwort);
            }
        } else {
            api.sendPrivateMessage(invokerID, antwort);
        }
    }

    public void moveTo(String invokerUid, int invokerID, String friendName, String invokerName) {
        for (int clientListFor = 0; clientListFor < clients.size(); clientListFor++) {
            if (getClientAtIndex(clientListFor).getClientUID().equals(invokerUid)) {
                FormatManager.checkAndSendLanguage(api,invokerID, serverConfig.getLanguage(), "Ich schaue mal ob " + friendName + " online ist.", "I look if " + friendName + " is online.");
                searchInFriendList(clientListFor, invokerUid, invokerID, friendName, invokerName);
                break;
            } else if (clientListFor == clients.size() - 1) {
                FormatManager.checkAndSendLanguage(api,invokerID, serverConfig.getLanguage(), "Ich konnte keinen Freund mit dem Namen " + friendName + " finden." +
                            "Um die Freundesliste anzuzeigen verwende !friendlist", "I couldn't find a friend called " + friendName + " ." + "Use \"!friendlist\" to display your friendlist");
            }
        }
    }

    private void searchInFriendList(int clientListFor, String invokerUid, int invokerID, String friendName, String invokerName) {
        int endFriendList = getClientAtIndex(clientListFor).getClientFriends().size();
        Search:
        for (int friendListFor = 0; friendListFor < endFriendList; friendListFor++) {
            if (getClientAtIndex(clientListFor).getFriendAtIndex(friendListFor).getName().contains(friendName)) {
                for (Client clientTS : api.getClients()) {
                    if (clientTS.getUniqueIdentifier().equals(getClientAtIndex(clientListFor).getFriendAtIndex(friendListFor).getUid())) {
                        searchForPermission(invokerUid, invokerID, friendName, invokerName, clientTS);
                        break Search;
                    }
                }
            }
        }
    }

    private void searchForPermission(String invokerUid, int invokerID, String friendName, String invokerName, Client clientTS) {
        FormatManager.checkAndSendLanguage(api, invokerID, serverConfig.getLanguage(), "Ich schaue mal ob du für den Channel Berechtigungen hast.",
                "Let me look if you have channel rights ");
        boolean move = false;
        PermList:
        for (int permChFor = 0; permChFor < api.getChannelPermissions(clientTS.getChannelId()).size(); permChFor++) {
            int help = api.getChannelPermissions(clientTS.getChannelId()).get(permChFor).getValue();
            if (api.getChannelPermissions(clientTS.getChannelId()).get(permChFor).getName().equals("i_channel_needed_join_power")) {
                for(int groupID : clientTS.getServerGroups()){
                    for(ServerGroupData serverGroup : serverGroupList) {
                        if(serverGroup.isGroupSkip() && serverGroup.getGroupJoinPower() < help){
                            move = false;
                            break PermList;
                        }
                        if (groupID == serverGroup.getGroupId() && serverGroup.getGroupJoinPower() >= help) {
                            move = true;
                            break PermList;
                        }else if (serverGroup.getGroupJoinPower() < help){
                            break;
                        }
                    }
                }
            } else if (permChFor == api.getChannelPermissions(clientTS.getChannelId()).size() - 1) {
                move = true;
                break;
            }
        }
        if (move) {
            api.moveClient(invokerID, clientTS.getChannelId());
        } else {
            moveRequest(invokerUid, invokerID, friendName, invokerName, clientTS);
        }
    }

    private void moveRequest(String invokerUid, int invokerID, String friendName, String invokerName, Client clientTS) {
        FormatManager.checkAndSendLanguage(api,invokerID, serverConfig.getLanguage(), "Du besitzt nicht die nötigen Rechte um in den Channel zu joinen. " +
                "Daher wurde " + friendName + " angestupst um deine move Anfrage zu akzeptieren oder abzulehnen", "You don't have the right to join this channel. " +
                "I poked  " + friendName + ", he can accept or decline your move request.");
        FormatManager.checkAndSendLanguage(api, clientTS.getId(), serverConfig.getLanguage(), "Schreibe !accept " + invokerName + " oder !decline " + invokerName + " damit er gemoved oder abgewiesen wird", "Write !accept " + invokerName + " or !decline " + invokerName + " to get him moved to you or not.");
        if(serverConfig.getLanguage().equals("german")) {
            api.pokeClient(clientTS.getId(), invokerName + " möchte gemoved werden. Schaue im Chat.");
            }else{
            api.pokeClient(clientTS.getId(), invokerName + " wants to join you. Watch chat.");
        }
        int zaehler = 0;
        do {
            if (zaehler != 0) {
                if (clientRequest.get(zaehler).getUid().equals(clientTS.getUniqueIdentifier())) {
                    clientRequest.get(zaehler).addFriendWantMove(invokerUid, invokerID, invokerName);
                }
            } else if (zaehler == clientRequest.size() - 1 || zaehler == clientRequest.size()) {
                clientRequest.add(new MoveToRequest(clientTS.getUniqueIdentifier(), clientTS.getNickname(), clientTS.getChannelId(), invokerUid, invokerID, invokerName));
            }
            zaehler++;
        } while (zaehler < clientRequest.size());
    }

    public void acceptMove(String friendName, String invokerUid) {
        Suche:
        for (int z = 0; z < clientRequest.size(); z++) {
            if (clientRequest.get(z).getUid().equals(invokerUid)) {
                for (int x = 0; x < clientRequest.get(z).getFriendWantMove().size(); x++) {
                    if (clientRequest.get(z).getFriendAtIndex(x).getName().equals(friendName)) {
                        api.moveClient(clientRequest.get(z).getFriendAtIndex(x).getClientID(), clientRequest.get(z).getClientChannelId());
                        clientRequest.get(z).getFriendWantMove().remove(x);
                        break Suche;
                    }
                }
            }
        }
    }

    public void declineMove(String friendName, String invokerUid) {
        Suche:
        for (int z = 0; z < clientRequest.size(); z++) {
            if (clientRequest.get(z).getUid().equals(invokerUid)) {
                for (int x = 0; x < clientRequest.get(z).getFriendWantMove().size(); x++) {
                    if (clientRequest.get(z).getFriendAtIndex(x).getName().equals(friendName)) {
                        if(serverConfig.getLanguage().equals("german")) {
                            api.pokeClient(clientRequest.get(z).getFriendAtIndex(x).getClientID(), clientRequest.get(z).getName() + " hat deine Anfrage abgelehnt!");
                        }else{
                            api.pokeClient(clientRequest.get(z).getFriendAtIndex(x).getClientID(), clientRequest.get(z).getName() + " declined your request!");
                        }
                        clientRequest.get(z).removeFriendWantMove(x);
                        break Suche;
                    }
                }
            }
        }
    }
//endregion

//region TextDatei
    public void addFriend(String invokerUid, int invokerID, String message) {
        String friendUid = null;
        message = message.replace("!addfriend ","");
        String help = "";
        if(message.contains(" = ")){
            help = " = ";
        } else if (message.contains("= ")) {
            help = "= ";
        } else if (message.contains(" =")) {
            help = " =";
        } else if (message.contains("=")) {
            help = "=";
        }
        String messageArr[] = message.split(help);
        String friendName = messageArr[0];
        for (Client clientTS : api.getClients()) {
            if (clientTS.getNickname().equals(messageArr[1])) {
                friendUid = clientTS.getUniqueIdentifier();
                break;
            }
        }
        if (friendUid != null) {
            if(clients.size() == 0){
                newUser(invokerID, invokerUid, friendName, friendUid);
            } else {
                for (int z = 0; z < clients.size(); z++) {
                    if (getClientAtIndex(z).getClientUID().equals(invokerUid)) {
                        getClientAtIndex(z).addClientFriends(friendName, friendUid);
                        FormatManager.checkAndSendLanguage(api, invokerID, serverConfig.getLanguage(), "Erfolgreich " + friendName + " zur Freundesliste hinzugefügt.",
                                "Successfully added " + friendName + " to your friendlist.");
                        break;
                    }else if(z == clients.size()-1){
                        newUser(invokerID, invokerUid, friendName, friendUid);
                        break;
                    }
                }
            }
            writeList();
        } else {
            FormatManager.checkAndSendLanguage(api, invokerID, serverConfig.getLanguage(), "Tut mir leid. Ich kann aber keinen User Namens \"" + messageArr[1] + "\" finden der online ist.",
                    "I am sorry. I can not find a user called \"" + messageArr[1] + "\" who is online.");
        }
    }

    private void newUser(int invokerID, String invokerUid, String friendName, String friendUid){
        addUser(invokerUid, friendName, friendUid);
        FormatManager.checkAndSendLanguage(api, invokerID, serverConfig.getLanguage(), "Erfolgreich " + friendName + " zur Freundesliste hinzugefügt.",
                "Successfully added " + friendName + " to your friendlist.");
    }

    public void removeFriend(String invokerUid, int invokerID, String message) {
        String friendName = message.replace("!removefriend ", "");
        removeFor:
        for (int z = 0; z < clients.size(); z++) {
            if (getClientAtIndex(z).getClientUID().equals(invokerUid)) {
                for (int lauf = 0; lauf < getClientAtIndex(z).getClientFriends().size(); lauf++) {
                    if (getClientAtIndex(z).getFriendAtIndex(lauf).getName().equals(friendName)) {
                        getClientAtIndex(z).removeClientFriend(lauf);
                        FormatManager.checkAndSendLanguage(api, invokerID, serverConfig.getLanguage(), "Erfolgreich " + friendName + " von der zur Freundesliste entfernt.",
                                "Successfully removed " + friendName + " from your friendlist.");
                        break removeFor;
                    }
                }
            } else if (z == clients.size() - 1) {
                FormatManager.checkAndSendLanguage(api, invokerID, serverConfig.getLanguage(), "Tut mir leid. Ich kann aber keinen User Namens \"" + friendName + "\" finden.",
                        "I am sorry. I can not find a user called \"" + friendName + "\".");
            }
        }
        writeList();
    }
//endregion

    private void writeList(){
        List<String> userList = new ArrayList<>();
        for(MoveToUserData userData : clients) {
            String user = userData.getClientUID() + " = ";
            for(ClientFriendData friendData : userData.getClientFriends()){
                user = user + "{" + friendData.getClientID() + ", " + friendData.getName() + ", " + friendData.getUid() + "}";
            }
            userList.add(user);
        }
        ListManager.writeLists(userList, backupFile);
    }

    private void readList(){
        if(ListManager.fileExist(backupFile)){
            List<String> userList = new ArrayList<>();
            userList.addAll(ListManager.readLists(backupFile));
            int maxUser = 0;
            for(String userStr : userList){
                String[] userArr = userStr.split(" = ");
                if(userArr[1].contains("}{")){
                    clients.add(new MoveToUserData(userArr[0]));
                    String[] userFriendsArr = userArr[1].replace("}{"," # ").split(" # ");
                    for(String userFriends : userFriendsArr){
                        String[] userFriendArr = userFriends.replace("{","").replace("}","").split(", ");
                        getClientAtIndex(maxUser).getClientFriends().add(new ClientFriendData(userFriendArr[1], userFriendArr[2]));
                    }
                }else{
                    String[] userFriendArr = userArr[1].replace("{","").replace("}","").split(", ");
                    addUser(userArr[0], userFriendArr[1], userFriendArr[2]);
                }
                maxUser++;
            }
        }
    }

    private void addGroup(int groupId, int groupJoinPower, boolean groupSkip){
        serverGroupList.add(new ServerGroupData(groupId,groupJoinPower,groupSkip));
    }

    private void addUser(String invokerUid, String friendName, String friendUid) {
        clients.add(new MoveToUserData(invokerUid, friendName, friendUid));
    }

    private MoveToUserData getClientAtIndex(int index) {
        return clients.get(index);
    }
}

package de.ts3bot.app.features;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.ts3bot.app.manager.FormatManager;
import de.ts3bot.app.manager.ListManager;
import de.ts3bot.app.models.data.BroadcastUserData;
import de.ts3bot.app.models.TS3ServerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Captain on 07.12.2015.
 **/


public class BroadcastMessage extends TS3EventAdapter {
    private ArrayList<BroadcastUserData> clientArr;
    private ArrayList<String> broadcastBerechtigt;
    private int clientBotId;
    private TS3Api api;
    private String backupFile;
    private TS3ServerConfig serverConfig;
    private boolean stopFunction;

    public BroadcastMessage(TS3ServerConfig serverConfig) {
        this.api = null;
        clientBotId = -1;
        stopFunction = false;
        broadcastBerechtigt = null;
        clientArr = new ArrayList<>();
        this.serverConfig = serverConfig;
        this.backupFile = serverConfig.getConfigFolder() +  "/backup/BroadcastMessageData.cfg";
        readList();
    }

//region setter
    public void setApi(TS3Api api) {
        this.api = api;
    }

    public void setClientBotId(int clientBotId) {
        this.clientBotId = clientBotId;
    }

    public void setBroadcastBerechtigt(ArrayList<String> broadcastBerechtigt) {
        this.broadcastBerechtigt = broadcastBerechtigt;
    }

    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void setStopFunction(boolean stopFunction) {
        this.stopFunction = stopFunction;
    }

    //endregion

    public void onTextMessage(TextMessageEvent e) {
        if (e.getTargetMode() == TextMessageTargetMode.CLIENT && e.getInvokerId() != api.whoAmI().getId() && !e.getMessage().startsWith("!2wayauth")) {
            if(!stopFunction) {
                String message = e.getMessage();
                if (message.equals("!broadcast")) {
                    broadcast(e);
                    writeList();
                }
                if (message.equals("!MESSAGE")) {
                    message(e);
                    writeList();
                }
                if (message.equals("!overwrite")) {
                    overwrite(e);
                    writeList();
                }
                if (message.startsWith("=")) {
                    incomingMessage(e, message);
                    writeList();
                }
                if (message.equals("!READ")) {
                    read(e);
                }
                if (message.equals("!ABORT!")) {
                    abort(e);
                    writeList();
                }
                if (message.startsWith("@")) {
                    send(e, message);
                }
            }
        }
    }

//region Befehle
    private void broadcast(TextMessageEvent e) {
        String information;
        if(serverConfig.getLanguage().equals("german")) {
            information = "Um eine Rundmail zu verschicken benoetige ich zwei Dinge."
                    + "\n1. Ich brauch die Nachricht die verschickt werden soll. Um die Nachricht zu beginnen schreibe !MESSAGE."
                    + "\n   Alles was nach der Eingabe kommt zaehlt zur Nachricht."
                    + "\n2. An wen ist diese Nachricht gerichtet? Verwende dafuer @ALL oder @[GruppenID]";
        }else{
            information = "I need two things to send a circular email."
                    + "\n1. I need a message which I should send. To start the message write !MESSAGE"
                    + "\n2. Who is addressed to this message? User @ALL or @[GroupID]";
        }
        Broadcast:
        for (String broadcastUid : broadcastBerechtigt) {
            if (e.getInvokerUniqueId().equals(broadcastUid)) {
                if (clientArr.size() == 0) {
                    api.sendPrivateMessage(e.getInvokerId(), information);
                    addUser(e.getInvokerUniqueId(), "", false, false);
                    break;
                } else {
                    for (BroadcastUserData userData : clientArr) {
                        if (!userData.getUID().equals(e.getInvokerUniqueId())) {
                            api.sendPrivateMessage(e.getInvokerId(), information);
                            addUser(e.getInvokerUniqueId(), "", false, false);
                            break Broadcast;
                        }
                    }
                    api.sendPrivateMessage(e.getInvokerId(), information);
                }
            }
        }
    }

    private void message(TextMessageEvent e){
        for (int z = 0; z < clientArr.size(); z++) {
            if (e.getInvokerUniqueId().equals(getClientAtIndex(z).getUID()) && getClientAtIndex(z).isNachrichtExist()) {
                FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(), "Du hattest schonmal eine Broadcast Nachricht geschrieben."
                            + "\n Du kannst sie dir nochmal ansehen mit \"!READ\" oder sie gleich ueberschreiben mit \"!overwrite\".",
                        "You wrote a broadcast message before."
                            + "\n You can read this message again with \"!READ\" or overwrite it with \"!overwrite\".");
                z = clientArr.size();
            } else if (z == (clientArr.size() - 1)) {
                FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(), "Alles was jetzt mit einem = startet wird danach als Broadcast Nachricht"
                            + "gewertet. Starte mit einem \"=\"."
                            + "\nSolltest du es abbrechen wollen so schreibe !ABORT!",
                        "Everything what cames behind \"=\" will be the broadcast message. Start the message with a \"=\"."
                            + "\nWhen you want to abort it write \"!ABORT!\" instead of something else.");
                getClientAtIndex(z).setStart(true);
                z = clientArr.size();
            }
        }
    }

    private void incomingMessage(TextMessageEvent e, String message){
        for (int z = 0; z < clientArr.size(); z++) {
            if (e.getInvokerUniqueId().equals(getClientAtIndex(z).getUID()) && getClientAtIndex(z).isStart()) {
                if (message.startsWith("=")) {
                    getClientAtIndex(z).setNachricht(message.replace(FormatManager.sucheZeichen(message,"="), ""));
                }
                FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(), "Um die Nachricht vorher nochmal zu lesen schreibe !READ .",
                        "To read the message write \"!READ\" .");
                getClientAtIndex(z).setStart(false);
                getClientAtIndex(z).setNachrichtExist(true);
                z = clientArr.size();
            }
        }
    }

    private void overwrite(TextMessageEvent e){
        for (int z = 0; z < clientArr.size(); z++) {
            if (e.getInvokerUniqueId().equals(getClientAtIndex(z).getUID())) {
                FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(), "Alles was jetzt mit einem = startet wird danach als Broadcast Nachricht"
                                + "gewertet. Starte mit einem \"=\"."
                                + "\nSolltest du es abbrechen wollen so schreibe !ABORT!",
                            "Everything what cames behind \"=\" will be the broadcast message. Start the message with a \"=\"."
                                + "\nWhen you want to abort it write \"!ABORT!\" instead of something else.");
                getClientAtIndex(z).setStart(true);
                z = clientArr.size();
            }
        }
    }

    private void read(TextMessageEvent e){
        for (int z = 0; z < clientArr.size(); z++) {
            if (e.getInvokerUniqueId().equals(getClientAtIndex(z).getUID())) {
                api.sendPrivateMessage(e.getInvokerId(), getClientAtIndex(z).getNachricht());
                z = clientArr.size();
            }
        }
    }

    private void abort(TextMessageEvent e){for (int z = 0; z < clientArr.size(); z++) {
        if (e.getInvokerUniqueId().equals(getClientAtIndex(z).getUID())) {
            FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(), "Broadcast Nachricht wurde abgebrochen",
                    "Canceled broadcast message.");
            getClientAtIndex(z).setStart(false);
            z = clientArr.size();
        }
    }}

    private void send(TextMessageEvent e, String message){
        if (message.equals("@ALL")) {
            for (int z = 0; z < clientArr.size(); z++) {
                if (e.getInvokerUniqueId().equals(getClientAtIndex(z).getUID())) {
                    for (Client client : api.getClients()) {
                        if (client.getId() != clientBotId) {
                            FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(), "Rundmail von " + e.getInvokerName() + " fuer alle:\n" + getClientAtIndex(z).getNachricht(),
                                    "Circular email from " + e.getInvokerName() + " for all:\n" + getClientAtIndex(z).getNachricht());
                        }
                    }
                    z = clientArr.size();
                }
            }
        } else {
            int gruppe = Integer.valueOf(message.replace("@", ""));
            ArrayList<Integer> ClientID = new ArrayList<>();
            for (int z = 0; z < clientArr.size(); z++) {
                if (e.getInvokerUniqueId().equals(getClientAtIndex(z).getUID())) {
                    for (Client client : api.getClients()) {
                        int[] clientServerGruppen = client.getServerGroups().clone();
                        for (int zahl = 0; zahl < clientServerGruppen.length; zahl++) {
                            if (clientServerGruppen[zahl] == gruppe) {
                                if (client.getId() != clientBotId) {
                                    ClientID.add(client.getId());
                                    zahl = clientServerGruppen.length;
                                }
                            }
                        }
                    }
                    for(int x : ClientID){
                        FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(), "Rundmail von " + e.getInvokerName() + " fuer einen bestimmten Kreis:\n"
                                    + getClientAtIndex(z).getNachricht(),
                                "Circular mail from " + e.getInvokerName() + " for a special group:\n" + getClientAtIndex(z).getNachricht());
                    }
                    z = clientArr.size();
                }
            }
        }
    }
//endregion

    private void writeList(){
        List<String> userList = new ArrayList<>();
        for(BroadcastUserData userData : clientArr) {
            String user = userData.getUID() + " #=# {"
                    + userData.getNachricht() + "},{"
                    + userData.isNachrichtExist() + "},{"
                    + userData.isStart() + "}";
            userList.add(user);
        }
        ListManager.writeLists(userList, backupFile);
    }

    private void readList(){
        if(ListManager.fileExist(backupFile)){
            List<String> userList = new ArrayList<>();
            userList.addAll(ListManager.readLists(backupFile));
            for(String userStr : userList){
                if(!userStr.equals("")) {
                    String[] userArr = userStr.split(" #=# ");
                    String[] userBroadcastArr = userArr[1].replace("},{", " # ").replace("{", "").replace("}", "").split(" # ");
                    clientArr.add(new BroadcastUserData(userArr[0], userBroadcastArr[0], Boolean.parseBoolean(userBroadcastArr[2]), Boolean.parseBoolean(userBroadcastArr[1])));
                }
            }
        }
    }

    private void addUser(String clientUID, String nachricht, boolean start, boolean nachrichtExist) {
        clientArr.add(new BroadcastUserData(clientUID, nachricht, start, nachrichtExist));
    }

    private BroadcastUserData getClientAtIndex(int index) {
        return clientArr.get(index);
    }
}
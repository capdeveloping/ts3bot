package de.ts3bot.app.features;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import de.ts3bot.app.manager.GeneralManager;
import de.ts3bot.app.manager.ListManager;
import de.ts3bot.app.models.TS3ServerConfig;
import de.ts3bot.app.models.functions.FunctionAcceptRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

public class AcceptRulesEvent extends TS3EventAdapter {
    private final Logger log = LogManager.getLogger(AcceptRulesEvent.class.getName());

    private TS3ServerConfig serverConfig;
    private Timer timer;
    private TS3Api api;
    private boolean stopFunction;
    private FunctionAcceptRules acceptRules;
    private String privatMessage = "";
    private List<String> forbiddenNames;

    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        acceptRules = serverConfig.getFunctionAcceptRules();
        loadForbiddenNames();
    }

    public void setApi(TS3Api api) {
        this.api = api;
        checkForNewUsers();
    }

    public AcceptRulesEvent(TS3ServerConfig serverConfig){
        this.serverConfig = serverConfig;
        stopFunction = false;
        acceptRules = serverConfig.getFunctionAcceptRules();
        timer = new Timer();
        loadForbiddenNames();
        loadPrivateMessage();
    }

    private void loadForbiddenNames(){
        File file = new File(acceptRules.getForbiddenNamesFilePath());

        if( ! file.exists() ){
            createForbiddenNamesConfig();
            return;
        }
        forbiddenNames = ListManager.readLists(acceptRules.getForbiddenNamesFilePath());
    }

    private void createForbiddenNamesConfig(){
        log.info("create forbidden names file");
        try (FileWriter fileWriter = new FileWriter(acceptRules.getForbiddenNamesFilePath());
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("# Information:\n");
            bufferedWriter.write("#    Pro Eintrag eine Zeile und immer klein schreiben!\n");
            bufferedWriter.write("#    Jeder Eintrag kann als regex format geschrieben werden\n");
            bufferedWriter.write("#    Beispiel: t[e3][a4]m[s25]peakuser\n");
        }
        catch(IOException ex) {
            log.error("writing to file: {}", ex.getMessage());
        }
    }

    private void loadPrivateMessage(){
        File newFile = new File(acceptRules.getPrivateMessageFilePath());

        if( ! newFile.exists() ){
            createPrivateMessageConfig();
            return;
        }

        StringBuilder builder = new StringBuilder();
        try ( BufferedReader reader = new BufferedReader(new FileReader(newFile) )){
            String line;
            while ( (line = reader.readLine()) != null ){
                if(line.startsWith("#")){
                    continue;
                }
                builder.append(line).append("\n");
            }
        } catch (IOException ex) {
            log.error(ex.getStackTrace());
        }
        privatMessage = builder.toString();
    }

    private void createPrivateMessageConfig(){
        log.info("create accept rules private message file");
        try (FileWriter fileWriter = new FileWriter(acceptRules.getPrivateMessageFilePath());
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("# Information\n");
            bufferedWriter.write("# Private Nachricht, kann über mehrere Zeilen gehen\n");
        }
        catch(IOException ex) {
            log.error("writing to file: {}", ex.getMessage());
        }
    }

    private void writePrivateMessage(){
        File newFile = new File(acceptRules.getPrivateMessageFilePath());
        try (FileWriter writer = new FileWriter(newFile)){
            writer.write("# Information\n");
            writer.write("# Private Nachricht, kann über mehrere Zeilen gehen\n");
            writer.write(privatMessage);
        } catch (IOException ex) {
            log.error(ex.getStackTrace());
        }
    }

    public void setStopFunction(boolean stopFunction) {
        this.stopFunction = stopFunction;
        userChangedNicknameTimer();
    }

    private void checkForNewUsers(){
        for (Client client : api.getClients()){
            for (int group : client.getServerGroups()) {
                if ( group == acceptRules.getFirstConnectionGroup() ) {
                    newUserDetected(client.getNickname(), client.getDatabaseId(), client.getId());
                    return;
                }
            }
        }
    }

    private void newUserDetected(String clientNickname, int clientDBID, int clientID) {
        try {
            if( nameIsCorrect( clientNickname ) ){
                api.addClientToServerGroup(acceptRules.getAcceptedGuest(), clientDBID);
                return;
            }

            api.pokeClient( clientID, acceptRules.getPokeMessage());
            api.sendPrivateMessage( clientID, privatMessage);
        }catch (Exception ex){
            log.error("{}: Error in newUserDetected {}", serverConfig.getBotName(), ex.getStackTrace());
        }
    }

    @Override
    public void onClientJoin(ClientJoinEvent e) {
        if(stopFunction) {
            return;
        }

        if ( e.getClientServerGroups().contains(String.valueOf(acceptRules.getFirstConnectionGroup()))){
            newUserDetected(e.getClientNickname(), e.getClientDatabaseId(), e.getClientId());
        }
        if(e.getClientServerGroups().contains(String.valueOf(acceptRules.getAcceptedGuest()))){
            removeUserFromGroup(e.getClientNickname(), e.getClientDatabaseId(), e.getClientId());
        }
    }

    private void removeUserFromGroup(String clientNickname, int clientDBID, int clientID) {
        if( nameIsCorrect( clientNickname ) ){
            return;
        }

        try {
            api.removeClientFromServerGroup(acceptRules.getAcceptedGuest(), clientDBID);
            api.sendPrivateMessage(clientID, "Wie ich sehe ist der TeamSpeakName nicht entsprechend der Regeln.\n" +
                    "Damit du dich wieder frei bewegen kannst musst du deinen Namen entsprechend anpassen. (Vornamen | Gamername)");
        }catch (Exception ex){
            log.error("{}: Error in removeUserFromGroup {}", serverConfig.getBotName(), ex.getStackTrace());
        }
    }

    @Override
    public void onTextMessage(TextMessageEvent e) {
        if ( e.getTargetMode() == TextMessageTargetMode.CLIENT
                && e.getInvokerId() != api.whoAmI().getId()
                && e.getMessage().startsWith("!accept")) {
            String uid = e.getInvokerUniqueId();
            String message = e.getMessage();
            boolean isBotAdmin = GeneralManager.IsUserBotAdmin(serverConfig, uid);
            boolean isBotFullAdmin = GeneralManager.IsUserBotFullAdmin(serverConfig, uid);

            if(message.equals("!accept")){
                int cldbID = api.getDatabaseClientByUId(e.getInvokerUniqueId()).getDatabaseId();
                userSendMessage(e.getInvokerName(), e.getInvokerId(), cldbID);
            }

//region ****** Admin Bereich *******
            if ( ! isBotAdmin && ! isBotFullAdmin) {
                return;
            }

            if (message.equals("!acceptconfigure")) {
                sendHelp(e);
            }

            message = message.replace("!acceptconfigure ","");

            if (message.startsWith("read")) {
                checkReadFunctions(e, message);
            }
            if (message.startsWith("set")) {
                checkSetFunctions(e, message);
            }

        }
    }

    //region Befehle
    private void sendHelp(TextMessageEvent e){
        StringBuilder builder = new StringBuilder("[u]Information:[/u]\n\n");
        builder.append("[u]Folgende Struktur muss fuer jeden Befehl eingehalten werden:[/u] \n");
        builder.append("!acceptconfigure readtext \n");
        builder.append("!acceptconfigure readpokemsg \n");
        builder.append("!acceptconfigure settext = <input>\n");
        builder.append("!acceptconfigure setpokemsg = <input>\n");
        api.sendPrivateMessage(e.getInvokerId(), builder.toString());
    }

    private void checkReadFunctions(TextMessageEvent e, String message){
        FunctionAcceptRules acceptRules = serverConfig.getFunctionAcceptRules();

        if (message.startsWith("readtext")){
            if( ! privatMessage.isEmpty() ){
                System.out.println(privatMessage);
                api.sendPrivateMessage(e.getInvokerId(), privatMessage);
                return;
            }
            api.sendPrivateMessage(e.getInvokerId(), "Aktuell ist noch keine Willkommensnachricht definiert. Setze eine mit" +
                    " !acceptconfigure settext = <text>");
        }
        if (message.startsWith("readpokemsg")){
            if( ! acceptRules.getPokeMessage().isEmpty() ){
                api.sendPrivateMessage(e.getInvokerId(), acceptRules.getPokeMessage());
                return;
            }
            api.sendPrivateMessage(e.getInvokerId(), "Aktuell ist noch keine Poke Nachricht definiert (100 Zeichen lang!). Setze eine mit" +
                    " !acceptconfigure setpokemsg = <text>");
        }
    }

    private void checkSetFunctions(TextMessageEvent e, String message){
        boolean valid = false;
        String input = "";
        if(message.contains("=")){
            valid = true;

            String[] arrHelper = message.split("=");
            input = arrHelper[1].trim();
        }

        if (message.startsWith("settext")){
            setTextMessage(valid, input, e.getInvokerId());
        }
        if (message.startsWith("setpokemsg")){
            setPokeTextMessage(valid, input, e.getInvokerId());
        }
    }

    private void setTextMessage(boolean valid, String input, int clientId){
        if( ! valid ){
            api.sendPrivateMessage(clientId, "Folgende Werte kannst du automatisch ersetzen lassen: \n" +
                    "%date% = Wenn ein Enddatum gesetzt wurde wird es damit automatisch gesetzt\n" +
                    "%:x% = Dadurch kann der Benutzer direkt angesprochen werden\n\n"+
                    "Schreibe die komplette Willkommensnachricht hinter dem '='." +
                    "Bsp.: !acceptconfigure settext = Hallo %user%, am %date% findet ein Clan Event statt. Sei bereit.");
            return;
        }
        privatMessage = input;
        api.sendPrivateMessage(clientId, "Wert wurde erfolgreich gesetzt.");
        writePrivateMessage();
    }

    private void setPokeTextMessage(boolean valid, String input, int clientId){
        if( ! valid ){
            api.sendPrivateMessage(clientId, "Schreibe die komplette Poke Willkommensnachricht hinter dem '='.\n" +
                    "ACHTUNG: Der Text darf nicht länger als 100 Zeichen sein. Sonst wird er abgeschnitten." +
                    "Bsp.: !acceptconfigure setpokemsg = [INFO] Es gibt eine neue Ankündigung. Schaue bitte im privaten Chat nach.");
            return;
        }
        String retValue = serverConfig.getFunctionAcceptRules().setPokeMessage(input);
        acceptRules.setPokeMessage(input);
        if(retValue.equals("")){
            api.sendPrivateMessage(clientId, "Wert wurde erfolgreich gesetzt. Vergiss nicht die Config mit !botconfigsave zu speichern.");
        }else{
            api.sendPrivateMessage(clientId, "Wert wurde NICHT erfolgreich gesetzt. Siehe -> " + retValue);
        }
    }
//endregion

    private void userSendMessage(String nickname, int clid, int cldbID){
        try {
            if (nameIsCorrect(nickname)) {
                for (ServerGroup serverGroup : api.getServerGroupsByClientId(cldbID)) {
                    if (serverGroup.getId() == acceptRules.getFirstConnectionGroup()) {
                        api.addClientToServerGroup(acceptRules.getAcceptedGuest(), cldbID);
                        api.sendPrivateMessage(clid, "Du kannst dich nun frei bewegen.");
                    }
                }
            } else {
                api.sendPrivateMessage(clid, "Leider hast du dir die Regeln nicht genau durchgelesen, denn dein TeamSpeak Name passt nicht. (Vorname | Gamername)");
            }
        }catch (Exception ex){
            log.error("{}: Error in userSendMessage {}", serverConfig.getBotName(), ex.getStackTrace());
        }
    }

    private void userChangedNickname(){
        try {
            for (Client client : api.getClients()) {
                int cldbID = api.getDatabaseClientByUId(client.getUniqueIdentifier()).getDatabaseId();
                if (client.getServerGroups().length == 1
                        && client.getServerGroups()[0] == acceptRules.getFirstConnectionGroup()
                        && nameIsCorrect(client.getNickname())) {
                    api.addClientToServerGroup(acceptRules.getAcceptedGuest(), cldbID);
                    api.sendPrivateMessage(client.getId(), "Du kannst dich nun frei bewegen.");
                }
            }
        }catch (Exception ex){
            log.error("{}: Error in userChangedNickname {}", serverConfig.getBotName(), ex.getStackTrace());
        }
    }

    private boolean nameIsCorrect(String clientNickname) {
        for (String forbiddenName : forbiddenNames){
            if(clientNickname.toLowerCase().matches("(.*)" + forbiddenName + "(.*)")){
                return false;
            }
        }

        for (String separator : acceptRules.getNameSeperators()){
            if(separator.matches("\\|")
                    && ( clientNickname.matches("\\w.* \\| \\w.*") || clientNickname.matches("\\w.*\\|\\w.*") )){
                return true;
            }
            if( ! separator.matches("\\|") &&
                    ( clientNickname.matches("\\w.* " + separator + " \\w.*")
                            || clientNickname.matches("\\w.*" + separator + "\\w.*")) ){
                return true;
            }
        }
        return false;
    }

    public void userChangedNicknameTimer(){
        int delaySeconds = 5 * 1000;
        int periodSeconds = 1000;
        if( ! stopFunction ){
            timer.schedule(new TimerTask() {
                public void run() {
                    userChangedNickname();
                }
            }, delaySeconds, periodSeconds);
        } else{
            timer.cancel();
            timer.purge();
        }
    }
}

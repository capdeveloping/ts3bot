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
    private boolean stopFunction;
    private Timer timer;
    private HashMap<String, List<String>> userLists;
    private HashMap<String, String> backupFiles;
    private HashMap<String, Date> removeDates;
    private long plusday = 86400000;
    private String daily = "daily";

    public WelcomeMessageEvent(TS3ServerConfig serverConfig) {
        this.api = null;
        stopFunction = false;
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

    public void setStopFunction(boolean stopFunction) {
        this.stopFunction = stopFunction;
    }

//endregion

    @Override
    public void onClientJoin(ClientJoinEvent e) {
        if(stopFunction) {
            return;
        }

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

    private void sendPokeMessage(ClientJoinEvent e, FunctionWelcomeMessage welcomeObj){
        if( ! welcomeObj.isPokeClient() && welcomeObj.getPokeMessage().isEmpty() ){
            return;
        }

        api.pokeClient(e.getClientId(), welcomeObj.getPokeMessage());
    }

    private void searchAndSendMessage(ClientJoinEvent e, FunctionWelcomeMessage welcomeObj){
        String serverGroups = e.getClientServerGroups();
        for (int group : welcomeObj.getWelcome_group_ids()){
            if(serverGroups.contains(String.valueOf(group))){
                sendPokeMessage(e, welcomeObj);
                sendMessage(welcomeObj, e);
                break;
            }
        }
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

    @Override
    public void onTextMessage(TextMessageEvent e) {
        if (e.getTargetMode() == TextMessageTargetMode.CLIENT && e.getInvokerId() != api.whoAmI().getId() && e.getMessage().startsWith("!welcome")) {
            if(stopFunction) {
                return;
            }
            String message = messageToLowerCase(e.getMessage());
            String uid = e.getInvokerUniqueId();
            boolean isBotAdmin = GeneralManager.IsUserBotAdmin(serverConfig, uid);
            boolean isBotFullAdmin = GeneralManager.IsUserBotFullAdmin(serverConfig, uid);

//region ****** Admin Bereich *******
            if ( ! isBotAdmin && ! isBotFullAdmin) {
                return;
            }

            if (message.equals("!welcome")) {
                sendHelp(e);
            }

            message = message.replace("!welcome ","");

            if (message.startsWith("read")) {
                checkReadFunctions(e, message);
            }
            if (message.startsWith("set")) {
                checkSetFunctions(e, message);
            }
        }
    }

    private String messageToLowerCase(String message) {
        if ( message.startsWith("!welcome read") ) {
            return message.toLowerCase();
        } else {
            return message;
        }
    }

//region Befehle
    private void sendHelp(TextMessageEvent e){
        StringBuilder builder = new StringBuilder("[u]Information:[/u]\n");
        builder.append("Für jeden Befehl brauchst du die ID der WelcomeMessage. Es ist wichtig das eine Leerzeichen bis zum = beizubehalten.\n");
        builder.append("[u]Folgende Struktur muss für jeden Befehl eingehalten werden:[/u] \n");
        builder.append("!welcome readtext <id>\n");
        builder.append("!welcome readrepeat <id>\n");
        builder.append("!welcome readexpiry <id>\n");
        builder.append("!welcome readpokemsg <id>\n");
        builder.append("!welcome readclientpoked <id>\n");
        builder.append("!welcome settext <id> = <input>\n");
        builder.append("!welcome setexpiry <id> = <input>\n");
        builder.append("!welcome setrepeat <id> = <input>\n");
        builder.append("!welcome setpokemsg <id>\n");
        builder.append("!welcome setclientpoked <id>\n\n");
        builder.append("Beispiel, nehmen wir an die ID wäre 'gast' und du willst den Text ausgeben: !welcome readtext gast\n\n");
        builder.append("Folgende IDs existieren:\n");
        for (String key : serverConfig.getFunctionWelcomeMessageHashMap().keySet()){
            builder.append("- ").append(key).append("\n");
        }
        api.sendPrivateMessage(e.getInvokerId(), builder.toString());
    }

    private void checkReadFunctions(TextMessageEvent e, String message){
        String messageID = message.substring(message.indexOf(' ')).trim();
        if( ! serverConfig.getFunctionWelcomeMessageHashMap().containsKey(messageID) ){
            api.sendPrivateMessage(e.getInvokerId(), "Leider konnte ich die ID: '" + messageID + "' nicht finden.");
            return;
        }

        FunctionWelcomeMessage welcomeMessage = serverConfig.getFunctionWelcomeMessageHashMap().get(messageID);

        if (message.startsWith("readtext")){
            if( ! welcomeMessage.getWelcome_message().isEmpty() ){
                api.sendPrivateMessage(e.getInvokerId(), welcomeMessage.getWelcome_message());
                return;
            }
            api.sendPrivateMessage(e.getInvokerId(), "Aktuell ist noch keine Willkommensnachricht definiert. Setze eine mit" +
                    " !welcome settext <id> = <text>");
        }
        if (message.startsWith("readrepeat")){
            api.sendPrivateMessage(e.getInvokerId(), "Wie oft soll der Client die Nachricht erhalten: " +
                    welcomeMessage.getWelcome_repeat());
        }
        if (message.startsWith("readexpiry")){
            if(welcomeMessage.getDateUntil() == null ){
                api.sendPrivateMessage(e.getInvokerId(), "Willkommenstext läuft nicht ab.");
                return;
            }
            api.sendPrivateMessage(e.getInvokerId(), "Willkommenstext läuft am: " + formatDate(messageID) + " ab.");
        }
        if (message.startsWith("readclientpoked")){
            if(welcomeMessage.getDateUntil() != null ){
                api.sendPrivateMessage(e.getInvokerId(), "Clients werden, wenn sie joinen, angestupst: "
                                        + welcomeMessage.isPokeClient());
                return;
            }
        }
        if (message.startsWith("readpokemsg")){
            if( ! welcomeMessage.getPokeMessage().isEmpty() ){
                api.sendPrivateMessage(e.getInvokerId(), welcomeMessage.getPokeMessage());
                return;
            }
            api.sendPrivateMessage(e.getInvokerId(), "Aktuell ist noch keine Poke Willkommensnachricht definiert (100 Zeichen lang!). Setze eine mit" +
                    " !welcome setpokemsg <id> = <text>");
        }
    }

    private String formatDate(String key){
        Format formatter = new SimpleDateFormat("dd.MM.yyyy 'um' HH : mm");
        Date date = serverConfig.getFunctionWelcomeMessageHashMap().get(key).getDateUntil();
        return formatter.format(date);
    }

    private void checkSetFunctions(TextMessageEvent e, String message){
        boolean valid = false;
        String input = "";
        String messageID;
        if(message.contains("=")){
            valid = true;

            String[] arrHelper = message.split("=");
            input = arrHelper[1].trim();
            messageID = arrHelper[0].substring(arrHelper[0].indexOf(' ')).trim();
        }else{
            if(message.contains(" ")) {
                messageID = message.substring(message.indexOf(' ')).trim();
            }else{
                api.sendPrivateMessage(e.getInvokerId(), "Es wurde keine ID angegeben.");
                return;
            }
        }

        if( ! serverConfig.getFunctionWelcomeMessageHashMap().containsKey(messageID) ){
            api.sendPrivateMessage(e.getInvokerId(), "Leider konnte ich die ID: '" + messageID + "' nicht finden.");
            return;
        }
        if (message.startsWith("settext")){
            setTextMessage(valid, messageID, input, e.getInvokerId());
        }
        if (message.startsWith("setpokemsg")){
            setPokeTextMessage(valid, messageID, input, e.getInvokerId());
        }
        if (message.startsWith("setclientpoked")){
            setClientPoked(valid, messageID, input, e.getInvokerId());
        }
        if (message.startsWith("setrepeat")){
            setRepeatMessage(valid, messageID, input, e.getInvokerId());
        }
        if (message.startsWith("setexpiry")){
            setExpiryMessage(valid, messageID, input, e.getInvokerId());
        }
    }

    private void setClientPoked(boolean valid, String messageID, String input, int clientId) {
        if( ! valid ){
            api.sendPrivateMessage(clientId, "Schreibe true oder false hinter dem '='.\n" +
                    "ACHTUNG: Der Text darf nicht länger als 100 Zeichen sein. Sonst wird er abgeschnitten." +
                    "Bsp.: !welcome setclientpoked " + messageID + " = true");
            return;
        }
        try{
            serverConfig.getFunctionWelcomeMessageHashMap().get(messageID).setPokeClient(Boolean.parseBoolean(input));
            api.sendPrivateMessage(clientId, "Wert wurde erfolgreich gesetzt. Vergiss nicht die Config mit !botconfigsave zu speichern.");
        }catch(Exception ex){
            api.sendPrivateMessage(clientId, "Wert wurde NICHT erfolgreich gesetzt. Der angegebene Wert ist nicht true oder false");
        }
    }

    private void setTextMessage(boolean valid, String messageID, String input, int clientId){
        if( ! valid ){
            api.sendPrivateMessage(clientId, "Folgende Werte kannst du automatisch ersetzen lassen: \n" +
                    "%date% = Wenn ein Enddatum gesetzt wurde wird es damit automatisch gesetzt\n" +
                    "%:x% = Dadurch kann der Benutzer direkt angesprochen werden\n\n"+
                    "Schreibe die komplette Willkommensnachricht hinter dem '='." +
                    "Bsp.: !welcome settext " + messageID + " = Hallo %user%, am %date% findet ein Clan Event statt. Sei bereit.");
            return;
        }
        serverConfig.getFunctionWelcomeMessageHashMap().get(messageID).setWelcome_message(input);
        api.sendPrivateMessage(clientId, "Wert wurde erfolgreich gesetzt. Vergiss nicht die Config mit !botconfigsave zu speichern.");
    }

    private void setPokeTextMessage(boolean valid, String messageID, String input, int clientId){
        if( ! valid ){
            api.sendPrivateMessage(clientId, "Schreibe die komplette Poke Willkommensnachricht hinter dem '='.\n" +
                    "ACHTUNG: Der Text darf nicht länger als 100 Zeichen sein. Sonst wird er abgeschnitten." +
                    "Bsp.: !welcome setpokemsg " + messageID + "  = [INFO] Es gibt eine neue Ankündigung. Schaue bitte im privaten Chat nach.");
            return;
        }
        String retValue = serverConfig.getFunctionWelcomeMessageHashMap().get(messageID).setPokeMessage(input);
        if(retValue.equals("")){
            api.sendPrivateMessage(clientId, "Wert wurde erfolgreich gesetzt. Vergiss nicht die Config mit !botconfigsave zu speichern.");
        }else{
            api.sendPrivateMessage(clientId, "Wert wurde NICHT erfolgreich gesetzt. Siehe -> " + retValue);
        }
    }

    private void setRepeatMessage(boolean valid, String messageID, String input, int clientId){
        if( ! valid ){
            api.sendPrivateMessage(clientId, "Diese beiden Werte können gesetzt werden:\n" +
                    "daily = einmal täglich bekommt der Benutzer diese Nachricht\n" +
                    "always = jedes mal wenn der Benutzer joint bekommt er diese Nachricht\n" +
                    "Bsp.: !welcome setrepeat " + messageID + " = daily");
            return;
        }
        if( ! input.equals(daily) && ! input.equals("always") ){
            api.sendPrivateMessage(clientId,"'" + input + "' ist leider nicht richtig. Bitte nutze daily oder always.");
            return;
        }
        serverConfig.getFunctionWelcomeMessageHashMap().get(messageID).setWelcome_repeat(input);
        api.sendPrivateMessage(clientId, "Wert wurde erfolgreich gesetzt.");
    }

    private void setExpiryMessage(boolean valid, String messageID, String input, int clientId){
        if( ! valid ){
            api.sendPrivateMessage(clientId, "Das Ablaufdatum muss wie folgt gesetzt werden: \n" +
                    "dd.mm.yy hh:MM\n" +
                    "Bsp.: !welcome setexpiry " + messageID + " = 01.10.2019 20:00\n\n" +
                    "Es ist auch möglich das Datum nicht ablaufen zu lassen. Setze dafür empty ein.\n" +
                    "Bsp.: !welcome setexpiry " + messageID + " = empty");
            return;
        }
        if(input.equals("empty")){
            serverConfig.getFunctionWelcomeMessageHashMap().get(messageID).setDateUntil(null);
            serverConfig.getFunctionWelcomeMessageHashMap().get(messageID).setEndDate(false);
            return;
        }
        try{
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN);
            Date date = format.parse(input);
            serverConfig.getFunctionWelcomeMessageHashMap().get(messageID).setDateUntil(date);
            serverConfig.getFunctionWelcomeMessageHashMap().get(messageID).setEndDate(true);
        }catch (Exception ex){
            api.sendPrivateMessage(clientId, "Leider stimmt der Datumsformat nicht überein: dd.mm.yyyy hh:MM");
        }
        api.sendPrivateMessage(clientId, "Wert wurde erfolgreich gesetzt.");
    }

//endregion

    public void deleteUserListTimer(){
        int delaySeconds = 5 * 1000;
        int periodSeconds = 1800 * 1000; // 30 minuten
        if( ! stopFunction ){
            timer.schedule(new TimerTask() {
                public void run() {
                    for(String id : serverConfig.getFunctionWelcomeMessageHashMap().keySet()){
                        deleteUserList(id);
                    }
                }
            }, delaySeconds, periodSeconds);
        } else{
            timer.cancel();
            timer.purge();
        }
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
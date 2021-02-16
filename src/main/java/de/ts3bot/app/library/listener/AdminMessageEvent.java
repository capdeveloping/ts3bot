package de.ts3bot.app.library.listener;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.*;
import de.ts3bot.app.TaskTimer;
import de.ts3bot.app.features.TwitchController;
import de.ts3bot.app.library.configload.TS3ConfigWrite;
import de.ts3bot.app.manager.GeneralManager;
import de.ts3bot.app.manager.TS3ServerConfigManager;
import de.ts3bot.app.library.configload.TS3TextLoad;
import de.ts3bot.app.manager.FormatManager;
import de.ts3bot.app.models.BotInstanceManager;
import de.ts3bot.app.models.ReloadAfterConnect;
import de.ts3bot.app.models.TS3ServerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created by Captain on 25.10.2015.
 * Schaut ob jemand etwas im Server/Channel/Privaten Chat an ihn schriebt und schreibt dementsprechend zur�ck.
 */

public class AdminMessageEvent extends TS3EventAdapter {
    private final Logger log = LogManager.getLogger(AdminMessageEvent.class.getName());
    private TS3ConfigWrite configWrite;
    private ReloadAfterConnect afterConnect;
    private TS3Api api;
    private TS3ServerConfig serverConfig;
    private TS3TextLoad textLoad;
    private String datum;
    private String botName;
    private TaskTimer taskTimer;
    private BotInstanceManager botInstanceManager;
    private TwitchController twitchController;
    private static final String GERMAN = "german";

    public AdminMessageEvent(TS3ServerConfig serverConfig, TS3TextLoad textLoad, TS3ConfigWrite configWrite, BotInstanceManager botInstanceManager, TwitchController twitchController) {
        this.serverConfig = serverConfig;
        this.textLoad = textLoad;
        this.configWrite = configWrite;
        this.botInstanceManager = botInstanceManager;
        this.twitchController = twitchController;
    }
//region setter
    public void setAfterConnect(ReloadAfterConnect afterConnect, TaskTimer taskTimer, String botName) {
        this.afterConnect = afterConnect;
        this.taskTimer = taskTimer;
        this.botName = botName;
    }

    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        setDate();
    }

    private void setDate(){
        if(datum != null){
            return;
        }

        Date date = new Date();
        DateFormat df;
        if(serverConfig.getLanguage().equals(GERMAN)) {
            df = new SimpleDateFormat("'Online seit' dd. MMM yyyy 'um' HH:mm:ss");
        }else{
            df = new SimpleDateFormat("'Online since' dd/MMM/yyyy 'at' HH:mm:ss");
        }

        datum = df.format(date);
    }

    public void setApi(TS3Api api) {
        this.api = api;
    }

//endregion
    @Override
    public void onTextMessage(TextMessageEvent e) {
        if (e.getTargetMode() == TextMessageTargetMode.CLIENT
                && e.getInvokerId() != api.whoAmI().getId()
                && !e.getMessage().startsWith("!2wayauth")
                && (e.getMessage().equals("!admin")
                || e.getMessage().startsWith("!bot")
                || e.getMessage().startsWith("!csgo")
                || e.getMessage().startsWith("!getusers")
                || e.getMessage().startsWith("!rmuser")
                || e.getMessage().startsWith("!twitchadduser")
                || e.getMessage().equals("!quit"))) {
            String message;
            if (!e.getMessage().startsWith("!botnickname set") && !e.getMessage().startsWith("!botfunction st")
                    && !e.getMessage().startsWith("!botconfigaddvalue")) {
                message = e.getMessage().toLowerCase();
            } else {
                message = e.getMessage();
            }
            String uid = e.getInvokerUniqueId();
            boolean isBotAdmin = GeneralManager.IsUserBotAdmin(serverConfig, uid);
            boolean isBotFullAdmin = GeneralManager.IsUserBotFullAdmin(serverConfig, uid);

//region ****** Admin Bereich *******
            if (isBotAdmin || isBotFullAdmin) {
                checkAdminArea(message, e, isBotFullAdmin);
            }
//endregion
//region ****** Full Admin Bereich *******

            if (isBotFullAdmin){
                checkFullAdminArea(message, e);
            }
//endregion
        }
    }

    private void checkAdminArea(String message, TextMessageEvent e, boolean isBotFullAdmin) {
        switch (message) {
            case "!admin":
                api.sendPrivateMessage(e.getInvokerId(), textLoad.getInfoAdmin());
                if (isBotFullAdmin) {
                    api.sendPrivateMessage(e.getInvokerId(), textLoad.getInfoFullAdmin());
                }
                break;
            case "!botconfigreload":
                botConfigReload(e, false);
                break;
            case "!botconfigsave":
                botConfigSave(e);
                break;
            case "!botconfigadd":
                botConfigAdd(isBotFullAdmin, e);
                break;
            case "!botfunction":
                sendFunctions(e, isBotFullAdmin);
                break;
            case "!botuptime":
                api.sendPrivateMessage(e.getInvokerId(), datum);
                break;
            case "!csgoscramble":
                csgoScrambleTeam(e);
            default:
                break;
        }

        if (message.startsWith("!getusers")) {
            getUsersConnectionTime(e, message);
        }

        if (message.startsWith("!rmuser")) {
            removeUserFromAllGroups(e, message);
        }

        if (message.startsWith("!twitchadduser")) {
            twitchAddUser(e, message);
        }

        if (message.startsWith("!botconfigaddvalue")) {
            try {
                String antwort;
                boolean changed = configWrite.setConfigKey(message, isBotFullAdmin);
                if (changed) {
                    antwort = "Successfully changed" +
                            "\nSobald du fertig bist speichere bitte die Config mit !botconfigsave";
                } else {
                    antwort = "Wrong config Key." +
                            "\nWhen you have finished with all please save the config with \"!botconfigsave\"";
                }
                FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(),
                        antwort,
                        antwort);
            } catch (Exception ex) {
                log.error("Couldn't convert value as expected : {}", FormatManager.StackTraceToString(ex));
                FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(),
                        "Etwas ist beim config updated schief gelaufen. Möglicherweise wurde Text für eine Zahl verwendet.",
                        "Recheck your value for the configkey. Maybe text was used instead of a number.");
            }
        }
    }

    private void removeUserFromAllGroups(TextMessageEvent e, String message) {
        String[] userArr = message.replace("!rmuser", "").trim().split(" ");
        if(message.equals("!rmuser") || userArr[0].isEmpty()){
            api.sendPrivateMessage(e.getInvokerId(), "!rmuser <uid>.\n" +
                    "Bsp 1: !rmuser lCAe2kp38Js5cKqtWlMRQDMux4U=\n" +
                    "Bsp 2(mehrere gleichzeitig): !rmuser lCAe2kp38Js5cKqtWlMRQDMux4U= lCAe2kp38Js5cKqtWlMRQDMux4U= lCAe2kp38Js5cKqtWlMRQDMux4U=\n" +
                    "Loescht den User mit der UID aus allen Servergruppen.");
            return;
        }

        api.sendPrivateMessage(e.getInvokerId(), "Lass mich den User kurz bereinigen.");

        StringBuffer stringBuffer = new StringBuffer();
        for(String uid : userArr){
            if(uid.isEmpty()){
                continue;
            }
            int cldbId;
            DatabaseClientInfo dbclient;
            try {
                dbclient = api.getDatabaseClientByUId(uid);
                cldbId = dbclient.getDatabaseId();
            }catch(Exception ex){
                stringBuffer.append("User mit der UID '").append(uid).append("' ")
                        .append(" konnte nicht gefunden werden!\n\n");
                continue;
            }

            stringBuffer.append("Aus folgenden Gruppe wurde der User( '")
                    .append(dbclient.getNickname())
                    .append("' mit der UID: '").append(uid).append("' ")
                    .append(" ) entfernt: \n\n");
            for (ServerGroup serverGroup : api.getServerGroupsByClientId(cldbId)){
                if(serverGroup.getId() == api.getServerInfo().getDefaultServerGroup()){
                    continue;
                }
                stringBuffer.append("Gruppenid: ").append(serverGroup.getId());
                stringBuffer.append(" | Gruppenname: ").append(serverGroup.getName()).append("\n");
                api.removeClientFromServerGroup(serverGroup.getId(), dbclient.getDatabaseId());
            }
            stringBuffer.append("------------------------------\n");
        }
        api.sendPrivateMessage(e.getInvokerId(), stringBuffer.toString());
    }

    private void getUsersConnectionTime(TextMessageEvent e, String message) {
        if(message.equals("!getusers")){
            api.sendPrivateMessage(e.getInvokerId(), "!getusers <gruppenid> <1w|2w|3w|4w|1m|2m|3m|4m|5m>.\n" +
                    "Bsp: !getusers 3 1w\n" +
                    "Gibt alle User der Gruppe aus mit der Gruppenid 3 die länger als 1 Woche nicht mehr auf dem TS waren.");
            return;
        }

        api.sendPrivateMessage(e.getInvokerId(), "Lass mich das kurz nachschauen.");

        String[] groupIdStr = message.replace("!getusers", "").trim().split(" ");
        StringBuffer stringBuffer = new StringBuffer();
        int groupId = 321;
        String timeDistance = "2w";
        if( ! groupIdStr[0].isEmpty() ){
            groupId = Integer.parseInt(groupIdStr[0]);
        }
        if( groupIdStr.length > 1 && ! groupIdStr[1].isEmpty() ){
            timeDistance = groupIdStr[1];
        }

        for (DatabaseClient dbclient : api.getDatabaseClients()) {
            int cldbId = dbclient.getDatabaseId();
            if( ! isUserInServergroup(groupId, cldbId) ){
                continue;
            }
            Date lastConnectedDate = dbclient.getLastConnectedDate();
            Date oldate = getDate(timeDistance);
            if(lastConnectedDate.before(oldate)){
                stringBuffer.append("Nickname: ").append(FormatManager.fillStringToSpecifiLength(dbclient.getNickname(), 25));
                stringBuffer.append(" | UID: ").append(dbclient.getUniqueIdentifier());
                stringBuffer.append(" | Letzte Verbindung: ").append(lastConnectedDate).append("\n");
            }
        }
        api.sendPrivateMessage(e.getInvokerId(), "Folgende user waren seit " + timeDistance + " nicht mehr auf dem TS:\n\n" + stringBuffer.toString());
    }

    private Date getDate(String timeDistance) {
        switch (timeDistance){
            case "1w":
                return Date.from(ZonedDateTime.now().minusWeeks(1).toInstant());
            case "3w":
                return Date.from(ZonedDateTime.now().minusWeeks(3).toInstant());
            case "4w":
                return Date.from(ZonedDateTime.now().minusWeeks(4).toInstant());
            case "1m":
                return Date.from(ZonedDateTime.now().minusMonths(1).toInstant());
            case "2m":
                return Date.from(ZonedDateTime.now().minusMonths(2).toInstant());
            case "3m":
                return Date.from(ZonedDateTime.now().minusMonths(3).toInstant());
            case "4m":
                return Date.from(ZonedDateTime.now().minusMonths(4).toInstant());
            case "5m":
                return Date.from(ZonedDateTime.now().minusMonths(5).toInstant());
            default:
                return Date.from(ZonedDateTime.now().minusWeeks(2).toInstant());
        }
    }

    private boolean isUserInServergroup(int groupId, int cldbId){
        for (ServerGroup serverGroup : api.getServerGroupsByClientId(cldbId)){
            if (serverGroup.getId() == groupId){
                return true;
            }
        }
        return false;
    }

    private void twitchAddUser(TextMessageEvent e, String message) {
        if(twitchController == null){
            return;
        }
        String[] twitchUser = message.replace("!twitchadduser ", "").split(" = ");
        if ( twitchController.addTwitchUser(twitchUser) ){
            FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(),
                    "Successfully changed.",
                    "Successfully changed.");
        }else{
            FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(),
                    "Da ist etwas schief gelaufen.",
                    "Something went wrong.");
        }
    }

    private void csgoScrambleTeam(TextMessageEvent e) {
        List<Client> scrambleClient = new ArrayList<>();
        int sourceChannel = csgoGetSourceChannel(e.getInvokerUniqueId());
        if(sourceChannel == -1){
            FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(),
                    "Konnte Source Channel nicht finden.",
                    "Couldn't find source channel.");
            return;
        }

        int firstSubChannel = csgoGetSubChannel(sourceChannel,-1);
        int secondSubChannel = csgoGetSubChannel(sourceChannel, firstSubChannel);
        for (Client client: api.getClients()) {
            if(client.getChannelId() == sourceChannel){
                scrambleClient.add(client);
            }
        }
        Collections.shuffle(scrambleClient, new Random());

        if(firstSubChannel == -1 && secondSubChannel == -1){
            FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(),
                    "Konnte SubChannel nicht finden.",
                    "Couldn't find sub channel.");
            return;
        }
        int targetChannel = firstSubChannel;
        for (int counter = 0; counter < scrambleClient.size(); counter++){
            if(counter == 5){
                targetChannel = secondSubChannel;
            }
            api.moveClient(scrambleClient.get(counter).getId(), targetChannel);
        }
    }

    private int csgoGetSubChannel(int parentChannel, int firstSubChannel) {
        for(Channel channel : api.getChannels()){
            if(firstSubChannel == -1 && channel.getParentChannelId() == parentChannel){
                return channel.getId();
            }
            if(channel.getId() != firstSubChannel && channel.getParentChannelId() == parentChannel){
                return channel.getId();
            }
        }
        return -1;
    }

    private int csgoGetSourceChannel(String uid){
        for(Client client : api.getClients()){
            if ( client.getUniqueIdentifier().equals(uid) ) {
                return client.getChannelId();
            }
        }
        return -1;
    }

    private void checkFullAdminArea(String message, TextMessageEvent e) {
        switch (message){
            case "!botconfigreloadall":
                botConfigReload(e, true);
                break;
            case "!botnickname":
                FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(), "Der Botnickname wird nur vorübergehend geändert." +
                        "\nBei einem Neustart des Bottes wird der Botnickname von der Config geladen." +
                        "\nUm den Botnickname zu aendern schreibe !botnickname set = [wie er heißen soll]", "Bot nickname is only change now not forever." +
                        "\nIf you restart the bot he will get the nickname from the config." +
                        "\nTo change the bot nickname write !botnickname set = [his name]");
                break;
            case "!quit":
                quit(e);
                break;
            default:
                break;
        }
        if (message.startsWith("!botfunction start")) {
            startFunctions(e,message);
        }
        if (message.startsWith("!botfunction stop")) {
            stopFunctions(e,message);
        }
        if (message.startsWith("!botnickname set")) {
            botNickname(message);
        }
    }

    private void sendFunctions(TextMessageEvent e, boolean isBotFullAdmin){
        StringBuilder antwort = new StringBuilder();
        antwort.append("[u][COLOR=#00aa00]Aktive Funktionen:[/COLOR][/u]\n");
        if(!serverConfig.getFunctionNames().keySet().toString().equals("[]")) {
            antwort.append("\n");
            for (String key : serverConfig.getFunctionNames().keySet()) {
                if (!afterConnect.getDeactivatedFuntions().contains(serverConfig.getFunctionNames().get(key))) {
                    antwort.append(serverConfig.getFunctionNames().get(key));
                    antwort.append(":");
                    antwort.append(key);
                    antwort.append(", ");
                }
            }
            antwort.replace(antwort.lastIndexOf(","),antwort.lastIndexOf(",")+1,"");
            antwort.append("\n");
        }
        antwort.append("\n[u][COLOR=red]Inaktive Funktionen:[/COLOR][/u]\n");

        if(!afterConnect.getDeactivatedFuntions().toString().equals("[]")) {
            antwort.append("\n");
            antwort.append(afterConnect.getDeactivatedFuntions().toString().replace("[", "").replace("]", ""));
            antwort.append("\n");
        }
        if (isBotFullAdmin) {
            antwort.append("\nUm Funktionen zu aktivieren schreibe \"!botfunction start + [die Funktion]\"\n");
            antwort.append("Um Funktionen zu deaktivieren schreibe \"!botfunction stop + [die Funktion]\".\n");
        }
        api.sendPrivateMessage(e.getInvokerId(),antwort.toString());
    }

    private void botConfigAdd(boolean isBotFullAdmin, TextMessageEvent e){
        String antwort;
        if(serverConfig.getLanguage().equals(GERMAN)){
            antwort = "Du kannst folgende Keys aendern:\n";
        }else{
            antwort = "You can change the following keys:\n";
        }
        if (isBotFullAdmin) {
            antwort = antwort + "- ts3_ip\n" +
                    "- ts3_port\n" +
                    "- login_name\n" +
                    "- login_password\n" +
                    "- bot_admin (list)\n" +
                    "- bot_full_admin (list)\n";
        }
        antwort = antwort + "- bot_standard_channel\n" +
                "- bot_nickname\n" +
                "- bot_nickname2\n";
        StringBuilder funcAntwort = new StringBuilder();
        for (String key : serverConfig.getFunctionNames().keySet()) {
            switch (serverConfig.getFunctionNames().get(key)){
                case "ChannelAutoCreate":
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_channel_check_subchannel (list)\n");
                    break;
                case "Viewer":
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_ts3_viewer_file_location\n");
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_ts3_viewer_update_time (in minuten)\n");
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_ts3_viewer_text_color\n");
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_ts3_viewer_background_color\n");
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_ts3_viewer_server_ip\n");
                    break;
                case "AcceptRules":
                    funcAntwort.append("- ");
                    funcAntwort.append("!acceptconfigure (for more help))\n");
                    break;
                case "WelcomeMessage":
                    funcAntwort.append("- ");
                    funcAntwort.append("!welcome (for more help))\n");
                    break;
                case "ClientMove":
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_client_moved_channel(list)\n");
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_client_moved_group_notify(list)\n");
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_client_moved_group_ids (list)\n");
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_client_moved_group_action\n");
                    break;
                case "ClientAFK":
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_client_afk_time (in sekunden)\n");
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_client_afk_channel\n");
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_client_afk_channel_io (list)\n");
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_client_afk_channel_watch (ignore/only)\n");
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_client_afk_group_ids\n");
                    funcAntwort.append("- ");
                    funcAntwort.append(key);
                    funcAntwort.append("_client_afk_group_watch (ignore/only)\n");
                    break;
                default:
                    break;
            }
        }
        antwort = antwort + funcAntwort.toString();
        if(serverConfig.getLanguage().equals(GERMAN)){
            antwort = antwort +
                    "\n\nBei den Key's wo \"(list)\" hinter steht bedeutet es das du den Wert nur hinzufuegst nich ueberschreibst!" +
                    "\n!botconfigaddvalue <Key> = <Value> <- Um einen Wert vom Key zu aendern. Form beibehalten aber ohne <>!!" +
                    "\n\nBeispiel: !botconfigaddvalue bot_nickname = Supporter";
        }else{
            antwort = antwort +
                    "\n\nYou don't overwrite keys where \"(list)\" is behind the word you only add newer ones!" +
                    "\n!botconfigaddvalue <Key> = <Value> <- To change the value of the key. It's important to keep this form correct.(except this <...>)!" +
                    "\n\ne.g.: !botconfigaddvalue bot_nickname = Supporter";
        }
        api.sendPrivateMessage(e.getInvokerId(), antwort);
    }

    private void botNickname(String message){
        String nickname = message.replace("!botnickname set", "");
        nickname = nickname.replace(FormatManager.sucheZeichen(nickname,"="), "");
        api.setNickname(nickname);
    }

    private void quit(TextMessageEvent event){
        FormatManager.checkAndSendLanguage(api, event.getInvokerId(), serverConfig.getLanguage(), "Auf Wiedersehen Master.", "Bye Bye Master.");
        botInstanceManager.getInstanceList().get(botName).stopBot();
    }

    private void botConfigReload(TextMessageEvent e, boolean all){
        afterConnect.setServerConfig(TS3ServerConfigManager.readInstanceConfig(serverConfig, serverConfig.getConfigPath(), all));
        afterConnect.serverConfigUpdated();
        FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(), "Bot Config neugeladen.", "Bot config reloaded.");
    }

    private void botConfigSave(TextMessageEvent e) {
        boolean written = TS3ServerConfigManager.writeFile(serverConfig, serverConfig.getConfigPath());
        if (written) {
            afterConnect.setServerConfig(serverConfig);
            afterConnect.serverConfigUpdated();
            FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(), "Bot Config gespeichert.", "Bot config saved.");
        } else {
            FormatManager.checkAndSendLanguage(api, e.getInvokerId(), serverConfig.getLanguage(), "Es ist ein Problem beim speichern aufgetreten.", "A problem occured while trying to write config file.");
        }
    }

    private void startFunctions(TextMessageEvent e, String message){
        String function = message.replace("!botfunction start ","");
        controlFunctions(e, function, false);
    }

    private void stopFunctions(TextMessageEvent e, String message){
        String function = message.replace("!botfunction stop ","");
        controlFunctions(e, function.replace(" ", ""), true);
    }

    private void controlFunctions(TextMessageEvent e, String function, boolean stopTasks){
        String startStop;
        if(stopTasks)
            startStop = "Stopping";
        else
            startStop = "Starting";
        switch (function){
            case "ClientMove" :
                afterConnect.updateFunction(function, stopTasks);
                api.sendPrivateMessage(e.getInvokerId(), startStop + " ClientMove function");
                log.info("{}: {} ClientMove function", botName, startStop);
                break;
            case "VersionChecker" :
                afterConnect.updateFunction(function, stopTasks);
                api.sendPrivateMessage(e.getInvokerId(), startStop + " VersionChecker function");
                log.info("{}: {} VersionChecker function", botName, startStop);
                break;
            case "Friendlist" :
                afterConnect.updateFunction(function, stopTasks);
                api.sendPrivateMessage(e.getInvokerId(), startStop + " Friendlist function");
                log.info("{}: {} Friendlist function", botName, startStop);
                break;
            case "ChannelAutoCreate" :
                afterConnect.updateFunction(function, stopTasks);
                api.sendPrivateMessage(e.getInvokerId(), startStop + " ChannelAutoCreate function");
                log.info("{}: {} ChannelAutoCreate function", botName, startStop);
                break;
            case "ClientAFK" :
                api.sendPrivateMessage(e.getInvokerId(), startStop + " ClientAfk listener.");
                log.info("{}: {} ClientAfk listener.", botName, startStop);
                taskTimer.controlClientAfkTimer(stopTasks);
                break;
            case "Broadcast" :
                afterConnect.updateFunction(function, stopTasks);
                api.sendPrivateMessage(e.getInvokerId(), startStop + " Broadcast function");
                log.info("{}: {} Broadcast function", botName, startStop);
                break;
            case "Viewer" :
                afterConnect.updateFunction(function, stopTasks);
                api.sendPrivateMessage(e.getInvokerId(), startStop + "  Ts3Viewer function");
                log.info("{}: {} Ts3Viewer function", botName, startStop);
                break;
            case "TwoWayAuth" :
                api.sendPrivateMessage(e.getInvokerId(), startStop + "  TwoWayAuth function");
                log.info("{}: {} TwoWayAuth function", botName, startStop);
                afterConnect.updateFunction(function, stopTasks);
                break;
            default:
                api.sendPrivateMessage(e.getInvokerId(),"Die Funktion \"" + function + "\" existiert nicht.");
                startStop = "";
        }
        if(!startStop.equals("")){
            addOrDeleteFunction(startStop,function);
        }
    }

    private void addOrDeleteFunction(String startStop, String function){
        if(startStop.equals("Stopping")){
            if(!afterConnect.getDeactivatedFuntions().contains(function)){
                afterConnect.getDeactivatedFuntions().add(function);
            }
        }else{
            if(afterConnect.getDeactivatedFuntions().contains(function)){
                afterConnect.getDeactivatedFuntions().remove(function);
            }
        }
    }
}
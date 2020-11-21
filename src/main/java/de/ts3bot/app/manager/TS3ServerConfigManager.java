package de.ts3bot.app.manager;

import com.github.theholywaffle.teamspeak3.TS3Query;
import de.ts3bot.app.models.TS3ServerConfig;
import de.ts3bot.app.models.functions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Captain on 23.01.2016.
 * Manager/setzen der Daten
 */
public class TS3ServerConfigManager {
    private static final Logger LOG = LogManager.getLogger(TS3ServerConfigManager.class.getName());
    private static final String BOT_ADMIN = "bot_admin";

    private TS3ServerConfigManager(){ }

//region read config
    public static TS3ServerConfig readInstanceConfig(TS3ServerConfig serverConfig, String instancelocation, boolean all) {
        if (serverConfig == null) serverConfig = new TS3ServerConfig();

        File configFile = new File(instancelocation);
        String configFolder = configFile.getAbsolutePath().substring( 0, configFile.getAbsolutePath().lastIndexOf( File.separator ) );
        serverConfig.setConfigFolder(configFolder);
        serverConfig.setConfigPath(instancelocation);

        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(instancelocation)) {
            // load a properties file
            prop.load(input);
            // serverConfig.get the property value and print it out
            String configFunction = prop.getProperty("functions");
            String[] configFunctionArr = configFunction.split(",");
            HashMap<String, String> funcNames = new HashMap<>();
            for (String help : configFunctionArr) {
                if (help.contains(":"))
                    funcNames.put(help.substring(help.indexOf(':') + 1), help.substring(0, help.indexOf(':')));
            }
            serverConfig.setFunctionNames(funcNames);
            if (all) {
                serverConfig.setLanguage(prop.getProperty("language"));

                String floodrate = prop.getProperty("ts3_server_floodrate");
                if( ! floodrate.isEmpty() ){
                    serverConfig.setTs3ServerFloodrate(findTS3FloodRate(floodrate.toLowerCase()));
                }
                serverConfig.setTs3ServerIp(prop.getProperty("ts3_server_ip"));
                serverConfig.setTs3ServerQueryPort(Integer.parseInt(prop.getProperty("ts3_server_query_port")));
                serverConfig.setTs3ServerPort(Integer.parseInt(prop.getProperty("ts3_server_port")));
                serverConfig.setTs3ServerQueryLoginName(prop.getProperty("ts3_server_query_login_name"));
                serverConfig.setTs3ServerQueryLoginPassword(prop.getProperty("ts3_server_query_login_password"));
                serverConfig.setTs3BotChannelId(Integer.parseInt(prop.getProperty("ts3_bot_channel_id")));
            }
            serverConfig.setTs3BotNickname(prop.getProperty("ts3_bot_nickname"));
            serverConfig.setTs3BotNickname2(prop.getProperty("ts3_bot_nickname2"));
            if (! prop.getProperty(BOT_ADMIN).equals("")) {
                serverConfig.setBotAdmin(Arrays.asList(prop.getProperty(BOT_ADMIN).split(",")));
            }else {
                serverConfig.setBotAdmin(new ArrayList<>());
            }
            serverConfig.setBotFullAdmin(Arrays.asList(prop.getProperty("bot_full_admin").split(",")));

            readFunctions(prop, serverConfig, funcNames);
        } catch (IOException ex) {
            LOG.error("IOException error: {}", ex.getMessage());
        } catch (Exception ex) {
            LOG.error("Config konnte nicht geladen werden.");
            LOG.error("Read config - Exception: {}", FormatManager.StackTraceToString(ex));
        }

        return serverConfig;
    }

    private static void readFunctions(Properties prop, TS3ServerConfig serverConfig, HashMap<String, String> funcNames) {
        int clientMove = 0;
        int clientAFK = 0;
        int gameServer = 0;
        int welcomeMessage = 0;
        for (Map.Entry<String, String> entry : funcNames.entrySet()) {
            String key = entry.getKey();
            switch(funcNames.get(key)){
                case "ClientMove" :
                    serverConfig.addFunctionMove(key, readFunctionMove(prop, serverConfig, key, clientMove));
                    clientMove++;
                    break;
                case "Friendlist" :
                    serverConfig.setMoveToFriendNeededJoinPower(Integer.parseInt(prop.getProperty(key + "_move_to_friend_needed_join_power")));
                    break;
                case "ChannelAutoCreate" :
                    serverConfig.setFunctionChannelAutoCreate(readFunctionChannelAutoCreate(prop, key));
                    break;
                case "VersionChecker":
                    serverConfig.setVersionCheckTime(Integer.parseInt(prop.getProperty(key + "_version_check_time")));
                    break;
                case "ClientAFK" :
                    serverConfig.addFunctionAFK(key, readFunctionAfk(prop, serverConfig, key, clientAFK));
                    clientAFK++;
                    break;
                case "AcceptRules" :
                    serverConfig.setFunctionAcceptRules(readFunctionAcceptRules(prop, key));
                    break;
                case "AutoRemove" :
                    serverConfig.setFunctionAutoRemove(readFunctionAutoRemove(prop, key));
                    break;
                case "Broadcast" :
                    serverConfig.setBroadcastClients(prop.getProperty(key + "_broadcast_clients"));
                    break;
                case "Viewer" :
                    serverConfig.setTs3ViewerUpdateTime(Integer.parseInt(prop.getProperty(key + "_ts3_viewer_update_time")));
                    serverConfig.setTs3ViewerTextColor(prop.getProperty(key + "_ts3_viewer_text_color"));
                    serverConfig.setTs3ViewerBackgroundColor(prop.getProperty(key + "_ts3_viewer_background_color"));
                    serverConfig.setTs3ViewerFileLocation(prop.getProperty(key + "_ts3_viewer_file_location"));
                    serverConfig.setTs3ViewerServerIp(prop.getProperty(key + "_ts3_viewer_server_ip"));
                    break;
                case "UpdateGameChannel" :
                    serverConfig.addFunctionUpdateGameChannel(key, readFunctionGameServer(prop, serverConfig, key, gameServer));
                    gameServer++;
                    break;
                case "WelcomeMessage" :
                    serverConfig.addFunctionWelcomeMessage(key, readFunctionWelcomeMessage(prop, serverConfig, key, welcomeMessage));
                    welcomeMessage++;
                    break;
                case "Twitch" :
                    serverConfig.setTwitchApiClientID(prop.getProperty(key + "_twitch_api_client_id"));
                    serverConfig.setTwitchApiClientOauthToken(prop.getProperty(key + "_twitch_api_client_oauth_token"));
                    serverConfig.setTwitchLiveGruppe(Integer.parseInt(prop.getProperty(key + "_twitch_server_group")));
                    serverConfig.setTwitchConfigName(prop.getProperty(key + "_twitch_config_name"));
                    break;
                default:
                    LOG.error("Couldn't find the function \"" + funcNames.get(key) + "\"");
            }
        }
    }

    private static FunctionChannelAutoCreate readFunctionChannelAutoCreate(Properties prop, String key) {
        FunctionChannelAutoCreate functionChannelAutoCreate = new FunctionChannelAutoCreate();
        functionChannelAutoCreate.setChannelidlist(convertStringArrayToIntArray(prop.getProperty(key + "_channel_check_subchannel")));
        functionChannelAutoCreate.setChannelPasswordFilePath(prop.getProperty(key + "_channel_check_password_file_path"));
        return functionChannelAutoCreate;
    }

    private static FunctionModel readFunctionAutoRemove(Properties prop, String key){
        FunctionModel autoRemove = new FunctionModel();
        autoRemove.setGruppenIDs( FormatManager.convertStrToIntList(new ArrayList<>(),prop.getProperty(key + "_auto_remove_group_ids")) );
        return autoRemove;
    }

    private static FunctionAcceptRules readFunctionAcceptRules(Properties prop, String key){
        FunctionAcceptRules acceptRules = new FunctionAcceptRules();
        acceptRules.setFirstConnectionGroup( Integer.parseInt( prop.getProperty( key + "_accept_rules_first_group")) );
        acceptRules.setAcceptedGuest( Integer.parseInt( prop.getProperty( key + "_accept_rules_accepted_group")) );
        acceptRules.setForbiddenNamesFilePath( prop.getProperty( key + "_accept_rules_forbidden_file_path") );
        acceptRules.setPokeMessage( prop.getProperty( key + "_accept_rules_poke_message") );
        acceptRules.setPrivateMessageFilePath( prop.getProperty( key + "_accept_rules_message_file_path") );
        List<String> nameSeparators = FormatManager.convertStrToStrList( prop.getProperty(key + "_accept_rules_name_seperators" ) );
        acceptRules.setNameSeperators( nameSeparators );
        return acceptRules;
    }

    private static FunctionWelcomeMessage readFunctionWelcomeMessage(Properties prop, TS3ServerConfig serverConfig, String key, int welcomeMessage) {
        FunctionWelcomeMessage message;
        if ( serverConfig.getFunctionWelcomeMessageHashMap() == null || ! serverConfig.getFunctionWelcomeMessageHashMap().containsKey(key) ) {
            message = new FunctionWelcomeMessage();
        }else{
            message = serverConfig.getFunctionWelcomeMessageHashMap().get(key);
        }

        if(prop.containsKey(key + "_welcome_date")) {
            String dateStr = prop.getProperty(key + "_welcome_date");
            if ( ! dateStr.isEmpty() && ! dateStr.equals("empty") ) {
                try{
                    DateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.GERMAN);
                    Date configDate = format.parse(dateStr);
                    message.setDateUntil(configDate);

                    Date today = new Date();
                    if( today.after(configDate) ){
                        message.setEndDate(true);
                    }
                }catch (Exception ex) {
                    LOG.error("Exception error: {}", FormatManager.StackTraceToString(ex));
                }
            }else if ( ! dateStr.isEmpty() ){
                message.setDateUntil(null);
                message.setEndDate(false);
            }
        }

        message.setWelcome_group_ids( FormatManager.convertStrToIntList(new ArrayList<>(),prop.getProperty(key + "_welcome_group_ids")) );
        message.setWelcome_message(prop.getProperty(key + "_welcome_message"));
        message.setWelcome_repeat(prop.getProperty(key + "_welcome_repeat"));
        message.setPokeClient(Boolean.parseBoolean(prop.getProperty(key + "_welcome_poke_client")));
        message.setPokeMessage(prop.getProperty(key + "_welcome_poke_message"));
        message.setFunctionId(welcomeMessage);
        message.setFunctionKey(key);
        return message;
    }

    private static FunctionGameServer readFunctionGameServer(Properties prop, TS3ServerConfig serverConfig, String key, int gameServer) {
        FunctionGameServer server;
        if ( serverConfig.getFunctionUpdateGameChannelHashMap() == null || ! serverConfig.getFunctionUpdateGameChannelHashMap().containsKey(key) ) {
            server = new FunctionGameServer();
        }else{
            server = serverConfig.getFunctionUpdateGameChannelHashMap().get(key);
        }
        server.setGame_type(prop.getProperty(key + "_game_type"));
        server.setGame_server_channel_name(prop.getProperty(key + "_game_server_channel_name"));
        server.setGame_server_channel_id(Integer.parseInt(prop.getProperty(key + "_game_server_channel_id")));
        server.setGame_server_ip(prop.getProperty(key + "_game_server_ip"));
        server.setGame_server_port(Integer.parseInt(prop.getProperty(key + "_game_server_port")));
        server.setGame_server_password(prop.getProperty(key + "_game_server_password"));
        server.setGame_server_rcon_password(prop.getProperty(key + "_game_server_rcon_password"));
        server.setFunctionId(gameServer);
        return server;
    }

    private static FunctionAFK readFunctionAfk(Properties prop, TS3ServerConfig serverConfig, String key, int clientAFK) {
        FunctionAFK afk;
        if ( serverConfig.getFunctionAFKHashMap() == null || ! serverConfig.getFunctionAFKHashMap().containsKey(key) ) {
            afk = new FunctionAFK();
        }else{
            afk = serverConfig.getFunctionAFKHashMap().get(key);
        }
        afk.setClient_afk_time(Integer.parseInt(prop.getProperty(key + "_client_afk_time")));
        afk.setClient_afk_channel(Integer.parseInt(prop.getProperty(key + "_client_afk_channel")));
        afk.setClient_afk_channel_io( FormatManager.convertStrToIntList(new ArrayList<>(),prop.getProperty(key + "_client_afk_channel_io")) );
        afk.setClient_afk_channel_watch( prop.getProperty(key + "_client_afk_channel_watch") );
        afk.setAfk_client_afk_group_ids( FormatManager.convertStrToIntList(new ArrayList<>(),prop.getProperty(key + "_client_afk_group_ids")) );
        afk.setAfk_client_afk_group_watch(prop.getProperty(key + "_client_afk_group_watch"));
        afk.setFunctionId(clientAFK);
        return afk;
    }

    private static FunctionMove readFunctionMove(Properties prop, TS3ServerConfig serverConfig, String key, int clientMove) {
        FunctionMove move;
        if ( serverConfig.getFunctionMoveHashMap() == null || ! serverConfig.getFunctionMoveHashMap().containsKey(key) ) {
            move = new FunctionMove();
        }else{
            move = serverConfig.getFunctionMoveHashMap().get(key);
        }
        move.setClient_moved_channel(Integer.parseInt(prop.getProperty(key + "_client_moved_channel")));
        move.setClient_moved_group_notify( FormatManager.convertStrToIntList(new ArrayList<>(),prop.getProperty(key + "_client_moved_group_notify")) );
        move.setClient_moved_group_ids( FormatManager.convertStrToIntList(new ArrayList<>(),prop.getProperty(key + "_client_moved_group_ids")) );
        move.setClient_moved_group_action(prop.getProperty(key + "_client_moved_group_action"));
        move.setFunctionId(clientMove);
        return move;
    }

    public static TS3Query.FloodRate findTS3FloodRate(String floodrate){
        switch (floodrate){
            case "unlimited":
                return TS3Query.FloodRate.UNLIMITED;
            case "default":
                return TS3Query.FloodRate.DEFAULT;
            default:
                try{
                    int time = Integer.parseInt(floodrate);
                    return TS3Query.FloodRate.custom(time);
                }catch(Exception ex){
                    LOG.error("Floodrate isn't one of unlimited, default or number: " + floodrate);
                    LOG.info("Setting default floodrate.");
                    return TS3Query.FloodRate.DEFAULT;
                }
        }
    }
//endregion

//region write config
    public static boolean writeFile(TS3ServerConfig serverConfig, String instancelocation){
        // The name of the file to open.
        String fileName = instancelocation+".new";
        String line;

        try (FileReader fileReader = new FileReader(instancelocation);
             BufferedReader bufferedReader = new BufferedReader(fileReader);
             FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter))
        {
            while((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("#") || line.equals("")|| line.startsWith("-")){
                    bufferedWriter.write(line+"\n");
                }else{
                    String lineTmp = getkeyValuePair(serverConfig, line);
                    if(lineTmp.equals("")){
                        bufferedWriter.write(line+"\n");
                    }else{
                        bufferedWriter.write(lineTmp);
                    }
                }
            }
            // Note that write() does not automatically
            // append a newline character.
        }
        catch(IOException ex) {
            LOG.error("Error writing to file: {}", fileName);
            LOG.error("{}", ex.getMessage());
        }catch(Exception ex){
            LOG.error("{}", FormatManager.StackTraceToString(ex));
        }
        try{
            Files.move(Paths.get(fileName), Paths.get(instancelocation), StandardCopyOption.REPLACE_EXISTING);
            return true;
        }catch (Exception ex){
            LOG.error("{}", FormatManager.StackTraceToString(ex));
        }
        return false;
    }

    private static String getkeyValuePair(TS3ServerConfig serverConfig, String line){
        String name = line.substring(0,line.indexOf('=')).replace(" ","");
        switch (name){
            case "language":
                return name + " = " + serverConfig.getLanguage() + "\n";
            case "ts3_server_ip":
                return name + " = " + serverConfig.getTs3ServerIp() + "\n";
            case "ts3_server_port":
                return name + " = " + serverConfig.getTs3ServerPort() + "\n";
            case "ts3_server_query_port":
                return line + "\n";
            case "ts3_server_query_login_name":
                return name + " = " + serverConfig.getTs3ServerQueryLoginName() + "\n";
            case "ts3_server_query_login_password":
                return name + " = " + serverConfig.getTs3ServerQueryLoginPassword() + "\n";
            case "ts3_bot_nickname":
                return name + " = " + serverConfig.getTs3BotNickname() + "\n";
            case "ts3_bot_nickname2":
                return name + " = " + serverConfig.getTs3BotNickname2() + "\n";
            case "ts3_bot_channel_id":
                return name + " = " + serverConfig.getTs3BotChannelId() + "\n";
            case "ts3_viewer_file_location":
                return name + " = " + serverConfig.getTs3ViewerFileLocation() + "\n";
            case "ts3_viewer_text_color":
                return name + " = " + serverConfig.getTs3ViewerTextColor() + "\n";
            case "ts3_viewer_background_color":
                return name + " = " + serverConfig.getTs3ViewerBackgroundColor() + "\n";
            case "ts3_viewer_server_ip":
                return name + " = " + serverConfig.getTs3ViewerServerIp() + "\n";
            case BOT_ADMIN:
                if (serverConfig.getBotAdmin().isEmpty()){
                    return name + " = \n";
                }else {
                    return name + " = " + String.join(",", serverConfig.getBotAdmin()) + "\n";
                }
            case "bot_full_admin":
                return name + " = " + String.join(",",serverConfig.getBotFullAdmin()) + "\n";
            case "functions":
                StringBuilder sb = new StringBuilder();
                for (String key : serverConfig.getFunctionNames().keySet()){
                    sb.append(serverConfig.getFunctionNames().get(key));
                    sb.append(":").append(key).append(",");
                }
                sb.replace(sb.lastIndexOf(","),sb.lastIndexOf(",")+1,"");
                return name + " = " + sb + "\n";
            default:
                return getkeyValuePairOfFunc(serverConfig, name);
        }
    }

    private static String getkeyValuePairOfFunc(TS3ServerConfig serverConfig, String name){
        for (String key : serverConfig.getFunctionNames().keySet()) {
            if ( ! name.startsWith(key) ){
                continue;
            }
            name = name.substring(name.indexOf('_')+1);

            switch (serverConfig.getFunctionNames().get(key)){
                case "ChannelAutoCreate":
                    return getChannelAutoCreate(name, key, serverConfig);
                case "ClientMove":
                    return getClientMoveSetting(name, key, serverConfig);
                case "ClientAFK":
                    return getClientAFKSetting(name, key, serverConfig);
                case "Viewer":
                    return getViewerSetting(name, key, serverConfig);
                case "AcceptRules":
                    return getAcceptRulesSetting(name, key, serverConfig);
                case "WelcomeMessage":
                    return getWelcomeMessageSetting(name, key, serverConfig);
                case "Twitch":
                    return getTwitchSetting(name, key, serverConfig);
            }
        }
        return "";
    }

    private static String getChannelAutoCreate(String name, String key, TS3ServerConfig serverConfig) {
        switch (name) {
            case "channel_check_subchannel":
                String listString = serverConfig.getFunctionChannelAutoCreate().getChannelidlist().toString();
                return key + "_channel_check_subchannel = " + listString.substring(1, listString.length()-1).replace(" ","") + "\n";
            case "channel_check_password_file_path":
                return key + "_channel_check_password_file_path = " + serverConfig.getFunctionChannelAutoCreate().getChannelPasswordFilePath() + "\n";
        }
        return "";
    }

    private static String getTwitchSetting(String name, String key, TS3ServerConfig serverConfig){
        switch (name) {
            case "twitch_api_client_id":
                return key + "_twitch_api_client_id = " + serverConfig.getTwitchApiClientID() + "\n";
            case "twitch_api_client_secret":
                return key + "_twitch_api_client_oauth_token = " + serverConfig.getTwitchApiClientOauthToken() + "\n";
            case "twitch_server_group":
                return key + "_twitch_server_group = " + serverConfig.getTwitchLiveGruppe() + "\n";
            case "twitch_config_name":
                return key + "_twitch_config_name = " + serverConfig.getTwitchConfigName() + "\n";
        }
        return "";
    }

    private static String getClientAFKSetting(String name, String key, TS3ServerConfig serverConfig){
        String listString;
        switch (name) {
            case "client_afk_time":
                return key + "_client_afk_time = " + serverConfig.getFunctionAFKHashMap().get(key).getClient_afk_time()+ "\n";
            case "client_afk_channel":
                return key + "_client_afk_channel = " + serverConfig.getFunctionAFKHashMap().get(key).getClient_afk_channel()+ "\n";
            case "client_afk_channel_io":
                listString = serverConfig.getFunctionAFKHashMap().get(key).getClient_afk_channel_io().toString();
                return key + "_client_afk_channel_io = " + listString.substring(1, listString.length()-1).replace(" ","") + "\n";
            case "client_afk_channel_watch":
                return key + "_client_afk_channel_watch = " + serverConfig.getFunctionAFKHashMap().get(key).getClient_afk_channel_watch()+ "\n";
            case "client_afk_group_ids":
                listString = serverConfig.getFunctionAFKHashMap().get(key).getAfk_client_afk_group_ids().toString();
                return key + "_client_afk_group_ids = " + listString.substring(1, listString.length()-1).replace(" ","") + "\n";
            case "client_afk_group_watch":
                return key + "_client_afk_group_watch = " + serverConfig.getFunctionAFKHashMap().get(key).getAfk_client_afk_group_watch()+ "\n";
        }
        return "";
    }

    private static String getClientMoveSetting(String name, String key, TS3ServerConfig serverConfig){
        String listString;
        switch (name) {
            case "client_moved_channel":
                return key + "_client_moved_channel = " + serverConfig.getFunctionMoveHashMap().get(key).getClient_moved_channel()+ "\n";
            case "client_moved_group_notify":
                listString = serverConfig.getFunctionMoveHashMap().get(key).getClient_moved_group_notify().toString();
                return key + "_client_moved_group_notify = " + listString.substring(1, listString.length()-1).replace(" ","") + "\n";
            case "client_moved_group_ids":
                listString = serverConfig.getFunctionMoveHashMap().get(key).getClient_moved_group_ids().toString();
                return key + "_client_moved_group_ids = " + listString.substring(1, listString.length()-1).replace(" ","") + "\n";
            case "client_moved_group_action":
                return key + "_client_moved_group_action = " + serverConfig.getFunctionMoveHashMap().get(key).getClient_moved_group_action()+ "\n";
        }
        return "";
    }

    private static String getAcceptRulesSetting(String name, String key, TS3ServerConfig serverConfig){
        FunctionAcceptRules acceptRules = serverConfig.getFunctionAcceptRules();
        switch (name) {
            case "accept_rules_first_group":
                return key + "_accept_rules_first_group = " + acceptRules.getFirstConnectionGroup() + "\n";
            case "accept_rules_poke_message":
                return key + "_accept_rules_poke_message = " + acceptRules.getPokeMessage() + "\n";
            case "accept_rules_message":
                return key + "accept_rules_message_file_path = " + acceptRules.getPrivateMessageFilePath() + "\n";
            case "accept_rules_accepted_group":
                return key + "_accept_rules_accepted_group = " + acceptRules.getAcceptedGuest() + "\n";
            case "accept_rules_name_seperators":
                return key + "_accept_rules_name_seperators = " + FormatManager.convertListStrToStr(acceptRules.getNameSeperators()) + "\n";
            case "accept_rules_forbidden_file_path":
                return key + "_accept_rules_forbidden_file_path = " + acceptRules.getForbiddenNamesFilePath() + "\n";
        }
        return "";
    }

    private static String getViewerSetting(String name, String key, TS3ServerConfig serverConfig){
        switch (name) {
            case "ts3_viewer_update_time":
                return key + "_ts3_viewer_update_time = " + serverConfig.getTs3ViewerUpdateTime() + "\n";
            case "ts3_viewer_file_location":
                return key + "_ts3_viewer_file_location = " + serverConfig.getTs3ViewerFileLocation() + "\n";
            case "ts3_viewer_text_color":
                return key + "_ts3_viewer_text_color = " + serverConfig.getTs3ViewerTextColor() + "\n";
            case "ts3_viewer_background_color":
                return key + "_ts3_viewer_background_color = " + serverConfig.getTs3ViewerBackgroundColor() + "\n";
            case "ts3_viewer_server_ip":
                return key + "_ts3_viewer_server_ip = " + serverConfig.getTs3ViewerServerIp() + "\n";
        }
        return "";
    }

    private static String getWelcomeMessageSetting(String name, String key, TS3ServerConfig serverConfig){
        switch (name) {
            case "welcome_date":
                Format formatter = new SimpleDateFormat("dd.MM.yy HH:mm");
                if (serverConfig.getFunctionWelcomeMessageHashMap().get(key).getDateUntil() == null){
                    return  key + "_welcome_date = empty\n";
                }
                Date date = serverConfig.getFunctionWelcomeMessageHashMap().get(key).getDateUntil();
                return  key + "_welcome_date = " + formatter.format(date) + "\n";
            case "welcome_message":
                return key + "_welcome_message = " + serverConfig.getFunctionWelcomeMessageHashMap().get(key).getWelcome_message() + "\n";
            case "welcome_poke_message":
                return key + "_welcome_poke_message = " + serverConfig.getFunctionWelcomeMessageHashMap().get(key).getPokeMessage() + "\n";
            case "welcome_poke_client":
                return key + "_welcome_poke_client = " + serverConfig.getFunctionWelcomeMessageHashMap().get(key).isPokeClient() + "\n";
            case "welcome_repeat":
                return key + "_welcome_repeat = " + serverConfig.getFunctionWelcomeMessageHashMap().get(key).getWelcome_repeat() + "\n";
            case "welcome_group_ids":
                String listString = serverConfig.getFunctionWelcomeMessageHashMap().get(key).getWelcome_group_ids().toString();
                return key + "_welcome_group_ids = " + listString.substring(1, listString.length()-1).replace(" ","") + "\n";
        }
        return "";
    }

    private static List<Integer> convertStringArrayToIntArray(String list){
        String[] arr = list.split(",");
        List<Integer> arrList = new ArrayList<>();
        for (String str : arr){
            arrList.add(Integer.parseInt(str));
        }
        return arrList;
    }
//endregion
}

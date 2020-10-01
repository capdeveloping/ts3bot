package de.ts3bot.app.library.configload;

import de.ts3bot.app.manager.FormatManager;
import de.ts3bot.app.manager.TS3ServerConfigManager;
import de.ts3bot.app.models.ReloadAfterConnect;
import de.ts3bot.app.models.TS3ServerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Captain on 23.12.2015.
 * Schreibe in die TS3 Bot Config
 */
public class TS3ConfigWrite {
    private final Logger log = LogManager.getLogger(TS3ConfigWrite.class.getName());
    private TS3ServerConfig serverConfig;
    private ReloadAfterConnect afterConnect;

    public TS3ConfigWrite(TS3ServerConfig serverConfig, ReloadAfterConnect afterConnect) {
        this.serverConfig = serverConfig;
        this.afterConnect = afterConnect;
    }

    public boolean setConfigKey(String message, boolean isBotFullAdmin) {
        message = message.replace("!botconfigaddvalue ", "");
        String[] configKeyValue = message.split(FormatManager.sucheZeichen(message, "="));
        boolean configChanged = false;
        String key = configKeyValue[0].replace(" ", "");
        String value = configKeyValue[1];
        if (isBotFullAdmin) {
            configChanged = setFullAdminConfigKey(key, value);
            if (configChanged) {
                return setConfigEnd(configChanged);
            }
        }
        switch (key) {
            case "language":
                serverConfig.setLanguage(value);
                configChanged = true;
                break;
            case "bot_standard_channel":
                serverConfig.setTs3BotChannelId(Integer.valueOf(value));
                configChanged = true;
                break;
            case "bot_nickname":
                serverConfig.setTs3BotNickname(value);
                configChanged = true;
                break;
            case "bot_nickname2":
                serverConfig.setTs3BotNickname2(value);
                configChanged = true;
                break;
            default:
                break;
        }

        if (configChanged) {
            return setConfigEnd(configChanged);
        }

        configChanged = setFuncConfigKey(key, value, configChanged);

        return setConfigEnd(configChanged);
    }

    private boolean setConfigEnd(boolean configChanged){
        if(configChanged){
            afterConnect.setServerConfig(serverConfig);
        }
        return configChanged;
    }

    private boolean setFullAdminConfigKey(String key, String value){
        switch (key){
            case "ts3_server_floodrate":
                serverConfig.setTs3ServerFloodrate(TS3ServerConfigManager.findTS3FloodRate(value));
                break;
            case "ts3_server_query_port":
                serverConfig.setTs3ServerQueryPort(Integer.valueOf(value));
                break;
            case "ts3_port":
                serverConfig.setTs3ServerPort(Integer.valueOf(value));
                break;
            case "ts3_ip":
                serverConfig.setTs3ServerIp(value);
                break;
            case "login_name":
                serverConfig.setTs3ServerQueryLoginName(value);
                break;
            case "login_password":
                serverConfig.setTs3ServerQueryLoginPassword(value);
                break;
            case "bot_admin":
                serverConfig.setBotAdmin(FormatManager.convertStrToStrList(serverConfig.getBotAdmin(), value));
                break;
            case "bot_full_admin":
                serverConfig.setBotFullAdmin(FormatManager.convertStrToStrList(serverConfig.getBotFullAdmin(), value));
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean setFuncConfigKey(String key, String value, boolean configChanged){
        String funcId = key.substring( 0, key.indexOf('_') );
        String funcKey = key.substring( key.indexOf('_') + 1 );
        switch(serverConfig.getFunctionNames().get(funcId)){
            case "ChannelAutoCreate":
                serverConfig.getFunctionChannelAutoCreate().getChannelidlist().add(Integer.valueOf(value));
                configChanged = true;
                break;
            case "ClientMove":
                configChanged = setClientMoveValue(funcId, funcKey, value);
                break;
            case "ClientAFK":
                configChanged = setClientAFKValue(funcId, funcKey, value);
                break;
            case "Viewer":
                configChanged = setTs3ViewerValue(funcKey, value);
                break;
            case "WelcomeMessage":
                configChanged = setWelcomeMessageValue(funcId, funcKey, value);
                break;
            case "AcceptRules":
                configChanged = setAcceptRulesValue(funcId, funcKey, value);
                break;
            case "Twitch":
                configChanged = setTwitchValue(funcKey, value);
                break;
        }
        return configChanged;
    }

    private boolean setAcceptRulesValue(String funcId, String funcKey, String value) {
        switch (funcKey) {
            case "accept_rules_name_seperators":
                serverConfig.getFunctionAcceptRules().getNameSeperators().add(value);
                break;
            case "accept_rules_accepted_group":
                try{
                    serverConfig.getFunctionAcceptRules().setAcceptedGuest( Integer.valueOf(value) );
                }
                catch (Exception ex){
                    log.error("Couldn't convert value as expected : {}", FormatManager.StackTraceToString(ex));
                }
                break;
            case "accept_rules_first_group":
                try{
                    serverConfig.getFunctionAcceptRules().setFirstConnectionGroup( Integer.valueOf(value) );
                }
                catch (Exception ex){
                    log.error("Couldn't convert value as expected : {}", FormatManager.StackTraceToString(ex));
                }
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean setTwitchValue(String funcKey, String value){
        switch (funcKey) {
            case "twitch_api_client_id":
                serverConfig.setTwitchApiClientID(value);
                break;
            case "twitch_api_client_secret":
                serverConfig.setTwitchApiClientOauthToken(value);
                break;
            case "twitch_server_group":
                try{
                    serverConfig.setTwitchLiveGruppe(Integer.parseInt(value));
                }
                catch (Exception ex){
                    log.error("Couldn't convert value as expected : {}", FormatManager.StackTraceToString(ex));
                }
                break;
            case "twitch_config_name":
                serverConfig.setTwitchConfigName(value);
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean setTs3ViewerValue(String funcKey, String value){
        switch (funcKey){
            case "ts3_viewer_update_time":
                serverConfig.setTs3ViewerUpdateTime(Integer.parseInt(value));
                break;
            case "ts3_viewer_file_location":
                serverConfig.setTs3ViewerFileLocation(value);
                break;
            case "ts3_viewer_text_color":
                serverConfig.setTs3ViewerTextColor(value);
                break;
            case "ts3_viewer_background_color":
                serverConfig.setTs3ViewerBackgroundColor(value);
                break;
            case "ts3_viewer_server_ip":
                serverConfig.setTs3ViewerServerIp(value);
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean setWelcomeMessageValue(String funcId, String funcKey, String value){
        switch (funcKey){
            case "welcome_message":
                serverConfig.getFunctionWelcomeMessageHashMap().get(funcId).setWelcome_message(value);
                break;
            case "welcome_date":
                try{
                    DateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.GERMAN);
                    Date date = format.parse(value);
                    serverConfig.getFunctionWelcomeMessageHashMap().get(funcId).setDateUntil(date);
                }catch (Exception ex){
                    log.error("Couldn't convert value as expected : {}", FormatManager.StackTraceToString(ex));
                }
                break;
            case "welcome_repeat":
                serverConfig.getFunctionWelcomeMessageHashMap().get(funcId).setWelcome_repeat(value);
                break;
            case "welcome_group_ids":
                serverConfig.getFunctionWelcomeMessageHashMap().get(funcId).getGruppenIDs().add(Integer.parseInt(value));
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean setClientAFKValue(String funcId, String funcKey, String value){
        switch (funcKey){
            case "client_afk_time":
                serverConfig.getFunctionAFKHashMap().get(funcId).setClient_afk_time(Integer.parseInt(value));
                break;
            case "client_afk_channel":
                serverConfig.getFunctionAFKHashMap().get(funcId).setClient_afk_channel(Integer.parseInt(value));
                break;
            case "client_afk_channel_io":
                serverConfig.getFunctionAFKHashMap().get(funcId).getClient_afk_channel_io().add(Integer.parseInt(value));
                break;
            case "client_afk_channel_watch":
                serverConfig.getFunctionAFKHashMap().get(funcId).setClient_afk_channel_watch(value);
                break;
            case "client_afk_group_ids":
                serverConfig.getFunctionAFKHashMap().get(funcId).getAfk_client_afk_group_ids().add(Integer.parseInt(value));
                break;
            case "client_afk_group_watch":
                serverConfig.getFunctionAFKHashMap().get(funcId).setAfk_client_afk_group_watch(value);
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean setClientMoveValue(String funcId, String funcKey, String value){
        switch (funcKey){
            case "client_moved_channel":
                serverConfig.getFunctionMoveHashMap().get(funcId).setClient_moved_channel(Integer.parseInt(value));
                break;
            case "client_moved_group_notify":
                serverConfig.getFunctionMoveHashMap().get(funcId).getClient_moved_group_notify().add(Integer.parseInt(value));
                break;
            case "client_moved_group_ids":
                serverConfig.getFunctionMoveHashMap().get(funcId).getClient_moved_group_ids().add(Integer.parseInt(value));
                break;
            case "client_moved_group_action":
                serverConfig.getFunctionMoveHashMap().get(funcId).setClient_moved_group_action(value);
                break;
            default:
                return false;
        }
        return true;
    }

    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void setAfterConnect(ReloadAfterConnect afterConnect) {
        this.afterConnect = afterConnect;
    }
}

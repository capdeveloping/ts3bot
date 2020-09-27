package de.ts3bot.app.manager;

import de.ts3bot.app.models.TS3ServerConfig;

public class GeneralManager {
    public static boolean IsUserBotAdmin(TS3ServerConfig serverConfig, String uid){
        if (serverConfig.getBotAdmin().isEmpty()){
            return serverConfig.getBotFullAdmin().contains(uid);
        }else{
            return serverConfig.getBotAdmin().contains(uid) || serverConfig.getBotFullAdmin().contains(uid);
        }
    }

    public static boolean IsUserBotFullAdmin(TS3ServerConfig serverConfig, String uid){
        return serverConfig.getBotFullAdmin().contains(uid);
    }
}

package de.ts3bot.app.models;

import de.ts3bot.app.library.configload.TS3Controller;
import de.ts3bot.app.manager.TS3ServerConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Captain on 24.01.2016.
 * Bot
 */

public class Bot {
    private TS3Controller ts3Instance;
    private TS3ServerConfig serverConfig;
    private BotInstance botInstance;
    private final Logger log = LogManager.getLogger(Bot.class);

    public Bot(String configPfad, String botName, BotInstanceManager botInstanceManager) {
        serverConfig = TS3ServerConfigManager.readInstanceConfig(null, configPfad, true);
        serverConfig.setBotName(botName);
        ts3Instance = new TS3Controller(serverConfig);
        botInstance = new BotInstance(ts3Instance, serverConfig, botInstanceManager);
    }

    public void connectBot() {
        log.info("{}: Lade TS3 API.", serverConfig.getBotName());
        log.info("{}: Verbinde zum TeamSpeak 3 Server.", serverConfig.getBotName());
        ts3Instance.startReconnect(botInstance);
    }

    public void disconnectBot(){
        log.info("{}: Lade TS3 API.", serverConfig.getBotName());
        log.info("{}: Verbinde zum TeamSpeak 3 Server.", serverConfig.getBotName());
        ts3Instance.disconnect(botInstance);
    }
}

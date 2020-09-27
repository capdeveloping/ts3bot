package de.ts3bot.app.library.configload;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import de.ts3bot.app.manager.FormatManager;
import de.ts3bot.app.models.BotInstance;
import de.ts3bot.app.models.TS3ServerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Captain on 25.10.2015.
 *
 */
public class TS3Controller {
    private final Logger log = LogManager.getLogger(TS3Controller.class);

    private TS3Api api ;
    private final TS3Config config;
    private TS3Query query;
    private TS3ServerConfig serverConfig;

    public TS3Api getApi(){
       return api;
    }

    public void setApi(TS3Api api){
        this.api = api;
    }

    public TS3Controller(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        config = new TS3Config();
        config.setHost(serverConfig.getTs3ServerIp());
        config.setQueryPort(serverConfig.getTs3ServerQueryPort());
        config.setEnableCommunicationsLogging(true);
        config.setFloodRate(serverConfig.getTs3ServerFloodrate());
    }

    public void startReconnect(BotInstance botInstance){
        config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());

        // Make stuff run every time the query (re)connects
        try{
            config.setConnectionHandler(new ConnectionHandler() {

                @Override
                public void onConnect(TS3Query ts3Query) {everyReconnect(ts3Query.getApi(), botInstance);}

                @Override
                public void onDisconnect(TS3Query ts3Query) {
                    botInstance.stopConfig();
                    query.exit();
                    log.info("{}: Disconnected", serverConfig.getBotName());
                }
            });
            // Here "stuffThatNeedsToRunEveryTimeTheQueryConnects" will be run!
            // (And every time the query reconnects)
            query = new TS3Query(config);
            query.connect();
        }catch(Exception ex){
            log.error("{}: Something went Wrong", serverConfig.getBotName());
            log.error("{}: Error message: {}", serverConfig.getBotName(), FormatManager.StackTraceToString(ex));
        }
        // Then do other stuff that only needs to be done once
    }

    public void disconnect(BotInstance botInstance){
        query.exit();
        botInstance.stopConfig();
        log.info("{}: Disconnected", serverConfig.getBotName());
    }

    private void everyReconnect(TS3Api api, BotInstance botInstance){
        // Logging in, selecting the virtual server, selecting a channel
        // and setting a nickname needs to be done every time we reconnect
        api.login(serverConfig.getTs3ServerQueryLoginName(), serverConfig.getTs3ServerQueryLoginPassword());
        api.selectVirtualServerByPort(serverConfig.getTs3ServerPort());
        setApi(api);
        log.info("{}: Connected", serverConfig.getBotName());
        botInstance.startConfig();
    }
}
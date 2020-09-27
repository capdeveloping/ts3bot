package de.ts3bot.app.models;

import de.ts3bot.app.manager.FormatManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.StringWriter;

public class InstanceThread extends Thread{
    private final Logger log = LogManager.getLogger(InstanceThread.class.getName());
    private String configpath;
    private String botname;
    private BotInstanceManager botInstanceManager;
    private Bot bot;
    private String online;

    InstanceThread(String configpath, String botname, BotInstanceManager botInstanceManager) {
        this.configpath = configpath;
        this.botname = botname;
        this.botInstanceManager = botInstanceManager;
    }

    @Override
    public void run() {
        super.run();
        try {
            bot = new Bot(configpath, botname, botInstanceManager);
            bot.connectBot();
        } catch (Exception ex) {
            log.error("Start bot failed - Exception: {}", FormatManager.StackTraceToString(ex));
            online = "offline";
        }
    }

    public void startBot() {
        this.start();
        online = "online";
    }

    public void stopBot() {
        bot.disconnectBot();
        bot = new Bot(configpath, botname, botInstanceManager);
        online = "offline";
    }

    public String getOnline() {
        return online;
    }

}

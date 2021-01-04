package de.ts3bot.app;

import com.github.theholywaffle.teamspeak3.TS3Api;
import de.ts3bot.app.features.TS3Viewer;
import de.ts3bot.app.features.ClientAfkMode;
import de.ts3bot.app.features.NewVersionChecker;
import de.ts3bot.app.features.UpdateGameServerChannel;
import de.ts3bot.app.models.BotInstance;
import de.ts3bot.app.models.TS3ServerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class TaskTimer {
    private final Logger LOG = LogManager.getLogger(BotInstance.class);
    private String botname;
    private List<ClientAfkMode> clientAfkMode;
    private TS3ServerConfig serverConfig;
    private NewVersionChecker versionChecker;
    private UpdateGameServerChannel updateGameServerChannel;

    public TaskTimer(List<ClientAfkMode> clientAfkMode, TS3ServerConfig serverConfig, String botname, NewVersionChecker versionChecker, UpdateGameServerChannel updateGameServerChannel) {
        this.clientAfkMode = clientAfkMode;
        this.serverConfig = serverConfig;
        this.botname = botname;
        this.versionChecker = versionChecker;
        this.updateGameServerChannel = updateGameServerChannel;
    }

    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    private Timer[] timer = new Timer[3];
    private Timer[] afkTimer;

    public void controlClientAfkTimer(boolean stopTask){
        if(afkTimer == null) {afkTimer = new Timer[clientAfkMode.size()];}
        int counter = 0;
        for (ClientAfkMode afkMode : clientAfkMode) {
            clientAfkTimer(stopTask, afkMode.getFunctionAFK().getFunctionId(), counter);
            counter++;
        }
    }

    private void clientAfkTimer(boolean stopTasks, int timerid, int counter) {
        if (afkTimer[timerid]  == null) afkTimer[timerid] = new Timer();
        if ( ! stopTasks ) {
            afkTimer[timerid].schedule(new TimerTask() {
                public void run(){
                    clientAfkMode.get(counter).clientAfk();
                }
            }, 500, 500);
        } else {
            afkTimer[timerid].cancel();
            afkTimer[timerid].purge();
            afkTimer[timerid] = new Timer();
        }
    }

    public void updateGameChannel(boolean stopTasks){
        int delaySeconds = 15;
        int periodSeconds = 60;
        if (timer[2]  == null) timer[2] = new Timer();
        if( ! stopTasks ){
            System.out.println(botname + ": Starting UpdateGameChannel Task in " + delaySeconds + "s");
            LOG.info(botname + ": Starting UpdateGameChannel Task in " + delaySeconds + "s");
            timer[2].schedule(new TimerTask() {
                public void run() {
                    updateGameServerChannel.updateChannel();
                    LOG.info(botname + ": Game Channel aktualisiert.");
                }
            }, delaySeconds * 1000, periodSeconds * 1000);
        } else {
            timer[2].cancel();
            timer[2].purge();
            timer[2] = new Timer();
            System.out.println(botname + ": Stopping UpdateGameChannel Task");
            LOG.info(botname + ": Stopping UpdateGameChannel Task");
        }
    }

    public void checkNewVersion(boolean stopTasks){
        int delaySeconds = 5;
        int periodSeconds = (serverConfig.getVersionCheckTime() * 60 * 60);
        if (timer[1]  == null) timer[1] = new Timer();
        if( ! stopTasks ){
            timer[1].schedule(new TimerTask() {
                public void run() {
                    versionChecker.checkVersion();
                    LOG.info(botname + ": Nach neuster Version geprüft.");
                }
            }, delaySeconds * 1000, periodSeconds * 1000);
        } else{
            timer[1].cancel();
            timer[1].purge();
            timer[1] = new Timer();
        }
    }
}

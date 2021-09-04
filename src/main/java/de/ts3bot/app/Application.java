package de.ts3bot.app;

/*
 * Created by Captain on 24.10.2015.
 * Start Datei
 */

import de.ts3bot.app.manager.BotInstanceConfigManager;
import de.ts3bot.app.models.BotInstanceManager;
import de.ts3bot.app.models.data.InstanceData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.file.Paths;
import java.util.List;

public class Application {
    private static final Logger LOG  = LogManager.getLogger(Application.class);
    private static String configpath;
    private static String instancepath;

    private static final Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread t, Throwable e) {
            handleCrash(t, e);
        }
    };

    private static void handleCrash(Thread t, Throwable e){
        LOG.error(e.getMessage(), e);
        t.interrupt();
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(handler);
        Thread.currentThread().setUncaughtExceptionHandler(handler);

        if( ! checkRunArguments(args) ){
            System.exit(1);
        }
        try {
            if( ! BotInstanceConfigManager.checkConfigFiles(configpath, instancepath) ){
                LOG.info("CFGs created please configure these cfgs and restart the bot.");
                System.exit(0);
            }else {
                List<InstanceData> instanceDataList = BotInstanceConfigManager.readInstanceConfig(instancepath);
                BotInstanceManager botInstanceManager = new BotInstanceManager();

                loadInstances(instanceDataList, botInstanceManager);
            }
        } catch (RuntimeException throwable) {
            handleCrash(Thread.currentThread(), throwable);
        }
    }

    private static boolean checkRunArguments(String[] args){
        boolean runArgumentsSet = true;
        for(String arg : args){
            if(arg.startsWith("configPath=")){
                configpath = arg.replace("configPath=","");
            }
            if(arg.startsWith("instanceFile=")){
                instancepath = arg.replace("instanceFile=","");
            }
        }
        if(configpath == null){
            LOG.error("Missing configPath as run argument!");
            runArgumentsSet = false;
        }
        if(instancepath == null){
            LOG.error("Missing instanceFile as run argument!");
            runArgumentsSet = false;
        }
        return runArgumentsSet;
    }

    private static void loadInstances(List<InstanceData> instanceDataList, BotInstanceManager botInstanceManager){
        for(InstanceData instance : instanceDataList){
            final String EMPTY = "empty";

            if ( ! instance.getConfigpath().equals(EMPTY) && ! Paths.get(instance.getConfigpath()).toFile().exists() ){
                createCFGs(instance);
                continue;
            }

            if ( ( ! instance.getBotname().equals(EMPTY) || ! instance.getConfigpath().equals(EMPTY) || ! instance.getActive().equals(EMPTY) ) && instance.getActive().equals("true")) {
                botInstanceManager.setNewThread(instance.getBotname(), instance.getConfigpath());
                if (instance.getActive().equals("true")) {
                    botInstanceManager.startThread(instance.getBotname());
                }
            }else{
                botInstanceManager.setNewThread(instance.getBotname(), instance.getConfigpath());
            }
        }
    }

    private static void createCFGs(InstanceData instance){
        LOG.info("Server config not found: {}!", instance.getConfigpath());
        LOG.info("No cfgs found, a default cfg was created please configure it and restart the bot.");
        BotInstanceConfigManager.createFirstServerConfig(configpath);
    }
}
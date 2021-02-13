package de.ts3bot.app.manager;

import de.ts3bot.app.models.data.InstanceData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class BotInstanceConfigManager {
    private static final Logger log = LogManager.getLogger(BotInstanceConfigManager.class.getName());

    public static List<InstanceData> readInstanceConfig(String instancelocation) {
        List<InstanceData> instanceDataList = new ArrayList<>();

        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(instancelocation)){
            // load a properties file
            prop.load(input);
            // serverConfig.get the property value and print it out
            for (int i = 1; i < 11; i++){
                if (prop.getProperty(i + ".instance_name") != null && prop.getProperty(i + ".instance_config_pfad") != null && prop.getProperty(i + ".instance_activ") != null)
                    instanceDataList.add(
                            new InstanceData(
                                    prop.getProperty(i + ".instance_name", "empty"),
                                    prop.getProperty(i + ".instance_config_pfad", "empty"),
                                    prop.getProperty(i + ".instance_activ", "false")
                            )
                    );
            }
        } catch (IOException ex) {
            log.error("Failure in reading instanceconfig: {}", ex.getMessage());
        }

        return instanceDataList;
    }

    public static boolean checkConfigFiles(String configpath, String instancepath){
        if ( ! Paths.get(configpath).toFile().isDirectory() ){
            createInstanceConfig(configpath, instancepath);
            createFirstServerConfig(configpath);
            return false;
        }
        if ( Paths.get(configpath).toFile().isDirectory() && ! new File(instancepath).isFile()){
            createInstanceConfig(configpath, instancepath);
            createFirstServerConfig(configpath);
            return false;
        }
        return true;
    }

    private static void createInstanceConfig(String configpath, String instancelocation){
        log.info("create instance config folder + file");
        (new File(configpath)).mkdir();
        try (FileWriter fileWriter = new FileWriter(instancelocation); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("1.instance_name = server1\n");
            bufferedWriter.write("1.instance_config_pfad = " + configpath + "/server1/serverconfig.cfg\n");
            bufferedWriter.write("1.instance_activ = true");

            // Always close files.
        }
        catch(IOException ex) {
            log.error("writing to file: {}", ex.getMessage());
        }
    }

    public static void createFirstServerConfig(String configpath){
        log.info("create first server config folder + file");
        (new File(configpath + "/server1")).mkdir();
        String firstcfg = configpath + "/server1/serverconfig.cfg";
        try (FileWriter fileWriter = new FileWriter(firstcfg);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter))
        {
            bufferedWriter.write("language = \n" +
                    "ts3_server_floodrate =\n" +
                    "ts3_server_ip =\n" +
                    "ts3_server_port =\n" +
                    "ts3_server_query_port = 10011\n" +
                    "ts3_server_query_login_name =\n" +
                    "ts3_server_query_login_password =\n" +
                    "ts3_bot_nickname =\n" +
                    "ts3_bot_nickname2 =\n" +
                    "ts3_bot_channel_id = 0\n" +
                    "bot_admin = \n" +
                    "bot_full_admin =\n" +
                    "functions =\n");

        }
        catch(IOException ex) {
            log.error("writing to file: {}", ex.getMessage());
        }
    }
}

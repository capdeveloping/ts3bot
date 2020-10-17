package de.ts3bot.app.manager;

import de.ts3bot.app.models.data.InstanceData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
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
        return true;
    }

    private static void createInstanceConfig(String configpath, String instancelocation){
        log.info("create instance config folder + file");
        (new File(configpath)).mkdir();
        try (FileWriter fileWriter = new FileWriter(instancelocation); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);) {
            bufferedWriter.write("# Der Instance Name ist wichtig für den Bot damit man ihn später starten oder stoppen kann\n");
            bufferedWriter.write("# Verwende keine Leerzeichen - nur Zahlen, Minuse oder Unterstriche.\n");
            bufferedWriter.write("1.instance_name = srv1\n");
            bufferedWriter.write("\n");
            bufferedWriter.write("# Jeder Bot benötigt einen config Pfad damit die Config geladen werden kann.\n");
            bufferedWriter.write("1.instance_config_pfad = data/configs/server1/serverconfig.cfg\n");
            bufferedWriter.write("\n");
            bufferedWriter.write("# Welcher Bot ist aktiv und welcher soll nicht gestartet werden.\n");
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
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);)
        {
            final String SPACER = "-------------------------------------------------------------------------------------------------------------\n";
            final String EIDs = "# Eine mit Komma getrennte Liste (ohne Leerzeichen) mit der Einzigartigen ID\n";

            bufferedWriter.write("# Setzen der Bot Sprache\n" +
                    "language =\n" +
                    "\n" +
                    "# TS3 Query Floodrate\n" +
                    "ts3_server_floodrate =\n" +
                    "\n" +
                    "# TS3 Server IP\n" +
                    "ts3_server_ip =\n" +
                    "\n" +
                    "# TS3 Server Port\n" +
                    "ts3_server_port =\n" +
                    "\n" +
                    "# TS3 Server Query Port\n" +
                    "ts3_server_query_port = 10011\n" +
                    "\n" +
                    "# TS3 Server Query Admin Name\n" +
                    "ts3_server_query_login_name =\n" +
                    "\n" +
                    "# TS3 Server Query Admin Passwort\n" +
                    "ts3_server_query_login_password =\n" +
                    "\n" +
                    "# TS3 Bot Nickname\n" +
                    "ts3_bot_nickname =\n" +
                    "# TS3 Bot Nickname2, falls der erste schon verwendet wird\n" +
                    "ts3_bot_nickname2 =\n" +
                    "# TS3 Bot Channel in welchem er joint nach dem Connect\n" +
                    "# 0 = Standard Channel\n" +
                    "ts3_bot_channel_id = 0\n" +
                    "\n" +
                    "#######################################################################################\n" +
                    "# Es werden nur die Functions geladen. Eine Komma getrennte Liste ohne Leerzeichen.\n" +
                    "# Stehen keine Funktionen hinter functions werden sie nicht geladen.\n" +
                    "#\n" +
                    "# Können mehrmals verwendet werden:\n" +
                    "# Client Moved in einem bestimmten Channel : ClientMove:[anyname]\n" +
                    "# Clients die eine bestimmte Zeit AFK sind : ClientAFK:[anyname]\n" +
                    "# Clients die eine Willkommensnachricht erhalten sollen : WelcomeMessage:[anyname]\n" +
                    "#\n" +
                    "# Können nur einmal verwendet werden: \n" +
                    "# Clients koennen friendlists erstellen : Friendlist:[anyname]\n" +
                    "# Bestimmte Clients koennen broadcast Messages schreiben und verschicken : Broadcast:[anyname]\n" +
                    "# Ts3Viewer mit laufen lassen : Viewer:[anyname]\n" +
                    "# Automatische Channel erstellen : ChannelAutoCreate:[anyname]\n" +
                    "# Gruppe die automatisch geloescht werden soll : AutoRemove:[anyname]\n" +
                    "# TS3 Regeln die erst akzeptiert werden muessen : AcceptRules:[anyname]\n" +
                    "# User die automatisch die Live Gruppe erhalten sollen : Twitch:[anyname]\n" +
                    "#\n" +
                    "functions =\n" +
                    "\n" +
                    "########################################################################################\n" +
                    "\n" +
                    SPACER +
                    "\n" +
                    "# Liste der Clients die Bot Admins sind.\n" +
                    EIDs +
                    "# Admins haben automatisch Rechte zu broadcast Nachrichten\n" +
                    "bot_admin = \n" +
                    "\n" +
                    "# Liste der Clients die Bot Full Admins sind.\n" +
                    EIDs +
                    "# Full Admins haben automatisch Rechte zu broadcast Nachrichten\n" +
                    "bot_full_admin =\n");

        }
        catch(IOException ex) {
            log.error("writing to file: {}", ex.getMessage());
        }
    }
}

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
                    "ts3_bot_channel_id =\n" +
                    "\n" +
                    "#######################################################################################\n" +
                    "# Es werden nur die Functions geladen. Eine Komma getrennte Liste ohne Leerzeichen.\n" +
                    "# Stehen keine Funktionen hinter functions werden sie nicht geladen.\n" +
                    "#\n" +
                    "# Können mehrmals verwendet werden:\n" +
                    "# Client Moved in einem bestimmten Channel : ClientMove:[anyname]\n" +
                    "# Clients die eine bestimmte Zeit AFK sind : ClientAFK:[anyname]\n" +
                    "#\n" +
                    "# Können nur einmal verwendet werden: \n" +
                    "# Clients kÃ¶nnen friendlists erstellen : Friendlist:[anyname]\n" +
                    "# Bestimmte Clients kÃ¶nnen broadcast Messages schreiben und verschicken : Broadcast:[anyname]\n" +
                    "# Jeder Monat soll von vorne getracked werden : Monthly:[anyname]\n" +
                    "# Die ersten drei Plätze als Gruppen im TS anzeigen lassen : TopThree:[anyname]\n" +
                    "# Ts3Viewer mit laufen lassen : Viewer:[anyname]\n" +
                    "# Ts3Ranklist mit laufen lassen : Ts3Ranklist:[anyname]\n" +
                    "# Two way Authentification um seine Statistiken ab zusichern : TwoWayAuth:[anyname]\n" +
                    "# Automatisches Channel erstellen : ChannelAutoCreate:[anyname]\n" +
                    "#\n" +
                    "functions =\n" +
                    "\n" +
                    "########################################################################################\n" +
                    "\n" +
                    SPACER +
                    "\n" +
                    "\tTs3Viewer\n" +
                    "\n" +
                    "# Es muss ein Ort festgelegt werden wo die html Datei abgelegt werden soll\n" +
                    "#FUNCTIONNAME_ts3_viewer_file_location = ts3viewer.html\n" +
                    "# Textfarbe von den Channeln sowie deren Clients\n" +
                    "# Entweder die HTML Farbcodes(#3829FF) oder der Farbenname(black) auf Englisch\n" +
                    "#FUNCTIONNAME_ts3_viewer_text_color = white\n" +
                    "# Hintergrundfarbe vom Ts3Viewer\n" +
                    "# Entweder die HTML Farbcodes(#3829FF) oder der Farbenname(black) auf Englisch\n" +
                    "#FUNCTIONNAME_ts3_viewer_background_color = black\n" +
                    "# only important if ts3_server_ip is localhost\n" +
                    "#FUNCTIONNAME_ts3_viewer_server_ip =\n" +
                    "\n" +
                    SPACER +
                    "\n" +
                    "\tBroadcast\n" +
                    "\n" +
                    "# Clients die eine Broadcast Nachricht schicken dÃ¼rfen\n" +
                    EIDs +
                    "# Beispiel +z7a/exrm6PqPWXmh+47eJxaCcA=,hXuT3tgCmIF+oeq3RELL9xZaYK8=\n" +
                    "#FUNCTIONNAME_broadcast_clients =\n" +
                    "\n" +
                    SPACER +
                    "\n" +
                    "\tFriendlist\n" +
                    "\t\n" +
                    "# Join Power die der Channel hÃ¶chstens haben darf um Client zu moven\n" +
                    "#FUNCTIONNAME_move_to_friend_needed_join_power =\n" +
                    "\n" +
                    SPACER +
                    "\tClientAFK\n" +
                    "\n" +
                    "# Zeit bis der Client engültig gemoved wird\n" +
                    "# Angaben in Sekunden\n" +
                    "#FUNCTIONNAME_client_afk_time =\n" +
                    "# Channel wo die AFK Clients hin gemoved werden\n" +
                    "#FUNCTIONNAME_client_afk_channel =\n" +
                    "# Channel die ignoriert werden sollen vom Bot oder auf denen nur geachtet werden sollen\n" +
                    "# Eine mit Komma getrennte Liste (ohne Leerzeichen) mit Channel IDs.\n" +
                    "#FUNCTIONNAME_client_afk_channel_io = \n" +
                    "# Channel sollen ignoriert oder nur diese sollen gesehen werden. -> ignore/only\n" +
                    "#FUNCTIONNAME_client_afk_channel_watch = ignore\n" +
                    "# Gruppen die ignoriert werden sollen vom Bot oder auf denen nur geachtet werden sollen\n" +
                    "# Eine mit Komma getrennte Liste (ohne Leerzeichen) mit Gruppen IDs.\n" +
                    "#FUNCTIONNAME_client_afk_group_ids =\n" +
                    "# Gruppen sollen ignoriert oder nur diese sollen gesehen werden. -> ignore/only\n" +
                    "#FUNCTIONNAME_client_afk_group_watch = ignore\n" +
                    "\n" +
                    "#FUNCTIONNAME_channel_check_subchannel =\n" +
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

package de.ts3bot.app.features;

import de.ts3bot.app.manager.VersionNumberComparator;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.ts3bot.app.BotInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Captain on 02.10.2016.
 *
 */
public class NewVersionChecker extends TS3EventAdapter {
    private final Logger log = LogManager.getLogger(NewVersionChecker.class);
    private TS3Api api;
    private List<String> botFullAdminList;
    private String onlineVersion;
    private boolean checkForBotAdminsJoined = false;

    @Override
    public void onClientJoin(ClientJoinEvent e) {
        if( ! checkForBotAdminsJoined ){
            return;
        }

        if(contactUser()){
            checkForBotAdminsJoined = false;
        }
    }

    public void setApi(TS3Api api) {
        this.api = api;
    }

    public void setBotFullAdminList(List<String> botFullAdminList) {
        this.botFullAdminList = botFullAdminList;
    }

    public void checkVersion(){
        boolean oldVersion = false;
        try {
            oldVersion = check();
        }catch (Exception ex) {
            log.error(ex.getMessage());
        }
        if( ! oldVersion) {
            return;
        }
        if( ! contactUser() ){
            checkForBotAdminsJoined = true;
        }
    }

    private boolean contactUser(){
        List<Client> clients = api.getClients();
        for(String uid : botFullAdminList){
            if( ! clients.toString().contains(uid)) {
                continue;
            }
            for(Client client : clients){
                if( ! client.getUniqueIdentifier().equals(uid) ){
                    continue;
                }
                try {
                    api.sendPrivateMessage(client.getId(), "Eine neue Version vom TeamSpeak 3 Bot ist verfuegbar.\n\n" +
                            "Momentane Version: " + BotInfo.getVersion() + "\n" +
                            "Neueste Version: [url=https://hub.docker.com/repository/docker/capdeveloping/ts3bot]" + onlineVersion + "[/url]");
                }catch (Exception ex){
                    log.error(ex.getMessage());
                }
                return true;
            }
        }
        return false;
    }

    private boolean check() {
        final List<String> versions = new ArrayList<>();
        try {
            String url2 = "https://registry.hub.docker.com/v1/repositories/capdeveloping/ts3bot/tags";
            URL url = new URL(url2);
            InputStream is = url.openConnection().getInputStream();

            BufferedReader reader = new BufferedReader( new InputStreamReader( is )  );
            Pattern p = Pattern.compile("([0-9]+)\\.([0-9]+)\\.([0-9]+)");
            String line = reader.readLine();
            Matcher m = p.matcher(line);
            while(m.find()) {
                versions.add(m.group());
            }
            reader.close();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        versions.sort(VersionNumberComparator.getInstance());

        if( ! versions.get(versions.size()-1).equals( BotInfo.getVersion() )){
            onlineVersion = versions.get(versions.size()-1);
            return true;
        }
        return false;
    }
}

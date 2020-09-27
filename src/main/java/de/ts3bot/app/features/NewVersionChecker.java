package de.ts3bot.app.features;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.ts3bot.app.BotInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by Captain on 02.10.2016.
 *
 */
public class NewVersionChecker {
    private final Logger log = LogManager.getLogger(NewVersionChecker.class);
    private TS3Api api;
    private List<String> botFullAdminList;
    private String onlineVersion;
    private static final int TIMEOUT_IN_MS = 5000;

    public NewVersionChecker() {
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
        }catch (IOException ex) {
            log.error(ex.getMessage());
        }
        if(oldVersion){
            boolean send;
            do {
                send = contactUser();
            }while(!send);
        }
    }

    private boolean contactUser(){
        List<Client> client = api.getClients();
        boolean send = false;
        for(String uid : botFullAdminList){
            if(client.toString().contains(uid)){
                for(Client client1 : client){
                    if(client1.getUniqueIdentifier().equals(uid)){
                        api.sendPrivateMessage(client1.getId(),"Eine neue Version vom TeamSpeak 3 Ranking Bot ist verfügbar\n\n" +
                                "Momentane Version: " + BotInfo.getVersion() + "\n" +
                                "Neueste Version: [url=https://github.com/Captaln/TeamSpeak3RankingBot]" + onlineVersion + "[/url]");
                        send = true;
                    }
                }
                break;
            }
        }
        if(!send) {
            try {
                // thread to sleep for 60 seconds
                Thread.sleep(60000);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return send;
    }

    private boolean check() throws IOException {
        Document doc = Jsoup.parse(new URL("https://github.com/Captaln/TeamSpeak3RankingBot/blob/master/README.md"), TIMEOUT_IN_MS);
        Elements h2 = doc.select("h2");
        boolean oldVersion = false;
        for(Element element : h2) {
            if(element.text().startsWith("TeamSpeak 3 Ranking Bot")){
                String version = element.text().replace("TeamSpeak 3 Ranking Bot ","");
                if(!version.equals(BotInfo.getVersion())){
                    oldVersion = true;
                    onlineVersion = version;
                    break;
                }
            }
        }
        return oldVersion;
    }
}

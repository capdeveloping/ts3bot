package de.ts3bot.app.models;

/**
 * Created by Captain on 09.01.2016.
 * Client Daten
 */
public class Twitchuser extends User {
    private boolean serverpost;

    public Twitchuser(String name, String uid, boolean serverpost) {
        super(name, uid);
        this.serverpost = serverpost;
    }

    public boolean isServerpost() {
        return serverpost;
    }

    public void setServerpost(boolean serverpost) {
        this.serverpost = serverpost;
    }
}
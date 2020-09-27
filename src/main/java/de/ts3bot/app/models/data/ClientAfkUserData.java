package de.ts3bot.app.models.data;

/**
 * Created by Captain on 05.01.2016.
 * Client Afk User Data Objekt
 */
public class ClientAfkUserData {
    private String uid;
    private int lastChannelId;
    private int afkChannelId;

    public ClientAfkUserData(String clientUID, int lastChannelId, int afkChannelId) {
        this.uid = clientUID;
        this.afkChannelId = afkChannelId;
        this.lastChannelId = lastChannelId;
    }

    public int getLastChannelId() {
        return lastChannelId;
    }

    public String getUid() {
        return uid;
    }

    public int getAfkChannelId() {
        return afkChannelId;
    }

    public void setLastChannelId(int lastChannelId) {
        this.lastChannelId = lastChannelId;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAfkChannelId(int afkChannelId) {
        this.afkChannelId = afkChannelId;
    }
}

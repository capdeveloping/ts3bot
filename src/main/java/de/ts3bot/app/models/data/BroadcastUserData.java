package de.ts3bot.app.models.data;

/**
 * Created by Captain on 08.12.2015.
 * Broadcast User Data Objekt
 */
public class BroadcastUserData {
    private String uid;
    private String nachricht;
    private boolean start;
    private boolean nachrichtExist;

    public BroadcastUserData(String uid, String text, boolean start, boolean nachrichtExist) {
        this.uid = uid;
        this.nachricht = text;
        this.start = start;
        this.nachrichtExist = nachrichtExist;
    }

    public String getNachricht() {
        return nachricht;
    }

    public String getUID() {
        return uid;
    }

    public boolean isStart() {
        return start;
    }

    public boolean isNachrichtExist() {
        return nachrichtExist;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void setNachrichtExist(boolean nachrichtExist) {
        this.nachrichtExist = nachrichtExist;
    }
}

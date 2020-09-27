package de.ts3bot.app.models;

/**
 * Created by Captain on 09.01.2016.
 * Client Daten
 */
public class User {
    private String name;
    private String uid;
    private int talkpower;

    public User(){
        this.name = "";
        this.uid = "";
    }

    public User(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    public User(String name, int talkpower) {
        this.name = name;
        this.talkpower = talkpower;
    }

    public int getTalkpower() {
        return talkpower;
    }

    public void setTalkpower(int talkpower) {
        this.talkpower = talkpower;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
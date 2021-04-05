package de.ts3bot.app.models.data;

public class CollectedData {
    private int id;
    private String name;
    private String uid;

    public CollectedData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CollectedData(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package de.ts3bot.app.models.data;

public class InstanceData {
    private String botname;
    private String configpath;
    private String active;

    public InstanceData(String botname, String configpath, String active){
        this.botname = botname;
        this.configpath = configpath;
        this.active = active;
    }

    public String getBotname() {
        return botname;
    }

    public String getActive() {
        return active;
    }

    public String getConfigpath() {
        return configpath;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public void setBotname(String botname) {
        this.botname = botname;
    }

    public void setConfigpath(String configpath) {
        this.configpath = configpath;
    }
}

package de.ts3bot.app.models.data;

public class GameServerData {
    private String hostname;
    private String humanClients;
    private String maxCliente;
    private String serverIp;
    private String serverOs;
    private String version;
    private String serverMap;
    private String type;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServerMap() {
        return serverMap;
    }

    public void setServerMap(String serverMap) {
        this.serverMap = serverMap;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getHumanClients() {
        return humanClients;
    }

    public void setHumanClients(String humanClients) {
        this.humanClients = humanClients;
    }

    public String getMaxCliente() {
        return maxCliente;
    }

    public void setMaxCliente(String maxCliente) {
        this.maxCliente = maxCliente;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerOs() {
        return serverOs;
    }

    public void setServerOs(String serverOs) {
        this.serverOs = serverOs;
    }
}

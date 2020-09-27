package de.ts3bot.app.models.functions;

public class FunctionGameServer extends FunctionModel {
    private String game_type;
    private String game_server_ip;
    private int game_server_port;
    private String game_server_rcon_password;
    private String game_server_password;
    private String game_server_channel_name;
    private int game_server_channel_id;

    public String getGame_server_password() {
        return game_server_password;
    }

    public void setGame_server_password(String game_server_password) {
        this.game_server_password = game_server_password;
    }

    public String getGame_server_channel_name() {
        return game_server_channel_name;
    }

    public void setGame_server_channel_name(String game_server_channel_name) {
        this.game_server_channel_name = game_server_channel_name;
    }

    public int getGame_server_channel_id() {
        return game_server_channel_id;
    }

    public void setGame_server_channel_id(int game_server_channel_id) {
        this.game_server_channel_id = game_server_channel_id;
    }

    public String getGame_type() {
        return game_type;
    }

    public void setGame_type(String game_type) {
        this.game_type = game_type;
    }

    public String getGame_server_ip() {
        return game_server_ip;
    }

    public void setGame_server_ip(String game_server_ip) {
        this.game_server_ip = game_server_ip;
    }

    public int getGame_server_port() {
        return game_server_port;
    }

    public void setGame_server_port(int game_server_port) {
        this.game_server_port = game_server_port;
    }

    public String getGame_server_rcon_password() {
        return game_server_rcon_password;
    }

    public void setGame_server_rcon_password(String game_server_rcon_password) {
        this.game_server_rcon_password = game_server_rcon_password;
    }
}

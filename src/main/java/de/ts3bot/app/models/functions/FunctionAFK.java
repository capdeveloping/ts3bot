package de.ts3bot.app.models.functions;

import java.util.List;

/**
 * Created by Captain on 19.07.2016.
 *
 */
public class FunctionAFK extends FunctionModel{
    private int client_afk_time;
    private int client_afk_channel;
    private List<Integer> client_afk_channel_io;
    private String client_afk_channel_watch;
    private List<Integer> afk_client_afk_group_ids;
    private String afk_client_afk_group_watch;

    public List<Integer> getClient_afk_channel_io() {
        return client_afk_channel_io;
    }

    public List<Integer> getAfk_client_afk_group_ids() {
        return afk_client_afk_group_ids;
    }

    public String getAfk_client_afk_group_watch() {
        return afk_client_afk_group_watch;
    }

    public void setAfk_client_afk_group_ids(List<Integer> afk_client_afk_group_ids) {
        this.afk_client_afk_group_ids = afk_client_afk_group_ids;
    }

    public void setAfk_client_afk_group_watch(String afk_client_afk_group_watch) {
        this.afk_client_afk_group_watch = afk_client_afk_group_watch;
    }

    public void setClient_afk_channel_io(List<Integer> client_afk_channel_io) {
        this.client_afk_channel_io = client_afk_channel_io;
    }

    public String getClient_afk_channel_watch() {
        return client_afk_channel_watch;
    }

    public void setClient_afk_channel_watch(String client_afk_channel_watch) {
        this.client_afk_channel_watch = client_afk_channel_watch;
    }

    public int getClient_afk_time() {
        return client_afk_time;
    }

    public void setClient_afk_time(int client_afk_time) {
        this.client_afk_time = client_afk_time;
    }

    public int getClient_afk_channel() {
        return client_afk_channel;
    }

    public void setClient_afk_channel(int client_afk_channel) {
        this.client_afk_channel = client_afk_channel;
    }
}

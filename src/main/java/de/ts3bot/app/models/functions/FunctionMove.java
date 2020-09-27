package de.ts3bot.app.models.functions;

import java.util.List;

/**
 * Created by Captain on 05.05.2019.
 *
 */
public class FunctionMove extends FunctionModel{
    private int client_moved_channel;
    private List<Integer>  client_moved_group_notify;
    private List<Integer> client_moved_group_ids;
    private String client_moved_group_action;

    public int getClient_moved_channel() {
        return client_moved_channel;
    }

    public void setClient_moved_channel(int client_moved_channel) {
        this.client_moved_channel = client_moved_channel;
    }

    public List<Integer> getClient_moved_group_notify() {
        return client_moved_group_notify;
    }

    public void setClient_moved_group_notify(List<Integer> client_moved_group_notify) {
        this.client_moved_group_notify = client_moved_group_notify;
    }

    public List<Integer> getClient_moved_group_ids() {
        return client_moved_group_ids;
    }

    public void setClient_moved_group_ids(List<Integer> client_moved_group_ids) {
        this.client_moved_group_ids = client_moved_group_ids;
    }

    public String getClient_moved_group_action() {
        return client_moved_group_action;
    }

    public void setClient_moved_group_action(String client_moved_group_action) {
        this.client_moved_group_action = client_moved_group_action;
    }
}
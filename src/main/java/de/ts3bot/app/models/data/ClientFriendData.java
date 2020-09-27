package de.ts3bot.app.models.data;

import de.ts3bot.app.models.User;

/**
 * Created by Captain on 09.01.2016.
 * asd
 */
public class ClientFriendData extends User{
    private int clientID;

    public ClientFriendData(String clientName, String clientUid) {
        super(clientName, clientUid);
    }

    public ClientFriendData(int clientID, String clientName, String clientUid) {
        super(clientName, clientUid);
        this.clientID = clientID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }
}

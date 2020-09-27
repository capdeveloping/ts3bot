package de.ts3bot.app.models.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Captain on 06.01.2016.
 * Move to User Data
 */
public class MoveToUserData {
    private String clientUID;
    private List<ClientFriendData> clientFriends;

    public MoveToUserData(String clientUID){
        setClientUID(clientUID);
        if(clientFriends == null){
            clientFriends = new ArrayList<>();
        }
    }

    public MoveToUserData(String clientUID, String friendname, String frienduid){
        setClientUID(clientUID);
        if(clientFriends == null){
            clientFriends = new ArrayList<>();
        }
        this.clientFriends.add(new ClientFriendData(friendname,frienduid));
    }

    public String getClientUID() {
        return clientUID;
    }

    public List<ClientFriendData> getClientFriends() {
        return clientFriends;
    }

    public ClientFriendData getFriendAtIndex(int index) {
        return clientFriends.get(index);
    }

    public void addClientFriends(String friendname, String frienduid) {
        this.clientFriends.add(new ClientFriendData(friendname,frienduid));
    }

    public void removeClientFriend(int index){
        this.clientFriends.remove(index);
    }

    private void setClientUID(String clientUID) {
        this.clientUID = clientUID;
    }
}

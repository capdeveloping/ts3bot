package de.ts3bot.app.models;

import de.ts3bot.app.models.data.ClientFriendData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Captain on 09.01.2016.
 * Anfrage an einem User
 */
public class MoveToRequest extends User{
    private int clientChannelId;
    private List<ClientFriendData> friendWantMove;

    public MoveToRequest(String clientUid, String clientName , int clientChannelId, String invokerUid, int invokerID, String invokerName) {
        super(clientName, clientUid);
        this.clientChannelId = clientChannelId;
        if (friendWantMove == null) {
            friendWantMove = new ArrayList<>();
        }
        addFriendWantMove(invokerUid, invokerID, invokerName);
    }

    public ClientFriendData getFriendAtIndex(int index) {
        return friendWantMove.get(index);
    }

    public void addFriendWantMove(String friendUid, int friendId , String friendName) {
        this.friendWantMove.add(new ClientFriendData(friendId,friendName,friendUid));
    }

    public void removeFriendWantMove(int index){
        this.friendWantMove.remove(index);
    }

    public int getClientChannelId() {
        return clientChannelId;
    }

    public List<ClientFriendData> getFriendWantMove() {
        return friendWantMove;
    }
}

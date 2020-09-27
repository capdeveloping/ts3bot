package de.ts3bot.app.models.data;

/**
 * Created by Captain on 12/08/2016.
 *
 */
public class ServerGroupData {
    private int groupId;
    private int groupJoinPower;
    private boolean groupSkip;

    public ServerGroupData(int groupId, int groupJoinPower, boolean groupSkip) {
        this.groupId = groupId;
        this.groupJoinPower = groupJoinPower;
        this.groupSkip = groupSkip;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getGroupJoinPower() {
        return groupJoinPower;
    }

    public void setGroupJoinPower(int groupJoinPower) {
        this.groupJoinPower = groupJoinPower;
    }

    public boolean isGroupSkip() {
        return groupSkip;
    }

    public void setGroupSkip(boolean groupSkip) {
        this.groupSkip = groupSkip;
    }
}

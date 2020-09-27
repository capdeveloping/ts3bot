package de.ts3bot.app.models.functions;

import java.util.List;

public class FunctionTs3Viewer {
    private int firstConnectionGroup;
    private int acceptedGuest;
    private List<String> nameSeperators;
    private String forbiddenNamesFilePath;

    public String getForbiddenNamesFilePath() {
        return forbiddenNamesFilePath;
    }

    public void setForbiddenNamesFilePath(String forbiddenNamesFilePath) {
        this.forbiddenNamesFilePath = forbiddenNamesFilePath;
    }

    public int getFirstConnectionGroup() {
        return firstConnectionGroup;
    }

    public void setFirstConnectionGroup(int firstConnectionGroup) {
        this.firstConnectionGroup = firstConnectionGroup;
    }

    public int getAcceptedGuest() {
        return acceptedGuest;
    }

    public void setAcceptedGuest(int acceptedGuest) {
        this.acceptedGuest = acceptedGuest;
    }

    public List<String> getNameSeperators() {
        return nameSeperators;
    }

    public void setNameSeperators(List<String> nameSeperators) {
        this.nameSeperators = nameSeperators;
    }

}

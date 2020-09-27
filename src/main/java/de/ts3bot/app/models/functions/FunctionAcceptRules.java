package de.ts3bot.app.models.functions;

import java.util.List;

public class FunctionAcceptRules {
    private int firstConnectionGroup;
    private int acceptedGuest;
    private String pokeMessage;
    private String privateMessageFilePath;
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

    public String getPokeMessage() {
        return pokeMessage;
    }

    public String setPokeMessage(String pokeMessage) {
        if(pokeMessage.length() > 100){
            this.pokeMessage = pokeMessage.substring(0,99);
            return "Poke Nachricht musste verkürzt werden da sie länger als 100 Zeichen war. -> '" + this.pokeMessage + "'";
        }else{
            this.pokeMessage = pokeMessage;
        }
        return "";
    }

    public String getPrivateMessageFilePath() {
        return privateMessageFilePath;
    }

    public void setPrivateMessageFilePath(String privateMessageFilePath) {
        this.privateMessageFilePath = privateMessageFilePath;
    }
}

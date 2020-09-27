package de.ts3bot.app.models.functions;

import java.util.Date;
import java.util.List;

public class FunctionWelcomeMessage extends FunctionModel{
    private String welcome_message;
    private List<Integer> welcome_group_ids;
    private String welcome_repeat;
    private Date dateUntil;
    private String pokeMessage;
    private boolean pokeClient;
    private boolean endDate;

    public String getPokeMessage() {
        return pokeMessage;
    }

    public void setPokeClient(boolean pokeClient) {
        this.pokeClient = pokeClient;
    }

    public boolean isPokeClient() {
        return pokeClient;
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

    public boolean isEndDate() {
        return endDate;
    }

    public void setEndDate(boolean endDate) {
        this.endDate = endDate;
    }

    public Date getDateUntil() {
        return dateUntil;
    }

    public void setDateUntil(Date dateUntil) {
        this.dateUntil = dateUntil;
    }

    public String getWelcome_message() {
        return welcome_message;
    }

    public void setWelcome_message(String welcome_message) {
        this.welcome_message = welcome_message;
    }

    public List<Integer> getWelcome_group_ids() {
        return welcome_group_ids;
    }

    public void setWelcome_group_ids(List<Integer> welcome_group_ids) {
        this.welcome_group_ids = welcome_group_ids;
    }

    public String getWelcome_repeat() {
        return welcome_repeat;
    }

    public void setWelcome_repeat(String welcome_repeat) {
        this.welcome_repeat = welcome_repeat;
    }
}

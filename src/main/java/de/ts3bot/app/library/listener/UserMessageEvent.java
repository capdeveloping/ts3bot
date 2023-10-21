package de.ts3bot.app.library.listener;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import de.ts3bot.app.BotInfo;
import de.ts3bot.app.features.MoveToClient;
import de.ts3bot.app.library.configload.TS3TextLoad;

import java.util.*;

/**
 * Created by Captain on 25.10.2015.
 * Schaut ob jemand etwas im Server/Channel/Privaten Chat an ihn schriebt und schreibt dementsprechend zur�ck.
 */
public class UserMessageEvent extends TS3EventAdapter {
    private TS3Api api;
    private MoveToClient moveToClient;
    private TS3TextLoad textLoad;
    private boolean stopMoveFunc;

    public UserMessageEvent(MoveToClient moveToClient) {
        this.moveToClient = moveToClient;
        stopMoveFunc = this.moveToClient == null;
    }

//region setter
    public void setApi(TS3Api api) {
        this.api = api;
    }

    public void setTextLoad(TS3TextLoad textLoad) {
        this.textLoad = textLoad;
    }

    public void setStopMoveFunc(boolean stopMoveFunc) {
        this.stopMoveFunc = stopMoveFunc;
    }

    //endregion
    @Override
    public void onTextMessage(TextMessageEvent e) {
        if ( (e.getTargetMode() == TextMessageTargetMode.CLIENT
                || e.getTargetMode() == TextMessageTargetMode.CHANNEL
                || e.getTargetMode() == TextMessageTargetMode.SERVER )
                && e.getInvokerId() != api.whoAmI().getId()
                && !e.getMessage().startsWith("!2wayauth")
        ) {
            String message;
            if (e.getMessage().startsWith("!addfriend") || e.getMessage().startsWith("!removefriend")
                    || e.getMessage().startsWith("!moveto") || e.getMessage().startsWith("!accept")
                    || e.getMessage().startsWith("!decline")) {
                message = e.getMessage();
            } else {
                message = e.getMessage().toLowerCase();
            }

//region Gäste und Member

            switch (message){
                case "!hilfe":
                case "!help":
                    help(true,e);
                    break;
                case "danke":
                    api.sendPrivateMessage(e.getInvokerId(), "Your welcome.");
                    break;
                case "!ichbinneu":
                    api.sendPrivateMessage(e.getInvokerId(), textLoad.getIchBinNeu());
                    break;
                case "!botinfo":
                    api.sendPrivateMessage(e.getInvokerId(), BotInfo.getInfo());
                    break;
                case "!kontakt":
                    api.sendPrivateMessage(e.getInvokerId(), textLoad.getKontakt());
                    break;
                case "!gameserver":
                    api.sendPrivateMessage(e.getInvokerId(), textLoad.getGameserver());
                    break;
                default:
                    break;
            }

            if (message.startsWith("hallo")) {
                api.sendPrivateMessage(e.getInvokerId(), "Hallo " + e.getInvokerName() + "! \n Wie kann ich dir helfen?");
            }
//endregion
//region nur Member
            if(!stopMoveFunc) {
                manageFunctionMove(e, message);
            }
//endregion
        }
    }

    private void manageFunctionMove(TextMessageEvent e, String message) {
        if (message.equals("!friendlist")) {
            moveToClient.showFriendlist(e.getInvokerUniqueId(), e.getInvokerId());
        }
        if (message.startsWith("!addfriend")) {
            moveToClient.addFriend(e.getInvokerUniqueId(), e.getInvokerId(), message);
        }
        if (message.startsWith("!removefriend")) {
            moveToClient.removeFriend(e.getInvokerUniqueId(), e.getInvokerId(), message);
        }
        if (message.startsWith("!moveto")) {
            moveToClient.moveTo(e.getInvokerUniqueId(), e.getInvokerId(), message.replace("!moveto ", ""), e.getInvokerName());
        }
        if (message.startsWith("!accept")) {
            moveToClient.acceptMove(message.replace("!accept ", ""), e.getInvokerUniqueId());
        }
        if (message.startsWith("!decline")) {
            moveToClient.declineMove(message.replace("!decline ", ""), e.getInvokerUniqueId());
        }
    }

    private void help(boolean gast, TextMessageEvent e){
        // Ein String maximal 876 Zeichen dann trennen.
        List<String> messageList = new ArrayList<>();
        List<String> messageBefehleListe = new ArrayList<>();
        if(gast){
            messageList.addAll(calculateMessage(textLoad.getGastHilfe()));
            messageBefehleListe.addAll(calculateMessage(textLoad.getGastBefehle()));
        }else{
            messageList.addAll(calculateMessage(textLoad.getMemberHilfe()));
            messageBefehleListe.addAll(calculateMessage(textLoad.getMemberBefehle()));
        }
        for (String chatText : messageBefehleListe) {
            if (chatText != null)
                api.sendPrivateMessage(e.getInvokerId(), chatText);
        }
        for (String chatText : messageList) {
            if (chatText != null)
                api.sendPrivateMessage(e.getInvokerId(), chatText);
        }
    }

    private List calculateMessage (String message){
        double laenge = message.length() / 876d;
        int teilenMessage = ((int) laenge + 1);
        List<String> messageList = new ArrayList<>();
        for(int wdh = 1; wdh <= teilenMessage; wdh++){
            String help = "";
            int ende = 0;
            int anfang = 876 * (wdh - 1);
            if(wdh == teilenMessage){
                ende = message.length();
            }
            if(wdh < teilenMessage){
                ende = 876 * wdh;
            }
            help = message.substring(anfang, ende);
            messageList.add(help);
        }
        return messageList;
    }
}
package de.ts3bot.app.features;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.*;
import de.ts3bot.app.manager.FormatManager;
import de.ts3bot.app.manager.ListManager;
import de.ts3bot.app.models.TS3ServerConfig;
import de.ts3bot.app.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static de.ts3bot.app.manager.FormatManager.getDateFormatted;

public class TS3Viewer {
    private final Logger log = LogManager.getLogger(TS3Viewer.class.getName());
    private TS3Api api;
    private TS3ServerConfig serverConfig;
    private Stack<Integer> lastchannel = new Stack<Integer>();
    private static final String LIST_END = "</li>";
    private int timerPeriodSeconds;
    private HashMap<String, String> fehlendeGruppe;

    private Timer timer;

    public TS3Viewer(TS3ServerConfig serverconfig){
        this.serverConfig = serverconfig;
        lastchannel.push(0);
        timer  = new Timer();
        fehlendeGruppe = new HashMap<>();
        timerPeriodSeconds = serverConfig.getTs3ViewerUpdateTime() * 60;
        startTimer(false);
    }

    public void setApi(TS3Api api) {
        this.api = api;
    }

    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        if( timerPeriodSeconds != serverConfig.getTs3ViewerUpdateTime() * 60){
            timerPeriodSeconds = serverConfig.getTs3ViewerUpdateTime() * 60;
            startTimer(true);
            startTimer(false);
        }
    }

    public void startTimer(boolean stopTasks){
        int delaySeconds = 10;
        if( ! stopTasks ){
            System.out.println(serverConfig.getBotName() + ": Starting Ts3Viewer Task in " + delaySeconds + "s");
            log.info(serverConfig.getBotName() + ": Starting Ts3Viewer Task in " + delaySeconds + "s");
            timer.schedule(new TimerTask() {
                public void run() {
                    writeHTML();
                    logMissingFiles();
                    log.info(serverConfig.getBotName() + ": Ts3Viewer.html aktualisiert.");
                }
            }, delaySeconds * 1000, timerPeriodSeconds * 1000);
        } else{
            timer.cancel();
            timer.purge();
            timer = new Timer();
            System.out.println(serverConfig.getBotName() + ": Stopping Ts3Viewer Task");
            log.info(serverConfig.getBotName() + ": Stopping Ts3Viewer Task");
        }
    }

    public void writeHTML(){
        File file = new File(serverConfig.getTs3ViewerFileLocation());
        try ( FileWriter fileWriter = new FileWriter(file);
              BufferedWriter writer = new BufferedWriter(fileWriter) ){
            writer.write(htmlBegin());
            for(Channel channel : api.getChannels()){
                writer.write("\t<div class=\"tsv_content\">\n");
                if (channel.getParentChannelId() != 0) {
                    writer.write(collectData(channel));
                }else{
                    while(lastchannel.peek() != channel.getParentChannelId()){
                        lastchannel.pop();
                    }
                    writer.write("\t\t<div class=\"tsv_depth_" + (lastchannel.size() - 1) + "\">\n");
                    writer.write(getChannelStr(channel));
                }
                writer.write("\t\t</div>\n");
                writer.write(getClientStr(channel.getId()));
                writer.write("\t</div>\n");
            }

            writer.write("</body>");
        } catch (IOException ex) {
            log.error("{}: Error in write HTML file {}", serverConfig.getBotName(), ex.getStackTrace());
        }
    }

    private String htmlBegin(){
        return "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "<style type=\"text/css\">\n" +
                ".tsv_content {\n" +
                "   color: " + serverConfig.getTs3ViewerTextColor() + ";\n" +
                "   line-height: 1;\n" +
                "}\n" +
                ".tsv_suffix {\n" +
                "    float: right;\n" +
                "    position: relative;\n" +
                "    text-align: right;\n" +
                "}\n" +
                ".tsv_depth_0 {\n" +
                "   margin-left:0;\n" +
                "}\n" +
                ".tsv_depth_1 {\n" +
                "   margin-left:20;\n" +
                "}\n" +
                ".tsv_depth_2 {\n" +
                "   margin-left:40;\n" +
                "}\n" +
                ".tsv_depth_3 {\n" +
                "   margin-left:60;\n" +
                "}\n" +
                ".tsv_depth_4 {\n" +
                "   margin-left:80;\n" +
                "}\n" +
                ".channel_center {\n" +
                "\ttext-align:center;\n" +
                "}\n" +
                "channel_right {\n" +
                "\tdisplay:block;\n" +
                "\ttext-align:right;\n" +
                "}\n" +
                "/* Tooltip container */\n" +
                ".tooltip {\n" +
                "    position: relative;\n" +
                "}\n" +
                "\n" +
                "/* Tooltip text */\n" +
                ".tooltip .tooltiptext {\n" +
                "    visibility: hidden;\n" +
                "    width: 140px;\n" +
                "    background-color: white;\n" +
                "\tborder: 1px solid black;\n" +
                "    color: black;\n" +
                "    text-align: left;\n" +
                "    padding: 5px 5;\n" +
                "    top: 110%;\n" +
                "    left: 0%;\n" +
                "    border-radius: 6px;\n" +
                "    margin-top: -1px;\n" +
                "\n" +
                "    /* Position the tooltip text - see examples below! */\n" +
                "    position: absolute;\n" +
                "    z-index: 1;\n" +
                "}\n" +
                "\n" +
                ".tooltip .tooltiptext::after {\n" +
                "    content: \" \";\n" +
                "    position: absolute;\n" +
                "    bottom: 100%;  /* At the top of the tooltip */\n" +
                "    left: 10%;\n" +
                "    margin-left: -5px;\n" +
                "    border-width: 5px;\n" +
                "    border-style: solid;\n" +
                "    border-color: transparent transparent transparent;\n" +
                "}\n" +
                "\n" +
                "/* Show the tooltip text when you mouse over the tooltip container */\n" +
                ".tooltip:hover .tooltiptext {\n" +
                "    visibility: visible;\n" +
                "}\n" +
                "button {\n" +
                "\tposition: relative;\n" +
                "\tmargin: 0em;\n" +
                "\tpadding: 0em 0em;\n" +
                "\twidth: 10.5em;\n" +
                "\tbackground: #D8D8D8;\n" +
                "\tcolor: white;\n" +
                "\tborder: 1px solid grey;\n" +
                "\tborder-radius: 0px;\n" +
                "\tcursor: pointer;\n" +
                "    text-align: center;\n" +
                "}\n" +
                "button:hover, button:active {\n" +
                "\toutline: none;\n" +
                "\tbackground: #B9FF21;\n" +
                "\tcolor: darkblue;\n" +
                "}\n" +
                "a:link{\n" +
                "\ttext-decoration: none;\n" +
                "\tcolor: black;\n" +
                "}" +
                "</style>\n" +
                "<body bgcolor=\"" + serverConfig.getTs3ViewerBackgroundColor() + "\">\n" +

                "\t<div class=\"tsv_content tsv_depth_0\">\n" +
                "\t\t<div>\n" +
                "\t\t\t<p align=\"center\" >Letzte Aktualisierung " + getDateFormatted() + "</p>\n" +
                "\t\t</div>\n" +
                "\t\t<div>\n" +
                "\t\t\t<img src=\"./ts3_icons/server_open.png\">\n" +
                "\t\t\t<a class=\"link\" href=\"ts3server://" + serverConfig.getTs3ViewerServerIp() + "?port=" + serverConfig.getTs3ServerPort()+ "\" style=\"color:" + serverConfig.getTs3ViewerTextColor() + ";\">" + api.getServerInfo().getName()  + "</a>\n"+
                "\t\t</div>\n" +
                "\t\t<div>\n" +
                "\t\t\t<img src=\"./ts3_icons/ts3.png\"> User: " + api.getClients().size() + "/" + api.getServerInfo().getMaxClients() + "\n" +
                "\t\t</div>\n" +
                "\t\t<br>\n" +
                "\t</div>\n";
    }

    private String collectData(Channel channel){
        if (lastchannel.peek() == channel.getParentChannelId()) {
            String divBegin = "\t\t<div class=\"tsv_content tsv_depth_" + (lastchannel.size() - 1) + "\">\n";
            return divBegin + getChannelStr(channel) + "\n";
        }else if (lastchannel.search(channel.getParentChannelId()) == -1){
            lastchannel.push(channel.getParentChannelId());
            String divBegin = "\t\t<div class=\"tsv_content tsv_depth_" + (lastchannel.size() - 1) + "\">\n";
            return divBegin + getChannelStr(channel) + "\n";
        }else{
            while(lastchannel.peek() != channel.getParentChannelId()){
                lastchannel.pop();
            }
            String divBegin = "\t\t<div class=\"tsv_content tsv_depth_" + (lastchannel.size() - 1) + "\">\n";
            return divBegin + getChannelStr(channel) + "\n";
        }
    }

    private String getChannelStr(Channel channel){
        String channelString = checkChannelName(channel);
        channelString = checkChannelTyp(channel, channelString);
        return channelString;
    }

    private String checkChannelName(Channel channel){
        String channelIcon = "<img src=\"./ts3_icons/channel/!%CHANNELICON%!\">";
        String channelString = "!%ICON%!";
        final String SEARCH = "!%ICON%!";
        if( Pattern.matches("\\Q[\\E.*spacer.*\\Q]\\E.*", channel.getName()) ){
            String channelName = channel.getName().substring(channel.getName().indexOf(']') + 1 );
            if(channelName.equals("")){
                channelName = "_";
            }
            if( Pattern.matches("\\Q[\\E.*cspacer.*\\Q]\\E.*", channel.getName()) ) {
                channelString += "\t\t\t<div class=\"channel_center\">" + channelName + "</div>\n";
            }else if( Pattern.matches("\\Q[\\E.*rspacer.*\\Q]\\E.*", channel.getName()) ) {
                channelString += "\t\t\t<div class=\"channel_right\">" + channelName + "</div>\n";
            }else{
                channelString = channelString + channelName + "\n";
            }
            channelString = channelString.replace(SEARCH, "");
        }else{
            channelString = channelString.replace(SEARCH,"\t\t\t" + channelIcon);
            channelString += " " + channel.getName();
        }
        return channelString;
    }

    private String checkChannelTyp(Channel channel, String channelString){
        if(channel.isDefault()){
            channelString += "\t\t\t<img align=\"right\" src=\"./ts3_icons/channel/default.png\"> ";
        }
        if ( ! channelString.contains("!%CHANNELICON%!") ){
            return channelString;
        }

        if (channel.getMaxClients() == channel.getTotalClients()){
            channelString = channelString.replace("!%CHANNELICON%!","channel_red_subscribed.png");
        }else if(channel.hasPassword()){
            channelString = channelString.replace("!%CHANNELICON%!","channel_yellow_subscribed.png");
            channelString += "\t\t\t<img align=\"right\" src=\"./ts3_icons/channel/register.png\"> ";
        }else{
            channelString = channelString.replace("!%CHANNELICON%!","channel_green_subscribed.png");
        }
        if(channel.getNeededTalkPower() > 0){
            channelString += "\t\t\t<img align=\"right\" src=\"./ts3_icons/channel/moderated.png\"> ";
        }
        return channelString;
    }

    private String getClientStr(int channelId){
        StringBuilder clientStrBuilder = new StringBuilder();
        HashMap<String, User> clientMap = new HashMap<>();
        Set<Integer> talkPowerList = new HashSet<>();
        for(Client client : api.getClients()) {
            if (channelId == client.getChannelId() && !client.isServerQueryClient()) {
                clientMap.put(client.getNickname(), new User(getClientAsStr(client), client.getTalkPower()));
                talkPowerList.add(client.getTalkPower());
            }
        }

        List<Integer> talkPowerOrdered = new ArrayList<>(talkPowerList);
        talkPowerOrdered.sort(Collections.reverseOrder());

        List<String> clientByKey = new ArrayList<>(clientMap.keySet());
        Collections.sort(clientByKey);

        for(int sortId : talkPowerOrdered){
            for(String clientName : clientByKey){
                if(clientMap.get(clientName).getTalkpower() == sortId){
                    clientStrBuilder.append(clientMap.get(clientName).getName());
                }
            }
        }

        return clientStrBuilder.toString();
    }

    private String getClientAsStr(Client client) {
        StringBuilder clientStrBuilder = new StringBuilder();
        clientStrBuilder.append("\t\t<div class=\"tsv_content tooltip tsv_depth_").append(lastchannel.size()).append("\">\n");
        clientStrBuilder.append("\t\t\t").append(checkClientStatus(client)).append(" ");
        clientStrBuilder.append(getClientGruppenBaumName(client, 1)); // vor den namen
        clientStrBuilder.append(" ").append(client.getNickname()).append(" ");
        clientStrBuilder.append(getClientGruppenBaumName(client, 2)); // hinter den namen
        clientStrBuilder.append("\n\t\t\t<span class=\"tooltiptext\">\n");
        clientStrBuilder.append("\t\t\t\t<table>\n");
        clientStrBuilder.append(getClientInfo(client));
        clientStrBuilder.append("\t\t\t\t</span>\n");
        if( ! client.getCountry().toLowerCase().isEmpty() ){
            clientStrBuilder.append("\t\t\t\t<img title=\"").append(client.getCountry()).append("\" align=\"right\" src=\"./ts3_icons/countries/").append(client.getCountry().toLowerCase()).append(".png\">\n");
        }
        clientStrBuilder.append(getClientGroups(client));
        clientStrBuilder.append("\t\t</div>\n");
        return clientStrBuilder.toString();
    }

    private String getClientInfo(Client client){
        String info = "";
        try{
            ClientInfo clientInfo = api.getClientInfo(client.getId());
            if(clientInfo != null) {
                long time = clientInfo.getTimeConnected() /60 / 1000;
                long stunden = time / 60;
                long minuten = time - stunden * 60;
                info = sortTime("online", stunden, minuten);

                long idleTime = client.getIdleTime() / 60 / 1000;
                long idleStunden = idleTime / 60;
                long idleMinuten= idleTime -idleStunden * 60;
                info = info + sortTime("idle", idleStunden, idleMinuten);

                info = info + "\t\t\t\t\t\t</table>\n" +
                        addConnectButton(client.getChannelId());
            }
        }catch (Exception ex){
            log.error( FormatManager.StackTraceToString(ex) );
        }
        return info;
    }

    private String addConnectButton(int channelId){
        String button = "\t\t\t\t\t\t<button type=\"button\">\n" +
                "\t\t\t\t\t\t\t<a class=\"link\" href=\"ts3server://" +
                serverConfig.getTs3ViewerServerIp() +
                "?port=" +
                serverConfig.getTs3ServerPort() +
                "&cid=" + channelId + "\">!%BUTTONNAME%!</a>\n" +
                "\t\t\t\t\t\t</button>\n";
        button = button.replace("!%BUTTONNAME%!","zum Client verbinden");
        return button;
    }

    private String sortTime(String activIdle, long stunden, long minuten){
        String info;
        final String TAB = "\t";
        final String TABBLOCK = TAB + TAB + TAB + TAB + TAB + TAB + TAB;
        final String TRSTART = TABBLOCK + "<tr>\n";
        final String TDSTART = TABBLOCK + "<td>";
        final String TDEND= "</td>\n";
        final String TREND = TABBLOCK + "</tr>\n";

        if((stunden/24) < 1){
            if(stunden >= 1) {
                info =  TRSTART +
                        TDSTART + activIdle + TDEND +
                        TDSTART + stunden + "hrs " + minuten + "min" + TDEND +
                        TREND;
            }else{
                info =  TRSTART +
                        TDSTART + activIdle + TDEND +
                        TDSTART + minuten + "min" + TDEND +
                        TREND;
            }
        }else{
            long tage = stunden / 24;
            stunden = stunden - tage * 24;
            if(stunden >= 1) {
                if(minuten >= 1) {
                    info =  TRSTART +
                            TDSTART + activIdle + TDEND +
                            TDSTART+ tage + "dys " + stunden + "hrs " + minuten + "min" + TDEND +
                            TREND;
                }else {
                    info =  TRSTART +
                            TDSTART + activIdle + TDEND +
                            TDSTART + tage + "dys " + stunden + "hrs " + TDEND +
                            TREND;
                }
            }else if(minuten >= 1) {
                info =  TRSTART +
                        TDSTART + activIdle + TDEND +
                        TDSTART + tage + "dys " + minuten + "min" + TDEND +
                        TREND;
            }else{
                info =  TRSTART +
                        TDSTART + activIdle + TDEND +
                        TDSTART + tage + "dys" + TDEND +
                        TREND;
            }
        }
        return info;
    }

    private String checkClientStatus(Client client){
        String status = "<img src=\"./ts3_icons/client/";
        if(!client.isOutputHardware()){
            status = status + "hardware_output_muted.png\">";
        }else if(client.isOutputMuted()){
            status = status + "output_muted.png\">";
        }else if(!client.isInputHardware()) {
            status = status + "hardware_input_muted.png\">";
        }else if(client.isInputMuted()){
            status = status + "input_muted.png\">";
        }else if(client.isAway()){
            status = status + "away.png\">";
        }else if(client.isChannelCommander()){
            status = status + "player_commander_off.png\">";
        }else if (client.isTalking()) {
            status = status + "player_on.png\">";
        }else {
            status = status + "player_off.png\">";
        }
        return status;
    }

    private String getClientGruppenBaumName(Client client, int groupNameMode){
        StringBuilder gruppenBaumName = new StringBuilder("");
        HashMap<Integer, String> groupMap = new HashMap<>();
        for (int gruppe : client.getServerGroups()) {
            for (ServerGroup serverGroup : api.getServerGroups()) {
                if (serverGroup.getNameMode() == 0) {
                    continue;
                }
                if(serverGroup.getId() != gruppe){
                    continue;
                }
                if(serverGroup.getNameMode() == groupNameMode){
                    groupMap.put(serverGroup.getSortId(), serverGroup.getName());
                }
            }
        }

        if(groupMap.size() == 0){
            return "";
        }

        List<Integer> groupByKey = new ArrayList<>(groupMap.keySet());
        Collections.sort(groupByKey, Collections.reverseOrder());

        for(int sortId : groupByKey){
            gruppenBaumName.append("[").append(groupMap.get(sortId)).append("]");
        }

        return gruppenBaumName.toString();
    }

    private String getClientGroups(Client client){
        StringBuilder icon = new StringBuilder("\t\t\t\t");
        HashMap<Integer, String> groupMap = new HashMap<>();
        for (int gruppe : client.getServerGroups()) {
            for (ServerGroup serverGroup : api.getServerGroups()) {
                if (serverGroup.getIconId() == 0) {
                    continue;
                }
                if(serverGroup.getId() != gruppe){
                    continue;
                }

                serverGroup.getMap();

                String iconPath = "./ts3_icons/gruppen/" + serverGroup.getIconId() + ".png";
                groupMap.put(serverGroup.getSortId(), iconPath + "\" title=\"" + serverGroup.getName() + "\"");
                testIconPath(iconPath, serverGroup);
            }
        }

        List<Integer> groupByKey = new ArrayList<>(groupMap.keySet());
        Collections.sort(groupByKey, Collections.reverseOrder());

        for(int sortId : groupByKey){
            icon.append("<img align=\"right\" src=\"").append(groupMap.get(sortId)).append(">");
        }

        return icon.toString() + "\n";
    }

    private void testIconPath(String iconPath, ServerGroup serverGroup){
        if( ListManager.fileExist(iconPath)){
            return;
        }
        fehlendeGruppe.put(serverGroup.getName(), iconPath);
    }

    private void logMissingFiles(){
        for (String groupName : fehlendeGruppe.keySet()){
            log.warn("{}: folgendes Icon konnte nicht gefunden werden Gruppenname: '{}' Filename: '{}' ", serverConfig.getBotName(), groupName, fehlendeGruppe.get(groupName));
        }
        fehlendeGruppe.clear();
    }
}

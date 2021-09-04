package de.ts3bot.app.library.configload;

import de.ts3bot.app.models.TS3ServerConfig;

/**
 * Created by Captain on 12.11.2015.
 * L�dt den Inhalt der Dateien
 */
public class TS3TextLoad {
    private String noPermissions;
    private String infoAdmin;
    private String infoFullAdmin;
    private String gameserver;
    private String gastHilfe;
    private String gastBefehle;
    private String memberHilfe;
    private String memberBefehle;
    private String ichBinNeu;
    private String clientJointChannel;
    private String willkommensnachrichtNeueClients;
    private String kontakt;
    private TS3ServerConfig ts3ServerConfig;

    public TS3TextLoad(TS3ServerConfig ts3ServerConfig) {
        this.ts3ServerConfig = ts3ServerConfig;
        gastBefehle = "";
        memberBefehle = "";
        setInfoAdmin("Admin BefehlsListe :" +
                "\n!broadcast" +
                "\n!botconfigreload" +
                "\n!botadminlistreload" +
                "\n!broadcastclientsreload" +
                "\n!botuptime" +
                "\n!csgoscramble");
        setInfoFullAdmin("Volle Admin BefehlsListe :" +
                "\n!botconfigreloadall" +
                "\n!botfulladminlistreload" +
                "\n!botreloadadminlists" +
                "\n!bottextreload" +
                "\n!botnickname");
        setNoPermissions("Es tut mir leid aber du bist nicht berechtigt den Befehl mir gegenueber auszuführen!");
        setGastBefehle("Gast Befehlsliste: "+
                "\n[COLOR=red]!ichbinneu[/color] <- alle weiteren Infos" +
                "\n[COLOR=red]!kontakt[/color] <- falls du ein persönliches anliegen hast bekommst du hier weitere Infos." +
                "\n[COLOR=red]!botinfo[/color] <- alle Informationen über den Bot ");
        setMemberBefehle("Befehlsliste: "+
                "\n[COLOR=red]!kontakt[/color] <- falls du ein persönliches anliegen hast bekommst du hier weitere Infos." +
                "\n[COLOR=red]!gameserver[/color] <- um die Daten der GameServer zu bekommen " +
                "\n[COLOR=red]!botinfo[/color] <- alle Informationen über den Bot" +
                "\n[COLOR=red]!uptime[/color] <- Deine Zeit am Teamspeak und deinen Fortschritt bis zum nächsten Level" +
                "\n[COLOR=red]!topfive [COLOR=green]/[/color] !top5[/color] <- Die 5 aktivesten Teamspeak Nutzer" +
                "\n[COLOR=red]!topten [COLOR=green]/[/color] !top10[/color] <- Die 10 aktivesten Teamspeak Nutzer" +
                "\n[COLOR=red]!ranking wobinich[/color] <- Um zu sehen wo du gerade in der Top Liste stehst.");
        for (String key : ts3ServerConfig.getFunctionNames().keySet()) {
            if(ts3ServerConfig.getFunctionNames().get(key).equals("Friendlist")) {
                setMemberBefehle("\n[COLOR=red]!addfriend[/color] [COLOR=green][Freundesliste Name][/color] [COLOR=red]=[/color] [COLOR=green][TS3 Server Nickname][/color] <- Freund zur Freundesliste hinzufügen, ohne [] Klammern" +
                        "\n[COLOR=red]!removefriend[/color] [COLOR=green][Freundesliste Name][/color] <- Freund von der Freundesliste entfernen" +
                        "\n[COLOR=red]!friendlist[/color] <- anzeigen der Freundesliste" +
                        "\n[COLOR=red]!moveto[/color] [COLOR=green][Freundesliste Name][/color] <- zum Freund moven lassen");
                break;
            }
        }
    }

    public void setTs3ServerConfig(TS3ServerConfig ts3ServerConfig) {
        this.ts3ServerConfig = ts3ServerConfig;
    }

    public void setClientJointChannel(String clientJointChannel) {
        this.clientJointChannel = clientJointChannel;
    }

    public void setGameserver(String gameserver) {
        this.gameserver = gameserver;
    }

    public void setGastHilfe(String gastHilfe) {
        this.gastHilfe = gastHilfe;
    }

    public void setGastBefehle(String gastBefehle) {
        this.gastBefehle = gastBefehle;
    }

    public void setIchBinNeu(String ichBinNeu) {
        this.ichBinNeu = ichBinNeu;
    }

    public void setInfoAdmin(String infoAdmin) {
        this.infoAdmin = infoAdmin;
    }

    public void setInfoFullAdmin(String infoFullAdmin) {
        this.infoFullAdmin = infoFullAdmin;
    }

    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }

    public void setMemberHilfe(String memberHilfe) {
        this.memberHilfe = memberHilfe;
    }

    public void setMemberBefehle(String memberBefehle) {
        this.memberBefehle = this.memberBefehle + memberBefehle;
    }

    public void setNoPermissions(String noPermissions) {
        this.noPermissions = noPermissions;
    }

    public void setWillkommensnachrichtNeueClients(String willkommensnachrichtNeueClients) {
        this.willkommensnachrichtNeueClients = willkommensnachrichtNeueClients;
    }

    public String getIchBinNeu() {
        return ichBinNeu;
    }

    public String getGameserver() {
        return gameserver;
    }

    public String getGastHilfe() {
        return gastHilfe;
    }

    public String getGastBefehle() {
        return gastBefehle;
    }

    public String getMemberHilfe() {
        return memberHilfe;
    }

    public String getMemberBefehle() {
        return memberBefehle;
    }

    public String getInfoAdmin() {
        return infoAdmin;
    }

    public String getNoPermissions() {
        return noPermissions;
    }

    public String getClientJointChannel() {
        return clientJointChannel;
    }

    public String getWillkommensnachrichtNeueClients() {
        return willkommensnachrichtNeueClients;
    }

    public String getKontakt() {
        return kontakt;
    }

    public String getInfoFullAdmin() {
        return infoFullAdmin;
    }
}

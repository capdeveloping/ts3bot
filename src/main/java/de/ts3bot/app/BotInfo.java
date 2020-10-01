package de.ts3bot.app;

/**
 * Created by Captain on 21.05.2016.
 *
 */
public class BotInfo {
    private static String version = "0.9.28";
    private static String info = "Version: " + version +
            "\n\nTeamSpeak 3 Bot." +
            "Zusätzlich können weitere Funktionen angeschaltet werden.(siehe [url=https://capdeveloping.de/?page_id=177]https://capdeveloping.de/?page_id=177[/url])" +
            "\n\nHaben Sie Fehler entdeckt oder haben Sie Fragen/Anregungen bezüglich des Bots?" +
            "\nSchreibe eine E-Mail mit dem Betreff \"TeamSpeak 3 Bot\": [url=mailto:captain@evil-lions.de]Captain[/url]";

    public static String getInfo() {
        return info;
    }

    public static String getVersion() {
        return version;
    }
}
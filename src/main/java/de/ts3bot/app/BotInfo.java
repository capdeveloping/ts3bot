package de.ts3bot.app;

/**
 * Created by Captain on 21.05.2016.
 *
 */
public class BotInfo {
    private static String version = "1.1.9";
    private static String info = "Version: " + version +
            "\n\nTeamSpeak 3 Bot." +
            "Zusätzlich können weitere Funktionen angeschaltet werden.(siehe [url=https://github.com/capdeveloping/ts3bot]https://github.com/capdeveloping/ts3bot[/url])" +
            "\n\nHaben Sie Fehler entdeckt oder haben Sie Fragen/Anregungen bezüglich des Bots?" +
            "\nSchreibe eine E-Mail mit dem Betreff \"TeamSpeak 3 Bot\": [url=mailto:captain@evil-lions.de]Captain[/url]";

    public static String getInfo() {
        return info;
    }

    public static String getVersion() {
        return version;
    }
}
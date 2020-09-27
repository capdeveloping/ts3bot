package de.ts3bot.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Captain on 20.12.2015.
 * log renamen
 */
public class LogInstance {
    public static void renameLogFile(){
        //File oldBotFile = new File("logs/bot.log");
        //File newBotFile = new File(oldBotFile.getParent(),"bot.log.old");
        File oldTs3File = new File("teamspeak.log");
        File newTs3File = new File(oldTs3File.getParent(),"teamspeak.log.old");
        try {
            //if(Files.exists(Paths.get("logs/bot.log"))){
            //    Files.deleteIfExists(newBotFile.toPath());
            //    Files.move(oldBotFile.toPath(), newBotFile.toPath());
            //}
            if(Files.exists(Paths.get("teamspeak.log"))){
                Files.deleteIfExists(newTs3File.toPath());
                Files.move(oldTs3File.toPath(), newTs3File.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

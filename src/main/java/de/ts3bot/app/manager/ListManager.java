package de.ts3bot.app.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Captain on 17.03.2016.
 * lade alle Listen
 * schreibe die Listen raus
 */

public class ListManager {
    private static final Logger log = LogManager.getLogger(ListManager.class);

    private ListManager() { }

    public static void writeLists(List<String> list, String fileLocation) {
        File newFile = new File(fileLocation);
        File dirFile = new File(newFile.getParent());
        if( ! dirFile.exists()) {
            dirFile.mkdir();
        }
        try (FileWriter writer = new FileWriter(newFile)){
            for(String entry : list){
                writer.write(entry + "\n");
            }
        } catch (IOException ex) {
            log.error(ex.getStackTrace());
        }
    }

    public static List readLists(String fileLocation) {
        List<String> strList = new ArrayList<>();
        File newFile = new File(fileLocation);
        try ( BufferedReader reader = new BufferedReader(new FileReader(newFile) )){
            String line;
            while ((line = reader.readLine()) != null){
                if(line.startsWith("#")){
                    continue;
                }
                strList.add(line);
            }
        } catch (IOException ex) {
            log.error(ex.getStackTrace());
        }
        return strList;
    }

    public static boolean fileExist(String fileLocation){
        File newFile = new File(fileLocation);
        return newFile.exists();
    }
}

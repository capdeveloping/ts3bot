package de.ts3bot.app.manager;

import com.github.theholywaffle.teamspeak3.TS3Api;

import java.text.SimpleDateFormat;
import java.util.*;

public class FormatManager {

    private FormatManager() { }

    public static String getDateFormatted(){
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("'am' dd.MM.yyyy 'um' HH:mm");
        return df.format(date);
    }

    public static boolean compareArrayToIntList(int[] array, List<Integer> searchList){
        for(int element : array){
            if ( searchList.contains(element) ){
                return true;
            }
        }
        return false;
    }

    public static String StackTraceToString(Exception ex){
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String convertListStrToStr(List<String> strList) {
        StringBuilder str = new StringBuilder();
        for (String element : strList) {
            str.append(",").append(element);
        }
        if(str.toString().isEmpty()){
            return str.toString();
        }
        return str.toString().substring(1);
    }

    public static List<Integer> convertStrToIntList(List<Integer> oldList, String str){
        if ( ! str.equals("") ) {
            String help = str.replace(" ", "");
            if (help.contains(",")) {
                for (String str2 : help.split(",")) {
                    oldList.add(Integer.valueOf(str2));
                }
            } else {
                oldList.add(Integer.valueOf(help));
            }
            return oldList;
        }else{
            return new ArrayList<>();
        }
    }

    public static List<String> convertStrToStrList(String str){
        List<String> emptyList = new ArrayList<>();
        if ( ! str.equals("") ) {
            String help = str.replace(" ", "");
            for (String element : Arrays.asList(help.split(","))) {
                emptyList.add(element);
            }
        }

        return emptyList;
    }

    public static List<String> convertStrToStrList(List<String> oldList, String str){
        if ( ! str.equals("") ) {
            String help = str.replace(" ", "");
            List<String> test = new ArrayList<>();
            for (String element : Arrays.asList(help.split(","))) {
                test.add(element);
            }
            test.addAll(oldList);
            return test;
        }else{
            return new ArrayList<>();
        }
    }

    public static void checkAndSendLanguage(TS3Api api, int invokerId, String language, String deutsch, String englisch){
        language = language.toLowerCase();
        if(language.equals("german") || language.equals("deutsch")){
            api.sendPrivateMessage(invokerId, deutsch);
        }else{
            api.sendPrivateMessage(invokerId, englisch);
        }
    }

    public static void checkAndPokeLanguage(TS3Api api, int invokerId, String language, String deutsch, String englisch){
        language = language.toLowerCase();
        if(language.equals("german") || language.equals("deutsch")){
            api.pokeClient(invokerId, deutsch);
        }else{
            api.pokeClient(invokerId, englisch);
        }
    }

    public static String sucheZeichen(String message, String zeichen){
        String help = "";
        if(message.contains(" " + zeichen + " ")){
            help = " " + zeichen + " ";
        } else if (message.contains(zeichen + " ")) {
            help = zeichen + " ";
        } else if (message.contains(" " + zeichen)) {
            help = " " + zeichen;
        } else if (message.contains(zeichen)) {
            help = zeichen;
        }
        return help;
    }

    public static String fillStringToSpecifiLength(String message, int length){
        StringBuilder messageBuilder = new StringBuilder(message);
        messageBuilder.append(" ".repeat(Math.max(0, length - messageBuilder.length() + 1)));
        message = messageBuilder.toString();
        return message;
    }
}

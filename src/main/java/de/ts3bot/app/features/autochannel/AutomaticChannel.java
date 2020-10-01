package de.ts3bot.app.features.autochannel;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelInfo;
import de.ts3bot.app.manager.ListManager;
import de.ts3bot.app.models.AutomaticChannelProperty;
import de.ts3bot.app.models.TS3ServerConfig;
import de.ts3bot.app.models.functions.FunctionChannelAutoCreate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

public class AutomaticChannel extends TS3EventAdapter{
    private final Logger log = LogManager.getLogger(AutomaticChannel.class.getName());
    private TS3Api api;
    private HashMap<Integer, AutomaticChannelProperty> channels;
    private CreateAutomaticChannel createAutomaticChannel;
    private DeleteAutomaticChannel deleteAutomaticChannel;
    private FunctionChannelAutoCreate functionChannelAutoCreate;

    public AutomaticChannel(TS3ServerConfig serverConfig){
        functionChannelAutoCreate = serverConfig.getFunctionChannelAutoCreate();
        channels = new HashMap<>();
        deleteAutomaticChannel = new DeleteAutomaticChannel(channels);
        createAutomaticChannel = new CreateAutomaticChannel(channels);
        reinitializeACP();
    }

    private void loadChannelPasswords(){
        File file = new File(functionChannelAutoCreate.getChannelPasswordFilePath());

        if( ! file.exists() ){
            createChannelPasswordsConfig();
            return;
        }
        List<String> lines = ListManager.readLists(functionChannelAutoCreate.getChannelPasswordFilePath());
        try {
            for (String line : lines) {
                String[] helpArr = line.split(" = ");
                int parentCID = Integer.parseInt(helpArr[0]);
                String password = helpArr[1];
                if(channels.containsKey(parentCID)){
                    channels.get(parentCID).setChannelPassword(password);
                    channels.get(parentCID).setHasPassword(true);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getStackTrace());
        }
    }

    private void createChannelPasswordsConfig(){
        log.info("create channel password file");
        try (FileWriter fileWriter = new FileWriter(functionChannelAutoCreate.getChannelPasswordFilePath());
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("# Information:\n");
            bufferedWriter.write("#    Pro Eintrag eine Zeile!\n");
            bufferedWriter.write("#    Hier wird ein Mapping von ParentChannel und deren Passwörter hinterlegt!\n");
            bufferedWriter.write("#    [PARENTCHANNELID] = [PASSWORD]\n");
            bufferedWriter.write("#    Beispiel: 3214 = Password123!\n");
        }
        catch(Exception ex) {
            log.error("writing to file: {}", ex.getMessage());
        }
    }

    //region setter
    public void setServerConfig(TS3ServerConfig serverConfig) {
        functionChannelAutoCreate = serverConfig.getFunctionChannelAutoCreate();
        channels.clear();
        reinitializeACP();
        loadChannelPasswords();
        getAllSubChannelsForPCIDs();
    }

    public void setApi(TS3Api api) {
        this.api = api;
        deleteAutomaticChannel.setApi(api);
        createAutomaticChannel.setApi(api);
        getAllSubChannelsForPCIDs();
    }

    public void setStopFunction(boolean stopTask){
        channels.clear();
        if (!stopTask) {
            reinitializeACP();
            getAllSubChannelsForPCIDs();
        }
        createAutomaticChannel.setChannels(channels);
        deleteAutomaticChannel.setChannels(channels);
        createAutomaticChannel.setStopFunction(stopTask);
        deleteAutomaticChannel.setStopFunction(stopTask);
    }
//endregion

    public void regisListener(){
        api.addTS3Listeners(deleteAutomaticChannel);
        api.addTS3Listeners(createAutomaticChannel);
    }

    private void reinitializeACP(){
        for (int id : functionChannelAutoCreate.getChannelidlist()){
            if (channels.isEmpty()){
                channels.put(id, new AutomaticChannelProperty(""));
            }else {
                if ( !channels.containsKey(id))
                    channels.put(id, new AutomaticChannelProperty(""));
            }
        }
    }

    private void getAllSubChannelsForPCIDs(){
        for (Channel chan : api.getChannels()){
            if ( channels.containsKey(chan.getParentChannelId())){
                if (channels.get(chan.getParentChannelId()).getBaseChannelName().equals("")) {
                    String channelName = chan.getName().substring( 0 , chan.getName().lastIndexOf(' ') );
                    channels.get(chan.getParentChannelId()).setBaseChannelName(channelName);
                }
                if(channels.get(chan.getParentChannelId()).getCidList().size() != 0){
                    channels.get(chan.getParentChannelId()).addCidTolist(chan.getId(), chan.getName());
                    continue;
                }
                channels.get(chan.getParentChannelId()).addCidTolist(chan.getId(), chan.getName());
                channels.get(chan.getParentChannelId()).setChannelPermissions(api.getChannelPermissions(chan.getId()));
                channels.get(chan.getParentChannelId()).setMaxChannelClients( String.valueOf(chan.getMaxClients()) );

                boolean hasUnlimitedCLients = api.getChannelInfo(chan.getId()).hasUnlimitedClients();
                if(hasUnlimitedCLients){
                    channels.get(chan.getParentChannelId()).setHasUnlimitedClients("1");
                }else{
                    channels.get(chan.getParentChannelId()).setHasUnlimitedClients("0");
                }

                channels.get(chan.getParentChannelId()).setChannelDescription(api.getChannelInfo(chan.getId()).getDescription());
            }
        }
    }
}

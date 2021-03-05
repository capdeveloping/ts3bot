package de.ts3bot.app.models;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.DatabaseClient;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.TreeMap;

public class CollectData {
    private final Logger log = LogManager.getLogger(CollectData.class.getName());
    private TS3Api api;
    private TS3ServerConfig serverConfig;

    public void setApi(TS3Api api) {
        this.api = api;
    }

    public void setServerConfig(TS3ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void startCollectingData(){
        Connection conn = connectDatabase();
        collectChannelData(conn);
        collectServerGroupData(conn);
        collectUserData(conn);
        closeDatabase(conn);
    }

    private void collectUserData(Connection conn){
        if ( ! clearTable(conn, serverConfig.getBotName() + "_users") ) {
            return;
        }

        String sql = "INSERT INTO \"" + serverConfig.getBotName() + "_users\"(uid, name, ip, groups) VALUES(?,?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for(DatabaseClient client : api.getDatabaseClients()){
                pstmt.setString(1, client.getUniqueIdentifier());
                pstmt.setString(2, client.getNickname());
                pstmt.setString(3, "");
                pstmt.setString(4, "");
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            log.info( "{}: sqlite error {}", serverConfig.getBotName(), e.getMessage());
        }
    }

    private void collectServerGroupData(Connection conn){
        HashMap<Integer, String> groups = new HashMap<>();
        for (ServerGroup serverGroup : api.getServerGroups()){
            groups.put(serverGroup.getId(), serverGroup.getName());
        }
        if ( clearTable(conn, serverConfig.getBotName() + "_groups") ){
            updateTable("INSERT INTO \"" + serverConfig.getBotName() + "_groups\"(id, name) VALUES(?,?)", groups, conn);
        }
    }

    private void collectChannelData(Connection conn){
        HashMap<Integer, String> channels = new HashMap<>();
        for (Channel channel : api.getChannels()){
            channels.put(channel.getId(), channel.getName());
        }

        if ( clearTable(conn, serverConfig.getBotName() + "_channels") ){
            updateTable("INSERT INTO \"" + serverConfig.getBotName() + "_channels\"(id, name) VALUES(?,?)", channels, conn);
        }
    }

    private boolean clearTable(Connection conn, String tableName){
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM \"" + tableName + "\"; VACUUM;");
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.info( "{}: sqlite error clearTable {}", serverConfig.getBotName(), e.getMessage());
            return false;
        }
    }

    private void updateTable(String sql, HashMap<Integer, String> hashMap, Connection conn) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (int key : hashMap.keySet()) {
                pstmt.setInt(1, key);
                pstmt.setString(2, hashMap.get(key));
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            log.info( "{}: sqlite error {}", serverConfig.getBotName(), e.getMessage());
        }
    }

    private Connection connectDatabase() {
        // SQLite connection string
        String url = "jdbc:sqlite:/data/db.sqlite3";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            log.info( "{}: sqlite error {}", serverConfig.getBotName(), e.getMessage());
        }
        return conn;
    }

    private void closeDatabase(Connection conn){
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            log.info( "{}: sqlite error {}", serverConfig.getBotName(), ex.getMessage());
        }
    }
}

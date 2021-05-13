package de.ts3bot.app.models;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.*;
import de.ts3bot.app.models.data.CollectedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

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

        String sql = "INSERT INTO \"" + serverConfig.getBotName() + "_users\"(uid, name, ip, groups, online) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            List<Client> clients = api.getClients();
            for(DatabaseClient client : api.getDatabaseClients()){
                pstmt.setString(1, client.getUniqueIdentifier());
                pstmt.setString(2, client.getNickname());
                pstmt.setString(3, "");
                pstmt.setString(4, "");
                pstmt.setString(5, "0");
                for(Client onlineClient : clients){
                    if(client.getUniqueIdentifier().equals(onlineClient.getUniqueIdentifier())){
                        // 1 ist online
                        pstmt.setString(5, "1");
                        break;
                    }
                }
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            log.error( "{}: sqlite error {}", serverConfig.getBotName(), e.getMessage());
        }
    }

    private void collectServerGroupData(Connection conn){
        HashMap<CollectedData, Integer> groupstmp = new HashMap<>();
        for (ServerGroup serverGroup : api.getServerGroups()){
            if (serverGroup.getId() < 6){
                continue;
            }
            int sortOrder = 100000;
            try {
                for (Permission permission : api.getServerGroupPermissions(serverGroup.getId())) {
                    if( ! permission.getName().equals("i_group_sort_id") ){
                        continue;
                    }
                    sortOrder = permission.getValue();
                    break;
                }
            }catch (Exception ex){
                log.error( "{}: api permission error {}", serverConfig.getBotName(), ex.getMessage());
            }
            groupstmp.put(new CollectedData(serverGroup.getId(), serverGroup.getName()), sortOrder);
        }

        List<CollectedData> groups = new ArrayList<>();
        List<Integer> sorted = new ArrayList<>(groupstmp.values());
        Collections.sort(sorted);

        try {
            for (int sortId : sorted) {
                for (CollectedData group : new HashMap<>(groupstmp).keySet()) {
                    if (groupstmp.get(group) == sortId) {
                        groups.add(group);
                        groupstmp.remove(group);
                    }
                }
            }
        }catch(Exception e){
            log.error( "{}: api permission error {}", serverConfig.getBotName(), e.getMessage());
        }
        for(CollectedData group : groupstmp.keySet()){
            groups.add(group);
        }

        if ( clearTable(conn, serverConfig.getBotName() + "_groups") ){
            updateTable("INSERT INTO \"" + serverConfig.getBotName() + "_groups\"(id, name) VALUES(?,?)", groups, conn);
        }
    }

    private void collectChannelData(Connection conn){
        if ( ! clearTable(conn, serverConfig.getBotName() + "_channels") ) {
            return;
        }

        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO \"" + serverConfig.getBotName() + "_channels\"(id, name) VALUES(?,?)");
            for (Channel channel : api.getChannels()){
                channel.getOrder();
                pstmt.setInt(1, channel.getId());
                pstmt.setString(2, channel.getName());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            log.error( "{}: sqlite error {}", serverConfig.getBotName(), e.getMessage());
        }
    }

    private boolean clearTable(Connection conn, String tableName){
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM \"" + tableName + "\"; VACUUM;");
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error( "{}: sqlite error clearTable {}", serverConfig.getBotName(), e.getMessage());
            return false;
        }
    }

    private void updateTable(String sql, List<CollectedData> datas, Connection conn) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (CollectedData data : datas ) {
                pstmt.setInt(1, data.getId());
                pstmt.setString(2, data.getName());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            log.error( "{}: sqlite error {}", serverConfig.getBotName(), e.getMessage());
        }
    }

    private Connection connectDatabase() {
        // SQLite connection string
        String url = "jdbc:sqlite:/data/db.sqlite3";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            log.error( "{}: sqlite error {}", serverConfig.getBotName(), e.getMessage());
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

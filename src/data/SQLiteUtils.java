package data;

import model.Message;
import model.UsrInfo;
import model.UsrList;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteUtils {
    private Connection conn = null;
    private boolean dbExist = true;
    private Statement state = null;

    public SQLiteUtils() throws IOException {
        if (!new File("chat.db").exists()) {
            File file = new File("chat.db");
            file.createNewFile();
            System.out.println(file.getAbsolutePath());
            dbExist = false;
        }
        try {
            System.out.println(new File("chat.db").getAbsolutePath());
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:chat.db");
            state = conn.createStatement();
            if (!dbExist) {
                String sqlUsrInfo = "create table usrInfo"
                        + "(mac text primary key not null,"
                        + "remark text,"
                        + "tag text,"
                        + "black int default 0);";
                String sqlChatLog = "create table chatLog"
                        + "(mac text primary key not null,"
                        + "recv int not null,"
                        + "type int not null,"
                        + "content text not null,"
                        + "time datetime not null);";
                state.execute(sqlChatLog);
                state.execute(sqlUsrInfo);
                dbExist = true;
            }
            if (!IsTableExist("usrInfo", state)) {
                String sqlUsrInfo = "create table usrInfo"
                        + "(mac text primary key not null,"
                        + "remark text,"
                        + "tag text"
                        + "black int default 0);";
                state.execute(sqlUsrInfo);
            }
            if (!IsTableExist("chatLog", state)) {
                String sqlChatLog = "create table chatLog"
                        + "(mac text primary key not null,"
                        + "recv int not null,"
                        + "type int not null,"
                        + "content text not null,"
                        + "date datetime not null);";
                state.execute(sqlChatLog);
            }
        } catch (Exception e) {
            System.out.println(e.getClass().getName() + ":" + e.toString());
            new File("chat.db").delete();
            //TODO:
        }
    }

    public ResultSet ExecuteQuery(String sql) {
        try {
            ResultSet rs = state.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void Execute(String sql) {
        try {
            state.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //按时间倒序排序
    public List<Message> getMessages(String mac) {
        List<Message> messageList = new ArrayList<Message>();
        try {
            String sql = "select * from chatLog where mac = \'" + mac + "\' order by time desc;";
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                Message message = new Message();
                message.setMAC(mac);
                message.setRecv(rs.getInt("recv"));
                message.setType(rs.getInt("type"));
                message.setContent(rs.getString("content"));
                message.setTime(rs.getTime("time"));
                messageList.add(message);
            }
            rs.close();
            return messageList;
        } catch (Exception e) {
            e.printStackTrace();
            return messageList;
        }
    }

    public void addMessage(Message message) {
        try {
            String sql = "insert into chatLog[(mac,recv,type,content,time)] values (\'" + message.getMAC() + "\',\'" + message.getRecv() +
                    "\',\'" + message.getType() + "\',\'" + message.getContent() + "\',\'" + message.getTime() + "\');";
            state.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UsrInfo getUsrInfo(String mac) {
        UsrInfo usrInfo = new UsrInfo();
        usrInfo.setMAC(mac);
        usrInfo.setBlack(0);
        try {
            SQLiteUtils sqLiteUtils = new SQLiteUtils();
            String sql = "select * from usrInfo where mac = \'" + mac + "\';";
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                usrInfo.setBlack(rs.getInt("black"));
                usrInfo.setRemark(rs.getString("remark"));
                usrInfo.setTag(rs.getString("tag"));
            }
            rs.close();
            return usrInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return usrInfo;
        }
    }

    public void setUsrInfo(UsrInfo usrInfo) {
        String sql = "select * from usrInfo where mac = \'" + usrInfo.getMAC() + "\';";
        try {
            ResultSet rs = state.executeQuery(sql);
            if (rs.next()) {
                rs.close();
                String sql2 = "update usrInfo set remark=\'" + usrInfo.getRemark() +
                        "\',tag=\'" + usrInfo.getTag() + "\',black=" + usrInfo.getBlack() + " where mac=\'" + usrInfo.getMAC() + "\';";
                state.execute(sql2);
            }
            else{
                rs.close();
                String sql2 = "insert into usrInfo[(mac,remark,tag,black)] values (\'" + usrInfo.getMAC() + "\',\'" + usrInfo.getRemark() +
                        "\',\'" + usrInfo.getTag() + "\'," + usrInfo.getBlack() + ");";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String,UsrInfo> UpdateUsrList(Map<String, String> onlineList) {
        Map<String,UsrInfo> usrInfoMap = new HashMap<String,UsrInfo>();
        for(Map.Entry<String,String> entry : onlineList.entrySet()){
            UsrInfo usrInfo = getUsrInfo(entry.getKey());
            usrInfo.setIP(entry.getValue());
            usrInfo.setState(true);
            usrInfoMap.put(entry.getKey(),usrInfo);
        }
        return usrInfoMap;
    }

    private boolean IsTableExist(String tableName, Statement s) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        try {
            s.execute("select * from " + tableName + ";");
            return true;
        } catch (Exception e) {
            return false;
            // TODO: handle exception
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        state.close();
        conn.close();
    }
}

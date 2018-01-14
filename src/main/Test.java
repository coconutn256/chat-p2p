package main;

import data.SQLiteUtils;
import fram.ChatForm;
import model.UsrInfo;
import model.UsrList;
import net.SocketServer;
import net.UDPClient;
import net.UDPServer;
import userlist.List;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static SQLiteUtils sqLiteUtils = new SQLiteUtils();
    public static UsrList usrList = new UsrList();
    public static List frmList = new List(usrList);
    public static Map<String, ChatForm> chatFormMap = new HashMap<String, ChatForm>();
    public static Map<String, Integer> onlineTTL = new HashMap<>();
    private static UsrInfo friend1 = new UsrInfo();
    private static UsrInfo friend2 = new UsrInfo();
    private static java.util.List<UsrInfo> usrInfoList = new ArrayList<>();

    public static void main(String[] args) {
        friend1.setRemark("狗子");
        friend1.setIP("10000");
        friend1.setState(1);
        friend2.setRemark("二狗");
        friend2.setIP("20000");
        friend2.setState(0);
        friend2.setBlack(1);
        usrInfoList.add(friend1);
        usrInfoList.add(friend2);
        HashMap<String, UsrInfo> map = new HashMap<>();
        map.put("狗子", friend1);
        map.put("二狗", friend2);
        usrList.usrInfoMap = map;
        frmList.refresh(usrList);

    }
}

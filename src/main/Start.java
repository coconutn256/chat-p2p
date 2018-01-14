package main;

import data.SQLiteUtils;
import fram.ChatForm;
import model.UsrInfo;
import model.UsrList;
import net.HostInfo;
import net.SocketServer;
import net.UDPClient;
import net.UDPServer;
import userlist.List;

import java.io.IOException;
import java.io.StringReader;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Start {
    public static SQLiteUtils sqLiteUtils = new SQLiteUtils();
    public static UsrList usrList = new UsrList();
    public static List frmList = new List(usrList);
    public static Map<String, ChatForm> chatFormMap = new HashMap<String, ChatForm>();
    public static Map<String, Integer> onlineTTL = new HashMap<>();

    public static void main(String[] args) {
        //String ip = "10.2.19.95";
        //String ip = "10.128.171.157";
        //Map<String,String> lanList = HostInfo.getMacIp();
        Map<String, String> lanList = new HashMap<String, String>();
        //lanList.put("f0-76-1c-b4-21-3c", "10.128.171.157");
        lanList.put("34-68-95-00-52-6d", "10.2.19.95");
        new Thread(
                ()-> {
                    try {
                        SocketServer socketServer = new SocketServer();
                        System.out.println("TCPServer服务启动成功");
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("TCPServer服务启动失败");
                    }
                }).start();
        new Thread(
                ()->{UDPServer udpServer = new UDPServer();}
        ).start();
        new Thread(
                ()->{
                    try {
                        while (true) {
                            for(Map.Entry<String,String> entry:lanList.entrySet()){
                                UDPClient.SendOnline(entry.getValue().trim());
                                if (onlineTTL.containsKey(entry.getKey())) {
                                    if (onlineTTL.get(entry.getKey()) > 0)
                                        onlineTTL.put(entry.getKey(), onlineTTL.get(entry.getKey()) - 1);
                                    else {
                                        usrList.usrInfoMap.get(entry.getKey()).setState(0);
                                        System.out.println(entry.getValue() + " offline.");
                                        frmList.refresh(usrList);
                                    }
                                }
                            }
                            Thread.sleep(500);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).start();

        /*new Thread(
                ()->{
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        frmList.refresh(usrList);
                        System.out.println("frmList refreshed");
                    }
        ).start();*/
    }
}

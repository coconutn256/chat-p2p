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
    public static void main(String[] args) {
        Map<String, UsrInfo> onlineList = new HashMap<String, UsrInfo>();
        Map<String,String> lanList = HostInfo.getMacIp();
        UsrList usrList = new UsrList();
        List frmList = new List()
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
                            }
                            UDPClient.SendOnline("127.0.0.1");
                            Thread.sleep(3000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }
}

package src;

import fram.ChatForm;
import model.UsrInfo;
import model.UsrList;
import userlist.List;

import java.util.HashMap;

public class TestClient {
    public static void main(String args[]) {
        UsrInfo test = new UsrInfo();
        test.setRemark("小冰");
        test.setBlack(0);
        test.setMAC("10000100");
        test.setIP("10.10.10.10");
        test.setState(true);
        HashMap<String,UsrInfo> map = new HashMap<>();
        map.put("小冰",test);
        UsrList testlist = new UsrList();
        testlist.usrInfoMap = map;
        List usrlist = new List(testlist);
        //SocketClient  socketClient = new SocketClient();
        //socketClient.SendJson("127.0.0.1");
    }
}

package test;

import fram.ChatForm;
import model.UsrInfo;
import model.UsrList;
import net.SocketClient;
import org.json.JSONObject;
import userlist.List;

import java.util.HashMap;
import java.util.Map;

public class TestClient {
    public static void main(String args[]) {
//        UsrInfo test = new UsrInfo();
//        //test.setRemark("小冰");
//        test.setBlack(0);
//        test.setMAC("10000100");
//        test.setIP("10.10.10.10");
//        test.setState(1);
//        UsrInfo test1 = new UsrInfo();
//        test1.setRemark("狗子");
//        test1.setBlack(0);
//        test1.setMAC("10000001");
//        test1.setIP("10.10.100.10");
//        test1.setState(1);
//
//        HashMap<String,UsrInfo> map = new HashMap<>();
//        map.put("1",test);
//        map.put("2",test1);
//        UsrList testlist = new UsrList();
//        List usrlist = new List(testlist);
//        testlist.usrInfoMap = map;
//        usrlist.refresh(testlist);
        SocketClient socketClient = new SocketClient("10.2.213.141");
        Map<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("type", String.valueOf(5));
        tempMap.put("uid", "safs");

        JSONObject json = new JSONObject(tempMap);
        socketClient.SendJson(json);
    }
}

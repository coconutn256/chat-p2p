package test;

import data.JsonUtils;
import fram.ChatForm;
import model.Message;
import model.UsrInfo;
import model.UsrList;
import net.SocketClient;
import org.json.JSONObject;
import userlist.List;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        Date date = new Date();//获得系统时间.
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//将时间格式转换成符合Timestamp要求的格式.
        Timestamp goodsC_date = Timestamp.valueOf(nowTime);//把时间转换

        File file = new File("D:\\Downloads\\纱雾.png");
        SocketClient socketClient = new SocketClient("10.128.171.157");
        System.out.println(file.getPath());
        Message message = new Message();
        message.setMAC("lohohoho");
        message.setType(Message.TEXT);
        message.setContent(file.getPath() + "," + file.length());
        message.setRecv(0);
        message.setTime(goodsC_date);
        socketClient.SendJson(JsonUtils.MessageToJson(message));
    }
}

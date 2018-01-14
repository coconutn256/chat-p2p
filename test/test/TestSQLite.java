package test;

import data.SQLiteUtils;
import model.Message;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestSQLite {
    public static void main(String args[]){
        SQLiteUtils sqLiteUtils = new SQLiteUtils();

        Date date = new Date();//获得系统时间.
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//将时间格式转换成符合Timestamp要求的格式.
        Timestamp goodsC_date = Timestamp.valueOf(nowTime);//把时间转换

        Message message = new Message();
        message.setMAC("lohohoho");
        message.setType(Message.RECVFILE);
        message.setContent("asdfasf");
        message.setRecv(0);
        message.setTime(goodsC_date);

        List<Message> list = sqLiteUtils.getAllMessages();
        for (Message message1 : list) {
            System.out.println(message1.getContent());
        }
        new Thread(
                () -> {
                    sqLiteUtils.addMessage(message);
                }
        ).start();
        sqLiteUtils.Close();
    }
}

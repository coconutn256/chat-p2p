package test;

import data.JsonUtils;
import model.Message;
import org.json.JSONException;
import org.json.JSONObject;

public class TestJson {
    public static void main(String[] args) {
        Message message = new Message();
        message.setMAC("123");
        message.setRecv(0);
        message.setTime(null);
        message.setType(Message.TEXT);
        message.setContent("123456789");
        JSONObject json = JsonUtils.MessageToJson(message);
        try {
            System.out.println(json.getString("time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

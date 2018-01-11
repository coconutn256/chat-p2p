package data;

import model.Message;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class JsonUtils {
    public static Message JsonToMessage(JSONObject json){
        Message message = new Message();
        try {
            message.setMAC(json.getString("mac"));
            message.setType(Integer.parseInt(json.getString("type")));
            message.setRecv(Integer.parseInt(json.getString("recv")));
            message.setContent(json.getString("content"));
            message.setTime(Timestamp.valueOf(json.getString("time")));
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static JSONObject MessageToJson(Message message){
        JSONObject json = null;
        try{
            Map<String,String> map = new HashMap<String, String>();
            map.put("mac",message.getMAC());
            map.put("recv",String.valueOf(message.getRecv()));
            map.put("type",String.valueOf(message.getType()));
            map.put("content",message.getContent());
            map.put("time",String.valueOf(message.getTime()));

            json = new JSONObject(map);
            return json;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

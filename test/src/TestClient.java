package src;

import fram.ChatForm;
import model.UsrInfo;
import userlist.List;

public class TestClient {
    public static void main(String args[]) {
        UsrInfo test = new UsrInfo();
        test.setIP("10.10.10.10.10");
        test.setRemark("小冰");
        ChatForm chatForm = new ChatForm(test);
        //SocketClient  socketClient = new SocketClient();
        //socketClient.SendJson("127.0.0.1");
    }
}

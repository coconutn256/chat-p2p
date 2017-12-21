package src;

import net.SocketClient;

public class TestClient {
    public static void main(String args[]) {
        SocketClient  socketClient = new SocketClient();
        socketClient.Send("127.0.0.1");
    }
}

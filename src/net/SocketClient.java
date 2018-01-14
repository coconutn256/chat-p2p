package net;

import model.Message;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class SocketClient {
    private String ip;
    private Socket socket;

    public SocketClient(String ip) {
        this.ip = ip;
    }

    public int SendJson(JSONObject json) {
        try {
            socket = new Socket(ip, 2015);
            socket.setSoTimeout(10000);

            byte[] jsonByte = json.toString().getBytes();
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeInt(Integer.parseInt(json.getString("type")));
            dos.writeUTF(json.getString("content"));
            dos.writeInt(jsonByte.length);
            dos.write(jsonByte);
            dos.flush();
            System.out.println("Send to: " + ip);

            if (Integer.parseInt(json.getString("type")) == Message.RECVFILE) {
//                new FileRecv().receiveFile("", ip, 2015);
                DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                byte[] buf = new byte[1024];
                int passlen = 0;
                String filePath = json.getString("content").split(",")[0];
                String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
                long len = Integer.parseInt(json.getString("content").split(",")[1]);
                System.out.println(fileName + "," + len);
                String savePath = "./" + fileName;
                DataOutputStream fileout = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(savePath)));
                long passedlen = 0;
                double percent = 0;
                while (true) {
                    int read = 0;
                    if (dis != null)
                        read = dis.read(buf);
                    passedlen += read;
                    if (read == -1)
                        break;
                    fileout.write(buf, 0, read);
                    if (passedlen * 100 / len - percent >= 1) {
                        percent = passedlen * 100 / len;
                        System.out.println("文件接收了" + (passedlen * 100 / len) + "%");
                    }
                }
                System.out.println("接收完成");
                fileout.close();
                dis.close();
                dos.close();
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

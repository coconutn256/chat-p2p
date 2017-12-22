package net;

import model.Message;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class SocketClient {
    private String ip;
    private Socket socket;

    public SocketClient(String ip) {
        this.ip = ip;
    }

    public int SendJson(JSONObject json) {
        try {
            socket = new Socket(ip, 2013);
            socket.setSoTimeout(20000);

            byte[] jsonByte = json.toString().getBytes();
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.write(jsonByte);
            dos.flush();
            socket.shutdownOutput();

            if (Integer.parseInt(json.getString("type")) == Message.RECVFILE) {
                try {
                    DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                    byte[] buf = new byte[1024];
                    int passlen = 0;
                    String[] temp = json.getString("content").split(File.separator);
                    String savePath = "./" + temp[temp.length - 1];
                    DataOutputStream fileout = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(savePath)));
                    long len = Integer.parseInt(json.getString("content").split(" ")[1]);
                    long passedlen = 0;
                    while (true) {
                        int read = 0;
                        if (dis != null)
                            read = dis.read(buf);
                        if (read == -1)
                            break;
                        passedlen += read;
                        fileout.write(buf, 0, read);
                        System.out.println("接收进度：" + passedlen / len * 100 + "%");
                    }
                    System.out.println("接收完成");
                    fileout.close();
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //TODO:处理返回信息
            return 0;
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            return -1;
        }finally {
            try {
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}

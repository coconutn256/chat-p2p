package net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import data.JsonUtils;
import data.SQLiteUtils;
import model.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SocketServer extends ServerSocket {
    private static final int SERVER_PORT = 2013;

    public SocketServer() throws IOException {
        super(SERVER_PORT);

        try {
            System.out.println("server started successfully.");
            while (true) {
                Socket socket = accept();
                new CreateServerThread(socket);//当有请求时，启一个线程处理
            }
        } catch (IOException e) {
            System.out.println("Creat server falied: " + e.toString());
            //TODO:错误信息可视化
        } finally {
            close();
        }
    }

    //线程类
    class CreateServerThread extends Thread {
        private Socket client;
        private BufferedReader bufferedReader;
        private PrintWriter printWriter;

        public CreateServerThread(Socket s) throws IOException {
            client = s;
            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            printWriter = new PrintWriter(client.getOutputStream(), true);
            System.out.println("Client(" + getName() + ") come in...");

            start();
        }

        public void run() {
            try {
                DataInputStream dis = new DataInputStream(client.getInputStream());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] bytes = new byte[2048];
                int n;
                while ((n = dis.read(bytes)) != -1) {
                    baos.write(bytes, 0, n);
                }
                JSONObject json = new JSONObject(baos.toByteArray());
                printWriter.close();
                bufferedReader.close();
                baos.close();
                dis.close();
                client.shutdownInput();

                int type = Integer.parseInt((String) json.get("type"));
                if (type!=Message.RECVFILE) {
                    SQLiteUtils sqLiteUtils = new SQLiteUtils();
                    sqLiteUtils.addMessage(JsonUtils.JsonToMessage(json));
                    sqLiteUtils.Close();
                }

                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                if (type == Message.RECVFILE) {
                    String filepath = json.getString("content").split(" ")[0];
                    byte[] bufArray = new byte[1024];
                    FileInputStream fileInputStream = new FileInputStream(filepath);
                    while (true) {
                        int read = 0;
                        if (fileInputStream != null)
                            read = fileInputStream.read(bufArray);
                        if (read == -1)
                            break;
                        dos.write(bufArray, 0, read);
                    }
                    dos.flush();
                    fileInputStream.close();

                    SQLiteUtils sqLiteUtils = new SQLiteUtils();
                    sqLiteUtils.addMessage(JsonUtils.JsonToMessage(json));
                    sqLiteUtils.Close();
                }
                dos.close();
            } catch (Exception e) {
                e.printStackTrace();
                //TODO:错误信息可视化(发送/接收失败)
            }finally {
                try {
                    if(client!=null)
                        client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

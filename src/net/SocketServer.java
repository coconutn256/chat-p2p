package net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import data.JsonUtils;
import data.SQLiteUtils;
import main.Start;
import model.Message;
import model.UsrInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SocketServer extends ServerSocket {
    private static final int SERVER_PORT = 2015;

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
                System.out.println(dis.available());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] bytes = new byte[2048];
                int n;
                while ((n = dis.read(bytes)) != -1) {
                    System.out.println(new String(bytes));
                    baos.write(bytes);
                    baos.flush();
                }
                JSONObject json = new JSONObject(baos.toString());
                System.out.println(json.toString());
                printWriter.close();
                bufferedReader.close();
                baos.close();
                dis.close();
                Message message = JsonUtils.JsonToMessage(json);
                message.setRecv(0);
                System.out.println(message.getMAC());
                System.out.println(Start.chatFormMap.keySet());
                int type = Integer.parseInt(json.getString("type"));
                if (Start.chatFormMap.containsKey(message.getMAC())) {
                    Start.chatFormMap.get(message.getMAC()).getMessage(message);
                    System.out.println("Add message to chatForm:" + message.getMAC());
                } else {
                    //TODO:置顶，显示未读
                }
                if (type != Message.RECVFILE) {
                    SQLiteUtils sqLiteUtils = Start.sqLiteUtils;
                    sqLiteUtils.addMessage(message);
                    sqLiteUtils.Close();
                    if (type == Message.TEXT) {
                        Start.usrList.usrInfoMap.get(message.getMAC()).setState(UsrInfo.UNREAD_TEXT);
                    }

                    if (type == Message.SENDFILE) {
                        Start.usrList.usrInfoMap.get(message.getMAC()).setState(UsrInfo.UNRECV_FILE);
                    }
                }

                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                if (type == Message.RECVFILE) {
                    String filepath = json.getString("content").split(",")[0];
                    String[] temp = filepath.split(File.separator);
                    byte[] bufArray = new byte[1024];
                    FileInputStream fileInputStream = new FileInputStream(filepath);
                    System.out.println("Sending " + temp[temp.length - 1] + " to " + client.getLocalAddress().getHostAddress());
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

                    SQLiteUtils sqLiteUtils = Start.sqLiteUtils;
                    sqLiteUtils.addMessage(message);
                }
                dos.close();
            } catch (Exception e) {
                e.printStackTrace();
                //TODO:错误信息可视化(发送/接收失败)
            } finally {
                if (client != null)
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}

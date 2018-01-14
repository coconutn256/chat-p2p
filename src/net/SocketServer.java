package net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import data.JsonUtils;
import main.Start;
import model.Message;
import model.UsrInfo;
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
        } finally {
            close();
        }
    }

    //线程类
    class CreateServerThread extends Thread {
        private Socket client;
        private BufferedReader bufferedReader;
        private PrintWriter printWriter;

        public CreateServerThread(Socket s) {
            client = s;
            System.out.println("Client(" + getName() + ") come in...");

            start();
        }

        public void run() {
            try {
                DataInputStream dis = new DataInputStream(client.getInputStream());
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                int type = dis.readInt();
                String content = dis.readUTF();
                if (type == Message.RECVFILE) {
                    String filePath = content.split(",")[0];
                    int len = Integer.parseInt(content.split(",")[1]);
//                    new FileSender().sendFile(filePath, 8821);
                    String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
                    System.out.println(filePath);
                    dos.flush();
                    System.out.println("Sending file...");

                    byte[] bufArray = new byte[1024];
                    DataInputStream fdis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
                    long passedlen = 0;
                    double percent = 0;
                    while (true) {
                        int read = 0;
                        if (fdis != null)
                            read = fdis.read(bufArray);
                        passedlen += read;
                        if (read == -1)
                            break;
                        dos.write(bufArray, 0, read);
                        dos.flush();
                        if (passedlen * 100 / len - percent >= 1) {
                            percent = passedlen * 100 / len;
                            System.out.println("文件发送了" + (passedlen * 100 / len) + "%");
                        }
                    }
                    fdis.close();
                }

                int jsonlen = dis.readInt();
                byte[] bytes = new byte[jsonlen];
                dis.read(bytes);
                JSONObject json = new JSONObject(new String(bytes));
                System.out.println(json.toString());
                dos.close();
                dis.close();
                Message message = JsonUtils.JsonToMessage(json);
                message.setRecv(0);
                Start.sqLiteUtils.addMessage(message);

                if (type != Message.RECVFILE) {
                    UsrInfo tempUsr = Start.usrList.usrInfoMap.get(message.getMAC());
                    Start.sqLiteUtils.addMessage(message);
                    if (Start.usrList.usrInfoList.contains(tempUsr))
                        Start.usrList.usrInfoList.remove(tempUsr);
                    Start.usrList.usrInfoList.add(0, tempUsr);
                    Start.frmList.refresh(Start.usrList.usrInfoList);

                    Message tempMessage;
                    if (Start.chatFormMap.containsKey(message.getMAC())) {
                        Start.chatFormMap.get(message.getMAC()).getMessage(message);
                        System.out.println("Add message to chatForm:" + message.getMAC());
                        if (type == Message.SENDFILE) {
                            //直接返回确认信息
                            tempMessage = message;
                            tempMessage.setType(Message.RECVFILE);
                            tempMessage.setMAC(HostInfo.getMac());
                            SocketClient socketClient = new SocketClient(client.getInetAddress().getHostAddress());
                            socketClient.SendJson(JsonUtils.MessageToJson(tempMessage));
                        }
                    } else {
                        if (type == Message.TEXT) {
                            Start.usrList.usrInfoMap.get(message.getMAC()).setUnread(1);
                        }
                        if (type == Message.SENDFILE) {
                            //文件直接接收，故显示未读即可
                            Start.usrList.usrInfoMap.get(message.getMAC()).setUnread(1);
                            //直接返回确认信息
                            tempMessage = message;
                            tempMessage.setType(Message.RECVFILE);
                            tempMessage.setMAC(HostInfo.getMac());
                            SocketClient socketClient = new SocketClient(client.getInetAddress().getHostAddress());
                            socketClient.SendJson(JsonUtils.MessageToJson(tempMessage));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

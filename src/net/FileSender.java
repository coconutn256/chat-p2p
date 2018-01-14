package net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileSender {
    private ServerSocket ss = null;

    public FileSender() {

    }

    public void sendFile(String filePath, int port) {
        DataOutputStream dos = null;
        DataInputStream dis = null;

        Socket socket = null;
        try {
            File file = new File(filePath);
            ss = new ServerSocket(port);
            socket = ss.accept();
            System.out.println("Sending " + file.getName() + " to " + socket.getInetAddress().getHostAddress());
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
            int buffferSize = 1024;
            byte[] bufArray = new byte[buffferSize];
            dos.writeUTF(file.getName());
            dos.flush();
            dos.writeLong(file.length());
            dos.flush();
            while (true) {
                int read = 0;
                if (dis != null) {
                    read = dis.read(bufArray);
                }

                if (read == -1) {
                    break;
                }
                dos.write(bufArray, 0, read);
            }
            dos.flush();

            System.out.println("Send file finished.");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            // 关闭所有连接
            try {
                if (dos != null)
                    dos.close();
            } catch (IOException e) {
            }
            try {
                if (dis != null)
                    dis.close();
            } catch (IOException e) {
            }
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
            }
            try {
                if (ss != null)
                    ss.close();
            } catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) {
        new FileSender().sendFile("D:\\Downloads\\system.web.dll", 8821);
    }
}
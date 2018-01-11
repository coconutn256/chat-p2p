package test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestFileRecv {
    public TestFileRecv() {

    }

    public void receiveFile(String savePath, String ip, int port) {
        Socket socket = null;
        try {
            socket = new Socket(ip, port);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new BufferedInputStream(socket
                    .getInputStream()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        int bufferSize = 1024;
        // 缓冲区
        byte[] buf = new byte[bufferSize];
        int passedlen = 0;
        long len = 0;
        // 获取文件名称
        try {
            savePath += dis.readUTF();
            DataOutputStream fileOut = new DataOutputStream(
                    new BufferedOutputStream(new BufferedOutputStream(
                            new FileOutputStream(savePath))));
            len = dis.readLong();
            System.out.println("文件的长度为:" + len + "KB");
            System.out.println("开始接收文件!");
            double percent = 0;
            while (true) {
                int read = 0;
                if (dis != null) {
                    read = dis.read(buf);
                }
                passedlen += read;
                if (read == -1) {
                    break;
                }
                if (passedlen * 100 / len - percent >= 1) {
                    percent = passedlen * 100 / len;
                    System.out.println("文件接收了" + (passedlen * 100 / len) + "%");
                }
                fileOut.write(buf, 0, read);
            }
            System.out.println("接收完成，文件存为" + savePath);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static void main(String[] args) {
        new TestFileRecv().receiveFile("D:\\", "localhost", 8821);
    }
}

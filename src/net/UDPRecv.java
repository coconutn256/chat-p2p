package net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPRecv {
    public static void Recv(String[] args) throws Exception {

        //创建udp服务对象，指定接收端口号为9999的报包
        DatagramSocket ds = new DatagramSocket(2014);

        //构造空的数据报包， 用于存储待会接收到的数据报包
        byte[] data = new byte[1024];
        DatagramPacket dp = new DatagramPacket(data, data.length);

        //接收数据
        ds.receive(dp);

        //显示数据
        String ipAddress = dp.getAddress().getHostAddress();
        int port = dp.getPort();
        String datas = new String(dp.getData(), 0, dp.getLength());
        System.out.println(ipAddress + "::" + datas + "::" + port);

        ds.close();
    }
}

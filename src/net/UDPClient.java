package net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static void SendOnline(String ip) throws Exception {
        //DatagramSocket类表示用来发送和接收(udp)数据报包的套接字。
        DatagramSocket ds = new DatagramSocket();
        //获取本机ip地址
        String localIp = HostInfo.getIp();
        //需要发送的数据
        String toSend = HostInfo.getMac() + ",1";
        byte[] buf = toSend.getBytes();
        DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(ip), 2014);
        //发送数据报包
        ds.send(dp);
        //关闭资源
        ds.close();
        System.out.println("Done.");
    }

    public static void SendOffline(String ip) throws Exception {
        //DatagramSocket类表示用来发送和接收(udp)数据报包的套接字。
        DatagramSocket ds = new DatagramSocket();
        //获取本机ip地址
        String localIp = HostInfo.getIp();
        //需要发送的数据
        String toSend = HostInfo.getMac() + ",0";
        byte[] buf = toSend.getBytes();
        DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(ip), 2014);
        //发送数据报包
        ds.send(dp);
        //关闭资源
        ds.close();
    }
}

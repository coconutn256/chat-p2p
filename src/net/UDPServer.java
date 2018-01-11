package net;

import main.Start;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.SplittableRandom;

public class UDPServer {
    public UDPServer() {
        try {
            init();
            while (true) {
                try {
                    byte[] buffer = new byte[1024 * 64]; // 缓冲区
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    receive(packet);
                    new Thread(new ServiceImpl(packet)).start();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DatagramPacket receive(DatagramPacket packet) throws Exception {
        try {
            datagramSocket.receive(packet);
            return packet;
        } catch (Exception e) {
            throw e;
        }
    }

    private void init() {
        try {
            socketAddress = new InetSocketAddress(HostInfo.getIp(), 2014);
            datagramSocket = new DatagramSocket(socketAddress);
            datagramSocket.setSoTimeout(5 * 1000);
            System.out.println("UDP服务端已经启动");
        } catch (Exception e) {
            datagramSocket = null;
            System.err.println("UDP服务端启动失败");
            e.printStackTrace();
        }
    }

    private static InetSocketAddress socketAddress = null; // 服务监听个地址
    private static DatagramSocket datagramSocket = null; // 连接对象

    class ServiceImpl implements Runnable {
        private DatagramPacket packet;

        private ServiceImpl(DatagramPacket packet) {
            this.packet = packet;
        }

        public void run() {
            try {
                String recv = new String(packet.getData());
                System.out.println(packet.getAddress().getHostAddress() + "," + packet.getPort() + "," + recv.split(",")[1]);
                if (Integer.parseInt(recv.split(",")[1].trim()) == 1) {
                    System.out.println(packet.getAddress().getHostAddress() + "," + packet.getPort() + "," + recv);
                    if (!Start.usrList.usrInfoMap.containsKey(recv.split(",")[0].trim())) {
                        Start.usrList.usrInfoMap.put(recv.split(",")[0].trim(), Start.sqLiteUtils.getUsrInfo(recv.split(",")[0].trim()));
                    }
                    Start.usrList.usrInfoMap.get(recv.split(",")[0].trim()).setState(1);
                    Start.usrList.usrInfoMap.get(recv.split(",")[0].trim()).setIP(packet.getAddress().getHostAddress());
                    Start.onlineTTL.put(recv.split(",")[0].trim(), 3);
                    String toSend = HostInfo.getMac() + ",0";
                    byte[] buf = toSend.getBytes();
                    //发送数据报包
                    packet.setData(buf);
                    datagramSocket.send(packet);
                } else if (Integer.parseInt(recv.split(",")[1].trim()) == 0) {
                    if (Start.usrList.usrInfoMap.containsKey(recv.split(",")[0].trim())) {
                        Start.usrList.usrInfoMap.get(recv.split(",")[0].trim()).setState(0);
                        System.out.println(packet.getAddress().getHostAddress() + " offline");
                    }
                }
                Start.frmList.refresh(Start.usrList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

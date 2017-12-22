package fram;

import model.Message;
import model.UsrInfo;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ChatForm extends JFrame {
    private TextField tf;
    private Button send;
    private Button log;
    private Button clear;
    private TextArea viewText;
    private TextArea sendText;
    private BufferedWriter bw;
    private UsrInfo usrInfo;

    public void getMessage(Message recmessage) {
        String message = recmessage.getContent(); //获取发送区域内容
        Time time = recmessage.getTime();             //获取当前时间
        String str = time.toString() + " 对我说\r\n" + message + "\r\n\r\n";  //alt + shift + l 抽取局部变量
        viewText.append(str);  //添加到显示区域
    }

    public ChatForm(UsrInfo usrInfo) {
        this.usrInfo = usrInfo;
        init();
        southPanel(usrInfo.getRemark());
        centerPanel(usrInfo.getRemark());
        event(usrInfo);
    }


    public void event(UsrInfo usrInfo) {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0); //关闭按钮
            }
        });

        sendText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        send(usrInfo.getRemark(), usrInfo.getIP());
                    } catch (IOException e1) {
                        e1.printStackTrace();//todo
                    }
                }
            }
        });

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    send(usrInfo.getRemark(), usrInfo.getIP());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //todo
            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewText.setText("");
            }
        });
    }

    private String getCurrentTime() {
        Date d = new Date();                        //创建当前日期对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return sdf.format(d);                       //将时间格式化
    }

    public void send(Message message, UsrInfo usrInfo) {
        //todo
    }

    public void send(String remark, String ip) throws IOException {
        String content = sendText.getText(); //获取发送区域内容
        Message sendmessage = new Message();
        sendmessage.setMAC(this.usrInfo.getMAC());
        sendmessage.setContent(content);
        sendmessage.setRecv(0);
        send(sendmessage, this.usrInfo);
        String time = getCurrentTime();
        String str = time + " 我对" + remark + "说\r\n" + content + "\r\n\r\n";  //alt + shift + l 抽取局部变量
        viewText.append(str);  //添加到显示区域
        sendText.setText("");  //清空发送区域
    }

    public void centerPanel(String name) {
        Panel center = new Panel();                 //创建中间的Panel
        viewText = new TextArea();
        sendText = new TextArea(5, 1);
        center.setLayout(new BorderLayout());       //设置为边界布局管理器
        center.add(sendText, BorderLayout.SOUTH);    //发送的文本区域放在南边
        center.add(viewText, BorderLayout.CENTER);   //显示区域放在中间
        viewText.setEditable(false);                //设置不可以编辑
        viewText.setBackground(Color.white);        //设置背景颜色
        sendText.setFont(new Font(name, Font.PLAIN, 15));
        viewText.setFont(new Font(name, Font.PLAIN, 15));
        this.add(center, BorderLayout.CENTER);
    }

    public void southPanel(String remark) {
        Panel south = new Panel();                  //创建南边的Panel
        tf = new TextField(15);
        tf.setText(remark);
        send = new Button("Send");
        log = new Button("Log");
        clear = new Button("Clear");
        south.add(tf);
        south.add(send);
        south.add(log);
        south.add(clear);
        this.add(south, BorderLayout.SOUTH);         //将Panel放在Frame的南边
    }

    public void init() {
        this.setLocation(500, 50);
        this.setSize(400, 600);

        try {
            bw = new BufferedWriter(new FileWriter("config.txt", true)); //需要在尾部追加
        } catch (Exception e) {

            e.printStackTrace();
        }
        this.setVisible(true);
    }
}

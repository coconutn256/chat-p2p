package fram;

import data.JsonUtils;
import data.SQLiteUtils;
import main.Start;
import model.Message;
import model.UsrInfo;
import net.HostInfo;
import net.SocketClient;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;

public class ChatForm extends JFrame {
    private TextField tf;
    private Button send;
    private Button log;
    private Button clear;
    private Button file;
    private TextArea viewText;
    private TextArea sendText;
    public UsrInfo usrInfo;


    public void getMessage(Message recmessage) {
        String content = recmessage.getContent(); //获取内容
        Timestamp time = recmessage.getTime();             //获取时间
        String str = "";
        if (recmessage.getRecv() == 0) {
            str = time.toString() + " 对我说\r\n" + content + "\r\n\r\n";  //alt + shift + l 抽取局部变量
        } else {
            str = time + " 我对" + this.usrInfo.getRemark() + "说\r\n" + content + "\r\n\r\n";
        }
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
                Start.chatFormMap.remove(usrInfo.getMAC());
                //System.exit(0); //关闭按钮
            }
        });

        sendText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
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
                List<Message> messageList = new ArrayList<>();
                try {
                    SQLiteUtils sqLiteUtils = Start.sqLiteUtils;
                    messageList = sqLiteUtils.getMessages(usrInfo.getMAC());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                for (int i = 0; i < messageList.size(); i++) {
                    getMessage(messageList.get(i));
                }
            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewText.setText("");
            }
        });

        file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //LocalFile localFile = new LocalFile();
                Message filemessage = new Message();
                filemessage.setType(Message.SENDFILE);
                filemessage.setContent("发送文件");
                filemessage.setMAC(usrInfo.getMAC());
                filemessage.setRecv(1);
                SocketClient socketClient = new SocketClient(usrInfo.getIP());
                JsonUtils jsonUtils = new JsonUtils();
                JSONObject Jmessage = JsonUtils.MessageToJson(filemessage);
                //socketClient.SendJson(Jmessage);
            }
        });
    }

    private String getCurrentTime() {
        Date d = new Date();                        //创建当前日期对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return sdf.format(d);                       //将时间格式化
    }

    public void send(String remark, String ip) {
        String content = sendText.getText(); //获取发送区域内容
        if (!content.isEmpty() && content.length() > 0) {
            Message sendmessage = new Message();
            sendmessage.setMAC(HostInfo.getMac());
            sendmessage.setContent(content);
            sendmessage.setRecv(1);
            sendmessage.setType(Message.TEXT);

            Date date = new Date();//获得系统时间.
            String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//将时间格式转换成符合Timestamp要求的格式.
            Timestamp goodsC_date = Timestamp.valueOf(nowTime);//把时间转换
            System.out.println(goodsC_date.toString());
            sendmessage.setTime(goodsC_date);
            SocketClient socketClient = new SocketClient(usrInfo.getIP());
            JsonUtils jsonUtils = new JsonUtils();
            JSONObject Jmessage = JsonUtils.MessageToJson(sendmessage);

            new Thread(
                    () -> {
                        socketClient.SendJson(Jmessage);
                    }
            ).start();
            String time = getCurrentTime();
            String str = time + " 我对" + remark + "说\r\n" + content + "\r\n\r\n";  //alt + shift + l 抽取局部变量
            viewText.append(str);  //添加到显示区域
            sendText.setText("");  //清空发送区域
        } else {
            tf.setText("发送消息不能为空");
        }
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
        file = new Button("File");
        clear = new Button("Clear");
        south.add(tf);
        south.add(send);
        south.add(log);
        south.add(file);
        south.add(clear);
        this.add(south, BorderLayout.SOUTH);         //将Panel放在Frame的南边
    }
    public void init() {
        this.setLocation(200, 100);
        this.setSize(650, 500);
        this.setVisible(true);
        this.setIconImage(new ImageIcon("resource/image/Icon.jpg").getImage());
        this.setTitle(usrInfo.getRemark());
    }
}

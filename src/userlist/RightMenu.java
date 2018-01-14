package userlist;

import model.UsrInfo;

import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.*;

import javax.swing.*;

public class RightMenu extends JPopupMenu {
    private UsrInfo usrInfo;

    public RightMenu(UsrInfo usrInfo) {
        this.usrInfo = usrInfo;
        this.setVisible(true);
        JMenuItem Tag = new JMenuItem();
        JMenuItem Black = new JMenuItem();
        JMenuItem Remark = new JMenuItem();
        Tag.setLabel("更改标签");
        Black.setLabel("拉黑");
        Remark.setLabel("更改备注");
        Tag.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JDialog jd = new JDialog();
                jd.setTitle("请输入标签");
                jd.setBounds(320, 180, 260, 100);
                JTextField tf = new JTextField(15);
                JButton button = new JButton("确定");
                jd.add(tf);
                jd.add(button);
                jd.setModal(true);
                jd.setVisible(true);
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            usrInfo.setTag(tf.getText());
                        }
                    }
                });
            }
        });

        Black.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usrInfo.setBlack(1);
                System.out.print(usrInfo.getIP());
                System.out.println(usrInfo.getBlack());
            }
        });
        Remark.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usrInfo.setRemark("");
            }
        });
        this.add(Tag);
        this.add(Black);
        this.add(Remark);
    }
}

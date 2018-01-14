package userlist;

import fram.ChatForm;
import main.Start;
import model.UsrInfo;
import model.UsrList;
import net.UDPClient;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


import javax.swing.*;


public class List extends CommonView implements KeyListener, MouseListener {

	private static final long serialVersionUID = 1101068431445178024L;
	JPanel backgroundPanel, bannerPanel, searchPanel, listPanel, footerPanel, scrollPanePanel;
	JButton closeButton;
	JLabel leftTopTitleLabel, avatarLabel, nickNameLabel, searchLabel;
	JTextField searchInput;
	JScrollPane listScrollPane;
    JLabel[] friendListLabel, friendListNicknameLabel, friendListStateLable;
	JPanel[] friendListPanel;
	private int currentFriendListPanelIndex = -1, lastClickFriendListPanelIndex = -1;
	private long lastUpdateGTimestamp = -1, lastClickFriendListPanelTimestamp = -1;

	public List(UsrList usrList) {
		/** main panel**/
		this.setTitle("快讯");
		this.backgroundPanel = new BackgroundPanel();
		this.backgroundPanel.setLayout(null);
		this.backgroundPanel.setPreferredSize(new Dimension(280, 700));
		this.add(this.backgroundPanel);

		/** banner **/
		this.bannerPanel = new JPanel();
		this.bannerPanel.setLayout(null);
		this.bannerPanel.setBounds(0, 0, 280, 110);
		this.bannerPanel.setPreferredSize(new Dimension(280, 110));
		this.bannerPanel.setOpaque(false);

		/** close button **/
		this.closeButton = new JButton(new ImageIcon("resource/image/close_press.png"));
		this.closeButton.setBounds(254, -4, 30, 30);
		this.closeButton.setContentAreaFilled(false);
		this.closeButton.setFocusPainted(false);
		this.closeButton.setBorderPainted(false);
		this.closeButton.setOpaque(false);
		this.closeButton.setBorder(null);
		this.closeButton.addMouseListener(this);
		this.bannerPanel.add(this.closeButton);

		/** leftTop label **/
		this.leftTopTitleLabel = new JLabel("快讯");
		this.leftTopTitleLabel.setForeground(new Color(215, 214, 218));
		this.leftTopTitleLabel.setBounds(10, -5, 60, 30);
		this.bannerPanel.add(this.leftTopTitleLabel);

		/** avatar label **/
		this.avatarLabel = new JLabel(new ImageIcon("resource/image/avatar_56_56.png"));
		this.avatarLabel.setBounds(10, 40, 56, 56);
		this.bannerPanel.add(this.avatarLabel);

		/** nickname label **/
		this.nickNameLabel = new JLabel("本机");
		this.nickNameLabel.setFont(new Font("谐体", Font.BOLD, 14));
		this.nickNameLabel.setBounds(70, 30, 60, 30);
		this.nickNameLabel.setForeground(Color.WHITE);
		this.bannerPanel.add(this.nickNameLabel);

        JButton norm = new JButton("正常");
        norm.setBounds(80, 80, 80, 20);
        norm.setVisible(true);
        this.bannerPanel.add(norm);
        norm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh(usrList);
            }
        });

        JButton black = new JButton("黑名单");
        black.setBounds(160, 80, 80, 20);
        black.setVisible(true);
        this.bannerPanel.add(black);
        black.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                black(usrList);
            }
        });

		this.backgroundPanel.add(this.bannerPanel);

		/** searchPanel **/
		this.searchPanel = new JPanel();
		this.searchPanel.setLayout(null);
		this.searchPanel.setBounds(0, 110, 280, 30);
		this.searchPanel.setOpaque(false);
		this.searchPanel.setBackground(Color.WHITE);

		this.searchInput = new JTextField();
		this.searchInput.setForeground(new Color(215, 214, 218));
		this.searchInput.setText("搜索联系人");
		this.searchInput.setBounds(4, 0, 280, 30);
		this.searchInput.setBorder(null);
		this.searchInput.setOpaque(false);
		this.searchInput.addMouseListener(this);
		this.searchInput.addKeyListener(this);
		this.searchInput.setMargin(new Insets(0, 10, 0, 0));
		this.searchPanel.add(this.searchInput);
		this.searchLabel = new JLabel(new ImageIcon("resource/image/find.png"));
		this.searchLabel.setBounds(250, 0, 30, 30);
		this.searchPanel.add(this.searchLabel);

		this.backgroundPanel.add(this.searchPanel);

		/** list panel **/
		this.listPanel = new JPanel();
		this.listPanel.setBounds(0, 140, 280, 500);
		this.listPanel.setLayout(null);
		this.listPanel.setOpaque(false);

		/** scrollpane **/
		this.listScrollPane = new JScrollPane();
		this.listScrollPane.setBorder(null);
		this.listScrollPane.setBounds(0, 0, 280, 500);
		this.listScrollPane.setBackground(new Color(1, 1, 1, 0.5f));
		this.listScrollPane.getViewport().setBackground(new Color(1, 1, 1, 0.5f));

		/** friendlist jlabel **/
		java.util.List<UsrInfo> usrInfoList = usrList.getRecentList();
		int friendListCount = usrInfoList.size();
		this.scrollPanePanel = new JPanel();
		this.scrollPanePanel.setPreferredSize(new Dimension(260, friendListCount * 50));
		this.scrollPanePanel.setOpaque(false);
		this.scrollPanePanel.setLayout(null);
		this.friendListLabel = new JLabel[friendListCount];
		this.friendListPanel = new JPanel[friendListCount];
		this.friendListNicknameLabel = new JLabel[friendListCount];
        this.friendListStateLable = new JLabel[friendListCount];
        String[] state = new String[]{"离线", "在线", "未读"};
		for (int i = 0; i < this.friendListLabel.length; i++) {
			this.friendListPanel[i] = new JPanel();
            if (usrInfoList.get(i).getRemark() != null)
                this.friendListNicknameLabel[i] = new JLabel(usrInfoList.get(i).getRemark());
            else
                this.friendListNicknameLabel[i] = new JLabel(usrInfoList.get(i).getIP());
			this.friendListNicknameLabel[i].setBounds(60, -10, 100, 50);
            this.friendListStateLable[i] = new JLabel(state[usrInfoList.get(i).getState()]);
            this.friendListStateLable[i].setBounds(200, -10, 100, 50);
            this.friendListPanel[i].addMouseListener(new FriendListPanelListener(i, usrInfoList.get(i), usrList.getRecentList()));
			this.friendListPanel[i].setOpaque(false);
			this.friendListPanel[i].setLayout(null);
			this.friendListPanel[i].setBounds(0, 50 * i, 280, 50);
			int top = 0;
			if (i == 0) {
				top = 1;
			}
			this.friendListPanel[i].setBorder(BorderFactory.createMatteBorder(top, 0, 1, 0, new Color(220, 219, 222)));
			int randomHeadIndex = (int) Math.ceil(Math.random() * 100);
			int index = 0;
			if (i + 1 > 260)
				index = i - 259;
			else
				index = i + 1;
			this.friendListLabel[i] = new JLabel(new ImageIcon("resource/image/head/" + index + ".png"));
			this.friendListLabel[i].setBounds(2, 0, 50, 50);
			this.friendListPanel[i].add(this.friendListNicknameLabel[i]);
            this.friendListPanel[i].add(this.friendListStateLable[i]);
			this.friendListPanel[i].add(this.friendListLabel[i]);
			this.scrollPanePanel.add(this.friendListPanel[i]);
			this.listScrollPane.setViewportView(this.scrollPanePanel);
			this.listScrollPane.addMouseWheelListener(new FriendListScrollPaneMouseWheelListener());
			this.listPanel.add(this.listScrollPane);
			this.backgroundPanel.add(this.listPanel);
		}

		/** footer panel **/
		this.footerPanel = new JPanel();
		this.footerPanel.setBackground(new Color(1, 1, 1, 0.95f));
		this.footerPanel.setBounds(0, 640, 280, 60);

		this.backgroundPanel.add(this.footerPanel);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.addMouseListener(this);
		int widthPos = screenSize.width / 4;
		if (widthPos < 300) widthPos = 300;
		this.setLocation(screenSize.width - (screenSize.width / 4), screenSize.height / 2 - 700 / 2);
		this.setUndecorated(true);
		this.setSize(new Dimension(280, 700));
		this.setResizable(false);
		this.setDragable(this);
		this.setVisible(true);
	}

    public void black(UsrList usrList) {
        java.util.List<UsrInfo> usrInfoList = usrList.getRecentList();
        int friendListCount = usrInfoList.size();
        java.util.List<UsrInfo> blackList = new ArrayList<UsrInfo>();
        for (int i = 0; i < friendListCount; i++) {
            if (usrInfoList.get(i).getBlack() == 1) {
                blackList.add(usrInfoList.get(i));
            }
        }

        int blackListCount = blackList.size();
        this.scrollPanePanel.removeAll();

        this.friendListLabel = new JLabel[blackListCount];
        this.friendListPanel = new JPanel[blackListCount];
        this.friendListNicknameLabel = new JLabel[blackListCount];
        this.friendListStateLable = new JLabel[blackListCount];
        String[] state = new String[]{"离线", "在线", "未读"};
        for (int i = 0; i < this.friendListLabel.length; i++) {
            this.friendListPanel[i] = new JPanel();
            if (usrInfoList.get(i).getRemark() != null)
                this.friendListNicknameLabel[i] = new JLabel(blackList.get(i).getRemark());
            else
                this.friendListNicknameLabel[i] = new JLabel(blackList.get(i).getRemark());
            this.friendListNicknameLabel[i].setBounds(60, -10, 100, 50);
            this.friendListStateLable[i] = new JLabel(state[blackList.get(i).getState()]);
            this.friendListStateLable[i].setBounds(200, -10, 100, 50);
            this.friendListPanel[i].addMouseListener(new FriendListPanelListener(i, blackList.get(i), usrList.getRecentList()));
            this.friendListPanel[i].setOpaque(false);
            this.friendListPanel[i].setLayout(null);
            this.friendListPanel[i].setBounds(0, 50 * i, 280, 50);
            int top = 0;
            if (i == 0) {
                top = 1;
            }
            this.friendListPanel[i].setBorder(BorderFactory.createMatteBorder(top, 0, 1, 0, new Color(220, 219, 222)));
            int randomHeadIndex = (int) Math.ceil(Math.random() * 100);
            int index = 0;
            if (i + 1 > 260)
                index = i - 259;
            else
                index = i + 1;
            this.friendListLabel[i] = new JLabel(new ImageIcon("resource/image/head/" + index + ".png"));
            this.friendListLabel[i].setBounds(2, 0, 50, 50);
            this.friendListPanel[i].add(this.friendListNicknameLabel[i]);
            this.friendListPanel[i].add(this.friendListStateLable[i]);
            this.friendListPanel[i].add(this.friendListLabel[i]);
            this.scrollPanePanel.add(this.friendListPanel[i]);
            this.scrollPanePanel.setOpaque(false);
            this.listScrollPane.setOpaque(false);
            this.listScrollPane.setViewportView(this.scrollPanePanel);
            this.listScrollPane.addMouseWheelListener(new FriendListScrollPaneMouseWheelListener());
            this.listPanel.add(this.listScrollPane);
            this.listPanel.setOpaque(false);
            this.backgroundPanel.add(this.listPanel);
            this.backgroundPanel.paint(getGraphics());
            List.this.updateG();
        }

    }

    public void refresh(UsrList usrList) {
        /** friendlist jlabel **/
        java.util.List<UsrInfo> usrInfoList = usrList.getRecentList();
        int friendListCount = usrInfoList.size();
        this.scrollPanePanel.removeAll();
        this.friendListLabel = new JLabel[friendListCount];
        this.friendListPanel = new JPanel[friendListCount];
        this.friendListNicknameLabel = new JLabel[friendListCount];
        this.friendListStateLable = new JLabel[friendListCount];
        String[] state = new String[]{"离线", "在线", "未读"};
        for (int i = 0; i < this.friendListLabel.length; i++) {
            this.friendListPanel[i] = new JPanel();
            if (usrInfoList.get(i).getRemark() != null)
                this.friendListNicknameLabel[i] = new JLabel(usrInfoList.get(i).getRemark());
            else
                this.friendListNicknameLabel[i] = new JLabel(usrInfoList.get(i).getIP());
            this.friendListNicknameLabel[i].setBounds(60, -10, 100, 50);
            this.friendListStateLable[i] = new JLabel(state[usrInfoList.get(i).getState()]);
            this.friendListStateLable[i].setBounds(200, -10, 100, 50);
            this.friendListPanel[i].addMouseListener(new FriendListPanelListener(i, usrInfoList.get(i), usrList.getRecentList()));
            this.friendListPanel[i].setOpaque(false);
            this.friendListPanel[i].setLayout(null);
            this.friendListPanel[i].setBounds(0, 50 * i, 280, 50);
            int top = 0;
            if (i == 0) {
                top = 1;
            }
            this.friendListPanel[i].setBorder(BorderFactory.createMatteBorder(top, 0, 1, 0, new Color(220, 219, 222)));
            int randomHeadIndex = (int) Math.ceil(Math.random() * 100);
            int index = 0;
            if (i + 1 > 260)
                index = i - 259;
            else
                index = i + 1;
            this.friendListLabel[i] = new JLabel(new ImageIcon("resource/image/head/" + index + ".png"));
            this.friendListLabel[i].setBounds(2, 0, 50, 50);
            this.friendListPanel[i].add(this.friendListNicknameLabel[i]);
            this.friendListPanel[i].add(this.friendListStateLable[i]);
            this.friendListPanel[i].add(this.friendListLabel[i]);
            this.scrollPanePanel.add(this.friendListPanel[i]);
            this.scrollPanePanel.setOpaque(false);
            this.listScrollPane.setOpaque(false);
            this.listScrollPane.setViewportView(this.scrollPanePanel);
            this.listScrollPane.addMouseWheelListener(new FriendListScrollPaneMouseWheelListener());
            this.listPanel.add(this.listScrollPane);
            this.listPanel.setOpaque(false);
            this.backgroundPanel.add(this.listPanel);
            this.backgroundPanel.paint(getGraphics());
        }
    }


    public void refresh(java.util.List<UsrInfo> usrList) {
        /** friendlist jlabel **/
        java.util.List<UsrInfo> usrInfoList = usrList;
        int friendListCount = usrInfoList.size();

        this.scrollPanePanel.removeAll();
        this.friendListLabel = new JLabel[friendListCount];
        this.friendListPanel = new JPanel[friendListCount];
        this.friendListNicknameLabel = new JLabel[friendListCount];
        this.friendListStateLable = new JLabel[friendListCount];
        String[] state = new String[]{"离线", "在线", "未读"};
        for (int i = 0; i < this.friendListLabel.length; i++) {
            this.friendListPanel[i] = new JPanel();
            if (usrInfoList.get(i).getRemark() == null)
                this.friendListNicknameLabel[i] = new JLabel(usrInfoList.get(i).getRemark());
            else
                this.friendListNicknameLabel[i] = new JLabel(usrInfoList.get(i).getIP());
            this.friendListNicknameLabel[i].setBounds(60, -10, 100, 50);
            this.friendListStateLable[i] = new JLabel(state[usrInfoList.get(i).getState()]);
            this.friendListStateLable[i].setBounds(200, -10, 100, 50);
            this.friendListPanel[i].addMouseListener(new FriendListPanelListener(i, usrInfoList.get(i), usrList));
            this.friendListPanel[i].setOpaque(false);
            this.friendListPanel[i].setLayout(null);
            this.friendListPanel[i].setBounds(0, 50 * i, 280, 50);
            int top = 0;
            if (i == 0) {
                top = 1;
            }
            this.friendListPanel[i].setBorder(BorderFactory.createMatteBorder(top, 0, 1, 0, new Color(220, 219, 222)));
            int randomHeadIndex = (int) Math.ceil(Math.random() * 100);
            int index = 0;
            if (i + 1 > 260)
                index = i - 259;
            else
                index = i + 1;
            this.friendListLabel[i] = new JLabel(new ImageIcon("resource/image/head/" + index + ".png"));
            this.friendListLabel[i].setBounds(2, 0, 50, 50);
            this.friendListPanel[i].add(this.friendListNicknameLabel[i]);
            this.friendListPanel[i].add(this.friendListStateLable[i]);
            this.friendListPanel[i].add(this.friendListLabel[i]);
            this.scrollPanePanel.add(this.friendListPanel[i]);
            this.scrollPanePanel.setOpaque(false);
            this.listScrollPane.setOpaque(false);
            this.listScrollPane.setViewportView(this.scrollPanePanel);
            this.listScrollPane.addMouseWheelListener(new FriendListScrollPaneMouseWheelListener());
            this.listPanel.add(this.listScrollPane);
            this.listPanel.setOpaque(false);
            this.backgroundPanel.add(this.listPanel);
            this.backgroundPanel.paint(getGraphics());
        }
    }

	class BackgroundPanel extends JPanel {
		private static final long serialVersionUID = -8637125902711760622L;

		public void paintComponent(Graphics g) {
			int x = 0, y = 0;
			ImageIcon icon = new ImageIcon("resource/image/list_bg.jpg");
			g.drawImage(icon.getImage(), x, y, getSize().width, getSize().height, this);
		}
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == this.closeButton) {
			/** close button event **/
			for (Map.Entry<String, UsrInfo> entry : Start.usrList.usrInfoMap.entrySet()) {
				try {
					UDPClient.SendOffline(entry.getValue().getIP());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			System.out.println("Close button event, system exit(0).");
			this.dispose();
			System.exit(0);
		} else if (e.getSource() == this.searchInput) {
			/** search input **/
			System.out.println("search input mouse clicked event");
			this.searchInput.setText("");
			this.searchInput.setForeground(Color.BLACK);
			this.searchInput.setOpaque(true);
			this.searchPanel.setOpaque(true);
			this.paint(this.getGraphics());
		} else {
			this.searchInput.setOpaque(false);
			this.searchInput.setForeground(new Color(215, 214, 218));
			this.searchInput.setText("搜索联系人");
			this.searchPanel.setOpaque(false);
			this.paint(this.getGraphics());
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {


	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.searchInput) {
			if (this.searchInput.getText().length() > 10) {
				e.setKeyChar('\0');
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void updateG() {
		if (this.lastUpdateGTimestamp == -1) {
			this.lastUpdateGTimestamp = new Date().getTime();
		}
		long now = new Date().getTime();
//		System.out.println("last udpateG timestamp:" + this.lastUpdateGTimestamp + " diff:" + (now - this.lastUpdateGTimestamp));
		if (now - this.lastUpdateGTimestamp > 50 || now - this.lastUpdateGTimestamp < 5) {
			this.lastUpdateGTimestamp = now;
			List.this.paint(List.this.getGraphics());
		}
	}

	class FriendListPanelListener extends MouseAdapter {

		/**
		 * For current the index is the user's id.
		 */
		private int index;
		private UsrInfo usrInfo;
        private java.util.List<UsrInfo> usrInfoList;

        public FriendListPanelListener(int index, UsrInfo usrInfo, java.util.List<UsrInfo> usrInfoList) {
			this.index = index;
			this.usrInfo = usrInfo;
            this.usrInfoList = usrInfoList;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (this.index == List.this.currentFriendListPanelIndex) {
				return;
			}
//			System.out.println("mouse entered the friendlist panel event.");
			JPanel jPanel = (JPanel) e.getSource();
			jPanel.setOpaque(true);
			jPanel.setBackground(new Color(249, 238, 194));
			List.this.updateG();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (this.index == List.this.currentFriendListPanelIndex) {
				return;
			}
//			System.out.println("mouse exited the friendlist panel event.");
			JPanel jPanel = (JPanel) e.getSource();
			jPanel.setOpaque(false);
			List.this.updateG();
		}

		public void mouseClicked(MouseEvent e) {
			long now = new Date().getTime();
            if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                /** double clicked event on the friendlist jpanel to open the chat window. **/
                if (!Start.chatFormMap.containsKey(usrInfo.getMAC())) {
                    ChatForm chatForm = new ChatForm(this.usrInfo, this.usrInfoList);
                    Start.chatFormMap.put(usrInfo.getMAC(), chatForm);
                    System.out.println("Open the chat window. The current uid is " + this.index);
				}
			}


            /** remember the lastClick info. **/
			List.this.lastClickFriendListPanelTimestamp = now;
			List.this.lastClickFriendListPanelIndex = this.index;

			/** set the last current panel to normal status. **/
			if (List.this.currentFriendListPanelIndex != -1) {
				List.this.friendListPanel[List.this.currentFriendListPanelIndex].setOpaque(false);
			}
			/** set current panel to clicked status **/
			List.this.currentFriendListPanelIndex = this.index;
			List.this.friendListPanel[this.index].setOpaque(true);
			List.this.friendListPanel[this.index].setBackground(new Color(250, 233, 172));
//			System.out.println("mouse clicked the friendlist panel event.");
			List.this.updateG();
            //右键菜单栏
            if (e.getButton() == MouseEvent.BUTTON3) {
                System.out.println("Right Clicked.");
                JPopupMenu popupMenu = new JPopupMenu();
                JMenuItem menuItem = new JMenuItem();
                JMenuItem menuItem1 = new JMenuItem();
                JMenuItem menuItem2 = new JMenuItem();
                menuItem.setLabel("修改备注");
                menuItem1.setLabel("修改标签");
                if (this.usrInfo.getBlack() == 0)
                    menuItem2.setLabel("拉黑此人");
                else
                    menuItem2.setLabel("取消拉黑");

                menuItem.setActionCommand("remark");
                menuItem.setActionCommand("tag");
                menuItem.setActionCommand("black");
                menuItem.setEnabled(true);
                menuItem1.setEnabled(true);
                menuItem2.setEnabled(true);

                menuItem2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        usrInfo.setBlack(1 - usrInfo.getBlack());
                        System.out.println("SetBlack to " + usrInfo.getBlack() + ".");
                        Start.usrList.usrInfoMap.get(usrInfo.getMAC()).setBlack(1 - usrInfo.getBlack());
                        if (usrInfo.getBlack() == 0)
                            menuItem2.setLabel("拉黑此人");
                        else
                            menuItem2.setLabel("取消拉黑");

                    }
                });

                popupMenu.add(menuItem);
                popupMenu.add(menuItem1);
                popupMenu.add(menuItem2);
                popupMenu.setVisible(true);
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }

		}

	}

	class FriendListScrollPaneMouseWheelListener implements MouseWheelListener {
		public void mouseWheelMoved(MouseWheelEvent e) {
		}
	}

	
}
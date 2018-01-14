package model;

import data.SQLiteUtils;

import java.io.IOException;
import java.sql.ResultSet;

public class UsrInfo {
    private String MAC;
    private String IP;
    private String Remark;
    private String Tag;
    private int Black;
    private int state;
    private int unread;
    public static final int OFFLINE = 0;
    public static final int ONLINE = 1;
    public static final int UNREAD_TEXT = 2;
    public static final int UNRECV_FILE = 3;

    public UsrInfo() {
        Remark = null;
        Tag = null;
        Black = 0;
        state = 0;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getState() {
        return state;
    }

    public String getMAC() {
        return MAC;
    }

    public String getRemark() {
        return Remark;
    }

    public String getTag() {
        return Tag;
    }

    public int getBlack() {
        return Black;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public void setBlack(int black) {
        Black = black;
    }

    public int getPriority() {
        int result = 0;
        if (state >= 2)
            result += 1000;
        if (Remark != null && Remark.length() != 0)
            result += 100;
        if (Tag != null && Tag.length() != 0)
            result += 10;
        if (Black == 1)
            result = -2;
        return result;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

}

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
    private boolean state;
    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
    public boolean getState() {
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

    public void setState(boolean state) {
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
}

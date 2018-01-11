package model;

import data.SQLiteUtils;

import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;

public class Message {
    private String MAC;
    private int Recv;
    private int Type;
    private String Content;
    private Timestamp time;

    public static final int NOT_MESSAGE = -1;
    public static final int TEXT = 0;
    public static final int SENDFILE = 1;
    public static final int RECVFILE = 2;

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public int getRecv() {
        return Recv;
    }

    public void setRecv(int recv) {
        Recv = recv;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}

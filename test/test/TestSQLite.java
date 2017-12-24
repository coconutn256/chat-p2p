package src;

import data.SQLiteUtils;

import java.io.IOException;

public class TestSQLite {
    public static void main(String args[]){
        try {
            SQLiteUtils sqLiteUtils = new SQLiteUtils();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

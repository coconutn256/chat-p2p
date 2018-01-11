package model;

import data.SQLiteUtils;

import java.io.IOException;
import java.util.*;

public class UsrList {
    public Map<String, UsrInfo> usrInfoMap;
    public SQLiteUtils sqLiteUtils;
    public List<UsrInfo> usrInfoList = new ArrayList<UsrInfo>();
    public List<UsrInfo> blackList = new ArrayList<UsrInfo>();

    public UsrList() {
        usrInfoMap = new HashMap<String, UsrInfo>();
        sqLiteUtils = new SQLiteUtils();
    }

    public List<UsrInfo> getRecentList() {
        usrInfoList.clear();
        for (Map.Entry<String, UsrInfo> entry : usrInfoMap.entrySet()) {
            usrInfoList.add(entry.getValue());
        }
        ComparatorRecent comparatorRecent = new ComparatorRecent();
        Collections.sort(usrInfoList, comparatorRecent);
        return usrInfoList;
    }

    public List<UsrInfo> getGroupList() {
        //TODO:
        return usrInfoList;
    }

    public List<UsrInfo> getBlackList() {
        blackList.clear();
        for (Map.Entry<String, UsrInfo> entry : usrInfoMap.entrySet()) {
            if (entry.getValue().getBlack() == 1)
                usrInfoList.add(entry.getValue());
        }
        ComparatorRecent comparatorRecent = new ComparatorRecent();
        Collections.sort(blackList, comparatorRecent);
        return blackList;
    }

    class ComparatorRecent implements Comparator {
        public int compare(Object arg0, Object arg1) {
            UsrInfo usr1 = (UsrInfo) arg0;
            UsrInfo usr2 = (UsrInfo) arg1;
            return usr2.getPriority() - usr1.getPriority();
        }
    }
}

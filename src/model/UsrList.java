package model;

import java.util.*;

public class UsrList {
    public Map<String,UsrInfo> usrInfoMap;
    public UsrList(){
        usrInfoMap = new HashMap<String,UsrInfo>();
    }

    public List<UsrInfo> getRecentList(){
        List<UsrInfo> usrInfoList = new ArrayList<UsrInfo>();
        for (Map.Entry<String, UsrInfo> entry : usrInfoMap.entrySet()) {
            usrInfoList.add(entry.getValue());
        }
        //TODO:排序
        return usrInfoList;
    }
    public List<UsrInfo> getGroupList(){
        List<UsrInfo> usrInfoList = new ArrayList<UsrInfo>();
        //TODO:
        return usrInfoList;
    }
    public List<UsrInfo> getBlackList(){
        List<UsrInfo> usrInfoList = new ArrayList<UsrInfo>();
        //TODO:
        return usrInfoList;
    }
}

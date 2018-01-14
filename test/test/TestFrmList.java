package test;

import model.UsrInfo;
import model.UsrList;
import userlist.List;

public class TestFrmList {
    public static void main(String[] args) {
        UsrList usrList = new UsrList();
        UsrInfo usrInfo = new UsrInfo();
        usrInfo.setMAC("ssss");
        usrInfo.setIP("10.128.171.157");
        usrInfo.setState(2);
        usrInfo.setRemark("王宁");
        usrInfo.setTag("wa");
        usrList.usrInfoMap.put("ssss", usrInfo);
        List list = new List(usrList);
    }
}

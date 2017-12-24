package src;

import net.HostInfo;

import java.util.List;
import java.util.Map;

public class TestHostInfo {
    public static void main(String[] args){
        System.out.println(HostInfo.getHostName()+'\n'+HostInfo.getIp()
                +'\n'+HostInfo.getMac());
        Map<String,String> list = HostInfo.getMacIp();
        for(Map.Entry<String,String> entry:list.entrySet())
            System.out.println(entry.getValue()+" "+entry.getKey());
    }
}

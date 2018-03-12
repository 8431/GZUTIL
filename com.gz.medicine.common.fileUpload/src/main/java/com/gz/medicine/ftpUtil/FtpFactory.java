package com.gz.medicine.ftpUtil;

import com.gz.medicine.common.util.PropertyUtil;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/18 0018.
 */

/**
 * 后期加入缓存池
 */
public class FtpFactory {
    public static final List<Ftp> FTPCASH=new ArrayList<Ftp>();

    public static Ftp getFtp(){
            String ip=PropertyUtil.getPropery("ftp.ip");
            String port=PropertyUtil.getPropery("ftp.port");
            String user=PropertyUtil.getPropery("ftp.user");
            String pass=PropertyUtil.getPropery("ftp.pass");
            Ftp f=new Ftp(ip,Integer.parseInt(port),user,pass);
          return f;
    }
}

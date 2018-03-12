package com.gz.medicine.ftpUtil;

import com.gz.medicine.common.util.PropertyUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/18 0018.
 */

/**
 * 后期加入缓存池
 */
public class FtpClientFactory implements PoolableObjectFactory {
    public static final Logger logger = Logger.getLogger(FTPClientPool.class);
    private static  FtpClientFactory fc=null;
   private  FtpClientFactory(){

   }
   public static FtpClientFactory getFtpClientFactory(){
       if(fc==null){
           fc=new FtpClientFactory();

       }
       return fc;
   }

    @Override
    public Object makeObject() throws Exception {
        String ip = PropertyUtil.getPropery("ftp.ip");
        String port = PropertyUtil.getPropery("ftp.port");
        String user = PropertyUtil.getPropery("ftp.user");
        String pass = PropertyUtil.getPropery("ftp.pass");
        Ftp f = new Ftp(ip, Integer.parseInt(port), user, pass);
        return f;
    }

    @Override
    public void destroyObject(Object ftp) throws Exception {
        Ftp f= (Ftp) ftp;
        f.ftpLogOut();
    }

    @Override
    public boolean validateObject(Object ftp) {
        Ftp f= (Ftp) ftp;
        FTPClient ftpClient= f.getFtpClient();
        f.ftpLogin();
        try {
            return ftpClient.sendNoOp();
        } catch (IOException e) {
            throw new RuntimeException("Failed to validate client: " + e, e);
        }
    }

    @Override
    public void activateObject(Object o) throws Exception {

    }

    @Override
    public void passivateObject(Object o) throws Exception {

    }
}

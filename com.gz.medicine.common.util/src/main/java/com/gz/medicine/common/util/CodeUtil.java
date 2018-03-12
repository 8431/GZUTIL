package com.gz.medicine.common.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/7/24 0024.
 */
public class CodeUtil {
    public static void main(String[] args) throws Base64Exception, NoSuchAlgorithmException {
//        String md5Str = DigestUtils.md5Hex("ssss");
//        System.out.println("MD5-->" + md5Str);
//        String base64Str = Base64.encode("sss".getBytes());
//        byte[] ss = Base64.decode(base64Str);
//        String s=new String(ss);
//        System.out.println(  s);
//
//        String str="中文123abc,./";
//        System.out.println("原始字符串为："+str);
//
////MD5加密
//        String md5Str2=DigestUtils.md5Hex(str);
//        System.out.println("MD5加密为："+md5Str2);
//
////SHA1加密
//        String sha1Str=DigestUtils.shaHex(str);
//        System.out.println("Sha1加密为："+sha1Str);
        MessageDigest  mg=MessageDigest.getInstance("MD5");
        mg.update("16899199".getBytes());
        byte[] digesta1=mg.digest();
        System.out.println("本信息摘要是:"+byte2hex(digesta1));

        MessageDigest  mg2=MessageDigest.getInstance("MD5");
        mg2.update("16899199".getBytes());
        byte[] digesta2=mg2.digest();
        System.out.println("本信息摘要是:"+byte2hex(digesta2));
        if (mg.isEqual(digesta1,digesta2)) {
            System.out.println("信息检查正常");
        }else{
            System.out.println("信息检查不正常");
        }






    }
    public static String byte2hex(byte[] b) //二行制转字符串
    {
        String hs="";
        String stmp="";
        for (int n=0;n<b.length;n++)
        {
            stmp=(java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length()==1) hs=hs+"0"+stmp;
            else hs=hs+stmp;
            if (n<b.length-1)  hs=hs+":";
        }
        return hs.toUpperCase();
    }

}

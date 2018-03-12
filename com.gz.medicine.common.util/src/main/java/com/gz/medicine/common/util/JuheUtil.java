package com.gz.medicine.common.util;

import com.google.gson.Gson;
import com.gz.medicine.common.exception.CommonException;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/9 0009.
 */
public class JuheUtil {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    //1.身份证信息查询
    public static SimpleResult getIdcardMsg(String idcard) throws CommonException {
        String appkey=PropertyUtil.getPropery("juhe.appKey");
        String url=PropertyUtil.getPropery("juhe.indexUrl");
        SimpleResult sr =null;
        Map params = new HashMap();//请求参数
        params.put("cardno",idcard);//身份证号码
        params.put("dtype","");//返回数据格式：json或xml,默认json
        params.put("key",appkey);//你申请的key
        try {
           String result =net(url, params, "GET");
            JSONObject object = JSONObject.fromObject(result);
            if(object.getInt("error_code")==0){
                sr=SimpleResult.success();
                sr.put("data",object.get("result"));
                sr.put("message",object.get("error_code")+":"+object.get("reason"));
            }else{
                sr= SimpleResult.error(SimpleCode.ERROR.getCode(),object.get("error_code")+":"+object.get("reason"));
            }
        } catch (Exception e) {
            sr= SimpleResult.error(SimpleCode.ERROR.getCode(),"调用身份证查询接口异常:"+e.getMessage());
        }
        return sr;
    }

    //2.身份证泄漏查询
//    public static void getRequest2(){
//        String result =null;
//        String url ="http://apis.juhe.cn/idcard/leak";//请求接口地址
//        Map params = new HashMap();//请求参数
//        params.put("cardno","");//身份证号码
//        params.put("dtype","");//返回数据格式：json或xml,默认json
//        params.put("key",APPKEY);//你申请的key
//
//        try {
//            result =net(url, params, "GET");
//            JSONObject object = JSONObject.fromObject(result);
//            if(object.getInt("error_code")==0){
//                System.out.println(object.get("result"));
//            }else{
//                System.out.println(object.get("error_code")+":"+object.get("reason"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    //3.身份证挂失查询
//    public static void getRequest3(){
//        String result =null;
//        String url ="http://apis.juhe.cn/idcard/loss";//请求接口地址
//        Map params = new HashMap();//请求参数
//        params.put("cardno","");//身份证号码
//        params.put("dtype","");//返回数据格式：json或xml,默认json
//        params.put("key",APPKEY);//你申请的key
//
//        try {
//            result =net(url, params, "GET");
//            JSONObject object = JSONObject.fromObject(result);
//            if(object.getInt("error_code")==0){
//                System.out.println(object.get("result"));
//            }else{
//                System.out.println(object.get("error_code")+":"+object.get("reason"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String,Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}

package com.gz.medicine.common.wechatPay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gz.medicine.common.util.SimpleResult;


/**
 * 
 * @Title: WeixinPay.java 
 * @Package com.gz.medicine.common.wechatPay 
 * @Description: 微信支付  暂停开发
 * @Author fendo
 * @Date 2017年8月10日 下午2:01:22 
 * @Version V1.0
 */

public class WeixinPay {

	public static String key="UNBcVGKSWnXa411pkmfqq6whxv9IZ45G";
	
	public static final Logger LOGGER = Logger.getLogger(WeixinPay.class);

	
	public SimpleResult WechatPay(String orderBody,String orderId,String price,String spBillid) {
		
		String data=null;
		String response=null;
		
		SimpleResult simpleResult=null;
		
		//---------------------------------------
		String url="https://api.mch.weixin.qq.com/pay/unifiedorder";
		String appid="wxaa4d20511f1df045";//应用ID
		String mch_id="1483590632";// 商户号
		String nonce_str=""; //随机码
		String sign="";//签名
		String body=orderBody;//商品描述
		String out_trade_no=orderId; //商户订单号
		String total_fee=price; // 金额
		String spbill_create_ip=spBillid; //终端ip
		String notify_url="https://gz.fromfuture.cn/chis/ROOT_CHIS/GZJKinterface.sp";//通知地址
		String trade_type="APP"; //交易类型

		
		String currTime = getCurrTime();  
		
        //8位日期  
        String strTime = currTime.substring(8, currTime.length());  
		
        //四位随机数  
        String strRandom = buildRandom(4) + ""; 
        
        
        //10位随机码
        nonce_str = strTime + strRandom;  
        
//        String datastr="appid="+appid+"&body="+body+"&fee_type=CNY&mch_id="+mch_id+"&nonce_str="+nonce_str+"&notify_url="+notify_url+"&out_trade_no="+out_trade_no+"&spbill_create_ip="+spbill_create_ip+"&total_fee="+total_fee+"&trade_type="+trade_type;
//        
//        datastr=datastr+"&key="+key;

        String characterEncoding = "UTF-8"; 
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();  
        parameters.put("appid", appid);  
        parameters.put("mch_id", mch_id);  
        parameters.put("fee_type", "CNY");  
        parameters.put("mch_id", mch_id);  
        parameters.put("nonce_str", nonce_str); 
        parameters.put("notify_url", notify_url);
        parameters.put("out_trade_no", out_trade_no);
        
        parameters.put("spbill_create_ip", spbill_create_ip);
        parameters.put("total_fee", total_fee);
        parameters.put("trade_type", trade_type);
        
        
        String Sign = createSign(characterEncoding,parameters);
        
		data="<xml>"+
				   "<appid>"+appid+"</appid>"+
				   "<mch_id>"+mch_id+"</mch_id>"+
				   "<nonce_str>"+nonce_str+"</nonce_str>"+
				   "<body>"+body+"</body>"+
				   "<out_trade_no>"+out_trade_no+"</out_trade_no>"+
				   "<total_fee>"+total_fee+"</total_fee>"+
				   "<fee_type>CNY</fee_type>"+
				   "<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
				   "<notify_url>"+notify_url+"</notify_url>"+
				   "<trade_type>"+trade_type+"</trade_type>"+
				   "<sign>"+sign+"</sign>"+
			  "</xml>";
		
		
		LOGGER.info("发送给微信的报文：" + data);
		LOGGER.info("加密后的的签名字符串：" + sign);
        
		
		try {
			response=sendPost(url,data);
			
			LOGGER.info("请求/pay/unifiedorder下单接口后返回数据：" + response);
	        
	        JSONObject jsonObject = JSONObject.parseObject(XmltoJsonUtil.xml2JSON(response)) ;    
	        JSONObject result_xml = jsonObject.getJSONObject("xml");  
	        JSONArray result_code =  result_xml.getJSONArray("result_code"); 
			
	        String code = (String)result_code.get(0);  
	        
	        List<String> datas = new ArrayList<String>();   
	        
	        
	        if(code.equalsIgnoreCase("FAIL")){  
	        	
	        	simpleResult=new SimpleResult("001", "支付失败!!");
	        	return simpleResult;
	        	
	        }else if(code.equalsIgnoreCase("SUCCESS")){  
	        	
	        	
	            JSONArray prepay_id = result_xml.getJSONArray("prepay_id");               
	            String prepayId = (String)prepay_id.get(0);  

	            JSONArray return_msg= result_xml.getJSONArray("return_msg");       
	            String returnmsg = (String)return_msg.get(0); 
	            
	            JSONArray app_id= result_xml.getJSONArray("appid");       
	            String appids = (String)app_id.get(0); 
	            
	            JSONArray mch_ids= result_xml.getJSONArray("mch_id");       
	            String mchids = (String)mch_ids.get(0); 	            

	            JSONArray nonce_strs= result_xml.getJSONArray("nonce_str");       
	            String noncestrs = (String)nonce_strs.get(0); 
	            
	            JSONArray sign_msg= result_xml.getJSONArray("sign");       
	            String signs = (String)sign_msg.get(0); 
	            
	            JSONArray result_codes= result_xml.getJSONArray("result_code");       
	            String resultcodes = (String)result_codes.get(0); 
	            
	            JSONArray trade_types= result_xml.getJSONArray("trade_type");       
	            String tradetypes = (String)trade_types.get(0); 	            
	            
	            
	            
	            
	        	simpleResult=new SimpleResult("000", "支付成功!!");
	        	return simpleResult;
	        }             

		} catch (Exception e) {
        	simpleResult=new SimpleResult("001", "支付错误!!");
        	return simpleResult;
		}
		
    	simpleResult=new SimpleResult("001", "支付错误!!");
    	simpleResult.put("data", response);
    	return simpleResult;
	}
	
	
	/**
	 * 获取当前时间 yyyyMMddHHmmss
	 * @return String
	 */ 
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}
	
	
	/**
	 * 取出一个指定长度大小的随机正整数.
	 * 
	 * @param length
	 *            int 设定所取出随机数的长度。length小于11
	 * @return int 返回生成的随机数。
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}
	
	
    /** 
     * 微信支付签名算法sign 
     * @param characterEncoding 
     * @param parameters 
     * @return 
     */  
    @SuppressWarnings("unchecked")  
    public static String createSign(String characterEncoding,SortedMap<Object,Object> parameters){  
        StringBuffer sb = new StringBuffer();  
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）  
        Iterator it = es.iterator();  
        while(it.hasNext()) {  
            Map.Entry entry = (Map.Entry)it.next();  
            String k = (String)entry.getKey();  
            Object v = entry.getValue();  
            if(null != v && !"".equals(v)   
                    && !"sign".equals(k) && !"key".equals(k)) {  
                sb.append(k + "=" + v + "&");  
            }  
        }  
        sb.append("key=" + key);  
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();  
        return sign;  
    }  
    
    
    public String sendPost(String url,String params) {
        String result = "";// 返回的结果  
        BufferedReader in = null;// 读取响应输入流  
        PrintWriter out = null; 
        try {
            // 创建URL对象  
            java.net.URL connURL = new java.net.URL(url);  
            // 打开URL连接  
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL  
                    .openConnection();  
            // 设置通用属性  
            httpConn.setRequestProperty("Accept", "*/*");  
            httpConn.setRequestProperty("Connection", "Keep-Alive");  
            httpConn.setRequestProperty("User-Agent",  
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            // 设置超时
            httpConn.setConnectTimeout(30000);
            httpConn.setReadTimeout(30000);
            // 设置POST方式  
            httpConn.setDoInput(true);  
            httpConn.setDoOutput(true);  
            // 获取HttpURLConnection对象对应的输出流  
            out = new PrintWriter(httpConn.getOutputStream());  
            // 发送请求参数  
            out.write(params);  
            // flush输出流的缓冲  
            out.flush();  
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式  
            in = new BufferedReader(new InputStreamReader(httpConn  
                    .getInputStream(), "UTF-8"));  
            String line;  
            // 读取返回的内容  
            while ((line = in.readLine()) != null) {  
                result += line;  
            }  
		} catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (in != null) {  
                    in.close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        } 
        return result;  
    }
    

        

}

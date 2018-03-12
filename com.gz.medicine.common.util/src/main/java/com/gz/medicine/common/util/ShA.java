package com.gz.medicine.common.util;

/**   
* File Name: ShA.java 
* Package: com.enviveus.test
* Date: 2017年2月22日 上午10:47:33 
* Version: V1.0   
* Copyright (c) 2016, enviveus.com All Rights Reserved. 
*/

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author: zhangkun@enviveus.com
 * @Date: 2017年2月22日 上午10:47:33
 */
public class ShA {
	public static String HMACSHA256(String originStr, String keyStr) {
        byte[] data = originStr.getBytes();
        byte[] key  = keyStr.getBytes();
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);
			return byte2hex(mac.doFinal(data));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append('0');
			hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}
	
	
	public static void main(String[] args) {
		System.out.println(MD5.MD5Encode(HMACSHA256("abc", "123").toLowerCase()));
	}
	

}

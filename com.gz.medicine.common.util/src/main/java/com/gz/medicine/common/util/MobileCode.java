package com.gz.medicine.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0
 * @Author fendo
 * @ClassName MobileCode
 * @PackageName com.gz.medicine.common.util
 * @Description 短信发送
 * @Data 2017-08-21 10:08
 **/
public class MobileCode {


    public static final Logger LOGGER = Logger.getLogger(MobileCode.class);


    /**
     *
     * @author fendo
     * @Describe 根据手机号发送自定义短信内容
     * @param mobile,content
     * @return
     * @throws Exception
     */
    public static Boolean sendAuthCode(String mobile,String content) throws Exception {

        if (StringUtils.isNotEmpty(mobile) && StringUtils.isNotEmpty(content)) {

            if (RegexUtils.isMobile(mobile)) {
                // 短信验证返回内容
                String restResource = null;
                // 请求URL
                String url = "http://sms.51sxun.com/sms.aspx";
                // 实例化一个请求参数Map
                Map<String, String> params = new HashMap<String, String>();
                // 验证手机
                params.put("extno", "");
                params.put("sendTime", "");
                params.put("content", content);
                params.put("mobile", mobile);
                params.put("account", "gzjk");
                params.put("password", "gzjk0805");
                params.put("userid", "318");
                params.put("action", "send");
                restResource = HttpRequest.sendPost(url, params);
                SAXReader reader = new SAXReader();
                Document document = DocumentHelper.parseText(restResource);
                Element root = document.getRootElement();
                String returnstatus = root.element("returnstatus").getText();
                if ("Success".equals(returnstatus)) {
                    LOGGER.error("短信发送成功!!");
                    return true ;
                } else {
                    LOGGER.error("短信发送失败!!");
                    return false;
                }

            }else {
                LOGGER.error("手机号码错误!!");
                return false;
            }

        } else {
            LOGGER.error("手机号与要发送的内容不能为空!!");
            return false;
        }

    }
}

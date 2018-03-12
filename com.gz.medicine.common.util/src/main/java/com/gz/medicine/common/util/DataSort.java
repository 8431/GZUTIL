/**
 * projectName: GZYUN
 * fileName: DataSort.java
 * packageName: com.gz.medicine.common.util
 * date: 2017-12-25 15:46
 * copyright(c) 2017-2020 xxx公司
 */
package com.gz.medicine.common.util;

import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * @version: V1.0
 * @author: fendo
 * @className: DataSort
 * @packageName: com.gz.medicine.common.util
 * @description: 时间排序
 * @data: 2017-12-25 15:46
 **/
public class DataSort implements Comparator {


    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public int compare(Object obj1, Object obj2) {
        JSONObject t1 = (JSONObject) obj1;
        JSONObject t2 = (JSONObject) obj2;
        Date d1, d2;
        try {
            d1 = format.parse(t1.get("InspectDate").toString());
            d2 = format.parse(t2.get("InspectDate").toString());
        } catch (Exception e) {
            // 解析出错，则不进行排序
            return 0;
        }
        if (d1.before(d2)) {
            return 1;
        } else {
            return -1;
        }
    }
}

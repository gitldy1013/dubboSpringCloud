package org.cmcc.service.sftp.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhaolei
 * @date 2021-01-05 15:17
 */
@Slf4j
public class TimeUtils {

    /**
     * 获取某一日期前后num天的日期字符串
     */
    public static String getDateBefore(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, num);
        Date time = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(time);
    }

    /**
     * 获取某一日期的字符串+1天的字符串格式
     */
    public static String getDateStr(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dt = null;
        try {
            Date date = sdf.parse(dateStr);
            dt = getDateBefore(date, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static String getCurrDateStr() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public static String getTimeStr(String time) {
        SimpleDateFormat df = new SimpleDateFormat("HHmmss");
        SimpleDateFormat dfRes = new SimpleDateFormat("HH:mm:ss");
        try {
            Date parse = df.parse(time);
            return dfRes.format(parse);
        } catch (ParseException e) {
            log.error(e.getMessage());
            return "数据库时间格式有误：" + e.getMessage();
        }
    }

    /*public static void main(String[] args) {
        String dateBefore = getDateBefore(new Date(), -1);
        System.out.println(dateBefore);
        String dateStr = getDateStr("20210127");
        System.out.println(dateStr);
    }*/


}

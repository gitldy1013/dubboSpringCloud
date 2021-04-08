package org.cmcc.service.hnzsh.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class TimeUtils {
    public static String getYestDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(calendar.getTime());
    }

    public static String formatAmt(String amt) {
        double v = Double.parseDouble(amt);
        return new DecimalFormat("0.00").format(v);
    }

    public static String getDateStr(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

    /**
     * 获取某一日期前后num天的日期字符串
     */
    public static String getDateBefore(Date date, int num, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, num);
        String dateStr = getDateStr(calendar.getTime(), pattern);
        return dateStr;
    }

    public static String getTimeStr(String time) {
        Date parse = getDate(time);
        SimpleDateFormat dfRes = new SimpleDateFormat("HH:mm:ss");
        return dfRes.format(parse);
    }

    public static Date getDate(String time) {
        SimpleDateFormat df = new SimpleDateFormat("HHmmss");
        try {
            return df.parse(time);
        } catch (ParseException e) {
            log.error(e.getMessage());
            return null;
        }
    }


    /**
     * 将日期串转成指定格式的日期串
     */
    public static String getPatternDateStr(String date, String beforePattern, String afterPattern) {
        SimpleDateFormat beforeFormat = new SimpleDateFormat(beforePattern);
        SimpleDateFormat afterFormat = new SimpleDateFormat(afterPattern);
        try {
            Date parseDate = beforeFormat.parse(date);
            String dateStr = afterFormat.format(parseDate);
            return dateStr;
        } catch (ParseException e) {
            log.error("======日期转换失败");
        }
        return "";
    }

    public static void main(String[] args) {
//        System.out.println(getDateStr(new Date(),"yyyy年MM月dd日"));
//        System.out.println(getDateStr(new Date(),"yyyyMM"));
//        System.out.println(getDateBefore(new Date(),-1,"yyyy年MM月dd日"));
        System.out.println(getPatternDateStr("20210101", "yyyyMMdd", "yyyy年MM月dd日"));
        System.out.println("20210101".substring(0, 6));
    }

}

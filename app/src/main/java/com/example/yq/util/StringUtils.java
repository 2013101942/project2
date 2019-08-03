package com.example.yq.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {
    /**
     * 判断字符串是否为null和空串，有空格也算
     * @param str 要判断的字符串
     * @return boolean 是否为空的字符串 true代表为空， false代表不为空
     */
    public static boolean isEmpty(String str) {
        if(str == null || str.equals("") || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否不为null和空串
     * @param str 要判断的字符串
     * @return boolean 不为空返回true, 为空返回false
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 将yyyy-MM-dd hh:mm:ss格式字符串转为util的Date
     * @param strDate 要转换的字符串格式的日期
     * @return date 转换好了的util的日期
     */
    public static Date fmtStrToDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转为sql的Timestamp类型
     * @param strDate 要转换的字符串日期
     * @return Timestamp
     */
    public static Timestamp fmtStrToTimestamp(String strDate) {
        return new Timestamp(fmtStrToDate(strDate).getTime());
    }

    /**
     * 将数据库中的timestamp类型转为String
     * @param date javax.sql.timestamp
     * @return java.lang.string
     */
    public static String fmtTimestampToStr(Timestamp date) {
        if(date == null) return null;
        Date dt = new Date(date.getTime());
        return fmtDateToStr(dt);
    }

    /**
     * 将util的Date转为yyyy-MM-dd hh:mm:ss格式字符串
     * @param date 要转换的日期
     * @return string 转换好的字符串格式的日期
     */
    public static String fmtDateToStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 将xxxx年xx月xx日格式转换成 xxxx-xx-xx
     * @return
     */
   public static String dateFmtChange(String str1){
       String year = str1.substring(0,str1.indexOf("年"));
       String month = str1.substring(str1.indexOf("年")+1,str1.indexOf("月"));
       String date = str1.substring(str1.indexOf("月")+1,str1.indexOf("日"));
       return year+"-"+month+"-"+date;
   }


}
package com.simple.common.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    private static final String TIMESTAMP_PATTERN_8        = "yyyyMMdd";

    public static final String  TIME_CONNECT_FORMAT_STRING = "yyyyMMddHHmm";

    public static final String  WEB_IN_PATTERN             = "yyyy-MM-dd";

    private static final String TIMESTAMP_PATTERN          = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取今天日期(yyyyMMdd)
     * @return
     */
    public static String getTimeStamp() {
        String temp = null;
        SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_PATTERN_8);
        temp = sdf.format(new Date());
        return temp;
    }

    /**
     * 日期转换(yyyy-MM-dd)
     * @param aDate
     * @return
     */
    public static Date getStringToDates(String aDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(WEB_IN_PATTERN);
        SimpleDateFormat formatter2 = new SimpleDateFormat(TIMESTAMP_PATTERN_8);
        Date date = null;
        try {
            date = formatter2.parse(aDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dString = formatter.format(date);
        Date data = java.sql.Date.valueOf(dString);
        return data;
    }

    /**
     * 日期转换(yyyy-MM-dd HH:mm:ss)
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date StringToDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(TIMESTAMP_PATTERN);
        Date newDate = null;
        if (date == null || date.trim().length() == 0) {
            return null;
        }
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            throw new ParseException(date, 0);
        }
        return newDate;
    }

    /**
     * Date转换到字符串yyyy-MM-dd
     * @param date Date
     * @return String yyyy-MM-dd
     * @see DateFormatUtils.DateFormatUtils#format(Date, String)
     */
    public static String date2StrDate(Date date) {
        return DateFormatUtils.format(date, WEB_IN_PATTERN);
    }

    /**
     * 获取某天日期
     * @return
     */
    public static Date getSomeDayDate(int effectiveDay) {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, effectiveDay);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        Date dateObj = null;
        try {
            dateObj = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateObj;
    }

    /**
     * 获取某天日期
     * @return
     */
    public static Date getSomeSecondDate(int second) {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);//把日期往后增加N秒.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_PATTERN);
        String dateString = formatter.format(date);
        Date dateObj = null;
        try {
            dateObj = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateObj;
    }

    /**
     * 指定日期加上天数后的日期
     * @param num 为增加的天数
     * @param newDate 创建时间
     * @return
     * @throws ParseException 
     */
    public static Date plusDayDateTime(int num, Date currdate) throws ParseException {
        Calendar ca = Calendar.getInstance();
        if (currdate != null) {
            ca.setTime(currdate);
        }
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        currdate = ca.getTime();
        return currdate;
    }

    /**
     * 获取今天日期
     * @return
     */
    public static Date getDateYearToday() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(WEB_IN_PATTERN);
        Date dateObj = null;
        try {
            dateObj = sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateObj;
    }

    /**
     * 获取今天日期(YYYYMMDD)
     * @return
     */
    public static String getYearMonDayDateStr() {
        String temp = null;
        SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_PATTERN_8);
        temp = sdf.format(new Date());
        return temp;
    }

    /**
     * 获取今天日期(YYYYMMDDMM)
     * @return
     */
    public static String getYearMonDayDateMinStr() {
        String temp = null;
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_CONNECT_FORMAT_STRING);
        temp = sdf.format(new Date());
        return temp;
    }

    /**
     * 获取今天日期
     * @return
     */
    public static Date getDateToday() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_PATTERN);
        Date dateObj = null;
        try {
            dateObj = sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateObj;
    }

    /**
     * 获取年月日(2019.06.23)
     * @return
     */
    public static String getTodayTimeFormat() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);//年
        int month = calendar.get(Calendar.MONTH) + 1;//月
        int day = calendar.get(Calendar.DATE);

        return year + "." + String.format("%02d", month) + "." + String.format("%02d", day);
    }

    /**
     * 获取月日(9月12日)
     * @return
     */
    public static String getTodayTimeMonthDayFormat() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;//月
        int day = calendar.get(Calendar.DATE);
        return month + "月" + day + "日";
    }

    /**
     * 获取月日(9月12日)
     * @return
     */
    public static String getTodayTimeMonthDayFormat(String dateDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStringToYmdDate(dateDay));
        int month = calendar.get(Calendar.MONTH) + 1;//月
        int day = calendar.get(Calendar.DATE);
        return month + "月" + day + "日";
    }

    /**
     * 获取当前日期天数值
     * @return
     */
    public static int getTodayDayNumber() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        return day;
    }

    /**
     * 获取当前日期天数值
     * @return
     */
    public static int getTodayDayNumber(String dateDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStringToYmdDate(dateDay));
        int day = calendar.get(Calendar.DATE);
        return day;
    }

    /**
     * 获取今天星期数
     * @return
     */
    public static String getTodayWeekOfDate() {
        Date date = getDateYearToday();
        String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获取今天星期数
     * @return
     */
    public static String getTodayWeekOfXingQi() {
        Date date = getDateYearToday();
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 根据日期获取星期数
     * @param dateDay
     * @return
     */
    public static String getTodayWeekOfXingQi(String dateDay) {
        Date date = getStringToYmdDate(dateDay);
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获取时间段返回中文
     * @return
     */
    public static String getTodayTimesStr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        int hour = Integer.valueOf(dateFormat.format(new Date()));
        String reusltStr = "";
        if (hour >= 18 || hour < 5) {
            reusltStr = "晚上好";
        } else if (hour >= 5 && hour < 12) {
            reusltStr = "早上好";
        } else if (hour >= 12 && hour < 18) {
            reusltStr = "下午好";
        }
        return reusltStr;
    }

    /**
     * 获得系统当前日期时间，以默认格式显示
     * 
     * @return
     */
    public static String getCurrentFormatDateTime() {
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormator = new SimpleDateFormat(TIME_CONNECT_FORMAT_STRING);
        return dateFormator.format(currentDate);
    }

    /**
     * 比较当前时间
     * false:在当前时间之后  true:当前时间之前
     * @param dateTime
     * @return
     */
    public static boolean compareCurrentDate(Date dateTime) {
        Date nowDate = getDateYearToday();
        Calendar end = Calendar.getInstance();
        end.setTime(dateTime);
        if (dateTime.before(nowDate)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 两日期比较
     * @param t1
     * @param t2
     * @return
     */
    public static int timeCompare(String t1, String t2) {
        SimpleDateFormat formatter = new SimpleDateFormat(WEB_IN_PATTERN);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(formatter.parse(t1));
            c2.setTime(formatter.parse(t2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result = c1.compareTo(c2);
        return result;
    }

    /**
     * 将字符串年-月-日转换成Date类型
     * @param dateTime
     * @return
     */
    public static Date getStringToYmdDate(String dateTime) {
        Date returnValue = null;
        SimpleDateFormat df;
        if (StringUtils.isNotBlank(dateTime)) {
            df = new SimpleDateFormat(WEB_IN_PATTERN);
            try {
                returnValue = df.parse(dateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }

    /**
     * 获取当前日期字符串（YYYY-MM-DD）
     * @return
     */
    public static String getStringToday() {
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(WEB_IN_PATTERN);
        String dateObj = sdf.format(currentDate);
        return dateObj;
    }

    /**
     * 获取某天日期(YYYY-MM-DD)
     * @return
     */
    public static String getStringSomeDay(int effectiveDay) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        //把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.DATE, effectiveDay);
        //这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static void main(String[] args) {
        System.out.println(getTodayTimeFormat());
        System.out.println(getTodayTimesStr());
        System.out.println(compareCurrentDate(getStringToYmdDate("2019-11-30")));
        System.out.println(getStringToDates("20191121"));
        System.out.println(getDateYearToday());
        System.out.println(getStringSomeDay(-1));
    }

}

package com.wjh.utils;

import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * @Classname DateUtil
 * @Description 时间工具类
 * @Date 2020/2/25 上午10:45
 * @Created by wjh
 */
public class DateUtil {

    /**
     * 格式化日期 格式：yyyyMMdd
     * Date
     *
     * @return String
     */
    public static String fromDateYMD(Date d) {
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        return format1.format(d);
    }

    /**
     * @param date
     * @return String 返回类型
     * @Title: getDateString
     * @Description: 使用"yyyy-MM-dd HH:mm:ss"格式化日期
     */
    public static String getDateStringH(Date date) {
        return getDateString(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @param date
     * @return String 返回类型
     * @Title: getDateString
     * @Description: 使用"yyyy/MM/dd HH:mm:ss"格式化日期
     */
    public static String getDateStringH2(Date date) {
        return getDateString(date, "yyyy/MM/dd HH:mm:ss");
    }

    /**
     * @param date   日期
     * @param format 模式
     * @return String 返回类型
     * @Title: getDateString
     * @Description: 格式化日期
     */
    public static String getDateString(Date date, String format) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            String dateString = formatter.format(date);
            return dateString;
        }
        return null;
    }

    /**
     * @param date
     * @return String 返回类型
     * @Title: getDateString
     * @Description: 使用"yyyy-MM-dd HH:mm:ss"格式化日期
     */
    public static String getDateStringY(Date date) {
        return getDateString(date, "yyyy-MM-dd");
    }

    /**
     * 返回当前时间 格式：yyyy-MM-dd
     *
     * @return String
     */
    public static String fromDateYMD() {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(new Date());
    }

    /**
     * 返回当前时间 格式：yyyyMMdd
     *
     * @return String
     */
    public static String fromDateY2() {
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        return format1.format(new Date());
    }

    /**
     * 返回当前时间 格式：yyyy-MM-dd hh:mm:ss
     *
     * @return String
     */
    public static String fromDateHms() {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format1.format(new Date());
    }

    /**
     * 返回当前时间 格式：yyyyMMddHHmmss
     *
     * @return String
     */
    public static String fromDateHms2() {
        DateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
        return format1.format(new Date());
    }

    /**
     * 返回当前时间 格式：yyyy/MM/dd HH:mm:ss
     *
     * @return String
     */
    public static String fromDateHms3() {
        DateFormat format1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return format1.format(new Date());
    }

    /**
     * 返回当前时间 格式：yyyyMMddHHmmssSSS
     *
     * @return String
     */
    public static String getCurrentMillisecond() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date());
    }

    /**
     * String转TimeStamp
     *
     * @param time
     * @return
     */
    public static Timestamp getTimestamp(String time) {
        return Timestamp.valueOf(time);
    }

    /**
     * Date转TimeStamp
     *
     * @param date
     * @return
     */
    public static Timestamp getTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 返回系统当前时间 唯一
     *
     * @return 以yyyyMMddHHmmss为格式的当前系统时间
     */
    public static String getOrderNum() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

        Random random = new Random();
        String rand = String.valueOf(random.nextInt(100000));

        for (int j = 0; j < 5 - rand.length(); j++) {
            rand += "0";
        }

        return df.format(date) + rand;
    }


    /**
     * 格式化字符串
     *
     * @param timestamp
     * @return
     */
    public static String getFormatDate(Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());
        String pattern = "yyyy-MM-dd";
        return getFormatDate(pattern, date);
    }

    public static String getFormatDateTime(Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return getFormatDate(pattern, date);
    }

    public static String getFormatDate(String pattern, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * 根据指定格式得到当前日期的字符串
     *
     * @param pattern String
     * @return String
     */
    public static String getCurrentDate(String pattern) {
        if (pattern == null || pattern.trim().equals("")) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        pattern = pattern.trim();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

    /**
     * 获取传入时间的零点时间
     *
     * @return String
     */
    public static Date getDayAm(Date now) {
        String str = getFormatDate("yyyy-MM-dd", now);
        return getDateYYYYMMddHHMMSS(str + " 00:00:00");
    }


    /**
     * 获取传入日期的23点59分59秒时间
     *
     * @return String
     */
    public static Date getDayPm(Date now) {
        String str = getFormatDate("yyyy-MM-dd", now);
        return getDateYYYYMMddHHMMSS(str + " 23:59:59");
    }

    public static Date getDateYYYYMMddHHMMSS(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date;
    }

    /**
     * add by luozf
     *
     * @param one 开始时间
     * @param two 结束时间 @ return String 返回结束时间和开始时间的 天 /小时/分/秒
     */
    public static String getDistanceTime(Date one, Date two) throws ParseException {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;

        long time1 = one.getTime();
        long time2 = two.getTime();
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return day + "天" + hour + "小时" + min + "分" + sec + "秒";
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate 较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date smdateNew = null;
        Date bdateNew = null;
        try {
            smdateNew = sdf.parse(sdf.format(smdate));
            bdateNew = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdateNew);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdateNew);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获取传入时间天的截止时间点
     *
     * @param d
     * @return
     */
    public static Date formEndTime(Date d) {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return getDateYYYYMMddHHMMSS(format1.format(d) + " 23:59:59");

    }

    /**
     * 获取传入时间天的开始时间点
     *
     * @param d
     * @return
     */
    public static Date formStartTime(Date d) {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return getDateYYYYMMddHHMMSS(format1.format(d) + " 00:00:00");

    }

    /**
     * 获取本月最后一天
     *
     * @param sDate
     * @return
     */
    public static int getLastDayOfMonth(Date sDate) {
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(sDate);
        int lastDay = cDay.getActualMaximum(Calendar.DAY_OF_MONTH);
        return lastDay;
    }

    /**
     * 判断当前指定时间点之内 如9：00-7：00
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isBetweenDay(String startTime, String endTime){
        if(!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime) && startTime.equals(endTime)){
            return true;
        }
        Calendar calendar = Calendar.getInstance();
        int startH = Integer.valueOf(startTime.split(":")[0]);
        int startM = Integer.valueOf(startTime.split(":")[1]);
        int endH = Integer.valueOf(endTime.split(":")[0]);
        int endM = Integer.valueOf(endTime.split(":")[1]);
        long startTimeMills = startH*3600000+startM*60000;
        long endTimeMills = endH*3600000+endM*60000;
        long nowTimeMills = calendar.get(Calendar.HOUR_OF_DAY)*3600000+calendar.get(Calendar.MINUTE)*60000+calendar.get(Calendar.SECOND)*1000+calendar.get(Calendar.MILLISECOND);
        return nowTimeMills >= startTimeMills && nowTimeMills <= endTimeMills;
    }

    /**
     * 日期时间比较,t1>t2则返回true
     *
     * @param t1
     * @param t2
     * @return t1>t2则返回true
     */
    public static boolean compareDateTime(Timestamp t1, Timestamp t2) {
        String d1 = DateUtil.getFormatDateTime(t1);
        String d2 = DateUtil.getFormatDateTime(t2);

        return d1.compareTo(d2) > 0;
    }

    /**
     * 日期时间比较,d1>d2则返回true
     *
     * @param d1
     * @param d2
     * @return d1>d2则返回true
     */
    public static boolean compareDate(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);

        return c1.compareTo(c2) > 0;
    }

    /**
     * 获取日期的月份
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH);
    }

    /**
     * 获取日期的天
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DATE);
    }

    /**
     * 获取日期的年份
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 滚动日期
     *
     * @param timestamp 被滚动的日期
     * @param calendarField 日历字段
     * @param rollAmount 滚动量
     * @return Timestamp
     */
    public static Timestamp getRollTime(Timestamp timestamp, int calendarField, int rollAmount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(timestamp.getTime()));
        cal.add(calendarField, rollAmount);
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 年滚动
     *
     * @param timestamp Timestamp
     * @param rollAmount 滚动量
     * @return Timestamp
     */
    public static Timestamp getRollYear(Timestamp timestamp, int rollAmount) {
        return getRollTime(timestamp, Calendar.YEAR, rollAmount);
    }

    /**
     * 月滚动
     *
     * @param timestamp Timestamp
     * @param rollAmount 滚动量
     * @return Timestamp
     */
    public static Timestamp getRollMonth(Timestamp timestamp, int rollAmount) {
        return getRollTime(timestamp, Calendar.MONTH, rollAmount);
    }

    /**
     * 天滚动
     *
     * @param timestamp Timestamp
     * @param rollAmount 滚动量
     * @return Timestamp
     */
    public static Timestamp getRollDay(Timestamp timestamp, int rollAmount) {
        return getRollTime(timestamp, Calendar.DAY_OF_WEEK, rollAmount);
    }

    /**
     * 根据时间返回周几
     * @param dateTime
     * @return
     */
    public static int dayForWeekNew(String dateTime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = f.parse(dateTime);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dayForWeek = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 0;
        } else {
            dayForWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

}

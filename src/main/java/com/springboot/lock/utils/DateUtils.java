package com.springboot.lock.utils;


import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 *
 * @author thinkpad
 */
@Slf4j
public class DateUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String AUDIT_TIME = "yyyy-MM-dd_HH:mm:ss";
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"
    };
    // 1分钟
    public final static long MINUTE = 60 * 1000L;
    //十分钟
    public final static long TEN_MINUTE = 10 * 60 * 1000L;
    //半小时
    public final static long HALF_HOUR = 30 * MINUTE;
    // 1小时
    public final static long HOUR = 60 * MINUTE;
    // 1天
    public final static long DAY = 24 * HOUR;
    // 1月
    public final static long MONTH = 31 * DAY;
    // 1年
    public final static long YEAR = 12 * MONTH;

    /**
     * 返回指定格式的字符串日期
     *
     * @param date   日期 允许NULL,为NULL时返回空字符
     * @param format 返回的字符串日期格式
     * @return
     */
    public static String dateToStr(Date date, String format) {
        String dateStr = null;
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            dateStr = simpleDateFormat.format(date);
        }
        return dateStr;
    }

    /**
     * 返回指定格式的日期
     *
     * @param date   日期 不允许NULL
     * @param format 返回的日期
     * @return
     */
    public static Date dateFormat(Date date, String format) throws ParseException {
        if (date == null) {
            return null;
        }
        if (StringUtils.isBlank(format)) {
            return date;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String dateStr = simpleDateFormat.format(date);
        return convertDate(dateStr, format);
    }

    /**
     * 日期转换为时间
     *
     * @param date
     * @return
     */
    public static Long toLong(Date date) {
        String dateStr = dateToStr(date, "yyyyMMdd");
        return new Long(dateStr);
    }




    public static int getMinDiff(Date startDate, Date endDate) {

        long nd = 1000L * 24 * 60 * 60;
        long nh = 1000L * 60 * 60;
        long nm = 1000L * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startDate.getTime();

        // 计算差多少分钟
        long min = diff % nd % nh / nm;

        return (int) min;
    }


    /**
     * 根据字符串返回指定格式的日期
     *
     * @param dateStr 日期(字符串)
     * @param format  日期格式
     * @return 日期(Date)
     * @throws ParseException
     */
    public static Date convertDate(String dateStr, String format) throws ParseException {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        date = simpleDateFormat.parse(dateStr);
        return date;
    }

    public static String format(String dateStr, String format) throws ParseException {
        Date date = convertDate(dateStr, format);
        return dateToStr(date, format);
    }

    /**
     * 秒的变动
     *
     * @param second
     * @return
     */
    public static Date secondChange(Date date, Integer second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * 分钟的变动
     *
     * @param minute
     * @return
     */
    public static Date minuteChange(Date date, Integer minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 小时的变动
     *
     * @param hour
     * @return
     */
    public static Date hourChange(Date date, Integer hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }

    /**
     * 天的变动
     *
     * @param day
     * @return
     */
    public static Date dayChange(Date date, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 月的变动
     *
     * @param month
     * @return
     */
    public static Date monthChange(Date date, Integer month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 年的变动
     *
     * @param year
     * @return
     */
    public static Date yearChange(Date date, Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 获取年
     *
     * @param date
     * @return
     */
    public static Integer getYear(Date date) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取月
     *
     * @param date
     * @return
     */
    public static Integer getMonth(Date date) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日
     *
     * @param date
     * @return
     */
    public static Integer getDay(Date date) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }


    public static Integer getHour(Date date) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 设置日
     *
     * @param date
     * @return
     */
    public static Date setDay(Date date, Integer day) {
        Calendar cal = Calendar.getInstance();
        if (null == date) {
            return null;
        }
        cal.setTime(date);
        cal.set(Calendar.DATE, day);
        return cal.getTime();
    }


    /**
     * 得到当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
        return simple.format(date);

    }

    /**
     * 取得两个时间之间相差的天数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int getIntervalDays(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        if (c1.after(c2)) {
            Calendar cal = c1;
            c1 = c2;
            c2 = cal;
        }
        long sl = c1.getTimeInMillis();
        long el = c2.getTimeInMillis();
        long ei = el - sl;
        return (int) (ei / (1000 * 60 * 60 * 24));
    }

    /**
     * 比较两个日期，计算中间的天数
     *
     * @param start
     * @param end
     * @return
     */
    public static int getIntervalDaysReal(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        int days = (int) (diff / (1000 * 60 * 60 * 24));

        return days;
    }

    /**
     * 比较两个日期，计算中间的天数，只接收"yyyy-MM-dd"类型的数据
     *
     * @param start
     * @param end
     * @return
     */
    public static int getIntervalDaysReal(String start, String end) {
        Date date1 = null, date2 = null;
        try {
            date1 = convertDate(start, DATE_FORMAT);
            date2 = convertDate(end, DATE_FORMAT);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return getIntervalDaysReal(date1, date2);
    }


    /**
     * 计算两个日期之间相差的月数
     */
    public static Integer getIntervalMouth(Date date1, Date date2) {
        Integer iMonth = 0;
        int flag = 0;
        try {
            Calendar objCalendarDate1 = Calendar.getInstance();
            objCalendarDate1.setTime(date1);
            Calendar objCalendarDate2 = Calendar.getInstance();
            objCalendarDate2.setTime(date2);
            if (objCalendarDate2.equals(objCalendarDate1)) {
                return 0;
            }
            if (objCalendarDate1.after(objCalendarDate2)) {
                Calendar temp = objCalendarDate1;
                objCalendarDate1 = objCalendarDate2;
                objCalendarDate2 = temp;
            }
            if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1.get(Calendar.DAY_OF_MONTH)) {
                flag = 1;
            }
            if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1.get(Calendar.YEAR)) {
                iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1.get(Calendar.YEAR)) * 12 + objCalendarDate2.get(Calendar.MONTH) - flag) - objCalendarDate1.get(Calendar.MONTH);
            } else {
                iMonth = objCalendarDate2.get(Calendar.MONTH) - objCalendarDate1.get(Calendar.MONTH) - flag;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iMonth;
    }

    /**
     * 获取当月的第一天
     *
     * @return
     */
    public static Date getTheFirstDayOfMonth() {
        return getTheStartOrEndDayByOneDate(new Date(), 1);
    }

    /**
     * 获取当月最后一天
     *
     * @retur
     */
    public static Date getTheEndDayOfMonth() {
        return getTheStartOrEndDayByOneDate(new Date(), 2);
    }


    /**
     * 取得两个时间之间相差的年数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static Double getIntervalYears(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        if (c1.after(c2)) {
            Calendar cal = c1;
            c1 = c2;
            c2 = cal;
        }
        long sl = c1.getTimeInMillis();
        long el = c2.getTimeInMillis();
        long ei = el - sl;
        return (double) (ei / (1000 * 60 * 60 * 24 * 365 * 1.0));
    }

    /**
     * 转换字符串为日期类型（固定格式） eg: 2011-7-7（2009-11-20）
     *
     * @param dateStr
     */
    public static Date coalitionDateStr2(String dateStr, String fgf) {
        String aaa = dateStr;
        if (StringUtils.isNotBlank(dateStr)) {
            String[] mry = dateStr.split(fgf);
            String y = mry[0];
            String m = mry[1];
            String d = mry[2];
            if (Integer.parseInt(m) < 10) {
                m = "0" + m;
            }
            if (Integer.parseInt(d) < 10) {
                d = "0" + d;
            }
            aaa = y + m + d;
        }
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            date = simpleDateFormat.parse(aaa);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // System.out.println(date.toString());
        return date;
    }

    /**
     * 转换字符串为日期类型（固定格式） eg: 01-1月 -00 12.00.00.000000000 上午（01-11月-00 12.00.00.000000000 上午）
     *
     * @param dateStr
     */
    public static Date coalitionDateStr(String dateStr) {
        String aaa = dateStr;
        if (StringUtils.isNotBlank(dateStr)) {
            String[] muStr = dateStr.split(" ");
            if (muStr.length > 3) {
                muStr[0] = muStr[0] + muStr[1];
                muStr[1] = muStr[2];
                muStr[2] = muStr[3];
            }
            String[] mry = muStr[0].split("-");
            String d = mry[0];
            String m = mry[1];
            String y = mry[2];
            String[] hfm = muStr[1].split("\\.");
            String h = hfm[0];
            if ("下午".equals(muStr[2])) {
                int ah = Integer.parseInt(h) + 12;
                h = ah + "";
            }
            String f = hfm[1];
            String miao = hfm[2];
            String hm = hfm[3];
            if (m.contains("月")) {
                m = m.replaceAll("月", "");
            }
            if (Integer.parseInt(m) < 10 && m.length() < 2) {
                m = "0" + m;
            }
            aaa = y + m + d + " " + h + ":" + f + ":" + miao + ":" + hm;
        }
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd hh:mm:ss:SS");
        try {
            date = simpleDateFormat.parse(aaa);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    /**
     * 时间大小比较
     *
     * @param t1
     * @param t2
     * @return
     */
    public static Integer timeCompare(Date t1, Date t2) {
        if (t1 != null && t2 != null) {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(t1);
            c2.setTime(t2);
            int result = c1.compareTo(c2);
            return result;
        }
        return null;
    }




    /**
     * 根据一个日期，获取当前月的开始与结束日期
     *
     * @param date
     * @param type : 参数类型：1-->开始日期；其余数字-->结束日期
     * @return
     */
    public static Date getTheStartOrEndDayByOneDate(Date date, int type) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (type) {
            case 1: {
                cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
                return cal.getTime();
            }
            default: {
                cal.set(Calendar.DATE, 1);
                cal.roll(Calendar.DATE, -1);
                return cal.getTime();
            }
        }
    }


    public static String changDayString(String dateStr, int days) {
        String reDateStr = "";

        if (dateStr != null && !"".equals(dateStr)) {
            Date date;
            try {
                date = convertDate(dateStr, DATE_FORMAT);
                date = dayChange(date, days);
                reDateStr = dateToStr(date, DATE_FORMAT);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return reDateStr;
    }


    /**
     * 时间差的分钟
     *
     * @param dateA
     * @param dateB
     * @param formate
     * @return
     */
    public static int getBetweenDayNumber(String dateA, String dateB, String formate) {
        long dayNumber = 0;
//    	1小时=60分钟=3600秒=3600000
        long mins = 60L * 1000L;
//    	long day= 24L * 60L * 60L * 1000L;计算天数之差
        SimpleDateFormat df = new SimpleDateFormat(formate);
        try {
            Date d1 = df.parse(dateA);
            Date d2 = df.parse(dateB);
            dayNumber = (d2.getTime() - d1.getTime()) / mins;
        } catch (Exception e) {
            log.error("获取时间差异常getBetweenDayNumber!");

        }
        return (int) dayNumber;
    }

    /**
     * 时间差的分钟
     *
     * @param dateA
     * @param dateB
     * @return
     */
    public static int getBetweenDayNumber(Date dateA, Date dateB) {
        if (null == dateA || null == dateB) {
            return 0;
        }
        long dayNumber = 0;
//    	1小时=60分钟=3600秒=3600000
        long mins = 60L * 1000L;
//    	long day= 24L * 60L * 60L * 1000L;计算天数之差
        try {
            dayNumber = (dateB.getTime() - dateA.getTime()) / mins;
        } catch (Exception e) {
            log.error("获取时间差异常getBetweenDayNumber!");
        }
        return (int) dayNumber;
    }

    /**
     * 时间差的分钟
     *
     * @param date
     * @param day
     * @return
     */
    public static Date addDate(Date date, Integer day) {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day * 24 * 60 * 60 * 1000; // 要加上的天数转换成毫秒数
        time += day; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }

    public static String long2String(Long time){
        SimpleDateFormat format =  new SimpleDateFormat(TIME_FORMAT);
        String d = format.format(time);
        return d;
    }

    public static void main(String args[]) throws ParseException {
        System.out.println(timeCompare(convertDate("2017-05-23", "yyyy-MM-dd"), new Date()));
        System.out.println(getIntervalDaysReal(convertDate("2012-06-27", "yyyy-MM-dd"), new Date()));
        System.out.println(getIntervalDaysReal("2012-06-27", "2012-06-23"));
        System.out.println(dateToStr(getTheFirstDayOfMonth(), "yyyy-MM-dd"));
        System.out.println(dateToStr(getTheStartOrEndDayByOneDate(convertDate("2012-02-02", "yyyy-MM-dd"), 1), "yyyy-MM-dd"));
        System.out.println(dateToStr(getTheEndDayOfMonth(), "yyyy-MM-dd"));
        System.out.println(dateToStr(getTheStartOrEndDayByOneDate(convertDate("2012-02-02", "yyyy-MM-dd"), 2), "yyyy-MM-dd"));
        System.out.println(dateToStr(dayChange(new Date(), -1), "yyyy-MM-dd"));


        System.out.println("----------");
        System.out.println(long2String(new Date().getTime()));
    }





}

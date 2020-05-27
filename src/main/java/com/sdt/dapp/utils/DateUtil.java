package com.sdt.dapp.utils;

import org.springframework.util.StringUtils;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 *
 * @author shichao
 */
public class DateUtil {

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd
     */
    public static final String DATE_FORMAT_2 = "yyyy-MM-dd";

    /**
     * yyyyMMddHHmmss
     */
    public static final String DATE_FORMAT_3 = "yyyyMMddHHmmss";


    /**
     * 根据date获取当前年份
     *
     * @param date
     * @return 年份
     */
    public static int year(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取今天的年份
     *
     * @return 年份
     */
    public static int year() {
        return year(new Date());
    }

    /**
     * 根据date获取月份
     *
     * @return 月份
     */
    public static int month(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取今天的月份
     *
     * @return 月份
     */
    public static int month() {
        return month(new Date());
    }

    /**
     * 根据年月日返回format之后的年月日，yyyy-MM-dd
     *
     * @return yyyy-MM-dd
     */
    public static String getDateByYearMonth(int year, int month, int date) {
        String yearStr = year + "";
        String monthStr = month + "";
        String dateStr = date + "";
        String newDate;
        if (yearStr.length() == 2)
            yearStr = "20" + yearStr;
        if (month < 10)
            monthStr = "0" + monthStr;
        if (date < 10)
            dateStr = "0" + dateStr;
        newDate = yearStr + "-" + monthStr + "-" + dateStr;
        return newDate;
    }

    /**
     * 求得最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static int endDate(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 字符串转日期函数：格式必须为"yyyy-mm-dd"
     */
    public static Date toDate(String str) {
        return toDate(str, "yyyy-MM-dd");
    }

    public static Date toDate(String date, String fmt) {
        try {
            if ("".equals(date))
                return null;
            SimpleDateFormat myFormatter = new SimpleDateFormat(fmt);
            return myFormatter.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date toDate(String date, String fmt, String zone) {
        try {
            if (StringUtils.isEmpty(date))
                return null;
            SimpleDateFormat myFormatter = new SimpleDateFormat(fmt);
            myFormatter.setTimeZone(TimeZone.getTimeZone(zone));

            return myFormatter.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将date按format格式进行转化
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateFormat(Date date, String format) {
        String retValue = "";
        if (date == null || StringUtils.isEmpty(format))
            return retValue;
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * 获取星期
     *
     * @param date
     * @return 日：0；一：1；二：2；……
     */
    public static int day(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static String dayStr(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int i = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (i == 0)
            return "日";
        else if (i == 1)
            return "一";
        else if (i == 2)
            return "二";
        else if (i == 3)
            return "三";
        else if (i == 4)
            return "四";
        else if (i == 5)
            return "五";
        else if (i == 6)
            return "六";
        else
            return "";
    }

    /**
     * 字符串转日期函数：格式必须为"yyyy-mm-dd"
     *
     * @param str
     * @return
     */
    public static Timestamp toTimestamp(String str) {
        try {
            if (str.equals(""))
                return null;
            if (str.indexOf(":") < 0)
                str += " 00:00:00";
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new Timestamp(myFormatter.parse(str).getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 指定格式的当前时间
     *
     * @param format
     * @return 指定格式的当前时间
     */
    public static String now(String format) {
        Date date = new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat(format);
        return dateFm.format(date);
    }

    /**
     * 当前时间=now
     *
     * @return Timestamp now
     */
    public static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 当前时间=now 无时分秒
     *
     * @return Timestamp now
     */
    public static Timestamp dateTimestamp() {
        String dateStr = now(DATE_FORMAT_2);
        return toTimestamp(dateStr);
    }

    /**
     * 当前日期时间，日期格式：yyyy-MM-dd HH:mm:ss
     *
     * @return String now
     */
    public static String now() {
        Date date = new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFm.format(date);
    }

    public static String nowdateFormat(String dateFormat) {
        Date date = new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat(dateFormat);
        return dateFm.format(date);
    }

    public static Date addYear(Date date, int addYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, addYear);
        return calendar.getTime();
    }

    public static Date addMonth(Date date, int addMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, addMonth);
        return calendar.getTime();
    }

    public static Date addDate(Date date, int addDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, addDate);
        return calendar.getTime();
    }

    public static Date addMinutes(Date date, int addMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, addMinutes);
        return calendar.getTime();
    }

    public static Date addSeconds(Date date, int addSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, addSeconds);
        return calendar.getTime();
    }

    /**
     * 将任意Timestamp转换成指定格式的String
     *
     * @param time
     * @param format
     * @return
     */
    public static String timestampFormat(Timestamp time, String format) {
        String retValue = "";
        if (time == null)
            return retValue;
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(time);
    }

    public static List<String> getStringWorkDays(Date date) {
        List<String> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        while (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
                dates.add(dateFormat((Date) cal.getTime().clone(), "yyyy-MM-dd"));
            }
            cal.add(Calendar.DATE, 1);
        }
        return dates;
    }


    public static List<Date> getWorkDays(Date date) {
        List<Date> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        while (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
                dates.add((Date) cal.getTime().clone());
            }
            cal.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static boolean isWorkDays(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        return !(day == Calendar.SUNDAY || day == Calendar.SATURDAY);
    }

    // 加八小时
    public static Date addHours(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }

    public static XMLGregorianCalendar stringDate2XMLGregorianCalendar(String date) {
        try {
            GregorianCalendar gcal = new GregorianCalendar();
            Date date2 = toDate(date, "yyyy-MM-dd HH:mm:ss");
            gcal.setTime(date2);
            XMLGregorianCalendar xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
            return xgcal;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 将xmlDate+8h后返回string
    public static String xmlDateToStringAdd8(XMLGregorianCalendar cal) {
        Date date = cal.toGregorianCalendar().getTime();
        //date = addHours(date, 8);
        String dateStr = dateFormat(date, DateUtil.DATE_FORMAT_1);
        return dateStr;
    }

    // 将前台的2018-09-02T07:34:00.811Z  加8小时后转为正常的yyyy-MM-dd
    public static String changeToNormalDate(String date1) {
        if (!StringUtils.isEmpty(date1)) {
            date1 = date1.replace("T", " ");
            date1 = date1.substring(0, 18);
            Date date = toDate(date1, DateUtil.DATE_FORMAT_1);
            date = addHours(date, 8);
            return dateFormat(date, DateUtil.DATE_FORMAT_2);
        }
        return "";
    }

    // 将前台的2018-09-02T07:34:00.811Z  加8小时后转为正常的yyyyMMddHHmmss
    public static String changeToNormalDateTime(String date1) {
        if (!StringUtils.isEmpty(date1)) {
            date1 = date1.replace("T", " ");
            date1 = date1.substring(0, 18);
            Date date = toDate(date1, DateUtil.DATE_FORMAT_1);
            date = addHours(date, 8);
            return dateFormat(date, "yyyyMMddHHmmss");
        }
        return "";
    }

    // 将前台的2018-09-02T07:34:00.811Z  加8小时后转为yyyy-MM-dd HH:mm:ss
    public static String changeToNormalDateTime2(String date1) {
        if (!StringUtils.isEmpty(date1)) {
            date1 = date1.replace("T", " ");
            date1 = date1.substring(0, 18);
            Date date = toDate(date1, DateUtil.DATE_FORMAT_1);
            date = addHours(date, 8);
            return dateFormat(date, DateUtil.DATE_FORMAT_1);
        }
        return null;
    }

    /**
     * 判断是否超时
     * date距离现在的时间是否已经大于overTime了
     * @param date
     * @param overTime 秒
     * @return
     */
    public static boolean overTime(Date date,int overTime){
        Date now  = new Date();
        Date date1 = addSeconds(date,overTime);
        if (date1.compareTo(now) >= 0){
            return false;
        } else
            return true;
    }

}

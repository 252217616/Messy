package com.example.demo01.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
	//日志记录器
	private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	//https://my.oschina.net/leejun2005/blog/152253 SimpleDateFormat 存在非线程安全问题 不可以这样用需要修改
    public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final DateFormat DATE_FORMAT_YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

    public static final DateFormat DEFAULT_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static final DateFormat DEFAULT_DATETIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    public static final DateFormat DATETIME_WITH_MS_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    public static final String DATETIME_FORMAT = "yyyyMMdd";

    private DateUtil() {
    }
    public static Date add(Date date,int day){
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.DAY_OF_YEAR, day);
		Date dt=rightNow.getTime();
		return dt;
    }
    public static Date addYear(Date date,int year){
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.YEAR, year);
		Date dt=rightNow.getTime();
		return dt;
    }
    /*
     * Date --> String
     */
    public synchronized static String date2String(Date date, DateFormat dateFormat) {
        if(date==null) return null;
        return dateFormat.format(date);
    }

    public static String date2String(Date date, String dateFormat) {
        if(date==null) return null;
        return date2String(date, new SimpleDateFormat(dateFormat));
    }

    public static String date2String(Date date) {
        if(date==null) return null;
        return date2String(date, DEFAULT_DATE_FORMAT);
    }

    public synchronized static String time2String(Date time, DateFormat dateFormat) {
        if(time==null) return null;
        return dateFormat.format(time);
    }

    public static String time2String(Date time, String dateFormat) {
        if(time==null) return null;
        return date2String(time, new SimpleDateFormat(dateFormat));
    }

    public static String time2String(Date time) {
        if(time==null) return null;
        return date2String(time, DEFAULT_TIME_FORMAT);
    }

    public synchronized static String dateTime2String(Date dateTime, DateFormat dateFormat) {
        if(dateTime==null) return null;
        return dateFormat.format(dateTime);
    }

    public static String dateTime2String(Date dateTime, String dateFormat) {
        if(dateTime==null) return null;
        return date2String(dateTime,dateFormat);
    }

    public static String dateTime2String(Date dateTime) {
        if(dateTime==null) return null;
        return date2String(dateTime, DEFAULT_DATETIME_FORMAT);
    }

    public static String dateTime2StringWithMs(Date dateTime) {
        if(dateTime==null) return null;
        return date2String(dateTime, DATETIME_WITH_MS_FORMAT);
    }

    /*
     * String -->Date
     */

    public synchronized static Date string2Date(String date, DateFormat dateFormat) {
        if(date==null) return null;
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Date string2Date(String date, String dateFormat) {
        return string2Date(date, new SimpleDateFormat(dateFormat));
    }

    public static Date string2Date(String date) {
        return string2Date(date, DEFAULT_DATE_FORMAT);
    }

    public static Time string2Time(String time, DateFormat timeFormat) {
        if(time==null) return null;
        return new Time(string2Date(time, timeFormat).getTime());
    }

    public static Time string2Time(String time, String timeFormat) {
        if(time==null) return null;
        return new Time(string2Date(time, timeFormat).getTime());
    }

    public static Time string2Time(String time) {
        return string2Time(time, DEFAULT_TIME_FORMAT);
    }

    public static Timestamp string2DateTime(String time, DateFormat timeFormat) {
        if(time==null) return null;
        return new Timestamp(string2Date(time, timeFormat).getTime());
    }

    public static Timestamp string2DateTime(String time, String timeFormat) {
        if(time==null) return null;
        return new Timestamp(string2Date(time, timeFormat).getTime());
    }

    public static Timestamp string2DateTime(String time) {
        return string2DateTime(time, DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 取得当前日期。日期格式为：yyyy-MM-dd 
     * @return 当前日期字符串。
     */
    public synchronized static String getCurrentDateAsString() {
        return DEFAULT_DATE_FORMAT.format(Calendar.getInstance().getTime());
    }

    /**
     * 取得当前日期时间。日期格式为：yyyyMMddHHmmss
     * @return 当前日期字符串。
     */
    public synchronized static String getCurrentDateTimeAsString() {
        return DEFAULT_DATETIME_FORMAT.format(Calendar.getInstance().getTime());
    }
    /**
     * 取得当前日期时间。日期格式为：yyyyMMdd
     * @return 当前日期字符串。
     */
    public synchronized static String getCurrentDateYYYYMMDDAsString() {
        return DATE_FORMAT_YYYYMMDD.format(Calendar.getInstance().getTime());
    }
    /**
     * 取得当前日期时间。日期格式为由dateFormat定义
     * 
     * @param dateFormat 格式串
     * @return 当前日期字符串。
     */
    public static String getCurrentDateAsString(String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(Calendar.getInstance().getTime());
    }

    /**
     * 取得昨日日期时间。日期格式为由dateFormat定义
     * 
     * @param dateFormat 格式串
     * @return 当前日期字符串。
     */
    public static String getYesterdayDateAsString(String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return sdf.format(cal.getTime());
    }
    
    /**
     * 根据dateFormat定义日期格式取得指定的日期
     * 
     * @param date
     *            指定的日期
     * @param dateFormat
     *            格式串
     * @return 日期字符串。
     */
    public static String getDateString(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    public static Date parseDate(String date, DateFormat df) {
        try {
            return df.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 根据dateFormat定义日期格式取得指定的日期
     * 
     * @param date
     *            指定的日期字符串
     * @param dateFormat
     *            格式串
     * @return 日期
     */
    public static Date parseDate(String date, String dateFormat) {
        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
        return parseDate(date, fmt);
    }

    public synchronized static Date parseDate(String date) {
        return parseDate(date, DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 取得当前的时间戳
     * 
     * @return 时间戳
     */
    public static Timestamp nowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 将指定的日期转换时间戳     * 
     * @param date
     *            日期
     * @return 时间戳
     */
    public static Timestamp toTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    public static String toString(Date time) {
        return getDateString(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static String fromUnixTime(Long ms) {
        return getDateString(new Date(ms.longValue() * 1000), "yyyy-MM-dd HH:mm:ss");
    }

    public static Long unixTimestamp(String date) {
        return new Long(parseDate(date).getTime() / 1000);
    }

    /**
     * 根据给定格式生成数字型的日期
     * 
     * @param date
     * @param dateFormat
     * @return 数字日期
     */
    public static Long unixTimestamp(String date, String dateFormat) {
        return new Long(parseDate(date, dateFormat).getTime() / 1000);
    }

    public static Long currentUnixTimestamp() {
        return new Long(System.currentTimeMillis() / 1000);
    }

    public static Long unixTimestamp(Date date) {
        return new Long(date.getTime() / 1000);
    }
    
	/**
	 * 取得验证码时间差 秒
	 * @param endTime
	 * @param beginTime
	 * @return
	 */
	public static long consumeTime(long endTime,long beginTime){
		return  (endTime - beginTime)/1000;
	}
	
	
	// ====================================================================================
		/**
		 * 比较两个日期天数
		 * 
		 * @param strBegin
		 * @param strEnd
		 * @return
		 */
		public static int getDifferDays(String strBegin, String strEnd) {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = null, date2 = null;
			int days = 0;
			try {
				date1 = f.parse(strBegin);
				date2 = f.parse(strEnd);
				days = (int) ((date2.getTime() - date1.getTime()) / 86400000);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			return days;
		}
		/**
		 * 两日期天数只差
		 * @param strBegin
		 * @param strEnd
		 * @param format
		 * @return
		 */
		public static boolean getDayBetween(String strBegin, String strEnd,String format) {
			SimpleDateFormat f = new SimpleDateFormat(format);
			Date date1 = null, date2 = null;
				try {
					date1 = f.parse(strBegin);
					date2 = f.parse(strEnd);
				} catch (ParseException e) {
					logger.info("日期格式转换错误");
				}
				return ((date2.getTime() - date1.getTime()) / 1000) < 0 ? true:false;
		}	
		/**
		 * 两日期天数只差
		 * @param strBegin
		 * @param strEnd
		 * @return
		 */
		public static boolean getDayBetween(String strBegin, String strEnd) {
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMddhhmmss");
			Date date1 = null, date2 = null;
				try {
					date1 = f.parse(strBegin);
					date2 = f.parse(strEnd);
				} catch (ParseException e) {
					logger.info("日期格式转换错误");
				}
				return ((date2.getTime() - date1.getTime()) / 1000) < 0 ? true:false;
		}	
		/**
		 * 当前日期与给定日期天数差
		 * @param strBegin
		 * @param strEnd
		 * @return
		 */
		public static long getDayBetweens(String strBegin) {
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMddhhmmss");
			Date date1 = null, date2 = new Date();
				try {
					date1 = f.parse(strBegin);
				} catch (ParseException e) {
					logger.info("日期格式转换错误");
				}
				return (date2.getTime() - date1.getTime()) / (1000*3600*24);  
		}	
		/**
		 * 当前日期与给定日期天数差
		 * @param strBegin
		 * @param strEnd
		 * @return
		 */
		public static long getDayBetweensFormat(String strBegin,String format) {
			SimpleDateFormat f = new SimpleDateFormat(format);
			Date date2 = null;
			Date date1 = new Date();
				try {
					date2 = f.parse(strBegin);
				} catch (ParseException e) {
					logger.info("日期格式转换错误");
				}
				return ((date1.getTime() - date2.getTime()) / (1000*3600*24));  
		}
		/**
		 * 按指定格式获得日期
		 * @param sourceDate
		 * @param format
		 * @return
		 */
		public static Date getDateByFormat(Date sourceDate,String format)
		{
			Date date = null;
			try
			{
				SimpleDateFormat f = new SimpleDateFormat(format);
				String dateString = f.format(sourceDate);
				date = f.parse(dateString);
			}catch(Exception e){
				logger.error(e.getMessage());
			}
			return date;
		}
		
		/**
		 * 按指定格式获得日期
		 * @param sourceDateStr
		 * @param format
		 * @return
		 */
		public static Date getDateByFormat(String sourceDateStr,String format)
		{
			Date date = null;
			try
			{
				SimpleDateFormat f = new SimpleDateFormat(format);
				date = f.parse(sourceDateStr);
			}catch(Exception e)
			{
				logger.error(e.getMessage());
			}
			return date;
		}
		
		/**
		 * 判断流标后是否能重新借款
		 * @param 当前时间
		 * @param auditDate 审核时间
		 * @param bidsAfterDays 能重新借款的时间间隔
		 * @return
		 */
		public static boolean isReborrowBidsAfter(Date currentDate,Date auditDate,int bidsAfterDays)
		{
			boolean ret = false;
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			try
			{
				Date curDate = f1.parse(f1.format(currentDate));
				Date audDate = f1.parse(f1.format(auditDate));
				cal2.setTime(audDate);
				cal2.add(Calendar.DAY_OF_YEAR, bidsAfterDays);
				
				cal1.setTime(curDate);
				if(cal2.getTimeInMillis() < cal1.getTimeInMillis())//当前时间>审核时间+能重新借款的时间间隔
				{
					ret = true;
				}
				
			}catch(Exception e)
			{
				logger.error(e.getMessage());
			}
			return  ret;
		}

		/**
		 * 计算两个时间差精确到（xxx天xx小时xx分）
		 * @author shaofan
		 * @param date
		 * @return
		 * @throws ParseException 
		 * date1  表示终止时间
		 */
		public static String getDateSubtract(Date date1,Date date2, boolean flag) throws ParseException
		{
			  String dayday="";
			  if(date1.getTime()-date2.getTime()>0)
			  {
				   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			       Date now =df.parse(df.format(date1));
			       Date date=df.parse(df.format(date2));
			       long l=now.getTime()-date.getTime();
			       long day=l/(24*60*60*1000);
			       long hour=(l/(60*60*1000)-day*24);
			       long min=((l/(60*1000))-day*24*60-hour*60);
			       dayday=""+day+"天"+hour+"小时"+min+"分";
			  }
			 return dayday;
		}
		
		/**
		 * 判断支付是否超时
		 * 如果发起时间与当前时间超过10分钟或是跨天的，视为超时
		 * @param sendDate
		 *               支付发起支付交易的时间
		 * @return
		 *        true 超时
		 *        false 不超时
		 */
		public static boolean isTimeOutPayBill(Date sendDate) throws Exception
		{
			boolean ret = false;
			long dayTime = 24*60*60*1000;//一天的时间
			long tenMuniTime = 10*60*1000;//10分钟的时间
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date currentDate = new Date();
			String sendDateStr = df.format(sendDate);
			String currentDateStr = df.format(currentDate);
			Date finalSendDate = df.parse(sendDateStr);
			Date finalCurrentDate = df.parse(currentDateStr);
			long dayDif = finalCurrentDate.getTime() - finalSendDate.getTime();
			long minuteDif = currentDate.getTime() - sendDate.getTime();
			if(dayDif >= dayTime || minuteDif >= tenMuniTime)
			{
				ret = true;
			}
			return ret;
		}
		
		/**
		 * 判断是否超时
		 * @param startDate
		 *                开始时间
		 * @param intervalTime
		 *                间隔时间，以毫秒为单位
		 * @return
		 * @throws Exception
		 */
		public static boolean isTimeOut(Date startDate,long intervalTime)
		{
			boolean ret = false;
			Date currentDate = new Date();
			long minuteDif = currentDate.getTime() - startDate.getTime();
			if(minuteDif >= intervalTime)
			{
				ret = true;
			}
			return ret;
		}
		
		/**
		 * 判断是否是在还款时间内还款
		 * 还款时间是指一天内可进行还款操作的时间
		 * 现规定可还款时间段为04:00:00--23:30:00
		 * @return
		 * @throws Exception
		 */
		public static boolean isRepaymentTime() throws Exception
		{
			boolean ret = false;
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate = new Date();
			String startDateStr = f1.format(currentDate).concat(" 04:00:00");
			String ednDateStr = f1.format(currentDate).concat(" 23:30:00");
			Date startDate = f2.parse(startDateStr);
			Date endDate = f2.parse(ednDateStr);
			if(currentDate.getTime() > startDate.getTime() && currentDate.getTime() < endDate.getTime())
			{
				ret = true;
			}
			return ret;
		}
		
		/**
		 * 获取指定日期的前几天
		 * 2015年5月28日 下午8:56:44 wangw添加此方法
		 * @param date
		 * @param day
		 * @return
		 */
		public static Date pastDate (Date date, int day) {
			if (date == null) {
				date = new Date();
			}
			Calendar newDate = Calendar.getInstance();
			newDate.setTime(date);
			newDate.set(Calendar.DATE, newDate.get(Calendar.DATE) - day);
			return newDate.getTime();
		}
		/**
		 * 给定日期取得要 -month 的月份
		 * @param date
		 * @param month
		 * @return
		 */
		public static Date monthJian(Date date,int month){

            Calendar  g = Calendar.getInstance();  
            g.setTime(date);  
            g.add(Calendar.MONTH,-month);
            Date d2 = g.getTime();
            
            return d2;
		}
		/**
		 * 给定日期取得要 -month 的月份
		 * @param date
		 * @param month
		 * @param format 返回日期串的格式
		 * @return
		 */
		public static String monthJian(Date date,int month,String format){

            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(monthJian(date,month));
		}
		/**
		 * 给定的日期是否和当前时间差 arg个月 yyyyMMddHHmmss
		 * @param dateStr
		 * @param arg
		 * @return
		 */
		public static boolean isDateInArgMonthyyyyMMddHHmmss(String dateStr,int arg){
			Date beginDate = null;
			try {
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				beginDate = dateFormat.parse(dateStr);
			} catch (ParseException e) {
				logger.info("日期格式化错误");
			}
			Date date = new Date();
            Calendar  g = Calendar.getInstance();  
            g.setTime(date);  
            g.add(Calendar.MONTH,-arg);
            Date endDate = g.getTime();
            if(beginDate.before(endDate)){
            	return true;
            }else{
            	return false;
            }
		}
		/**
		 * 给定的日期是否和当前时间差 arg天数 yyyyMMddHHmmss
		 * @param dateStr
		 * @param arg
		 * @return
		 */
		public static boolean isDateInArgDaysyyyyMMddHHmmss(String dateStr,int arg){
			Date beginDate = null;
			try {
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				beginDate = dateFormat.parse(dateStr);
			} catch (ParseException e) {
				logger.info("日期格式化错误");
			}
			Date date = new Date();
            Calendar  g = Calendar.getInstance();  
            g.setTime(date);  
            g.add(Calendar.DATE,-arg);
            Date endDate = g.getTime();
            if(beginDate.before(endDate)){
            	return true;
            }else{
            	return false;
            }
		}
		/**
		 * 给定的日期是否和当前时间差 arg个月 yyyyMMdd
		 * @param dateStr
		 * @param arg
		 * @return
		 */
		public static boolean isDateInArgMonthyyyyMMdd(String dateStr,int arg){
			Date beginDate = null;
			try {
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				beginDate = dateFormat.parse(dateStr);
			} catch (ParseException e) {
				logger.info("日期格式化错误");
			}
			Date date = new Date();
            Calendar  g = Calendar.getInstance();  
            g.setTime(date);  
            g.add(Calendar.MONTH,-arg);
            Date endDate = g.getTime();
            if(beginDate.before(endDate)){
            	return true;
            }else{
            	return false;
            }
		}
		/**
		 * 计算两个固定格式日期相差的月数
		 * @param format 日期格式
		 * @param dateStart 
		 * @param dateEnd
		 * @return
		 */
		public static int getMonthSpace(String format, String dateStart, String dateEnd){

	        SimpleDateFormat sdf = new SimpleDateFormat(format);

	        Calendar c1 = Calendar.getInstance();
	        Calendar c2 = Calendar.getInstance();
	        try {
		        c1.setTime(sdf.parse(dateStart));
		        c2.setTime(sdf.parse(dateEnd));
	        } catch (ParseException e) {
				logger.info("日期格式化错误");
			}
	        int month = (c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR))*12+c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
	        if((c2.get(Calendar.DAY_OF_MONTH)-c1.get(Calendar.DAY_OF_MONTH))<0){
	        	month = month - 1;
	        }
	        return month;

	    }
		
		/**
		 * 给定的日期是否和当前时间差 arg个天 yyyyMMdd
		 * @param dateStr
		 * @param arg
		 * @return
		 */
		public static boolean isDateInArgDaysyyyyMMdd(String dateStr,int arg){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date beginDate = null;
			try {
				beginDate = sdf.parse(dateStr);
			} catch (ParseException e) {
				logger.info("日期格式化错误");
			}
			Date date = new Date();
            Calendar  g = Calendar.getInstance();  
            g.setTime(date);  
            g.add(Calendar.DATE,-arg);
            Date endDate = g.getTime();
            if(beginDate.before(endDate)){
            	return true;
            }else{
            	return false;
            }
		}
		/**
		 * 固定时间在计算时间前 返回true
		 * @param dateStr
		 * @param arg
		 * @return
		 */
		public static boolean isDateInArgDate(String dateStr,int arg){
			Date beginDate = null;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				beginDate = sdf.parse(dateStr);
			} catch (ParseException e) {
				logger.info("日期格式化错误");
			}
			Date date = new Date();
            Calendar  g = Calendar.getInstance();  
            g.setTime(date);  
            g.add(Calendar.DATE,-arg);
            Date endDate = g.getTime();
            if(beginDate.before(endDate)){
            	return true;
            }else{
            	return false;
            }
		}
		/**
		 * 获取分钟差
		 * @param timeOne
		 * @param timeTwo
		 * @return
		 * @throws ParseException
		 */
		public static long getMiniteBetweenTime(Date timeOne,Date timeTwo){

			long nd = 1000 * 24 * 60 * 60;
		    long nh = 1000 * 60 * 60;
		    long nm = 1000 * 60;
			long diff = timeTwo.getTime() - timeOne.getTime();
			long min = diff % nd % nh / nm;
			return min;
		}
		/**
		 * 获取分钟差
		 * @param timeOne
		 * @param timeTwo
		 * @return
		 * @throws ParseException
		 */
		public static long getHourBetweenTime(Date timeOne,Date timeTwo){

		    long nh = 1000 * 60 * 60;
			long diff = timeTwo.getTime() - timeOne.getTime();
			long min = diff / nh ;
			return min;
		}		
		/**
		 * 获取当天所在周的开始时间
		 * 2015年8月12日 上午10:32:04 wangw添加此方法
		 * @return
		 */
		public static Date firstDayOfWeek ()  {
			Calendar currentDate = new GregorianCalendar();   
			currentDate.setFirstDayOfWeek(Calendar.SUNDAY);  
			currentDate.set(Calendar.HOUR_OF_DAY, 0);  
			currentDate.set(Calendar.MINUTE, 0);  
			currentDate.set(Calendar.SECOND, 0);  
			currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);  
			return currentDate.getTime();
		}
		/**
		 * 获取当天周几
		 * @return
		 */
		public static int getDayOfWeek(){
			Calendar now = Calendar.getInstance();
			//一周第一天是否为星期天
			boolean isFirstSunday = (now.getFirstDayOfWeek() == Calendar.SUNDAY);
			//获取周几
			int weekDay = now.get(Calendar.DAY_OF_WEEK);
			//若一周第一天为星期天，则-1
			if(isFirstSunday){
			  weekDay = weekDay - 1;
			  if(weekDay == 0){
			    weekDay = 7;
			  }
			}
			return weekDay;
		}
		/**
		 * 获取前小时
		 * @return
		 */
		public static int getHour(){
			Calendar now = Calendar.getInstance();
			int hour = now.get(Calendar.HOUR_OF_DAY); 
//			System.out.println(hour);
			return hour;
		}
		
		public static String String2String(String date, String dateFormat){
			Date temdate = string2Date(date, DEFAULT_DATETIME_FORMAT);
			return date2String(temdate, dateFormat);
		}
		
		public static String shortString2String(String date, String dateFormat){
			Date temdate = string2Date(date, DATE_FORMAT_YYYYMMDD);
			return date2String(temdate, dateFormat);
		}
		
	/**
	 * 得到指定天数前的日期字符串
	 * @return
	 */
	public static String someDaysBeforeToday(int dayNum) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -dayNum);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(calendar.getTime());
	}
	/**
	 * 得到指定天数前的日期字符串yyyyMMddHHmmss
	 * @return
	 */
	public static String someDaysAfterTodayyyyyMMddHHmmss(String dateStr ,int months) {
		Date beginDate = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			beginDate = format.parse(dateStr);
		} catch (ParseException e) {
			logger.info("日期格式化错误");
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginDate);  
		calendar.add(Calendar.MONTH, months);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(calendar.getTime());
	}
	/**
	 * 得到指定天数前的日期字符串yyyyMMddHHmmss
	 * @return
	 */
	public static String todayAddDaysyyyyyMMddHHmmss(String dateStr ,int days) {
		Date beginDate = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			beginDate = format.parse(dateStr);
		} catch (ParseException e) {
			logger.info("日期格式化错误");
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginDate);  
		calendar.add(Calendar.DATE, days);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(calendar.getTime());
	}
	/**
	 * 得到指定天数前的日期字符串yyyyMMdd
	 * @return
	 */
	public static String someDaysAfterTodayyyyMMdd(String dateStr ,int months) {
		Date beginDate = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			beginDate = format.parse(dateStr);
		} catch (ParseException e) {
			logger.info("日期格式化错误");
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginDate);  
		calendar.add(Calendar.MONTH, months);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(calendar.getTime());
	}
	/**
	 * 得到指定天数前的日期字符串yyyyMMdd
	 * @return
	 */
	public static String todayAddDaysyyyyMMdd(String dateStr ,int days) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date beginDate = null;
		try {
			beginDate = df.parse(dateStr);
		} catch (ParseException e) {
			logger.info("日期格式化错误");
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginDate);  
		calendar.add(Calendar.DATE, days);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(calendar.getTime());
	}
	/**
	 * compare date return begin > end true else false
	 * @param begin
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static boolean compareDate(String begin,String end) throws ParseException{
		if(begin.equals(end)){
			return true;
		}
		DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
		DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
		Date beginDate = df1.parse(begin);
		Date endDate = df2.parse(end);
		return beginDate.before(endDate);
	}
	/**
	 * compare date return begin > end true else false
	 * @param begin
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static boolean compareDate(String begin,String end,String dateFormat){
		if(begin.equals(end)){
			return true;
		}
		DateFormat format = new SimpleDateFormat(dateFormat);
		Date beginDate = null;
		Date endDate = null;
		try {
			beginDate = format.parse(begin);
			endDate = format.parse(end);
		} catch (ParseException e) {
			logger.info("日期时间格式错误");
		}
		return beginDate.before(endDate);
	}
	
    /** 
     * 获得本周的第一天，周一 
     *  
     * @return 
     */  
	public static Date getCurrentWeekDayStartTime() {  
        Calendar c = Calendar.getInstance();  
        try {  
        	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;  
            c.add(Calendar.DATE, -weekday);  
            c.setTime(sdf.parse(df.format(c.getTime()) + " 00:00:00"));  
        } catch (Exception e) {  
            logger.error(e.getMessage());  
        }  
        return c.getTime();  
    }  
    /** 
     * 获得本周的最后一天，周日 
     *  
     * @return 
     */  
    public static Date getCurrentWeekDayEndTime() {  
        Calendar c = Calendar.getInstance();  
        try {  
        	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int weekday = c.get(Calendar.DAY_OF_WEEK);  
            c.add(Calendar.DATE, 8 - weekday);  
            c.setTime(sdf.parse(df.format(c.getTime()) + " 23:59:59"));  
        } catch (Exception e) {  
            logger.error(e.getMessage());  
        }  
        return c.getTime();  
    } 
    /** 
     * 获得本月的开始时间，即2012-01-01 00:00:00 
     *  
     * @return 
     */  
    public static Date getCurrentMonthStartTime() {  
        Calendar c = Calendar.getInstance();  
        Date now = null;  
        try { 
        	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            c.set(Calendar.DATE, 1);  
            now = sdf.parse(df.format(c.getTime()));  
        } catch (Exception e) {  
            logger.error(e.getMessage());  
        }  
        return now;  
    }  
    /** 
     * 当前月的结束时间，即2012-01-31 23:59:59 
     *  
     * @return 
     */  
    public static Date getCurrentMonthEndTime() {  
        Calendar c = Calendar.getInstance();  
        Date now = null;  
        try {  
        	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            c.set(Calendar.DATE, 1);  
            c.add(Calendar.MONTH, 1);  
            c.add(Calendar.DATE, -1);  
            now = sdf.parse(df.format(c.getTime()) + " 23:59:59");  
        } catch (Exception e) {  
            logger.error(e.getMessage());  
        }  
        return now;  
    }  
    /**
     * 获取上月第二天日期
     * @return
     */
    public static String getSecondDayofLastMonth(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 2);
        return sdf.format(cal.getTime());
    }
    /**
     * 获取当月第一天
     * @return
     */
    public static String getFirstDayofMonth(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.MONTH, 0);
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	return sdf.format(cal.getTime());
    }
    
    /**
     * 根据生日获取年龄（周岁）
     * @param birthday 生日格式为yyyyMMdd
     * @return
     */
    public static int getAge(String birthday){
    	//当前时间
    	Date nowDate = new Date();
    	Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(nowDate);
		int year = rightNow.get(Calendar.YEAR);
		int age = year-Integer.parseInt(birthday.substring(0, 4));
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		Date birthDate = null;
		try {
			birthDate = f.parse(birthday);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		rightNow.setTime(birthDate);
		rightNow.add(Calendar.YEAR, age);
		Date dt=rightNow.getTime();
		//如果当前时间在其生日之前，则未满周岁，需要减一岁
		if(nowDate.before(dt)){
			age = age - 1;
		}
    	return age;
    }
    public static int getYear(){
    	Date nowDate = new Date();
    	Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(nowDate);
		int year = rightNow.get(Calendar.YEAR);
		return year;
    }
    public static String getYYMMDD(){
    	DateFormat df = new SimpleDateFormat("yyMMdd");
    	Date nowDate = new Date();
    	return df.format(nowDate);
    }
    /**
     * yyyyMMddtoyyyy_MM_dd
     * @param loanTimeStr
     * @return
     */
	public static String formatyyyyMMddtoyyyy_MM_dd(String loanTimeStr){
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Date loanTime = null;
		try {
			loanTime = df.parse(loanTimeStr.replace("-", ""));
		} catch (ParseException e) {
			logger.info("PARSE LOAN TIME ERROR:{}",e);
		}
		return df1.format(loanTime);
	} 
	
	/**
     * 将yyyyMMdd格式的日期按分隔符进行分割
     * 
     * @return 格式化后的日期字符串。
     */
    public static String formatDateBySeparator(String date, String yearSeparator, String monthSeparator, String daySeparator) {
        String formatDate = null;
        if(date==null||date.length()!=8){
        	formatDate = date;
        }else{
        	formatDate = date.substring(0, 4)+yearSeparator+date.substring(4, 6)+monthSeparator+date.substring(6, 8)+daySeparator;
        }
        return formatDate;
    }
    /** 
     * 获取系统时间之星期几
     *  
     * @return 
     */  
	public static int getCurrentWeekDay(String dateStr) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try {
			date = df.parse(dateStr);
		} catch (ParseException e) {
			logger.info("日期格式化失败");
		}
        Calendar c = Calendar.getInstance();  
        c.setTime(date);
        int w = c.get(Calendar.DAY_OF_WEEK) - 1;
        return w;  
    }
	/**
	 * 获取前小时
	 * @return
	 */
	public static int getDayOfMonth(String dateStr){
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try {
			date = df.parse(dateStr);
		} catch (ParseException e) {
			logger.info("日期格式化失败");
		}
        Calendar c = Calendar.getInstance();  
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return day;  
	}
	/**
	 * 获取当天剩余秒
	 * @return
	 */
	public static long getRemainingTime(){
		long totalSecond = 60 * 60 * 24;
		Calendar cal = Calendar.getInstance();
		int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
	    int minute = cal.get(Calendar.MINUTE);
	    int second = cal.get(Calendar.SECOND);
		long current = hourOfDay * 60 * 60 + minute * 60 + second;
		return totalSecond - current;
	}
	/**
	 * 两个日期相差天数 结果为+ -
	 * @param dateStr1
	 * @param dateStr2
	 * @return
	 */
	 public static int differentDaysByMillisecond(String dateStr1,String dateStr2){
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		int days = 0;
		try {
			Date date1 = df.parse(dateStr1);
			Date date2 = df.parse(dateStr2);
			days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
		} catch (ParseException e) {
			logger.info("日期相减方法错误：{}",e);
		}
		return days;
	}

	/**
	 * 根据开始时间和结束时间返回时间段内的时间集合
	 * @param beginDate
	 * @param endDate
	 * @return List
	 */
	public static List<String> getDatesBetweenTwoDate(String beginDateStr, String endDateStr) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date beginDate = sdf.parse(beginDateStr);
		Date endDate = sdf.parse(endDateStr);
		List<String> lDate = new ArrayList<String>();
		lDate.add(beginDateStr);// 把开始时间加入集合
		Calendar cal = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		cal.setTime(beginDate);
		while (true) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			cal.add(Calendar.DAY_OF_MONTH, 1);
			// 测试此日期是否在指定日期之后
			if (endDate.after(cal.getTime())) {
				String day = sdf.format(cal.getTime());
				lDate.add(day);
			} else {
				break;
			}
		}
		if (!endDateStr.equals(beginDateStr)) {
			lDate.add(endDateStr);// 把结束时间加入集合
		}
		return lDate;
	}

	/**
	 * 获取给定时间的下一天
	 * @param beginDate
	 * @param endDate
	 * @return List
	 */
	public static String getNextDay(String specialDate) {
		Date utilDate = null;
		try {
			utilDate = new SimpleDateFormat("yyyyMMdd").parse(specialDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cl = Calendar.getInstance();
		cl.setTime(utilDate);
		int day = cl.get(Calendar.DATE);
		cl.set(Calendar.DATE, day + 1);
		String nextDay = new SimpleDateFormat("yyyyMMdd").format(cl.getTime());
		return nextDay;
	}
	public static void main(String[] args) throws Exception {  
		System.out.println(getDatesBetweenTwoDate("20180423", "20180423"));
//		System.out.println(getCurrentWeekDay("20170725"));
//		System.out.println(getHour());
//		System.out.println(getDayOfMonth("20170625"));
//		System.out.println(date2String(add(string2Date("20170725", "yyyyMMdd"), 1), "yyyyMMdd"));
//		System.out.println(differentDaysByMillisecond("20170625", "20170525"));
//		String dateStr = getCurrentDateAsString("yyMMdd");
//		System.out.println(dateStr);
		
//	final String authTime = "20170525";
//	final int resusedLong = 30;
	
    //同时启动1000个线程，去进行i++计算，看看实际结果  
//    for (int i = 0; i < 1000; i++) {  
//        new Thread(new Runnable() {  
//            @Override  
//            public void run() {  
//            	boolean isCanLoan = DateUtil.isDateInArgMonthyyyyMMdd(authTime,resusedLong);
//        		System.out.println(isCanLoan);
//            }  
//        }).start();  
//    }  
    //这里每次运行的值都有可能不同,可能不为1000  
//    System.out.println("运行结果:Counter.count=" + Counter.count);  
}  
}
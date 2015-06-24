package com.wonders.framework.utils;

import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.StringUtils;

import com.wonders.framework.common.asset.AssertUtil;

public class DateUtil {
	
	public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String DF_YYYY = "yyyy";
	public static final String DF_MM = "MM";
	public static final String DF_YYYY_MM = "yyyy-MM";
	
	public static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	private static List<Calendar> holidayList;

	private static boolean holidayFlag;

	/**
	 * 计算工作日
	 * 具体节日包含哪些,可以在HolidayMap中修改
	 * @param src* 日期(源)
	 * @param adddays要加的天数
	 * @exception throws [违例类型] [违例说明]
	 */

	public static String addDateByWorkDay(String srcStr, int adddays){
		
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		 
		Date date = null;
		try {
			date = sdf.parse(srcStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 
		Calendar src = Calendar.getInstance();
		src.setTime(date);

		holidayFlag = false;

		for (int i = 0; i < adddays; i++){
			// 把源日期加一天

			src.add(Calendar.DAY_OF_MONTH, 1);
			holidayFlag = checkHoliday(src);
			if (holidayFlag){
				i--;
			}
//			System.out.println(src.getTime());
		}
//		System.out.println("Final Result:" + src.getTime());
		
		return sdf.format(src.getTime());
	}
	/**
	 * 计算两个日期之间的工作日天数
	 * 具体节日包含哪些,可以在HolidayMap中修改
	 * @param src* 日期(源)
	 * @param adddays要加的天数
	 * @exception throws [违例类型] [违例说明]
	 */

	public static int countTwoDatesOfWorkDays(String beginDate, String endDate){
		
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		 
		Date date = null;
		Date dateEnd = null;
		try {
			date = sdf.parse(beginDate);
			dateEnd = sdf.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 
		Calendar src = Calendar.getInstance();
		src.setTime(date);
		Calendar srcEnd = Calendar.getInstance();
		srcEnd.setTime(dateEnd);

		boolean holidayF = false;

		int result = 0;  
		while (src.compareTo(srcEnd) <= 0) {  
			holidayF = checkHoliday(src);
			if (!holidayF){
				result++;
			}
			src.add(Calendar.DAY_OF_MONTH, 1);
		}  

		return result;
	}

	/**
	 * 校验指定的日期是否在节日列表中
	 * 具体节日包含哪些,可以在HolidayMap中修改
	 * @param src要校验的日期(源)
	 */

	public static boolean checkHoliday(Calendar src){

		boolean result = false;
		if (holidayList == null){
			initHolidayList();
		}

		// 先检查是否是周六周日(有些国家是周五周六)
		if (src.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
		|| src.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			return true;
		}

		for (Calendar c : holidayList){

			if (src.get(Calendar.MONTH) == c.get(Calendar.MONTH)
			&& src.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)){
				result = true;
			}
		}

		return result;
	}

	/**
	 * 初始化节日List,如果需要加入新的节日,请在这里添加
	 * 加的时候请尽量使用Calendar自带的常量而不是魔鬼数字
	 * 注:年份可以随便写,因为比的时候只比月份和天
	 */

	private static void initHolidayList(){

		holidayList = new ArrayList();
		/*// 五一劳动节
		Calendar may1 = Calendar.getInstance();
		may1.set(Calendar.MONTH, Calendar.MAY);
		may1.set(Calendar.DAY_OF_MONTH, 1);
		holidayList.add(may1);

		Calendar may2 = Calendar.getInstance();
		may2.set(Calendar.MONTH, Calendar.MAY);
		may2.set(Calendar.DAY_OF_MONTH, 2);

		holidayList.add(may2);

		Calendar may3 = Calendar.getInstance();
		may3.set(Calendar.MONTH, Calendar.MAY);
		may3.set(Calendar.DAY_OF_MONTH, 3);
		holidayList.add(may3);

		Calendar h3 = Calendar.getInstance();
		h3.set(2000, 1, 1);
		holidayList.add(h3);

		Calendar h4 = Calendar.getInstance();
		h4.set(2000, 12, 25);

		holidayList.add(h4);

		// 中国母亲节：五月的第二个星期日

		Calendar may5 = Calendar.getInstance();

		// 设置月份为5月

		may5.set(Calendar.MONTH, Calendar.MAY);

		// 设置星期:第2个星期

		may5.set(Calendar.DAY_OF_WEEK_IN_MONTH, 2);

		// 星期日

		may5.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		// System.out.println(may5.getTime());

		holidayList.add(may5);*/

	}
	
	public static String addDateByDay(String srcStr, int adddays){
		
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		 
		Date date = null;
		try {
			date = sdf.parse(srcStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 
		Calendar src = Calendar.getInstance();
		src.setTime(date);
		src.add(Calendar.DATE, adddays);

		return sdf.format(src.getTime());
	}
	
	public static String getThisYear(){
		return formartDate(new Date(System.currentTimeMillis()),DF_YYYY);
	}
	
	public static String getThisMonth(){
		return formartDate(new Date(System.currentTimeMillis()),DF_MM);
	}
	
	public static String getThisYearMonth(){
		return formartDate(new Date(System.currentTimeMillis()),DF_YYYY_MM);
	}
	
	public static String getToday(){
		return formartDate(new Date(System.currentTimeMillis()),DF_YYYY_MM_DD);
	}
	
	
	public static String getNowTime(){
		return formartDate(new Date(System.currentTimeMillis()),DF_YYYY_MM_DD_HH_MM_SS);
	}
	
	public static String formartDate(Date date,String pattern){
		
		AssertUtil.hasLength(pattern,"date format pattern can not be null.");
		AssertUtil.notNull(date,"the date is null can not be formatted.");
		
		return new SimpleDateFormat(pattern).format(date);
	}
	
	public static String getFirstDateOfThisWeek() throws ParseException{
		return getDateOfWeek(getToday(),Calendar.MONDAY,DF_YYYY_MM_DD);
	}
	
	public static String getLastDateOfThisWeek() throws ParseException{
		return getDateOfWeek(getToday(),Calendar.SUNDAY,DF_YYYY_MM_DD);
	}

	
	public static String getFirstDateOfMonth(Date date){
		return formartDate(date,DF_YYYY_MM)+"-01";
	}
	public static String getFirstDateOfYear(Date date){
		return formartDate(date,DF_YYYY)+"-01-01";
	}
	public static String getLastDateOfYear(Date date){
		return formartDate(date,DF_YYYY)+"-12-31";
	}
	
	public static String getMonthFromDateString(String dateString){
		AssertUtil.hasLength(dateString,"date string can not be null.");
		if(!StringUtils.hasLength(dateString))
			return "-1";
		return dateString.substring(dateString.indexOf("-")+1,dateString.lastIndexOf("-"));
	}
	
	public static String getLastDateOfMonth(Date date) throws ParseException{
		String firstDayOfMonth = getFirstDateOfMonth(date);
		date  =  formatDateString(firstDayOfMonth,DF_YYYY_MM_DD);  
        Calendar  calendar  =  Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.MONTH,1);  
        calendar.add(Calendar.DAY_OF_YEAR,  -1);  
        return  formartDate(calendar.getTime(),DF_YYYY_MM_DD);  
	}
	
	public static Date formatDateString(String dateString,
			String pattern) throws ParseException {
		return new SimpleDateFormat(pattern).parse(dateString);
	}

	/**
	 * 
	 * @param date format as yyyy-MM-dd
	 * @param dateOfWeek 1--sunday 7--SATURDAY we can use Calender.MONDAY etc.
	 * @return
	 * @throws ParseException 
	 */
	public static String getDateOfWeek(String date,int dateOfWeek,String pattern) throws ParseException{
		//initial parameter 
		int datePlus = 0,dayOfWeek =0;
		String dateToRetrun = null;
		SimpleDateFormat df = null;
		Date d = null;
		Calendar cd = null;
		Date dToReturn = null;
		GregorianCalendar currentDate = null;
		
		//check the parameters
		AssertUtil.hasLength(date,"No date string found.");
		AssertUtil.isTrue(dateOfWeek>0&&dateOfWeek<8,"input date of week must in rage 1~7.");
		if(!StringUtils.hasLength(pattern))
			pattern = DF_YYYY_MM_DD;
		
		try {
			//start logic process
			dateOfWeek -= 1;
			if(dateOfWeek == 0)
				dateOfWeek = 7;
			//1.parse the date input
			df = new SimpleDateFormat(pattern);
			d = df.parse(date);
			
			//2.set the calendar to the date we input
			cd = Calendar.getInstance();
			cd.setTime(d);
			
			//3.get the day of this week 
			dayOfWeek = cd.get(Calendar.DAY_OF_WEEK)-1;
			dayOfWeek = dayOfWeek == 0?7:dayOfWeek;
				
			datePlus = dateOfWeek - dayOfWeek;
			
			currentDate = new GregorianCalendar();
			currentDate.setTime(d);
			currentDate.add(GregorianCalendar.DATE, datePlus);
			dToReturn = currentDate.getTime();

			dateToRetrun = df.format(dToReturn);
		} catch (ParseException e) {
			throw e;
		}finally{
			
		}
		
		return dateToRetrun;
	}
	
	public static Calendar addDateByMonth(String srcStr, int addmonths){
		
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		 
		Date date = null;
		try {
			date = sdf.parse(srcStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 
		Calendar src = Calendar.getInstance();
		src.setTime(date);
		src.add(Calendar.MONTH, addmonths);

		return src;
	}
	
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
//		Calendar src = Calendar.getInstance();
//		 
//        src.set(2010, Calendar.APRIL , 29);
// 
       String result = addDateByWorkDay("2013-11-12", 5);
       System.out.println("--------"+result);
       
       System.out.println("--------"+countTwoDatesOfWorkDays("2013-11-12","2013-11-12"));
// 
//        Calendar expected = Calendar.getInstance();
// 
//        expected.set(2010, Calendar.MAY , 4);
/*//        System.out.println(expected.getTime().toString()+"--------"+result);
		System.out.println(getThisYear());
		System.out.println(DateUtil.getLastDateOfThisWeek());
		String sss = "2013-10-29";
		System.out.println(sss.substring(0,10).length());
//		UUID.randomUUID().toString();
		System.out.println(UUID.randomUUID().toString());
		System.out.println(UUID.randomUUID().toString());

		System.out.println(UUID.randomUUID().toString());

		System.out.println(UUID.randomUUID().toString());
		System.out.println("".equals(null));*/
		
       String key ="projectName_2LIKE+receiveDate_LIKE_OR";
		
       int ln =key.lastIndexOf("_");
       int fn = key.indexOf("_");
		String name = substringBefore(key, "_");
		String operator = substringAfter(key, "_"); 
		
	       System.out.println(key.substring(0,ln)+"--------"+key.substring(ln+1));
	       System.out.println(ln+"--------"+fn+"2222222222::::"+key.indexOf("projectName_2LIKE"));

	}

}

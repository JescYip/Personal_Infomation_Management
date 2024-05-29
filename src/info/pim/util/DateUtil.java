package info.pim.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {
	public static String dateFormat = "yyyy-MM-dd";
	public static String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取当前时间的日期字符串
	 * @return 返回格式为yyyy-MM-dd
	 */
	public static String date(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	/**
	 * 获取当前时间的日期时间字符串
	 * @return 返回格式为yyyy-MM-dd HH:mm:ss
	 */
	public static String dateTime(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
		return sdf.format(date);
	}

	/**
	 * 获取指定时间的日期字符串
	 * @return 返回格式为yyyy-MM-dd
	 */
	public static String date(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			System.out.println("The format is inconsistent, return null");
		}
		return sdf.format(date);
	}

	/**
	 * 获取指定时间的日期时间字符串
	 * @return 返回格式为yyyy-MM-dd HH:mm:ss
	 */
	public static String dateTime(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			System.out.println("The format is inconsistent, return null");
		}
		return sdf.format(date);
	}
}


/**
 * 
 */
package commons.lang;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:23:49 AM Nov 25, 2013
 */
public class DateUtils {

	private static final Log LOG = LogFactory.getLog(DateUtils.class);

	public static final String yyyyMM = "yyyyMM";
	public static final String yyyyMMdd = "yyyyMMdd";
	public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";

	public static final String yyyy$MM$dd = "yyyy-MM-dd";
	public static final String yyyy$MM$dd$HH$mm$ss = "yyyy-MM-dd HH:mm:ss";
	public static final String yyyy$MM$ddTHH$mm$ss = "yyyy-MM-dd'T'HH:mm:ss";

	/**
	 * String转换成Date
	 * 
	 * <pre>
	 * 支持格式：
	 * yyyyMM
	 * yyyyMMdd
	 * yyyy-MM-dd
	 * yyyyMMddHHmmss
	 * yyyy-MM-dd HH:mm:ss
	 * yyyy-MM-dd'T'HH:mm:ss
	 * </pre>
	 * @param date
	 * @return
	 */
	public static Date parseDate(String date) {
		switch (date.length()) {
		case 6:
			return parseDate(date, yyyyMM);
		case 8:
			return parseDate(date, yyyyMMdd);
		case 10:
			return parseDate(date, yyyy$MM$dd);
		case 14:
			return parseDate(date, yyyyMMddHHmmss);
		case 19:
			return parseDate(date, yyyy$MM$dd$HH$mm$ss, yyyy$MM$ddTHH$mm$ss);
		default:
			LOG.error("can not get the Date:" + date + "'s pattern");
			return null;
		}
	}

	/**
	 * String转换成Date，指定格式
	 * @param date
	 * @param patterns
	 * @return
	 */
	public static Date parseDate(String date, String... patterns) {
		try {
			return org.apache.commons.lang3.time.DateUtils.parseDate(date, patterns);
		}
		catch (ParseException e) {
			// ignore
			LOG.error("error to parse Date:" + date + "by pattern:" + ArrayUtils.toString(patterns) + "\r\n", e);
			return null;
		}
	}

	/**
	 * String转换成Timestamp
	 * @param date
	 * @return
	 */
	public static Timestamp parseTimestamp(String date) {
		final Date d = parseDate(date);
		return d != null ? newTimestamp(d) : null;
	}

	/**
	 * String转换成Timestamp，指定格式
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Timestamp parseTimestamp(String date, String pattern) {
		final Date d = parseDate(date, pattern);
		return d != null ? newTimestamp(d) : null;
	}

	/**
	 * Date转换成String
	 * 
	 * <pre>
	 * 带时分秒转成yyyy-MM-dd HH:mm:ss
	 * 否则转换成yyyy-MM-dd
	 * </pre>
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		final long millis = date.getTime();
		final int offset = TimeZone.getDefault().getOffset(millis);
		if ((millis + offset) % DateStatics.ONE_DAY_MILLIS == 0) {
			return formatDate(date, yyyy$MM$dd);
		}
		else {
			return formatDate(date, yyyy$MM$dd$HH$mm$ss);
		}
	}

	/**
	 * Date转换成String，指定格式
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * 获取当前时间的String字符串
	 * @return
	 */
	public static String formatDate() {
		return formatDate(new Date(), yyyy$MM$dd$HH$mm$ss);
	}

	/**
	 * 获取当前时间的String字符串，指定格式
	 * @param pattern
	 * @return
	 */
	public static String formatDate(String pattern) {
		return formatDate(new Date(), pattern);
	}

	/**
	 * 创建Timestamp对象
	 * @param millis
	 * @return
	 */
	public static Timestamp newTimestamp(long millis) {
		return new Timestamp(millis);
	}

	/**
	 * 创建Timestamp对象，当前时间
	 * @return
	 */
	public static Timestamp newTimestamp() {
		return newTimestamp(System.currentTimeMillis());
	}

	/**
	 * 创建Timestamp对象
	 * @param date
	 * @return
	 */
	public static Timestamp newTimestamp(Date date) {
		return newTimestamp(date.getTime());
	}

	/**
	 * 创建Date对象，当前时间
	 * @return
	 */
	public static Date newDate() {
		return new Date();
	}

	/**
	 * 获取当前年月，格式yyyyMM
	 * @return
	 */
	public static String getYearMonth() {
		return formatDate(new Date(), yyyyMM);
	}

	/**
	 * 解析成 HH:mm:ss.SSS
	 * @param durationMills 毫秒数
	 * @return
	 */
	public static String formatExpression(long durationMills) {
		return formatExpression(durationMills, TimeUnit.MILLISECONDS);
	}

	/**
	 * 解析成 HH:mm:ss.SSS
	 * @param duration
	 * @param unit
	 * @return
	 */
	public static String formatExpression(long duration, TimeUnit unit) {
		long millis;

		final StringBuilder result = new StringBuilder();

		millis = unit.toMillis(duration);
		final long hours = millis / DateStatics.ONE_HOUR_MILLIS;
		if (hours < 10) {
			result.append('0');
		}
		result.append(hours).append(":");

		millis = millis % DateStatics.ONE_HOUR_MILLIS;
		final long minutes = millis / DateStatics.ONE_MINUTE_MILLIS;
		if (minutes < 10) {
			result.append('0');
		}
		result.append(minutes).append(":");

		millis = millis % DateStatics.ONE_MINUTE_MILLIS;
		final long seconds = millis / DateStatics.ONE_SECOND_MILLIS;
		if (seconds < 10) {
			result.append('0');
		}
		result.append(seconds);

		millis = millis % DateStatics.ONE_SECOND_MILLIS;
		if (millis != 0) {
			result.append('.').append(millis);
		}

		return result.toString();
	}

	/**
	 * 获取当前的年份
	 * @return
	 */
	public static int getCurrentYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	/**
	 * <pre>
	 * 获取date与当前时间相差的年数，不满一年按会被截取。
	 * </pre>
	 * @see #truncateDifferYears(Date, Date)
	 * @param date
	 * @return
	 */
	public static int truncateDifferYears(Date date) {
		return truncateDifferYears(date, new Date());
	}

	/**
	 * <pre>
	 * 获取两个日期间相差的年数，不满一年按会被截取。
	 * </pre>
	 * @see #calculateDifferYears(Date, Date, boolean)
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int truncateDifferYears(Date date1, Date date2) {
		return calculateDifferYears(date2, date1, true);
	}

	/**
	 * <pre>
	 * 获取date与当前时间相差的年数。
	 * </pre>
	 * @see #getDifferYears(Date, Date)
	 * @param date
	 * @return
	 */
	public static int getDifferYears(Date date) {
		return getDifferYears(date, new Date());
	}

	/**
	 * <pre>
	 * 获取两个日期间相差的年数。
	 * </pre>
	 * @see #calculateDifferYears(Date, Date, boolean)
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDifferYears(Date date1, Date date2) {
		return calculateDifferYears(date1, date2, false);
	}

	/**
	 * <pre>
	 * 获取两个日期间相差的年数
	 * 
	 * isTruncate = true >> 不满一年会被截取
	 * eg：本年2月10号与明年2月10号之前的日期不算1年
	 * DateUtils.calculateDifferYears(20130202,20140201,true) = 0
	 * DateUtils.calculateDifferYears(20130202,20140202,true) = 1
	 * DateUtils.calculateDifferYears(20130202,20150203,true) = 2
	 * 
	 * isTruncate = false >> 忽略month、day计算
	 * DateUtils.calculateDifferYears(20130202,20140201,false) = 1
	 * DateUtils.calculateDifferYears(20130202,20140202,false) = 1
	 * DateUtils.calculateDifferYears(20130202,20150203,false) = 2
	 * </pre>
	 * @param date1
	 * @param date2
	 * @param isTruncate
	 * @return
	 */
	private static int calculateDifferYears(Date date1, Date date2, boolean isTruncate) {
		final int[] bgnDate, endDate;
		if (date1.getTime() > date2.getTime()) {
			bgnDate = getYearMonthDay(date2);
			endDate = getYearMonthDay(date1);
		}
		else {
			bgnDate = getYearMonthDay(date1);
			endDate = getYearMonthDay(date2);
		}

		if (isTruncate) {
			if (endDate[1] > bgnDate[1]) {
				return endDate[0] - bgnDate[0];
			}
			else if (endDate[1] == bgnDate[1]) {
				if (endDate[2] >= bgnDate[2]) {
					return endDate[0] - bgnDate[0];
				}
				else {
					return endDate[0] - bgnDate[0] - 1;
				}
			}
			else {
				return endDate[0] - bgnDate[0] - 1;
			}
		}
		else {
			return endDate[0] - bgnDate[0];
		}
	}

	/**
	 * <pre>
	 * 获取date与当前时间相差的月数，不满一个月按会被截取。
	 * </pre>
	 * @see #truncateDifferMonths(Date, Date)
	 * @param date
	 * @return
	 */
	public static int truncateDifferMonths(Date date) {
		return truncateDifferMonths(date, new Date());
	}

	/**
	 * <pre>
	 * 获取两个日期间相差的月数，不满一个月按会被截取。
	 * </pre>
	 * @see #calculateDifferMonths(Date, Date, boolean)
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int truncateDifferMonths(Date date1, Date date2) {
		return calculateDifferMonths(date1, date2, true);
	}

	/**
	 * <pre>
	 * 获取date与当前时间相差的月数
	 * </pre>
	 * @see #getDifferMonths(Date, Date)
	 * @param date
	 * @return
	 */
	public static int getDifferMonths(Date date) {
		return getDifferMonths(date, new Date());
	}

	/**
	 * <pre>
	 * 获取两个日期间相差的月数
	 * </pre>
	 * @see #calculateDifferMonths(Date, Date, boolean)
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDifferMonths(Date date1, Date date2) {
		return calculateDifferMonths(date1, date2, false);
	}

	/**
	 * <pre>
	 * 获取两个日期间相差的月数
	 * 
	 * isTruncate = true >> 不满一个月会被截取
	 * eg：本月10号与下个月10号之前的日期不算1个月
	 * DateUtils.calculateDifferMonths(20140102,20140201,true) = 0
	 * DateUtils.calculateDifferMonths(20130102,20140202,true) = 13
	 * DateUtils.calculateDifferMonths(20120202,20140203,true) = 25
	 * 
	 * isTruncate = false >> 忽略day计算
	 * DateUtils.calculateDifferMonths(20140102,20140201,false) = 1
	 * DateUtils.calculateDifferMonths(20130102,20140202,false) = 13
	 * DateUtils.calculateDifferMonths(20120202,20140203,false) = 25
	 * </pre>
	 * @param date1
	 * @param date2
	 * @return
	 */
	private static int calculateDifferMonths(Date date1, Date date2, boolean isTruncate) {
		final int[] bgnDate, endDate;
		if (date1.getTime() > date2.getTime()) {
			bgnDate = getYearMonthDay(date2);
			endDate = getYearMonthDay(date1);
		}
		else {
			bgnDate = getYearMonthDay(date1);
			endDate = getYearMonthDay(date2);
		}

		final int endMonth;
		if (!isTruncate || endDate[2] >= bgnDate[2]) {
			endMonth = endDate[1];
		}
		else {
			endMonth = endDate[1] - 1;
		}

		if (endMonth >= bgnDate[1]) {
			return (endDate[0] - bgnDate[0]) * 12 + (endMonth - bgnDate[1]);
		}
		else {
			return (endDate[0] - bgnDate[0] - 1) * 12 + (12 + endMonth - bgnDate[1]);
		}
	}

	/**
	 * <pre>
	 * 获取当前时间的年、月、日
	 * </pre>
	 * @see #getYearMonthDay(Date)
	 * @return
	 */
	public static int[] getCurrentYearMonthDay() {
		return getYearMonthDay(new Date());
	}

	/**
	 * <pre>
	 * 获取date的年、月、日。
	 * 全部从1开始。
	 * </pre>
	 * @param date
	 * @return [年，月，日]
	 */
	public static int[] getYearMonthDay(Date date) {
		Calendar bgnDate = Calendar.getInstance();
		bgnDate.setTime(date);
		return new int[] { bgnDate.get(Calendar.YEAR), bgnDate.get(Calendar.MONTH) + 1, bgnDate.get(Calendar.DATE) };
	}

	/**
	 * @see #parseDDHHMMSS(long, TimeUnit)
	 * @param duration
	 * @return
	 */
	public static long[] parseDDHHMMSS(long duration) {
		return parseDDHHMMSS(duration, TimeUnit.MILLISECONDS);
	}

	/**
	 * <pre>
	 * 解析成 int[]{天, 时, 分, 秒}
	 * </pre>
	 * @param duration
	 * @param unit
	 * @return [dd, HH, mm, ss]
	 */
	public static long[] parseDDHHMMSS(long duration, TimeUnit unit) {
		final long[] results = new long[4];

		long millis = unit.toMillis(duration);
		// days
		results[0] = millis / DateStatics.ONE_DAY_MILLIS;
		// hours
		millis = millis % DateStatics.ONE_DAY_MILLIS;
		results[1] = millis / DateStatics.ONE_HOUR_MILLIS;
		// minutes
		millis = millis % DateStatics.ONE_HOUR_MILLIS;
		results[2] = millis / DateStatics.ONE_MINUTE_MILLIS;
		// seconds
		millis = millis % DateStatics.ONE_MINUTE_MILLIS;
		results[3] = millis / DateStatics.ONE_SECOND_MILLIS;

		return results;
	}
}

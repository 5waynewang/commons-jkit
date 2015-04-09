/**
 * 
 */
package commons.lang;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:23:09 AM Nov 25, 2013
 */
public interface DateStatics {
	/**
	 * 1分钟的秒数
	 */
	long ONE_MINUTE_SECONDS = 60;
	/**
	 * 1小时的秒数
	 */
	long ONE_HOUR_SECONDS = 60 * ONE_MINUTE_SECONDS;
	/**
	 * 1天的秒数
	 */
	long ONE_DAY_SECONDS = 24 * ONE_HOUR_SECONDS;
	/**
	 * 1秒钟的毫秒数
	 */
	long ONE_SECOND_MILLIS = 1000;
	/**
	 * 1分钟的毫秒数
	 */
	long ONE_MINUTE_MILLIS = ONE_MINUTE_SECONDS * 1000;
	/**
	 * 1小时的毫秒数
	 */
	long ONE_HOUR_MILLIS = 60 * ONE_MINUTE_MILLIS;
	/**
	 * 1天的毫秒数
	 */
	long ONE_DAY_MILLIS = 24 * ONE_HOUR_MILLIS;
	/**
	 * 1周的毫秒数
	 */
	long ONE_WEEK_MILLIS = 7 * ONE_DAY_MILLIS;
}
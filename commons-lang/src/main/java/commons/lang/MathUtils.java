/**
 * 
 */
package commons.lang;

import java.util.concurrent.ThreadLocalRandom;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:23:03 AM Aug 22, 2016
 */
public class MathUtils {

	/**
	 * 概率判断，达到 num/disvisor 的返回true
	 * 
	 * @param num
	 * @param divisor
	 * @return
	 */
	public static boolean randomPer(long num, long divisor) {
		return ThreadLocalRandom.current().nextLong(0, divisor) < num;
	}

	public static boolean randomPer100(long num) {
		return randomPer(num, 100);
	}

	public static boolean randomPer1k(long num) {
		return randomPer(num, 1000);
	}

	public static boolean randomPer10k(long num) {
		return randomPer(num, 10000);
	}
}

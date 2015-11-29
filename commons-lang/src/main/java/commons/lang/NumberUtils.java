/**
 * 
 */
package commons.lang;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:24:41 AM Nov 25, 2013
 */
public class NumberUtils {

	private static final Log LOG = LogFactory.getLog(NumberUtils.class);

	public static final byte ZERO_BYTE = 0;
	public static final short ZERO_SHORT = 0;
	public static final int ZERO_INT = 0;
	public static final long ZERO_LONG = 0;
	public static final float ZERO_FLOAT = 0f;
	public static final double ZERO_DOUBLE = 0d;

	/**
	 * n1是否等于n2
	 * 
	 * <pre>
	 * NumberUtils.eq(null, null)  = true
	 * NumberUtils.eq(0, null)  = false
	 * NumberUtils.eq(555, 555) = true
	 * NumberUtils.eq(555, 666) = false
	 * </pre>
	 * 
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static boolean eq(Number n1, Number n2) {
		return n1 == n2 || (n1 != null && n1.doubleValue() == n2.doubleValue());
	}

	/**
	 * n是否等于0
	 * 
	 * <pre>
	 * NumberUtils.eqZero(null)  = false
	 * NumberUtils.eqZero(0)  = true
	 * NumberUtils.eqZero(555) = false
	 * </pre>
	 * 
	 * @param n
	 * @return
	 */
	public static boolean eqZero(Number n) {
		return eq(n, 0);
	}

	/**
	 * s是否等于0
	 * 
	 * <pre>
	 * NumberUtils.eqZero(null)  = false
	 * NumberUtils.eqZero("")  = false
	 * NumberUtils.eqZero("  ")  = false
	 * NumberUtils.eqZero("0")  = true
	 * NumberUtils.eqZero("555") = false
	 * NumberUtils.eqZero("string") = false
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static boolean eqZero(String s) {
		try {
			return s != null && eq(Double.valueOf(s), 0);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * n是否等于0或null
	 * 
	 * <pre>
	 * NumberUtils.eqZeroOrNull(null)  = true
	 * NumberUtils.eqZeroOrNull(0)  = true
	 * NumberUtils.eqZeroOrNull(555) = false
	 * </pre>
	 * 
	 * @param n
	 * @return
	 */
	public static boolean eqZeroOrNull(Number n) {
		return n == null || eqZero(n);
	}

	/**
	 * s是否等于0或null
	 * 
	 * <pre>
	 * NumberUtils.eqZeroOrNull(null)  = true
	 * NumberUtils.eqZeroOrNull("0")  = true
	 * NumberUtils.eqZeroOrNull("") = false
	 * NumberUtils.eqZeroOrNull("   ") = false
	 * NumberUtils.eqZeroOrNull("555") = false
	 * NumberUtils.eqZeroOrNull("string") = false
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static boolean eqZeroOrNull(String s) {
		try {
			return s == null || eq(Double.valueOf(s), 0);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * s是否等于0或空
	 * 
	 * <pre>
	 * NumberUtils.eqZeroOrBlank(null)  = true
	 * NumberUtils.eqZeroOrBlank("0")  = true
	 * NumberUtils.eqZeroOrBlank("") = true
	 * NumberUtils.eqZeroOrBlank("   ") = true
	 * NumberUtils.eqZeroOrBlank("555") = false
	 * NumberUtils.eqZeroOrBlank("string") = false
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static boolean eqZeroOrBlank(String s) {
		try {
			return StringUtils.isBlank(s) || eq(Double.valueOf(s), 0);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * n是否大于0
	 * 
	 * <pre>
	 * NumberUtils.gtZero(null)  = false
	 * NumberUtils.gtZero(-1)  = false
	 * NumberUtils.gtZero(0) = false
	 * NumberUtils.gtZero(1) = true
	 * </pre>
	 * 
	 * @param n
	 * @return
	 */
	public static boolean gtZero(Number n) {
		return n != null && n.doubleValue() > 0;
	}

	/**
	 * s是否大于0
	 * 
	 * <pre>
	 * NumberUtils.gtZero(null)  = false
	 * NumberUtils.gtZero("-1")  = false
	 * NumberUtils.gtZero("0") = false
	 * NumberUtils.gtZero("1") = true
	 * NumberUtils.gtZero("string") = false
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static boolean gtZero(String s) {
		try {
			return s != null && Double.parseDouble(s) > 0;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * n是否大于或等于0
	 * 
	 * <pre>
	 * NumberUtils.geZero(null)  = false
	 * NumberUtils.geZero(-1)  = false
	 * NumberUtils.geZero(0) = true
	 * NumberUtils.geZero(1) = true
	 * </pre>
	 * 
	 * @param n
	 * @return
	 */
	public static boolean geZero(Number n) {
		return n != null && n.doubleValue() >= 0;
	}

	/**
	 * s是否大于或等于0
	 * 
	 * <pre>
	 * NumberUtils.geZero(null)  = false
	 * NumberUtils.geZero("-1")  = false
	 * NumberUtils.geZero("0") = true
	 * NumberUtils.geZero("1") = true
	 * NumberUtils.geZero("string") = false
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static boolean geZero(String s) {
		try {
			return s != null && Double.parseDouble(s) >= 0;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * n是否小于0
	 * 
	 * <pre>
	 * NumberUtils.ltZero(null)  = false
	 * NumberUtils.ltZero(-1)  = true
	 * NumberUtils.ltZero(0) = false
	 * NumberUtils.ltZero(1) = false
	 * </pre>
	 * 
	 * @param n
	 * @return
	 */
	public static boolean ltZero(Number n) {
		return n != null && n.doubleValue() > 0;
	}

	/**
	 * s是否小于0
	 * 
	 * <pre>
	 * NumberUtils.ltZero(null)  = false
	 * NumberUtils.ltZero("-1")  = true
	 * NumberUtils.ltZero("0") = false
	 * NumberUtils.ltZero("1") = false
	 * NumberUtils.ltZero("string") = false
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static boolean ltZero(String s) {
		try {
			return s != null && Double.parseDouble(s) < 0;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * n是否小于或等于0
	 * 
	 * <pre>
	 * NumberUtils.ltZero(null)  = false
	 * NumberUtils.ltZero(-1)  = true
	 * NumberUtils.ltZero(0) = true
	 * NumberUtils.ltZero(1) = false
	 * </pre>
	 * 
	 * @param n
	 * @return
	 */
	public static boolean leZero(Number n) {
		return n != null && n.doubleValue() <= 0;
	}

	/**
	 * s是否小于或等于0
	 * 
	 * <pre>
	 * NumberUtils.leZero(null)  = false
	 * NumberUtils.leZero("-1")  = true
	 * NumberUtils.leZero("0") = true
	 * NumberUtils.leZero("1") = false
	 * NumberUtils.leZero("string") = false
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static boolean leZero(String s) {
		try {
			return s != null && Double.parseDouble(s) <= 0;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 截取数字 默认截取整数位
	 * 
	 * @param num
	 * @return
	 */
	public static double truncate(double num) {
		return truncate(num, 0);
	}

	/**
	 * 截取数字
	 * 
	 * @param num
	 * @param offset
	 *            位数, 正数-截取小数点后的数字(小数位), 从0开始; 负数-截取小数点前的数字(整数位), 从-1开始
	 * @return
	 */
	public static double truncate(double num, int offset) {
		String numStr = String.valueOf(num);
		String[] numStrs = numStr.split("\\.");
		if (offset == 0) {
			return Double.parseDouble(numStrs[0]);
		} else if (offset > 0) {
			if (numStrs.length == 1) {
				return Double.parseDouble(numStrs[0]);
			} else {
				if (numStrs[1].length() <= offset) {
					return num;
				} else {
					return Double.parseDouble(numStrs[0] + "." + numStrs[1].substring(0, offset));
				}
			}
		} else {
			if ((numStrs[0].length() - (num >= 0 ? 0 : 1)) <= Math.abs(offset)) {
				return 0;
			} else {
				return Double.parseDouble(numStrs[0].substring(0, numStrs[0].length() - Math.abs(offset)));
			}
		}
	}

	/**
	 * 四舍五入 默认整数位
	 * 
	 * @param num
	 * @return
	 */
	public static double round(double num) {
		return round(num, 0);
	}

	/**
	 * 四舍五入
	 * 
	 * @param num
	 * @param offset
	 *            位数, 正数-四舍五入小数点后的数字(小数位), 从0开始; 负数-四舍五入小数点前的数字(整数位), 从-1开始
	 *            [..-2(至千位)-1(至百位)0(至个位)1(至小数点后1位)2(至小数点后2位)...]
	 * @return
	 */
	public static double round(double num, int offset) {
		if (offset == 0) {
			return Math.round(num);
		}
		final String numStr = String.valueOf(num);
		final String[] numStrs = numStr.split("\\.");
		if (offset > 0) {
			if (numStrs.length == 1) {
				return Double.parseDouble(numStrs[0]);
			} else {
				if (numStrs[1].length() <= offset) {
					return num;
				} else {
					return Double.parseDouble(numStrs[0] + "." + numStrs[1].substring(0, offset))
							+ (numStrs[1].charAt(offset) >= 53 ? (1 / Math.pow(10, offset)) : 0);
				}
			}
		} else {
			if (numStrs[0].length() + (num >= 0 ? 0 : 1) < Math.abs(offset) + 1) {
				return 0;
			}

			int index = numStrs[0].length() + offset;
			if (index <= 0 || index >= numStrs[0].length()) {
				return 0;
			}

			return (Double.parseDouble(numStrs[0].substring(0, index)) + (numStrs[0].charAt(index) >= 53 ? 1 : 0))
					* Math.pow(10, Math.abs(offset));
		}
	}

	/**
	 * 解析二进制的一串
	 * 
	 * <pre>
	 * NumberUtils.parseBinaryString("1") = 1	 
	 * NumberUtils.parseBinaryString("10") = 2
	 * NumberUtils.parseBinaryString("111") = 7
	 * </pre>
	 * 
	 * @param binaryString
	 * @return
	 * @throws NullPointerException,IllegalArgumentException
	 */
	public static long parseBinaryString(String binaryString) {
		if (StringUtils.isBlank(binaryString)) {
			throw new IllegalArgumentException("binaryString is blank");
		}
		final long len = binaryString.length();
		long result = 0;
		for (int i = 0; i < len; i++) {
			final char ch = binaryString.charAt(i);
			if (ch != '0' && ch != '1') {
				throw new IllegalArgumentException(binaryString);
			}
			result += (ch - 48) * Math.pow(2, len - i - 1);
		}
		return result;
	}

	/**
	 * <pre>
	 * NumberUtils.toByteObject(null)  = null
	 * NumberUtils.toByteObject("string")  = null
	 * NumberUtils.toByteObject("55") = 55
	 * </pre>
	 * 
	 * @param str
	 * @return
	 */
	public static Byte toByteObject(String str) {
		try {
			return StringUtils.isBlank(str) ? null : Byte.valueOf(str);
		} catch (Exception e) {
			// ignore
			LOG.error("error to convert " + str + " to " + Byte.class.getName() + ", " + e.getClass().getName());
			return null;
		}
	}

	/**
	 * <pre>
	 * NumberUtils.toShortObject(null)  = null
	 * NumberUtils.toShortObject("string")  = null
	 * NumberUtils.toShortObject("555") = 555
	 * </pre>
	 * 
	 * @param str
	 * @return
	 */
	public static Short toShortObject(String str) {
		try {
			return StringUtils.isBlank(str) ? null : Short.valueOf(str);
		} catch (Exception e) {
			// ignore
			LOG.error("error to convert " + str + " to " + Short.class.getName() + ", " + e.getClass().getName());
			return null;
		}
	}

	/**
	 * <pre>
	 * NumberUtils.toIntegerObject(null)  = null
	 * NumberUtils.toIntegerObject("string")  = null
	 * NumberUtils.toIntegerObject("555") = 555
	 * </pre>
	 * 
	 * @param str
	 * @return
	 */
	public static Integer toIntegerObject(String str) {
		try {
			return StringUtils.isBlank(str) ? null : Integer.valueOf(str);
		} catch (Exception e) {
			// ignore
			LOG.error("error to convert " + str + " to " + Integer.class.getName() + ", " + e.getClass().getName());
			return null;
		}
	}

	/**
	 * <pre>
	 * NumberUtils.toLongObject(null)  = null
	 * NumberUtils.toLongObject("string")  = null
	 * NumberUtils.toLongObject("555") = 555l
	 * </pre>
	 * 
	 * @param str
	 * @return
	 */
	public static Long toLongObject(String str) {
		try {
			return StringUtils.isBlank(str) ? null : Long.valueOf(str);
		} catch (Exception e) {
			// ignore
			LOG.error("error to convert " + str + " to " + Long.class.getName() + ", " + e.getClass().getName());
			return null;
		}
	}

	/**
	 * <pre>
	 * NumberUtils.toFloatObject(null)  = null
	 * NumberUtils.toFloatObject("string")  = null
	 * NumberUtils.toFloatObject("555.5") = 555.5f
	 * </pre>
	 * 
	 * @param str
	 * @return
	 */
	public static Float toFloatObject(String str) {
		try {
			return StringUtils.isBlank(str) ? null : Float.valueOf(str);
		} catch (Exception e) {
			// ignore
			LOG.error("error to convert " + str + " to " + Float.class.getName() + ", " + e.getClass().getName());
			return null;
		}
	}

	/**
	 * <pre>
	 * NumberUtils.toDoubleObject(null)  = null
	 * NumberUtils.toDoubleObject("string")  = null
	 * NumberUtils.toDoubleObject("555.5") = 555.5d
	 * </pre>
	 * 
	 * @param str
	 * @return
	 */
	public static Double toDoubleObject(String str) {
		try {
			return StringUtils.isBlank(str) ? null : Double.valueOf(str);
		} catch (Exception e) {
			// ignore
			LOG.error("error to convert " + str + " to " + Double.class.getName() + ", " + e.getClass().getName());
			return null;
		}
	}

	/**
	 * <pre>
	 * NumberUtils.defaultByte(null)  = 0
	 * NumberUtils.defaultByte(0)  = 0
	 * NumberUtils.defaultByte(5) = 5
	 * </pre>
	 * 
	 * @param dest
	 * @return
	 */
	public static Byte defaultByte(Byte dest) {
		return dest == null ? ZERO_BYTE : dest;
	}

	/**
	 * <pre>
	 * NumberUtils.defaultByte(null, defaultValue)  = defaultValue
	 * NumberUtils.defaultByte(0, defaultValue)  = 0
	 * NumberUtils.defaultByte(5, defaultValue) = 5
	 * </pre>
	 * 
	 * @param dest
	 * @param defaultValue
	 * @return
	 */
	public static Byte defaultByte(Byte dest, Byte defaultValue) {
		return dest == null ? defaultValue : dest;
	}

	/**
	 * <pre>
	 * NumberUtils.defaultIfBlank(null, defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("", defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("  ", defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("0", defaultValue)  = 0
	 * NumberUtils.defaultIfBlank("5", defaultValue) = 5
	 * </pre>
	 * 
	 * @param dest
	 * @param defaultValue
	 * @return
	 */
	public static Byte defaultIfBlank(String dest, Byte defaultValue) {
		return StringUtils.isBlank(dest) ? defaultValue : Byte.valueOf(dest);
	}

	/**
	 * <pre>
	 * NumberUtils.defaultShort(null)  = 0
	 * NumberUtils.defaultShort(0)  = 0
	 * NumberUtils.defaultShort(5) = 5
	 * </pre>
	 * 
	 * @param dest
	 * @return
	 */
	public static Short defaultShort(Short dest) {
		return dest == null ? ZERO_SHORT : dest;
	}

	/**
	 * <pre>
	 * NumberUtils.defaultShort(null, defaultValue)  = defaultValue
	 * NumberUtils.defaultShort(0, defaultValue)  = 0
	 * NumberUtils.defaultShort(5, defaultValue) = 5
	 * </pre>
	 * 
	 * @param dest
	 * @param defaultValue
	 * @return
	 */
	public static Short defaultShort(Short dest, Short defaultValue) {
		return dest == null ? defaultValue : dest;
	}

	/**
	 * <pre>
	 * NumberUtils.defaultIfBlank(null, defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("", defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("  ", defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("0", defaultValue)  = 0
	 * NumberUtils.defaultIfBlank("5", defaultValue) = 5
	 * </pre>
	 * 
	 * @param dest
	 * @param defaultValue
	 * @return
	 */
	public static Short defaultIfBlank(String dest, Short defaultValue) {
		return StringUtils.isBlank(dest) ? defaultValue : Short.valueOf(dest);
	}

	/**
	 * <pre>
	 * NumberUtils.defaultInteger(null)  = 0
	 * NumberUtils.defaultInteger(0)  = 0
	 * NumberUtils.defaultInteger(5) = 5
	 * </pre>
	 * 
	 * @param dest
	 * @return
	 */
	public static Integer defaultInteger(Integer dest) {
		return dest == null ? ZERO_INT : dest;
	}

	/**
	 * <pre>
	 * NumberUtils.defaultInteger(null, defaultValue)  = defaultValue
	 * NumberUtils.defaultInteger(0, defaultValue)  = 0
	 * NumberUtils.defaultInteger(5, defaultValue) = 5
	 * </pre>
	 * 
	 * @param dest
	 * @param defaultValue
	 * @return
	 */
	public static Integer defaultInteger(Integer dest, Integer defaultValue) {
		return dest == null ? defaultValue : dest;
	}

	/**
	 * <pre>
	 * NumberUtils.defaultIfBlank(null, defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("", defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("  ", defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("0", defaultValue)  = 0
	 * NumberUtils.defaultIfBlank("5", defaultValue) = 5
	 * </pre>
	 * 
	 * @param dest
	 * @param defaultValue
	 * @return
	 */
	public static Integer defaultIfBlank(String dest, Integer defaultValue) {
		return StringUtils.isBlank(dest) ? defaultValue : Integer.valueOf(dest);
	}

	/**
	 * <pre>
	 * NumberUtils.defaultLong(null)  = 0
	 * NumberUtils.defaultLong(0)  = 0
	 * NumberUtils.defaultLong(5l) = 5l
	 * </pre>
	 * 
	 * @param dest
	 * @return
	 */
	public static Long defaultLong(Long dest) {
		return dest == null ? ZERO_LONG : dest;
	}

	/**
	 * <pre>
	 * NumberUtils.defaultLong(null, defaultValue)  = defaultValue
	 * NumberUtils.defaultLong(0, defaultValue)  = 0
	 * NumberUtils.defaultLong(5l, defaultValue) = 5l
	 * </pre>
	 * 
	 * @param dest
	 * @param defaultValue
	 * @return
	 */
	public static Long defaultLong(Long dest, Long defaultValue) {
		return dest == null ? defaultValue : dest;
	}

	/**
	 * <pre>
	 * NumberUtils.defaultIfBlank(null, defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("", defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("  ", defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("0", defaultValue)  = 0
	 * NumberUtils.defaultIfBlank("5", defaultValue) = 5l
	 * </pre>
	 * 
	 * @param dest
	 * @param defaultValue
	 * @return
	 */
	public static Long defaultIfBlank(String dest, Long defaultValue) {
		return StringUtils.isBlank(dest) ? defaultValue : Long.valueOf(dest);
	}

	/**
	 * <pre>
	 * NumberUtils.defaultDouble(null)  = 0
	 * NumberUtils.defaultDouble(0)  = 0
	 * NumberUtils.defaultDouble(5d) = 5d
	 * </pre>
	 * 
	 * @param dest
	 * @return
	 */
	public static Float defaultFloat(Float dest) {
		return dest == null ? ZERO_FLOAT : dest;
	}

	/**
	 * <pre>
	 * NumberUtils.defaultFloat(null, defaultValue)  = defaultValue
	 * NumberUtils.defaultFloat(0, defaultValue)  = 0
	 * NumberUtils.defaultFloat(5d, defaultValue) = 5d
	 * </pre>
	 * 
	 * @param dest
	 * @param defaultValue
	 * @return
	 */
	public static Float defaultFloat(Float dest, Float defaultValue) {
		return dest == null ? defaultValue : dest;
	}

	/**
	 * <pre>
	 * NumberUtils.defaultIfBlank(null, defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("", defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("  ", defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("0", defaultValue)  = 0
	 * NumberUtils.defaultIfBlank("5.5", defaultValue) = 5.5
	 * </pre>
	 * 
	 * @param dest
	 * @param defaultValue
	 * @return
	 */
	public static Float defaultIfBlank(String dest, Float defaultValue) {
		return StringUtils.isBlank(dest) ? defaultValue : Float.valueOf(dest);
	}

	/**
	 * <pre>
	 * NumberUtils.defaultDouble(null)  = 0
	 * NumberUtils.defaultDouble(0)  = 0
	 * NumberUtils.defaultDouble(5d) = 5d
	 * </pre>
	 * 
	 * @param dest
	 * @return
	 */
	public static Double defaultDouble(Double dest) {
		return dest == null ? ZERO_DOUBLE : dest;
	}

	/**
	 * <pre>
	 * NumberUtils.defaultDouble(null, defaultValue)  = defaultValue
	 * NumberUtils.defaultDouble(0, defaultValue)  = 0
	 * NumberUtils.defaultDouble(5d, defaultValue) = 5d
	 * </pre>
	 * 
	 * @param dest
	 * @param defaultValue
	 * @return
	 */
	public static Double defaultDouble(Double dest, Double defaultValue) {
		return dest == null ? defaultValue : dest;
	}

	/**
	 * <pre>
	 * NumberUtils.defaultIfBlank(null, defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("", defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank(" ", defaultValue)  = defaultValue
	 * NumberUtils.defaultIfBlank("0", defaultValue)  = 0
	 * NumberUtils.defaultIfBlank("5.5", defaultValue) = 5.5
	 * </pre>
	 * 
	 * @param dest
	 * @param defaultValue
	 * @return
	 */
	public static Double defaultIfBlank(String dest, Double defaultValue) {
		return StringUtils.isBlank(dest) ? defaultValue : Double.valueOf(dest);
	}

	/**
	 * 求和
	 * 
	 * @param values
	 * @return
	 */
	public static int sum(int... values) {
		if (ArrayUtils.isEmpty(values)) {
			return 0;
		}
		int sum = 0;
		for (int value : values) {
			sum += value;
		}
		return sum;
	}

	/**
	 * 求和
	 * 
	 * @param values
	 * @return
	 */
	public static Integer sum(Integer... values) {
		if (ArrayUtils.isEmpty(values)) {
			return null;
		}
		Integer sum = 0;
		for (Integer value : values) {
			if (value != null) {
				sum += value;
			}
		}
		return sum;
	}

	/**
	 * 求和
	 * 
	 * @param values
	 * @return
	 */
	public static long sum(long... values) {
		if (ArrayUtils.isEmpty(values)) {
			return 0;
		}
		long sum = 0;
		for (long value : values) {
			sum += value;
		}
		return sum;
	}

	/**
	 * 求和
	 * 
	 * @param values
	 * @return
	 */
	public static Long sum(Long... values) {
		if (ArrayUtils.isEmpty(values)) {
			return null;
		}
		Long sum = 0L;
		for (Long value : values) {
			if (value != null) {
				sum += value;
			}
		}
		return sum;
	}

	/**
	 * 求和
	 * 
	 * @param values
	 * @return
	 */
	public static double sum(double... values) {
		if (ArrayUtils.isEmpty(values)) {
			return 0;
		}
		double sum = 0;
		for (double value : values) {
			sum += value;
		}
		return sum;
	}

	/**
	 * 求和
	 * 
	 * @param values
	 * @return
	 */
	public static Double sum(Double... values) {
		if (ArrayUtils.isEmpty(values)) {
			return null;
		}
		Double sum = 0.0;
		for (Double value : values) {
			if (value != null) {
				sum += value;
			}
		}
		return sum;
	}
}

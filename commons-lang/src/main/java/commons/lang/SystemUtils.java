/**
 * 
 */
package commons.lang;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:24:57 AM Nov 25, 2013
 */
public class SystemUtils {

	public static String getSystemProperty(String key, String def) {
		try {
			final String value = System.getProperty(key).trim();
			return StringUtils.isBlank(value) ? def : value;
		}
		catch (Throwable e) {
			return def;
		}
	}

	public static byte getSystemPropertyForByte(String key, byte def) {
		try {
			final String value = System.getProperty(key).trim();
			return StringUtils.isBlank(value) ? def : Byte.parseByte(value);
		}
		catch (Throwable e) {
			return def;
		}
	}

	public static short getSystemPropertyForShort(String key, short def) {
		try {
			final String value = System.getProperty(key).trim();
			return StringUtils.isBlank(value) ? def : Short.parseShort(value);
		}
		catch (Throwable e) {
			return def;
		}
	}

	public static int getSystemPropertyForInt(String key, int def) {
		try {
			final String value = System.getProperty(key).trim();
			return StringUtils.isBlank(value) ? def : Integer.parseInt(value);
		}
		catch (Throwable e) {
			return def;
		}
	}

	public static long getSystemPropertyForLong(String key, long def) {
		try {
			final String value = System.getProperty(key).trim();
			return StringUtils.isBlank(value) ? def : Long.parseLong(value);
		}
		catch (Throwable e) {
			return def;
		}
	}

	public static float getSystemPropertyForFloat(String key, float def) {
		try {
			final String value = System.getProperty(key).trim();
			return StringUtils.isBlank(value) ? def : Float.parseFloat(value);
		}
		catch (Throwable e) {
			return def;
		}
	}

	public static double getSystemPropertyForDouble(String key, double def) {
		try {
			final String value = System.getProperty(key).trim();
			return StringUtils.isBlank(value) ? def : Double.parseDouble(value);
		}
		catch (Throwable e) {
			return def;
		}
	}

	public static boolean getSystemPropertyForBoolean(String key, boolean def) {
		try {
			final String value = System.getProperty(key).trim();
			return StringUtils.isBlank(value) ? def : Boolean.parseBoolean(value);
		}
		catch (Throwable e) {
			return def;
		}
	}
}

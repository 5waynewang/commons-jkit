/**
 * 
 */
package commons.lang;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:22:16 AM Nov 25, 2013
 */
public class ByteUtils {

	/**
	 * 二进制转十六进制字符
	 * 
	 * @param b
	 * @return
	 */
	public static String toHexString(byte b) {
		return Integer.toHexString(b & 0xff).toUpperCase();
	}

	public static byte[] convertIntToBytes(int i) {
		byte[] b = new byte[4];

		b[0] = (byte) (0xff & i);
		b[1] = (byte) ((0xff00 & i) >> 8);
		b[2] = (byte) ((0xff0000 & i) >> 16);
		b[3] = (byte) ((0xff000000 & i) >> 24);
		return b;
	}

	public static int convertBytesToInt(byte[] bytes) {
		int num = bytes[0] & 0xFF;
		num |= ((bytes[1] << 8) & 0xFF00);
		num |= ((bytes[2] << 16) & 0xFF0000);
		num |= ((bytes[3] << 24) & 0xFF000000);
		return num;
	}

	public static byte[] convertLongToBytes(long l) {
		byte[] b = new byte[8];
		for (int i = 0; i < 8; i++) {
			b[i] = (byte) (l >>> (56 - (i * 8)));
		}
		return b;
	}

	public static long convertBytesToLong(byte[] bytes) {
		long temp = 0;
		long res = 0;
		for (int i = 0; i < 8; i++) {
			res <<= 8;
			temp = bytes[i] & 0xff;
			res |= temp;
		}
		return res;
	}

	public static String toString(byte[] bytes) {
		return toString(bytes, 50);
	}

	public static String toString(byte[] bytes, int rowSize) {
		final StringBuilder result = new StringBuilder();
		int index = 0;
		for (byte b : bytes) {
			result.append(StringUtils.rightPad(String.valueOf(b), 5, ' '));
			if (++index % rowSize == 0) {
				result.append("\r\n");
			}
		}
		return result.toString();
	}
}

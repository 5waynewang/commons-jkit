/**
 * 
 */
package commons.lang;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:22:58 AM Nov 25, 2013
 */
public class CodecUtils {

	/**
	 * 对long类型的数字编码
	 * 
	 * <pre>
	 * 不会报错
	 * </pre>
	 * 
	 * @param dest
	 * @return
	 */
	public static String encode(long dest) {
		String bs = Long.toBinaryString(dest);
		if (bs.length() % 7 != 0) {
			bs = StringUtils.leftPad(bs, (bs.length() / 7 + 1) * 7, '0');
		}
		final int len = bs.length() / 7;
		final byte[] bytes = new byte[len];
		byte b = 0;
		int index = 0, l = 0;
		for (int i = 0; i < bs.length(); i++) {
			b += (bs.charAt(i) - 48) * Math.pow(2, 6 - index);
			if (i == bs.length() - 1) {
				bytes[l++] = b;
			}
			else if (index == 6) {
				bytes[l++] = b;
				index = 0;
				b = 0;
			}
			else {
				index++;
			}
		}
		return Base64.encodeBase64String(bytes);
	}

	/**
	 * 解码 通过encode(long dest)方法编码得到的字符串
	 * 
	 * <pre>
	 * 调用encode(long dest)方法成功得到的数据是用此方法解码都不会出错
	 * </pre>
	 * 
	 * @param str
	 * @return
	 */
	public static long decodeLong(String str) {
		ExceptionUtils.throwIllegalArgumentExceptionIfBlank(str, "the encode str");

		final byte[] bytes = Base64.decodeBase64(str);
		final StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(StringUtils.leftPad(Long.toBinaryString(b), 7, '0'));
		}
		return NumberUtils.parseBinaryString(sb.toString());
	}
}

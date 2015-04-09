package commons.lang;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:24:18 AM Nov 25, 2013
 */
public class ImageUtils {

	/**
	 * 是否JPEG格式的图片
	 * 
	 * @param bytes
	 * @return
	 */
	public static boolean isJpeg(byte[] bytes) {
		try {
			return (bytes[0] & 0xff) == 0xff && (bytes[1] & 0xff) == 0xd8
					&& (bytes[bytes.length - 2] & 0xff) == 0xff
					&& (bytes[bytes.length - 1] & 0xff) == 0xd9;
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否PNG格式的图片
	 * 
	 * @param bytes
	 * @return
	 */
	public static boolean isPng(byte[] bytes) {
		try {
			return (bytes[0] & 0xff) == 0x89 && (bytes[1] & 0xff) == 0x50
					&& (bytes[2] & 0xff) == 0x4e && (bytes[3] & 0xff) == 0x47
					&& (bytes[4] & 0xff) == 0x0d && (bytes[5] & 0xff) == 0x0a
					&& (bytes[6] & 0xff) == 0x1a && (bytes[7] & 0xff) == 0x0a;
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否GIF格式的图片
	 * 
	 * @param bytes
	 * @return
	 */
	public static boolean isGif(byte[] bytes) {
		try {
			return (bytes[0] & 0xff) == 0x47 && (bytes[1] & 0xff) == 0x49
					&& (bytes[2] & 0xff) == 0x46 && (bytes[3] & 0xff) == 0x38
					&& ((bytes[4] & 0xff) == 0x39 || (bytes[4] & 0xff) == 0x37)
					&& (bytes[5] & 0xff) == 0x61;
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否BMP格式的图片
	 * 
	 * @param bytes
	 * @return
	 */
	public static boolean isBmp(byte[] bytes) {
		try {
			return (bytes[0] & 0xff) == 0x42 && (bytes[1] & 0xff) == 0x4d;
		}
		catch (Exception e) {
			return false;
		}
	}
}

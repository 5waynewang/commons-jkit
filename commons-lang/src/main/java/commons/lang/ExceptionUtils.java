/**
 * 
 */
package commons.lang;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:24:05 AM Nov 25, 2013
 */
public class ExceptionUtils {

	/**
	 * 参数为null，抛出异常
	 * 
	 * @param value
	 */
	public static void throwIllegalArgumentExceptionIfNull(Object value) {
		throwIllegalArgumentExceptionIfNull(value, "the argument is null");
	}

	/**
	 * 参数为null，抛出异常
	 * 
	 * @param value
	 * @param errorMsg 异常信息
	 */
	public static void throwIllegalArgumentExceptionIfNull(Object value, String errorMsg) {
		if (value == null) {
			throw new IllegalArgumentException(StringUtils.defaultIfBlank(errorMsg,
					"the argument is null"));
		}
	}

	/**
	 * 参数为空，抛出异常
	 * 
	 * @param value
	 */
	public static void throwIllegalArgumentExceptionIfBlank(String value) {
		throwIllegalArgumentExceptionIfBlank(value, "the argument is blank");
	}

	/**
	 * 参数为空，抛出异常
	 * 
	 * @param value 参数值
	 * @param errorMsg 异常信息
	 */
	public static void throwIllegalArgumentExceptionIfBlank(String value, String errorMsg) {
		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException(StringUtils.defaultIfBlank(errorMsg,
					"the argument is blank"));
		}
	}
}

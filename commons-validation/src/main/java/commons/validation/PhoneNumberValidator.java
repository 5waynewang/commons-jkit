/**
 * 
 */
package commons.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 3:41:30 PM Nov 14, 2015
 */
public class PhoneNumberValidator {

	static final Pattern MOBILE_PATTERN = Pattern.compile("^(\\+86)?1[\\d]{10}$");

	/**
	 * 是否中国的手机号，非座机
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isChineseMobile(String mobile) {
		final Matcher m = MOBILE_PATTERN.matcher(mobile);

		return m.matches();
	}
}

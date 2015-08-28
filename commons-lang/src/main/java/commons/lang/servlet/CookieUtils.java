/**
 * 
 */
package commons.lang.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * <pre>
 *
 * </pre>
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:08:14 AM Aug 28, 2015
 */
public class CookieUtils {

	/**
	 * 根据cookie名称获取cookie
	 * @param request
	 * @param name
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		final Cookie[] cookies = request.getCookies();

		if (ArrayUtils.isEmpty(cookies)) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if (StringUtils.equals(cookie.getName(), name)) {
				return cookie;
			}
		}

		return null;
	}

	/**
	 * 根据cookie名称获取cookie值
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		final Cookie cookie = getCookie(request, name);

		return cookie != null ? cookie.getValue() : null;
	}
}

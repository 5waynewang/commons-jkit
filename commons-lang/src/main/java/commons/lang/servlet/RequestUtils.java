/**
 * 
 */
package commons.lang.servlet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:44:26 AM Nov 23, 2015
 */
public class RequestUtils {
	static final String UNKNOWN = "unknown";

	/**
	 * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemoteIP(HttpServletRequest request) {
		String ip = null;
		// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
		ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotBlank(ip) && !StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
			return ip;
		}
		ip = request.getHeader("X-Real-IP");
		if (StringUtils.isNotBlank(ip) && !StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
			return ip;
		}
		ip = request.getHeader("Proxy-Client-IP");
		if (StringUtils.isNotBlank(ip) && !StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
			return ip;
		}
		ip = request.getHeader("WL-Proxy-Client-IP");
		if (StringUtils.isNotBlank(ip) && !StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
			return ip;
		}
		ip = request.getHeader("HTTP_CLIENT_IP");
		if (StringUtils.isNotBlank(ip) && !StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
			return ip;
		}
		ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (StringUtils.isNotBlank(ip) && !StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}
}

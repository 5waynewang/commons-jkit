/**
 * 
 */
package commons.lang;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 12:04:24 PM Feb 21, 2014
 */
public abstract class MBeanUtils {

	/**
	 * @see #regist(Object, String)
	 */
	public static void regist(Object o) {
		regist(o, null);
	}

	/**
	 * <pre>
	 * 注册MBean
	 * </pre>
	 * 
	 * @param o
	 * @param name
	 */
	public static void regist(Object o, String name) {
		final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		if (null != mbs) {
			final StringBuilder sb = new StringBuilder();
			sb.append(o.getClass().getPackage().getName());
			sb.append(":type=");
			sb.append(o.getClass().getSimpleName());
			if (null == name) {
				sb.append(",id=").append(o.hashCode());
			}
			else {
				sb.append(",name=").append(name).append("-").append(o.hashCode());
			}
			try {

				mbs.registerMBean(o, new ObjectName(sb.toString()));
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}

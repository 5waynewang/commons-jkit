/**
 * 
 */
package commons.lang;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:24:36 AM Nov 25, 2013
 */
public class LocalUtils {

	private static final Log LOG = LogFactory.getLog(LocalUtils.class);
	public static final String DEFAULT_LOCAL_IP = "127.0.0.1";
	public static final String LOCAL_IP;
	static {
		LOCAL_IP = getLocalAddress();
		// 打印本地IP
		System.out.println("LOCAL_IP： " + LOCAL_IP);
	}

	/**
	 * 获取本地地址
	 * 
	 * @return
	 */
	public static String getLocalAddress() {
		try {
			// 遍历网卡，查找一个非回路ip地址并返回
			Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
			InetAddress inet6Address = null;
			while (enumeration.hasMoreElements()) {
				NetworkInterface networkInterface = enumeration.nextElement();
				Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					InetAddress inetAddress = inetAddresses.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {
						if (inetAddress instanceof Inet6Address) {
							inet6Address = inetAddress;
						}
						else {
							// 优先使用ipv4
							return normalizeHostAddress(inetAddress);
						}
					}
				}
			}
			// 没有ipv4，再使用ipv6
			if (inet6Address != null) {
				return normalizeHostAddress(inet6Address);
			}
			return normalizeHostAddress(InetAddress.getLocalHost());
		}
		catch (Exception e) {
			LOG.warn("Error to get local address\r\n", e);
			return DEFAULT_LOCAL_IP;
		}
	}

	/**
	 * 获取主机地址
	 * 
	 * @param inetAddress
	 * @return
	 */
	public static String normalizeHostAddress(InetAddress inetAddress) {
		if (inetAddress instanceof Inet6Address) {
			return "[" + inetAddress.getHostAddress() + "]";
		}
		else {
			return inetAddress.getHostAddress();
		}
	}

	/**
	 * 获取物理网卡地址
	 * 
	 * @return
	 */
	public static String getPhysicalAddress() {
		try {
			Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
			while (enumeration.hasMoreElements()) {
				NetworkInterface networkInterface = enumeration.nextElement();
				Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					InetAddress inetAddress = inetAddresses.nextElement();
					// isSiteLocalAddress方法
					// 当IP地址是地区本地地址(SiteLocalAddress)时返回true，否则返回false。IPv4的地址本地
					// 地址分为三段：10.0.0.0 ~ 10.255.255.255、172.16.0.0 ~
					// 172.31.255.255、192.168.0.0
					// ~ 192.168.255.255。IPv6的地区本地地址的前12位是FEC，其他的位可以是任意取值，
					// 如FED0::、FEF1::都是地区本地地址。

					// isLoopbackAddress方法
					// 当IP地址是loopback地址时返回true，否则返回false。loopback地址就是代表本机的IP地址。
					// IPv4的loopback地址的范围是127.0.0.0 ~
					// 127.255.255.255，也就是说，只要第一个字节是127，
					// 就是lookback地址。如127.1.2.3、127.0.200.200都是loopback地址。IPv6的loopback地址是
					// 0:0:0:0:0:0:0:1，也可以简写成::1。
					if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {
						byte[] hardwareAddress = networkInterface.getHardwareAddress();
						if (ArrayUtils.isEmpty(hardwareAddress)) {
							continue;
						}
						StringBuilder result = new StringBuilder();
						for (byte b : hardwareAddress) {
							result.append("-");
							result.append(ByteUtils.toHexString(b));
						}
						return result.deleteCharAt(0).toString();
					}

				}
			}
		}
		catch (Exception e) {
			LOG.warn("Error to get physical address\r\n", e);
			// ignore
		}
		return null;
	}
}

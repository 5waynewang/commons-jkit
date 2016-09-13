/**
 * 
 */
package commons.lang.statistics;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:40:06 AM Sep 12, 2016
 */
public class JvmMemoryStatistics {
	private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

	/**
	 * 获取已用的heap大小，单位：byte
	 */
	public long getHeapMemoryUsed() {
		return memoryMXBean.getHeapMemoryUsage().getUsed();
	}

	/**
	 * 获取初始化的heap大小 -Xms，单位：byte
	 */
	public long getHeapMemoryInit() {
		return memoryMXBean.getHeapMemoryUsage().getInit();
	}

	/**
	 * 获取heap最大利用空间，不超过 -Xmx，单位：byte
	 */
	public long getHeapMemoryMax() {
		return memoryMXBean.getHeapMemoryUsage().getMax();
	}

	/**
	 * 这个应该是JVM还能用的heap大小
	 */
	public long getHeapMemoryCommitted() {
		return memoryMXBean.getHeapMemoryUsage().getCommitted();
	}

	/**
	 * non-heap（包括Perm Gen 和 native heap）
	 */
	public long getNonHeapMemoryInit() {
		return memoryMXBean.getNonHeapMemoryUsage().getInit();
	}

	public long getNonHeapMemoryUsed() {
		return memoryMXBean.getNonHeapMemoryUsage().getUsed();
	}

	public long getNonHeapMemoryMax() {
		return memoryMXBean.getNonHeapMemoryUsage().getMax();
	}

	public long getNonHeapMemoryCommitted() {
		return memoryMXBean.getNonHeapMemoryUsage().getCommitted();
	}
}

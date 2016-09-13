/**
 * 
 */
package commons.lang.statistics;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:31:03 AM Sep 12, 2016
 */
public class ClassLoadingStatistics {
	private final ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();

	public int getLoadedClassCount() {
		return classLoadingMXBean.getLoadedClassCount();
	}

	public long getTotalLoadedClassCount() {
		return classLoadingMXBean.getTotalLoadedClassCount();
	}

	public long getUnloadedClassCount() {
		return classLoadingMXBean.getUnloadedClassCount();
	}
}

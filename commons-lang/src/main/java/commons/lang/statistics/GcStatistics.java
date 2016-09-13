/**
 * 
 */
package commons.lang.statistics;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:24:19 AM Sep 12, 2016
 */
public class GcStatistics {
	private final List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();

	public String getCollectionCount() {
		StringBuilder builder = new StringBuilder();
		for (GarbageCollectorMXBean collector : garbageCollectorMXBeans) {
			builder.append(collector.getName()).append(":").append(collector.getCollectionCount()).append("\n");
		}
		return builder.toString();
	}

	public String getCollectionTime() {
		StringBuilder builder = new StringBuilder();
		for (GarbageCollectorMXBean collector : garbageCollectorMXBeans) {
			builder.append(collector.getName()).append(":").append(collector.getCollectionTime()).append("\n");
		}
		return builder.toString();
	}
}

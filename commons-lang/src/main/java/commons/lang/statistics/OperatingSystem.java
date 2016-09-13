/**
 * 
 */
package commons.lang.statistics;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:30:16 AM Sep 12, 2016
 */
public class OperatingSystem {
	private final OperatingSystemMXBean OS = ManagementFactory.getOperatingSystemMXBean();

	public int getAvailableProcessors() {
		return OS.getAvailableProcessors();
	}

	public String getArch() {
		return OS.getArch();
	}

	public String getName() {
		return OS.getName();
	}

	public double getSystemLoadAverage() {
		return OS.getSystemLoadAverage();
	}

	public String getVersion() {
		return OS.getVersion();
	}
}

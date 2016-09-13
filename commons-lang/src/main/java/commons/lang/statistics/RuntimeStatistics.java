/**
 * 
 */
package commons.lang.statistics;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:33:16 AM Sep 12, 2016
 */
public class RuntimeStatistics {
	private final RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();

	public String getRuntimeInfo() {
		StringBuilder builder = new StringBuilder();
		builder.append("bootClassPath").append(":").append(runtime.getBootClassPath()).append("\n");
		builder.append("classPath").append(":").append(runtime.getClassPath()).append("\n");
		builder.append("libraryPath").append(":").append(runtime.getLibraryPath()).append("\n");
		builder.append("managementSpecVersion").append(":").append(runtime.getManagementSpecVersion()).append("\n");
		builder.append("name").append(":").append(runtime.getName()).append("\n");
		builder.append("specName").append(":").append(runtime.getSpecName()).append("\n");
		builder.append("specVendor").append(":").append(runtime.getSpecVendor()).append("\n");
		builder.append("specVersion").append(":").append(runtime.getSpecVersion()).append("\n");
		builder.append("startTime").append(":").append(runtime.getStartTime()).append("\n");
		builder.append("uptime").append(":").append(runtime.getUptime()).append("\n");
		builder.append("vmName").append(":").append(runtime.getVmName()).append("\n");
		builder.append("vmVendor").append(":").append(runtime.getVmVendor()).append("\n");
		builder.append("vmVersion").append(":").append(runtime.getVmVersion()).append("\n");
		return builder.toString();
	}

	public String getInputArguments() {
		return runtime.getInputArguments().toString();
	}

	public String getSystemProperties() {
		return runtime.getSystemProperties().toString();
	}
}

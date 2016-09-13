/**
 * 
 */
package commons.lang.statistics;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:31:50 AM Sep 12, 2016
 */
public class ThreadStatistics {
	private final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

	private int stackDepth = 20;

	public void setContentionTracing(boolean val) {
		threadBean.setThreadContentionMonitoringEnabled(val);
	}

	public void setStackDepth(int stackDepth) {
		this.stackDepth = stackDepth;
	}

	/**
	 * 获取所有的线程数
	 * 
	 * @return 所有的线程数
	 */
	public int getAllThreadsCount() {
		return threadBean.getThreadCount();
	}

	/**
	 * 获取峰值线程数
	 * 
	 * @return 峰值线程数
	 */
	public int getPeakThreadCount() {
		return threadBean.getPeakThreadCount();
	}

	/**
	 * 获取守护线程数
	 * 
	 * @return 守护线程数
	 */
	public int getDaemonThreadCount() {
		return threadBean.getDaemonThreadCount();
	}

	/**
	 * 获取启动以来创建的线程数
	 * 
	 * @return 启动以来创建的线程数
	 */
	public long getTotalStartedThreadCount() {
		return threadBean.getTotalStartedThreadCount();
	}

	/**
	 * 获取死锁数
	 * 
	 * @return 死锁数
	 */
	public static int getDeadLockCount() {
		ThreadMXBean th = ManagementFactory.getThreadMXBean();
		long[] deadLockIds = th.findMonitorDeadlockedThreads();
		if (deadLockIds == null) {
			return 0;
		}
		else {
			return deadLockIds.length;
		}

	}

	public String getThreadInfoByNum(int i) {
		long[] threadIds = threadBean.getAllThreadIds();
		if (i >= threadIds.length) {
			return null;
		}
		long tid = threadIds[i];

		ThreadInfo info = threadBean.getThreadInfo(tid, stackDepth);
		StringBuilder builder = new StringBuilder();
		boolean contention = threadBean.isThreadContentionMonitoringEnabled();
		this.threadDump(builder, info, contention);
		return builder.toString();
	}

	public String getThreadInfoByContain(String contain) {
		boolean contention = threadBean.isThreadContentionMonitoringEnabled();
		long[] threadIds = threadBean.getAllThreadIds();
		StringBuilder builder = new StringBuilder();
		for (long tid : threadIds) {
			ThreadInfo info = threadBean.getThreadInfo(tid, stackDepth);
			if (info.getThreadName().contains(contain)) {
				this.threadDump(builder, info, contention);
			}
		}

		return builder.toString();
	}

	private void threadDump(StringBuilder builder, ThreadInfo info, boolean contention) {
		if (info == null) {
			builder.append("  Inactive");
			builder.append("\n");
			return;
		}
		builder.append("Thread " + getTaskName(info.getThreadId(), info.getThreadName()) + ":");
		builder.append("\n");
		Thread.State state = info.getThreadState();
		builder.append("  State: " + state);
		builder.append("\n");
		builder.append("  Blocked count: " + info.getBlockedCount());
		builder.append("\n");
		builder.append("  Waited count: " + info.getWaitedCount());
		builder.append("\n");
		if (contention) {
			builder.append("  Blocked time: " + info.getBlockedTime());
			builder.append("\n");
			builder.append("  Waited time: " + info.getWaitedTime());
			builder.append("\n");
		}
		if (state == Thread.State.WAITING) {
			builder.append("  Waiting on " + info.getLockName());
			builder.append("\n");
		}
		else if (state == Thread.State.BLOCKED) {
			builder.append("  Blocked on " + info.getLockName());
			builder.append("\n");
			builder.append("  Blocked by " + getTaskName(info.getLockOwnerId(), info.getLockOwnerName()));
			builder.append("\n");
		}
		builder.append("  Stack:");
		builder.append("\n");
		for (StackTraceElement frame : info.getStackTrace()) {
			builder.append("    " + frame.toString());
			builder.append("\n");
		}
	}

	/**
	 * Print all of the thread's information and stack traces.
	 */
	public String getThreadInfo() {

		boolean contention = threadBean.isThreadContentionMonitoringEnabled();
		long[] threadIds = threadBean.getAllThreadIds();

		StringBuilder builder = new StringBuilder("Process Thread Dump: ");
		builder.append("\n");
		builder.append(threadIds.length + " active threads");
		builder.append("\n");
		for (long tid : threadIds) {
			ThreadInfo info = threadBean.getThreadInfo(tid, stackDepth);
			this.threadDump(builder, info, contention);
		}
		return builder.toString();
	}

	private String getTaskName(long id, String name) {
		if (name == null) {
			return Long.toString(id);
		}
		return id + " (" + name + ")";
	}
}

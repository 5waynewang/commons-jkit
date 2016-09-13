/**
 * 
 */
package commons.lang.statistics;

import org.junit.Test;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:43:40 AM Sep 12, 2016
 */
public class JvmMemoryStatisticsTest {
	JvmMemoryStatistics testedObject = new JvmMemoryStatistics();

	@Test
	public void testGetHeapMemoryInit() {
		System.out.println("-Xms" + testedObject.getHeapMemoryInit() / 1024 / 1024 + "m");
		System.out.println("-Xmx" + testedObject.getHeapMemoryMax() / 1024 / 1024 + "m");
		System.out.println("heap used:" + testedObject.getHeapMemoryUsed() + " bytes");
		System.out.println("heap commited:" + testedObject.getHeapMemoryCommitted() + " bytes");

		System.out.println("non-heap init:" + testedObject.getNonHeapMemoryInit() / 1024 / 1024 + "m");
		System.out.println("non-heap max:" + testedObject.getNonHeapMemoryMax() / 1024 / 1024 + "m");
		System.out.println("non-heap used:" + testedObject.getNonHeapMemoryUsed() / 1024 + "k");
		System.out.println("non-heap commited:" + testedObject.getNonHeapMemoryCommitted() / 1024 + "k");
		for (int i = 0; i < 1000000; i++) {
			String.valueOf(i).intern();
		}
		System.out.println("------------------------------");
		System.out.println("non-heap used:" + testedObject.getNonHeapMemoryUsed() / 1024 + "k");
		System.out.println("non-heap commited:" + testedObject.getNonHeapMemoryCommitted() / 1024 + "k");

	}
}

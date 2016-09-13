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
 * @since 11:26:13 AM Sep 12, 2016
 */
public class GcStatisticsTests {
	GcStatistics testedObject = new GcStatistics();

	@Test
	public void test() {
		System.out.println(testedObject.getCollectionCount());
	}
}

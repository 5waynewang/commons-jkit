/**
 * 
 */
package wcommons.lang;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import commons.lang.ByteUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:25:55 AM Nov 25, 2013
 */
public class ByteUtilsTests {

	@Test
	public void testToString() {
		byte[] bytes = "昨天，高温终于牛不起来了，40℃以上的气温完全销声匿迹，连能达到35℃高温线的地方都很少。省气象台说，截至昨天下午3点，仅温州、文成和永嘉3县最高气温超35℃，其他地区一般为30～34℃，杭州城区33.1℃，比前天低了2度。这也标志着，杭州经历了44天的“炙烤”模式后，这场“高温大戏”终于在昨天暂时落下帷幕。由于高温范围缩小，强度减弱，中央气象台持续发布近一个月的高温预警终于也在昨天解除了。"
				.getBytes();
		System.out.println(ArrayUtils.toString(bytes));

		System.out.println(ByteUtils.toString(bytes));
	}
}

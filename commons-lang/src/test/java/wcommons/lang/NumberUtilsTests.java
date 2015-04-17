/**
 * 
 */
package wcommons.lang;

import org.junit.Assert;
import org.junit.Test;

import commons.lang.NumberUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:26:16 AM Nov 25, 2013
 */
public class NumberUtilsTests {

	@Test
	public void testEq() {
		Assert.assertEquals(NumberUtils.eq(null, 1), false);
		Assert.assertEquals(NumberUtils.eq(null, null), true);
		Assert.assertEquals(NumberUtils.eq(1, 1), true);
		Assert.assertEquals(NumberUtils.eq(1, 1.0), true);
		Assert.assertEquals(NumberUtils.eq(new Integer(1), new Double(1)), true);
		Assert.assertEquals(NumberUtils.eq(1, 2), false);
	}

	@Test
	public void testEqZero() {
		Assert.assertEquals(NumberUtils.eqZero(""), false);
		final String s = null;
		Assert.assertEquals(NumberUtils.eqZero(s), false);
		final Number n = null;
		Assert.assertEquals(NumberUtils.eqZero(n), false);
		Assert.assertEquals(NumberUtils.eqZero(0), true);
		Assert.assertEquals(NumberUtils.eqZero(new Double(0)), true);
	}

	@Test
	public void testTruncate() {
		Assert.assertEquals(NumberUtils.truncate(2.67), 2.0);
		Assert.assertEquals(NumberUtils.truncate(2.67, 1), 2.6);
		Assert.assertEquals(NumberUtils.truncate(2.67, -1), 0.0);
		Assert.assertEquals(NumberUtils.truncate(-2.67), -2.0);
		Assert.assertEquals(NumberUtils.truncate(-2.67, 1), -2.6);
		Assert.assertEquals(NumberUtils.truncate(-2.67, -1), 0.0);
	}

	@Test
	public void testRound() {
		Assert.assertEquals(NumberUtils.round(2.67, 0), 3.0);
		Assert.assertEquals(NumberUtils.round(2.45, 0), 2.0);
		Assert.assertEquals(NumberUtils.round(2, 0), 2.0);
		Assert.assertEquals(NumberUtils.round(2.45, 1), 2.5);
		Assert.assertEquals(NumberUtils.round(2.44, 1), 2.4);
		Assert.assertEquals(NumberUtils.round(2.44, 2), 2.44);
		Assert.assertEquals(NumberUtils.round(2.44, 3), 2.44);
		Assert.assertEquals(NumberUtils.round(-2.44, 3), -2.44);
		Assert.assertEquals(NumberUtils.round(2.4467, 3), 2.447);
		Assert.assertEquals(NumberUtils.round(-2.4467, 3), -2.447);
		Assert.assertEquals(NumberUtils.round(2.45, -1), 0.0);
		Assert.assertEquals(NumberUtils.round(12.45, -1), 10.0);
		Assert.assertEquals(NumberUtils.round(252.45, -2), 300.0);
		Assert.assertEquals(NumberUtils.round(-252.45, -2), -100.0);
		Assert.assertEquals(NumberUtils.round(-252.45, -1), -250.0);
		Assert.assertEquals(NumberUtils.round(-2552.45, -1), -2550.0);
		Assert.assertEquals(NumberUtils.round(672552.45, -5), 700000.0);
		Assert.assertEquals(NumberUtils.round(672552.45, -6), 0.0);
	}

	@Test
	public void testCost() {
		final long start = System.currentTimeMillis();
		System.out.println(Math.round(-672552.45));
		// NumberUtils.round(672552.45);
		System.out.println(System.currentTimeMillis() - start);
	}

	@Test
	public void testEncode() {
		System.out.println(Long.toBinaryString(Byte.MAX_VALUE).length());
		System.out.println(Long.toBinaryString(Short.MAX_VALUE).length());
		System.out.println(Long.toBinaryString(Integer.MAX_VALUE).length());
		System.out.println(Long.toBinaryString(2));
	}

	@Test
	public void testParseBinaryString() {
		Assert.assertEquals(NumberUtils.parseBinaryString("1"), 1);
		Assert.assertEquals(NumberUtils.parseBinaryString("10"), 2);
		Assert.assertEquals(NumberUtils.parseBinaryString(Long.toBinaryString(Byte.MAX_VALUE)), Byte.MAX_VALUE);
		Assert.assertEquals(NumberUtils.parseBinaryString(Long.toBinaryString(Short.MAX_VALUE)), Short.MAX_VALUE);
		Assert.assertEquals(NumberUtils.parseBinaryString(Long.toBinaryString(Integer.MAX_VALUE)), Integer.MAX_VALUE);
		Assert.assertEquals(NumberUtils.parseBinaryString(Long.toBinaryString(Long.MAX_VALUE)), Long.MAX_VALUE);
	}
}

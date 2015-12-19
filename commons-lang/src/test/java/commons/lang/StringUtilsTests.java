/**
 * 
 */
package commons.lang;

import org.junit.Assert;
import org.junit.Test;

import commons.lang.StringUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 5:54:48 PM Mar 5, 2014
 */
public class StringUtilsTests {

	@Test
	public void testEquals() {
		Assert.assertEquals(StringUtils.equals(null, (String[]) null), true);
		Assert.assertEquals(StringUtils.equals(null, "a"), false);

		Assert.assertEquals(StringUtils.equals(null, "a", null), true);
		Assert.assertEquals(StringUtils.equals("b", "a", null), false);
		Assert.assertEquals(StringUtils.equals("b", "a", null, "b"), true);
	}

	@Test
	public void testReplace() {
		Assert.assertEquals(StringUtils.replace("abcabca", "a", ""), "bcabca");
		Assert.assertEquals(StringUtils.replace("abcabca", "a", (String[]) null), "abcabca");
		Assert.assertEquals(StringUtils.replace("abcabca", "a", "", ""), "bcbca");
		Assert.assertEquals(StringUtils.replace("abcabca", "a", "", "", ""), "bcbc");
		Assert.assertEquals(StringUtils.replace("abcabca", "a", "a", "b", "c"), "abcbbcc");
		Assert.assertEquals(StringUtils.replace("abcabca", "bc", "a", "b", "c"), "aaaba");
		Assert.assertEquals(StringUtils.replace("abcabca", "bc", "12", "34", "c"), "a12a34a");
		Assert.assertEquals(StringUtils.replace("abcabca", "bc", "12", null, "c"), "a12aca");
		Assert.assertEquals(StringUtils.replace("abcabca", "bc", "12345", "3456", "c"), "a12345a3456a");
	}
}

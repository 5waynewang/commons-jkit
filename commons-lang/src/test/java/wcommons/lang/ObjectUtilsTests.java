/**
 * 
 */
package wcommons.lang;

import junit.framework.Assert;

import org.junit.Test;

import commons.lang.ObjectUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:26:21 AM Nov 25, 2013
 */
public class ObjectUtilsTests {

	@Test
	public void testHasNull() {
		Assert.assertEquals(ObjectUtils.hasNull("123", 123, null, 121, null), true);
		Assert.assertEquals(ObjectUtils.hasNull("123", 123, 121), false);
	}
}

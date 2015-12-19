/**
 * 
 */
package commons.lang;

import org.junit.Test;

import commons.lang.LocalUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:26:12 AM Nov 25, 2013
 */
public class LocalUtilsTests {

	@Test
	public void testGetLocalAddress() {
		System.out.println(LocalUtils.getLocalAddress());
	}
}

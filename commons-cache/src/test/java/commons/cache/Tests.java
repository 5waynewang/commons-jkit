/**
 * 
 */
package commons.cache;

import java.util.Arrays;

import org.junit.Test;

import commons.lang.ArrayUtils;
import commons.lang.ByteUtils;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 3:03:57 PM Aug 15, 2016
 */
public class Tests {

	@Test
	public void printBytes() {
		final byte[] bytes = new byte[] { 1, 2, 127, 29 };
		
		System.out.println(Arrays.toString(bytes));
		System.out.println(ByteUtils.toString(bytes));
	}
}

/**
 * 
 */
package commons.validation;

import org.junit.Assert;
import org.junit.Test;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 3:46:39 PM Nov 14, 2015
 */
public class PhoneNumberValidatorTest {

	@Test
	public void testIsChineseMobile() {
		Assert.assertTrue(PhoneNumberValidator.isChineseMobile("15306555959"));
		Assert.assertTrue(PhoneNumberValidator.isChineseMobile("+8615306555959"));
		Assert.assertFalse(PhoneNumberValidator.isChineseMobile("+18615306555959"));
		Assert.assertFalse(PhoneNumberValidator.isChineseMobile("05306555959"));
		Assert.assertFalse(PhoneNumberValidator.isChineseMobile("1530655595"));
		Assert.assertFalse(PhoneNumberValidator.isChineseMobile("153065559599"));
		Assert.assertFalse(PhoneNumberValidator.isChineseMobile("1530655595A"));
		Assert.assertFalse(PhoneNumberValidator.isChineseMobile("25306555959"));
	}
}

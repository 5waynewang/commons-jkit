/**
 * 
 */
package wcommons.lang;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Test;

import commons.lang.DateStatics;
import commons.lang.DateUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:26:04 AM Nov 25, 2013
 */
public class DateUtilsTests {

	@Test
	public void testTruncateDifferYears() {
		Assert.assertEquals(
				DateUtils.truncateDifferYears(DateUtils.parseDate("20130202"), DateUtils.parseDate("20140201")), 0);
		Assert.assertEquals(
				DateUtils.truncateDifferYears(DateUtils.parseDate("20130202"), DateUtils.parseDate("20140202")), 1);
		Assert.assertEquals(
				DateUtils.truncateDifferYears(DateUtils.parseDate("20130202"), DateUtils.parseDate("20150203")), 2);
	}

	@Test
	public void testGetDifferYears() {
		Assert.assertEquals(DateUtils.getDifferYears(DateUtils.parseDate("20130202"), DateUtils.parseDate("20140201")),
				1);
		Assert.assertEquals(DateUtils.getDifferYears(DateUtils.parseDate("20130202"), DateUtils.parseDate("20140202")),
				1);
		Assert.assertEquals(DateUtils.getDifferYears(DateUtils.parseDate("20130202"), DateUtils.parseDate("20150203")),
				2);
	}

	@Test
	public void testTruncateDifferMonths() {
		Assert.assertEquals(
				DateUtils.truncateDifferMonths(DateUtils.parseDate("20140102"), DateUtils.parseDate("20140201")), 0);
		Assert.assertEquals(
				DateUtils.truncateDifferMonths(DateUtils.parseDate("20130102"), DateUtils.parseDate("20140202")), 13);
		Assert.assertEquals(
				DateUtils.truncateDifferMonths(DateUtils.parseDate("20120102"), DateUtils.parseDate("20140203")), 25);
	}

	@Test
	public void testGetDifferMonths() {
		Assert.assertEquals(
				DateUtils.getDifferMonths(DateUtils.parseDate("20140102"), DateUtils.parseDate("20140201")), 1);
		Assert.assertEquals(
				DateUtils.getDifferMonths(DateUtils.parseDate("20130102"), DateUtils.parseDate("20140202")), 13);
		Assert.assertEquals(
				DateUtils.getDifferMonths(DateUtils.parseDate("20120102"), DateUtils.parseDate("20140203")), 25);
	}

	@Test
	public void testFormatDate() {
		final String strDate = "2013-08-01";
		final Date date = commons.lang.DateUtils.parseDate(strDate);
		Assert.assertEquals(strDate, commons.lang.DateUtils.formatDate(date));
	}

	@Test
	public void testFormatDate2() {
		final String strDate = "2013-08-01 00:00:00";
		final Date date = commons.lang.DateUtils.parseDate(strDate);
		Assert.assertEquals("2013-08-01", commons.lang.DateUtils.formatDate(date));
	}

	@Test
	public void testFormatDate3() {
		final String strDate = "2013-08-01 00:00:01";
		final Date date = commons.lang.DateUtils.parseDate(strDate);
		Assert.assertEquals(strDate, commons.lang.DateUtils.formatDate(date));
	}

	@Test
	public void testFormatDate4() {
		final String strDate = "2013-08-01T00:00:00";
		final Date date = commons.lang.DateUtils.parseDate(strDate);
		Assert.assertEquals("2013-08-01", commons.lang.DateUtils.formatDate(date));
	}

	@Test
	public void testFormatExpression() {
		Assert.assertEquals(DateUtils.formatExpression(61 * 1000), "00:01:01");
		Assert.assertEquals(DateUtils.formatExpression(61, TimeUnit.SECONDS), "00:01:01");
		Assert.assertEquals(DateUtils.formatExpression(3601, TimeUnit.SECONDS), "01:00:01");
		Assert.assertEquals(DateUtils.formatExpression(3661, TimeUnit.SECONDS), "01:01:01");
		Assert.assertEquals(DateUtils.formatExpression(25 * 3600 + 61, TimeUnit.SECONDS), "25:01:01");
	}

	@Test
	public void testParseDDHHMMSS() {
		final long[] results = { 5, 6, 7, 8 };

		final long duration = results[0] * DateStatics.ONE_DAY_MILLIS + results[1] * DateStatics.ONE_HOUR_MILLIS
				+ results[2] * DateStatics.ONE_MINUTE_MILLIS + results[3] * DateStatics.ONE_SECOND_MILLIS;

		final long[] rslts = DateUtils.parseDDHHMMSS(duration);

		Assert.assertEquals(results.length, rslts.length);

		for (int i = 0; i < results.length; i++) {
			Assert.assertEquals(results[i], rslts[i]);
		}
	}
}

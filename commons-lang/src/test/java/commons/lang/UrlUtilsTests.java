/**
 * 
 */
package commons.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import commons.lang.UrlUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 5:04:49 PM Dec 11, 2013
 */
public class UrlUtilsTests {
	
	@Test
	public void testExtractParam() {
		Assert.assertEquals(UrlUtils.extractParam("https://item.taobao.com/item.htm?spm=a219r.lm874.14.1.1s9AQE&id=528585549538&ns=1&abbucket=6#detail", "id"), "528585549538");
	}

	@Test
	public void testGetStringParams2() {
		final Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put(
				"content",
				"【短代预警】最近60分钟内的订单总量为195笔，其中未支付的订单为22笔，占总量的11.28%；用户参与支付的订单为173笔，其中支付成功的订单为166笔，成功率为95.95%，支付失败的订单为1笔，失败率为0.58%，未回执的订单为6笔，未回执率为3.47%。");
		params.put("telephone", "15306555959");
		params.put("token", "cf6df26d5762952727dd88b709e1d422");
		//http://192.168.11.44:8080/ReadPlatformClient_Content/servlet/clientSendSms?
		System.out.println(UrlUtils.getStringParams(params));
	}

	@Test
	public void testGetStringParams() {
		final Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("a", "1");
		params.put("b", 1);
		params.put("c", 1.0);
		params.put("d", null);
		params.put("e", new char[] { '1', '2' });
		params.put("f", new Character[] { '1', null });
		params.put("g", new byte[] { 1, 2 });
		params.put("h", new Byte[] { 1, null });
		params.put("i", new short[] { 1, 2 });
		params.put("j", new Short[] { 1, null });
		params.put("k", new int[] { 1, 2 });
		params.put("l", new Integer[] { 1, null });
		params.put("m", new long[] { 1, 2 });
		params.put("n", new Long[] { 1L, null });
		params.put("o", new float[] { 1.0f, 2.0f });
		params.put("p", new Float[] { 1.0f, null });
		params.put("q", new double[] { 1.0, 2.0 });
		params.put("r", new Double[] { 1.0, null });

		ArrayList<Object> s = new ArrayList<Object>();
		s.add("1");
		s.add(null);
		s.add("2");
		params.put("s", s);

		params.put("t", new String[] { "1", null });

		Assert.assertEquals(
				UrlUtils.getStringParams(params),
				"a=1&b=1&c=1.0&d=&e=1&e=2&f=1&f=&g=1&g=2&h=1&h=&i=1&i=2&j=1&j=&k=1&k=2&l=1&l=&m=1&m=2&n=1&n=&o=1.0&o=2.0&p=1.0&p=&q=1.0&q=2.0&r=1.0&r=&s=1&s=&s=2&t=1&t=");
	}

	@Test
	public void testSpliceParams() {
		Assert.assertEquals(UrlUtils.spliceParams("http://g.cn", "a=b&c=1"), "http://g.cn?a=b&c=1");
		Assert.assertEquals(UrlUtils.spliceParams("http://g.cn", "&a=b&c=1"), "http://g.cn?a=b&c=1");
		Assert.assertEquals(UrlUtils.spliceParams("http://g.cn?", "a=b&c=1"), "http://g.cn?a=b&c=1");
		Assert.assertEquals(UrlUtils.spliceParams("http://g.cn?", "&a=b&c=1"), "http://g.cn?a=b&c=1");
		Assert.assertEquals(UrlUtils.spliceParams("http://g.cn?d=5", "a=b&c=1"), "http://g.cn?d=5&a=b&c=1");
		Assert.assertEquals(UrlUtils.spliceParams("http://g.cn?d=5", "&a=b&c=1"), "http://g.cn?d=5&a=b&c=1");
		Assert.assertEquals(UrlUtils.spliceParams("http://g.cn?d=5&", "a=b&c=1"), "http://g.cn?d=5&a=b&c=1");
		Assert.assertEquals(UrlUtils.spliceParams("http://g.cn?d=5&", "&a=b&c=1"), "http://g.cn?d=5&a=b&c=1");
	}

	@Test
	public void testSpliceUrls() {
		Assert.assertEquals(UrlUtils.spliceUrls("http://g.cn/", "/gmail/", "/login.do"), "http://g.cn/gmail/login.do");
		Assert.assertEquals(UrlUtils.spliceUrls("http://g.cn", null, "gmail", null, "login.do"),
				"http://g.cn/gmail/login.do");
		Assert.assertEquals(UrlUtils.spliceUrls("http://g.cn", "gmail", "login.do"), "http://g.cn/gmail/login.do");
	}

	@Test
	public void testGetStringParamMap() {
		Assert.assertEquals(UrlUtils.getStringParamMap(null).size(), 0);
		Assert.assertEquals(UrlUtils.getStringParamMap("").size(), 0);
		Assert.assertEquals(UrlUtils.getStringParamMap("    ").size(), 0);

		Assert.assertEquals(UrlUtils.getStringParamMap("a=b&c=d").size(), 2);
		Assert.assertEquals(UrlUtils.getStringParamMap("a=b&c=d").get("a"), "b");
		Assert.assertEquals(UrlUtils.getStringParamMap("a=b&c=d").get("c"), "d");

		Assert.assertEquals(UrlUtils.getStringParamMap("a=b&c=d&a=e").size(), 2);
		Assert.assertEquals(UrlUtils.getStringParamMap("a=b&c=d&a=e").get("a"), "e");
		Assert.assertEquals(UrlUtils.getStringParamMap("a=b&c=d&a=e").get("c"), "d");

		Assert.assertEquals(UrlUtils.getStringParamMap("a=1&c=d&a=").size(), 2);
		Assert.assertEquals(UrlUtils.getStringParamMap("a=1&c=d&a=").get("a"), "");
		Assert.assertEquals(UrlUtils.getStringParamMap("a=1&c=d&a=").get("c"), "d");
	}

	@Test
	public void testGetListParamMap() {
		Assert.assertEquals(UrlUtils.getListParamMap(null).size(), 0);
		Assert.assertEquals(UrlUtils.getListParamMap("").size(), 0);

		Assert.assertEquals(UrlUtils.getListParamMap("a=b&c=d").size(), 2);
		Assert.assertEquals(UrlUtils.getListParamMap("a=b&c=d").get("a").size(), 1);
		Assert.assertEquals(UrlUtils.getListParamMap("a=b&c=d").get("a").get(0), "b");
		Assert.assertEquals(UrlUtils.getListParamMap("a=b&c=d").get("c").get(0), "d");

		Assert.assertEquals(UrlUtils.getListParamMap("a=b&c=d&a=e").size(), 2);
		Assert.assertEquals(UrlUtils.getListParamMap("a=b&c=d&a=e").get("a").size(), 2);
		Assert.assertEquals(UrlUtils.getListParamMap("a=b&c=d&a=e").get("a").get(0), "b");
		Assert.assertEquals(UrlUtils.getListParamMap("a=b&c=d&a=e").get("a").get(1), "e");
		Assert.assertEquals(UrlUtils.getListParamMap("a=b&c=d&a=e").get("c").get(0), "d");

		Assert.assertEquals(UrlUtils.getListParamMap("a=&c=d&a=").size(), 2);
		Assert.assertEquals(UrlUtils.getListParamMap("a=&c=d&a=").get("a").size(), 2);
		Assert.assertEquals(UrlUtils.getListParamMap("a=&c=d&a=").get("a").get(0), "");
		Assert.assertEquals(UrlUtils.getListParamMap("a=&c=d&a=").get("a").get(1), "");
	}
}

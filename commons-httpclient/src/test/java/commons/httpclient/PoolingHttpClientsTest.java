/**
 * 
 */
package commons.httpclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 7:50:26 PM Mar 10, 2015
 */
public class PoolingHttpClientsTest {
	
	@Test
	public void testCustomHeader() {
		final String proxy = "10.166.0.54:18888";
		
		HttpInvokeResult result = PoolingHttpClients.proxyGet("http://config.ixiaopu.com:54321/config.co?dataId=GLOBAL&group=XIANGQU", proxy);
		
		System.out.println(result.getHeader("Content-Md5"));
	}

	@Test
	public void testPostRedirect() {
		final String url = "http://xiangqu.kkkdtest.com:8888/v2/signin_check";
		final Collection<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("u", "15306555959"));
		params.add(new BasicNameValuePair("p", "e10adc3949ba59abbe56e057f20f883e"));
		final Collection<Header> headers = new ArrayList<Header>(1);
		headers.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));

		final HttpInvokeResult result = PoolingHttpClients.post(url, params, headers);

		System.out.println(result);
	}

	@Test
	public void testGetResponseBodyAsString() {
		HttpInvokeResult result = PoolingHttpClients.get("http://config.ixiaopu.com:54321/config.co?dataId=GLOBAL&group=XIANGQU");
		if (result.isSuccess()) {
			System.out.println(result.getResponseBodyAsString());
		} else if (result.getException() != null) {
			System.out.println(result);
			result.getException().printStackTrace();
		} else {
			System.out.println(result);
		}
	}
	
	@Test
	public void testGet2() {
		final String url = "http://www.xiangqu.com/advertise/qqAd?muid=472cba8f94b51247b47b63c95987f89d&click_time=1440379992&appid=575466337&click_id=f309e6a24b5b13493c9f610a9d2b83b8&app_type=ios&advertiser_id=200997";
		
		final HttpGet httpGet = PoolingHttpClients.createHttpGet(url, null);
		
		final HttpInvokeResult result = PoolingHttpClients.invoke(httpGet, 0);
		httpGet.setProtocolVersion(new ProtocolVersion("HTTP", 1, 0));
		
		System.out.println(result);
	}

	@Test
	public void testPost() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("string", "str1");
		params.put("stringArray", new String[] { "str1", "str2" });

		HttpInvokeResult result = PoolingHttpClients.post("http://www.baidu.com", params);
		if (result.isSuccess()) {
			System.out.println(result.getResponseBodyAsString());
		} else if (result.getException() != null) {
			System.out.println(result);
			result.getException().printStackTrace();
		} else {
			System.out.println(result);
		}
	}
}

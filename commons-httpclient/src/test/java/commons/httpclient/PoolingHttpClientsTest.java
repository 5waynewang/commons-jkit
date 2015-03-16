/**
 * 
 */
package commons.httpclient;

import java.util.HashMap;
import java.util.Map;

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
	public void testGet() {
		HttpInvokeResult result = PoolingHttpClients.get("http://www.baidu.com");
		if (result.isSuccess()) {
			System.out.println(result.getContent());
		} else if (result.getException() != null) {
			System.out.println(result);
			result.getException().printStackTrace();
		} else {
			System.out.println(result);
		}
	}

	@Test
	public void testPost() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("string", "str1");
		params.put("stringArray", new String[] { "str1", "str2" });

		HttpInvokeResult result = PoolingHttpClients.post("http://www.baidu.com", params);
		if (result.isSuccess()) {
			System.out.println(result.getContent());
		} else if (result.getException() != null) {
			System.out.println(result);
			result.getException().printStackTrace();
		} else {
			System.out.println(result);
		}
	}
}

/**
 * 
 */
package commons.lang.properties;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import commons.lang.properties.PropertiesListener;
import commons.lang.properties.PropertiesUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:26:26 AM Nov 25, 2013
 */
public class PropertiesUtilsTests {

	@Test
	public void testToString() throws Exception {
		final Properties prop = new Properties();
		prop.load(this.getClass().getResourceAsStream("/app.properties"));

		System.out.println(JSON.toJSONString(prop));
	}

	@Test
	public void testLoadProperties() throws Exception {
		final String path = this.getClass().getResource("/app.properties").getPath();

		final Properties properties = PropertiesUtils.loadProperties(path, new PropertiesListener() {
			@Override
			public void receive(String content) {
				System.out.println(content);
			}

			@Override
			public Executor getExecutor() {
				return null;
			}
		}, 5);

		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}

		Thread.sleep(5 * 60 * 1000);
	}
}

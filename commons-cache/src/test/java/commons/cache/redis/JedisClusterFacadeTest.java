/**
 * 
 */
package commons.cache.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import redis.clients.util.SafeEncoder;

import commons.cache.config.RedisConfig;
import commons.cache.facade.redis.JedisClusterFacade;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 5:16:08 PM Jul 9, 2015
 */
//@org.junit.Ignore
public class JedisClusterFacadeTest {

	JedisClusterFacade testedObject;

	final String profile = "dev";

	@Before
	public void before() throws Exception {
		final RedisConfig redisConfig = new RedisConfig();
		redisConfig.setClusters("10.8.100.180:7000 10.8.100.180:7001 10.8.100.180:7002 10.8.100.180:7003 10.8.100.180:7004 10.8.100.180:7005");
		this.testedObject = new JedisClusterFacade(redisConfig);
	}

	@Test
	public void testIncrAndGet() {
		final String key = "lucifer_test_key_incr_and_get";
		this.testedObject.delete(key);

		final Long incr1 = this.testedObject.incr(key, 5, 5, TimeUnit.MINUTES);
		Assert.assertEquals(incr1.longValue(), 5);

		final Long incr2 = this.testedObject.incr(key, 1, 5, TimeUnit.MINUTES);
		Assert.assertEquals(incr2.longValue(), 6);
	}
	
	@Test
	public void testMincr() {
		final String[] keys = { "lucifer_test_incr1", "lucifer_test_incr2", "lucifer_test_incr3" };
		final Map<String, Long> results1 = new HashMap<String, Long>();
		for (String key : keys) {
			final Long value = this.testedObject.incr(key, ThreadLocalRandom.current().nextInt(1, 100));
			results1.put(key, value);

			final byte[] bs = SafeEncoder.encode(value.toString());
			for (byte b : bs) {
				System.out.print(b);
				System.out.print(" ");
			}
			System.out.println();
		}

		final Map<String, Long> results = this.testedObject.mincr(keys);
		for (Map.Entry<String, Long> entry : results.entrySet()) {
			Assert.assertEquals(entry.getValue().longValue(), results1.get(entry.getKey()).longValue());
		}
	}
}

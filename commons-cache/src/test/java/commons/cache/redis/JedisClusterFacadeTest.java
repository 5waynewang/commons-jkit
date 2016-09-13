/**
 * 
 */
package commons.cache.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import redis.clients.util.JedisClusterCRC16;
import redis.clients.util.SafeEncoder;
import commons.cache.config.RedisConfig;
import commons.cache.facade.redis.JedisClusterFacade;
import commons.cache.facade.redis.SubscribeListener;

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

	@Before
	public void before() throws Exception {
		final RedisConfig redisConfig = new RedisConfig();
		redisConfig.setClusters("10.8.100.129:6379 10.8.100.129:6479 10.8.100.129:6579");
		this.testedObject = new JedisClusterFacade(redisConfig);
	}

	@Test
	public void testPublishAndSubcribe() throws Exception {
		final String topic = "lucifer_test_publishAndSubcribe";

		final int count = 10;
		final CountDownLatch cdl = new CountDownLatch(count);
		new Thread(new Runnable() {
			@Override
			public void run() {
				testedObject.subscribe(new SubscribeListener<String>() {
					@Override
					public void onMessage(String topic, String message) {
						try {
							System.out.println("topic: " + topic + ", message: " + message);
						}
						finally {
							cdl.countDown();
						}
					}
				}, topic);
			}
		}).start();
		for (int i = 0; i < count; i++) {
			testedObject.publish(topic, "message" + i);
		}

		cdl.await();
	}

	@Test
	public void testIncrGetSet() {
		final String key = "lucifer_test_incrGetSet";
		Assert.assertTrue(testedObject.setNoIncr(key, 200));
		Assert.assertTrue(testedObject.getNoIncr(key) == 200);
		Assert.assertTrue(testedObject.incr(key, 10) == 210);
		Assert.assertTrue(testedObject.getNoIncr(key) == 210);
		testedObject.delete(key);
	}
	
	@Test
	public void testDelete() {
		testedObject.deleteQuietly("1111");
	}

	@Test
	public void testGet() {
		final String key = "lucifer_test_get_set";
		final String value = "lucifer_test_get_set_value";

		this.testedObject.set(key, value, 5, TimeUnit.MINUTES);

		final String v = this.testedObject.get(key);

		Assert.assertEquals(value, v);

	}

	@Test
	public void testSadd() {
		final String key = "lucifer_test_sadd";

		this.testedObject.sadd(key, "1", null, "2");

		System.out.println(this.testedObject.scard(key));
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
	public void testGetCRC16() {
		for (int i = 1; i <= 10; i++) {
			String key = "key" + i;
			System.out.println(key + " = " + JedisClusterCRC16.getSlot(key));
		}
	}

	@Test
	public void testMincr() {
		final String[] keys = { "{lucifer_test_incr}1", "{lucifer_test_incr}2", "{lucifer_test_incr}3",
				"{lucifer_test_incr}4" };
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

	@Test
	public void testEval() {
//		final StringBuilder script = new StringBuilder();
//		script.append("redis.call('incr', KEYS[1])");
//		script.append("return redis.call('set', KEYS[2], ARGV[1]);");
//		script.append("return redis.call('set', KEYS[2], ARGV[1]);");
		
		final String script = 
        "if (redis.call('exists', KEYS[1]) == 0) then " +
        	"redis.call('set', KEYS[1], ARGV[1]); " +
        "else " +
	        "local pingtu = redis.call('get', KEYS[1]); " +
	        "local v = bor(pingtu, ARGV[1]); " +
	        "redis.call('set', KEYS[1], v); " +
	    "end; ";
//	    // +
//	    "redis.call('incr', KEYS[2]); " +
//	    // -
//	    "redis.call('decr', KEYS[3]); " +
//	    //
//	    "return redis.call('pttl', KEYS[1]);";
		
		System.out.println(this.testedObject.getNoIncr("{pingtu}137070"));
		
		final Object result = this.testedObject.eval(script.toString(), 1, "{pingtu}137070", "2");
//		final Object result = this.testedObject.eval(script.toString(), 2, "{pingtu}1counter", "{pingtu}137070", "4");
		
		System.out.println(result);
	}
}

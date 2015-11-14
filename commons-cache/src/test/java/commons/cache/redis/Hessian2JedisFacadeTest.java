/**
 * 
 */
package commons.cache.redis;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

import commons.cache.TestedModel;
import commons.cache.config.RedisConfig;
import commons.cache.exception.CancelCasException;
import commons.cache.facade.redis.Hessian2JedisFacade;
import commons.cache.facade.redis.JedisFacade;
import commons.cache.operation.CasOperation;
import commons.lang.concurrent.NamedThreadFactory;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 5:16:08 PM Jul 9, 2015
 */
@org.junit.Ignore
public class Hessian2JedisFacadeTest {

	JedisFacade testedObject;

	final String profile = "dev";

	@Before
	public void before() throws Exception {
		final RedisConfig redisConfig = new RedisConfig();
		redisConfig.setHost("10.8.100.2");
		this.testedObject = new Hessian2JedisFacade(redisConfig);
	}

	@Test
	public void testPushPoll() {
		final String key = "lucifer_test_push_poll";

		final TestedModel value1 = TestedModel.createObject();
		Assert.assertTrue(this.testedObject.rpush(key, value1) == 1);

		final TestedModel value2 = TestedModel.createObject();
		Assert.assertTrue(this.testedObject.rpush(key, value2) == 2);

		Assert.assertTrue(this.testedObject.lpop(key).equals(value1));
		Assert.assertTrue(this.testedObject.lpop(key).equals(value2));
		Assert.assertTrue(this.testedObject.lpop(key) == null);
	}

	@Test
	public void testTake() throws Exception {
		final int count = 1000;
		final String key1 = "lucifer_test_take1";
		final String key2 = "lucifer_test_take2";

		for (int i = 0; i < count; i++) {
			testedObject.rpush(key1, TestedModel.createObject());
			testedObject.rpush(key2, TestedModel.createObject());
		}

		final CountDownLatch cdl = new CountDownLatch(count * 2);

		final int corePoolSize = 8;
		final ExecutorService taskThreadExecutor = Executors.newFixedThreadPool(corePoolSize,
				new NamedThreadFactory("TakeThread_"));
		for (int i = 0; i < corePoolSize; i++) {
			taskThreadExecutor.submit(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							final TestedModel value = testedObject.brpop(0, key1, key2);
							if (value == null) {
								System.err.println(Thread.currentThread().getName() + " take 0 data");
							} else {
								System.err.println(Thread.currentThread().getName() + " take 1 data");
							}
						} finally {
							cdl.countDown();
						}
					}
				}
			});
		}

		cdl.await();
	}

	@Test
	public void testSerializeKey() throws Exception {
		serializeKey('a');
		serializeKey(Boolean.FALSE);
		serializeKey(100);
		serializeKey(100L);
		serializeKey(100f);
		serializeKey(100d);
		serializeKey("abcd");
	}

	<V> void serializeKey(V v) throws Exception {
		final byte[] sv = testedObject.serializeKey(v);
		Assert.assertTrue(sv.length > 0);
		Assert.assertEquals(testedObject.deserializeKey(sv), v);
	}

	@Test
	public void testSerializeValue() throws Exception {
		serializeValue(TestedModel.createObject());
		// serializeValue(java.math.BigDecimal.valueOf(99));
		serializeValue('a');
		serializeValue(Boolean.FALSE);
		serializeValue(100);
		serializeValue(100L);
		serializeValue(100f);
		serializeValue(100d);
		serializeValue("abcd");
		serializeArray(new Byte[] { 1, 2, 3, 4, 5 });
		serializeArray(new Integer[] { 1, 2, 3, 4, 5 });
		serializeArray(new String[] { "1", "2", "3" });
	}

	<V> void serializeValue(V v) throws Exception {
		final byte[] sv = testedObject.serializeValue(v);
		Assert.assertTrue(sv.length > 0);
		V dest = testedObject.deserializeValue(sv);
		Assert.assertEquals(dest, v);
	}

	<V> void serializeArray(V[] v) throws Exception {
		final byte[] sv = testedObject.serializeValue(v);
		Assert.assertTrue(sv.length > 0);
		Assert.assertArrayEquals((V[]) testedObject.deserializeValue(sv), v);
	}

	@Test
	public void testSetValue() {
		final String key = "lucifer_test_key_get_set";
		final Object value = TestedModel.createObject();
		System.out.println(JSON.toJSONString(value));
		this.testedObject.set(key, value);
		final TestedModel v = this.testedObject.get(key);
		System.out.println(JSON.toJSONString(value));
		Assert.assertEquals(v, value);
	}

	@Test
	public void testGetValue() {
		final String key = "lucifer_test_key_get_set";
		final Object value = this.testedObject.get(key);
		System.out.println(JSON.toJSONString(value));
	}

	@Test
	public void testGetSet() {
		final String key = "lucifer_test_key_get_set";
		final Object value = TestedModel.createObject();
		final Object value2 = TestedModel.createObject();
		this.testedObject.set(key, value);

		Assert.assertEquals(this.testedObject.get(key), value);
		Assert.assertEquals(this.testedObject.getSet(key, value2), value);
		Assert.assertEquals(this.testedObject.get(key), value2);
		this.testedObject.delete(key);
		Assert.assertNull(this.testedObject.get(key));
	}

	@Test
	public void testGetSet2() {
		final String key = "lucifer_test_key_get_set";
		final Object value = "lucifer_test_value_get_set";
		final Object value2 = "lucifer_test_value_get_set2";
		this.testedObject.set(key, value);

		Assert.assertEquals(this.testedObject.get(key), value);
		Assert.assertEquals(this.testedObject.getSet(key, value2), value);
		Assert.assertEquals(this.testedObject.get(key), value2);
		this.testedObject.delete(key);
		Assert.assertNull(this.testedObject.get(key));
	}

	@Test
	public void testCas() throws Exception {
		final String key = "lucifer_test_key_cas";
		this.testedObject.delete(key);
		Assert.assertNull(this.testedObject.get(key));

		final int corePoolSize = 8;
		final ScheduledExecutorService exec = Executors.newScheduledThreadPool(corePoolSize);

		final int size = 10000;
		final CountDownLatch cdl = new CountDownLatch(size);

		for (int i = 0; i < size; i++) {
			exec.submit(new Runnable() {
				@Override
				public void run() {
					try {
						Boolean ret = testedObject.cas(key, new CasOperation<Integer>() {
							@Override
							public Integer getNewValue(Integer value) throws CancelCasException {
								return value == null ? 1 : (value + 1);
							}

							@Override
							public int casMaxTries() {
								return 0;
							}
						});
						System.out.println(Thread.currentThread().getName() + " " + ret);
					} finally {
						cdl.countDown();
					}
				}
			});
		}

		cdl.await();

		Assert.assertEquals(testedObject.get(key), size);
		this.testedObject.delete(key);
		Assert.assertNull(this.testedObject.get(key));
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
}

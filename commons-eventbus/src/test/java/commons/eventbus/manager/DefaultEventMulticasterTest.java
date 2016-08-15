/**
 * 
 */
package commons.eventbus.manager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import commons.eventbus.RedisEventBus;
import commons.eventbus.closure.EventListenerAdapter;
import commons.eventbus.multicaster.DefaultEventMulticaster;
import redis.clients.jedis.BinaryJedisCluster;
import redis.clients.jedis.HostAndPort;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 1:21:34 PM Jul 21, 2016
 */
public class DefaultEventMulticasterTest {
	String profile = "dev";
	
	DefaultEventMulticaster ebm;

	@Before
	public void before() {
//		ebm = new DefaultEventMulticaster();
		
		final String clusters;
		if (StringUtils.endsWithIgnoreCase("online", profile)) {
			clusters = "10.18.10.206:6379 10.18.10.207:6379 10.18.10.208:6379 10.18.10.209:6379 10.18.10.234:6379 10.18.10.236:6379";
		}
		else if (StringUtils.endsWithIgnoreCase("pre", profile)) {
			clusters = "10.18.10.127:6379 10.18.10.127:6479 10.18.10.127:6579";
		}
		else if (StringUtils.endsWithIgnoreCase("test", profile)) {
			clusters = "10.8.100.129:6379 10.8.100.129:6479 10.8.100.129:6579";
		}
		else {
			clusters = "10.8.100.180:7000 10.8.100.180:7001 10.8.100.180:7002";
		}
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		for (String str : clusters.split("[,\\s\\t]+")) {
			final String[] arr = str.split(":");

			nodes.add(new HostAndPort(arr[0], Integer.parseInt(arr[1])));
		}
		BinaryJedisCluster jedisCluster = new BinaryJedisCluster(nodes);
		ebm = new DefaultEventMulticaster(new RedisEventBus(jedisCluster));
	}

	@Test
	public void testPC_once() throws InterruptedException {
		final Object event = "test_event_1";
		final int size = 10;
		final CountDownLatch cdl = new CountDownLatch(2 * size - 1);
		final int removedIndex = ThreadLocalRandom.current().nextInt(0, size);
		EventListenerAdapter removeListener = null;
		for (int i = 0; i < size; i++) {
			final int index = i;

			EventListenerAdapter listener = new EventListenerAdapter() {
				@Override
				public void execute(Object... args) {
					try {
						System.out.println("listener_" + index + "'s args:" + Arrays.toString(args));
					}
					finally {
						cdl.countDown();
					}
				}
			};

			ebm.addListener(event, listener);

			if (index == removedIndex) {
				removeListener = listener;
			}
		}

		ebm.multicastEvent(event, 1, 2, 3);

		final long start = System.nanoTime();
		cdl.await(1, TimeUnit.SECONDS);
		final long duration = System.nanoTime() - start;
		System.out.println("cost " + duration + " ns, also " + TimeUnit.NANOSECONDS.toMillis(duration) + " ms");

		// 删除一个lister
		ebm.removeListener(event, removeListener);

		System.out.println("remove listener " + removedIndex);

		ebm.multicastEvent(event, 1, 2, 3);

		final long start2 = System.nanoTime();
		cdl.await();
		final long duration2 = System.nanoTime() - start2;
		System.out.println("cost " + duration2 + " ns, also " + TimeUnit.NANOSECONDS.toMillis(duration2) + " ms");

	}
}

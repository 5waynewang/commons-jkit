/**
 * 
 */
package commons.eventbus.manager;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import commons.eventbus.closure.EventListenerAdapter;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 1:21:34 PM Jul 21, 2016
 */
public class DefaultEventBusManagerTest {
	DefaultEventBusManager ebm = new DefaultEventBusManager();

	@Before
	public void before() {
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

			ebm.registerListener(event, listener);

			if (index == removedIndex) {
				removeListener = listener;
			}
		}

		ebm.fireEvent(event, 1, 2, 3);

		final long start = System.nanoTime();
		cdl.await(1, TimeUnit.SECONDS);
		final long duration = System.nanoTime() - start;
		System.out.println("cost " + duration + " ns, also " + TimeUnit.NANOSECONDS.toMillis(duration) + " ms");

		// 删除一个lister
		ebm.removeListener(event, removeListener);

		System.out.println("remove listener " + removedIndex);

		ebm.fireEvent(event, 1, 2, 3);

		final long start2 = System.nanoTime();
		cdl.await();
		final long duration2 = System.nanoTime() - start2;
		System.out.println("cost " + duration2 + " ns, also " + TimeUnit.NANOSECONDS.toMillis(duration2) + " ms");

	}
}

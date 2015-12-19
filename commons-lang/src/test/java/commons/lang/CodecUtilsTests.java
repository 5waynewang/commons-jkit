/**
 * 
 */
package commons.lang;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Assert;
import org.junit.Test;

import commons.lang.CodecUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:26:00 AM Nov 25, 2013
 */
public class CodecUtilsTests {

	@Test
	public void testCodec() throws InterruptedException {
		final long start = System.currentTimeMillis();
		final CountDownLatch cd = new CountDownLatch(64);
		final long step = Long.MAX_VALUE / 32;
		final ScheduledExecutorService exec = Executors.newScheduledThreadPool(64);
		for (int i = 0; i < 32; i++) {
			final int index = i;
			exec.submit(new Runnable() {
				@Override
				public void run() {
					final long s = Long.MIN_VALUE + index * step;
					final long e = Long.MIN_VALUE + (index + 1) * step;
					for (long i = s; i <= e; i++) {
						Assert.assertEquals(CodecUtils.decodeLong(CodecUtils.encode(i)), i);
					}
					cd.countDown();
					System.out.println("complete " + s + " - " + e);
				}
			});
		}
		for (int i = 0; i < 32; i++) {
			final int index = i;
			exec.submit(new Runnable() {
				@Override
				public void run() {
					final long s = index * step;
					final long e = (index + 1) * step;
					for (long i = s; i <= e; i++) {
						Assert.assertEquals(CodecUtils.decodeLong(CodecUtils.encode(i)), i);
					}
					cd.countDown();
					System.out.println("complete " + s + " - " + e);
				}
			});
		}

		cd.await();
		System.out.println("cost: " + (System.currentTimeMillis() - start));
	}

	@Test
	public void testEncode() {

		System.out.println(CodecUtils.encode(Integer.MAX_VALUE));
		System.out.println(CodecUtils.encode(4));
		System.out.println(CodecUtils.encode(12));
		System.out.println(CodecUtils.encode(34567823));
	}

	@Test
	public void testDecodeLong() {
		Assert.assertEquals(CodecUtils.decodeLong(CodecUtils.encode(Integer.MAX_VALUE)),
				Integer.MAX_VALUE);
		Assert.assertEquals(CodecUtils.decodeLong(CodecUtils.encode(Long.MAX_VALUE)),
				Long.MAX_VALUE);
		Assert.assertEquals(CodecUtils.decodeLong(CodecUtils.encode(1)), 1);
		Assert.assertEquals(CodecUtils.decodeLong(CodecUtils.encode(5)), 5);
		Assert.assertEquals(CodecUtils.decodeLong(CodecUtils.encode(321321)), 321321);
		Assert.assertEquals(CodecUtils.decodeLong(CodecUtils.encode(3456782)), 3456782);
		Assert.assertEquals(CodecUtils.decodeLong(CodecUtils.encode(34526782)), 34526782);
	}
}

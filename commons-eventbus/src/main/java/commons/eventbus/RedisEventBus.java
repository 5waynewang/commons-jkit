/**
 * 
 */
package commons.eventbus;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import commons.serialization.hessian.Hessian2Serialization;
import redis.clients.jedis.BinaryJedisCluster;
import redis.clients.jedis.BinaryJedisPubSub;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 3:46:01 PM Aug 15, 2016
 */
public class RedisEventBus extends DefaultEventBus implements EventBus {
	private final AtomicInteger threadIndex = new AtomicInteger(0);

	private final BinaryJedisCluster jedisCluster;

	public RedisEventBus(BinaryJedisCluster jedisCluster) {
		super(true);
		this.jedisCluster = jedisCluster;
	}

	@Override
	protected ClosureExSet createClosureExSet(final Object event) {
		ClosureExSet set = super.createClosureExSet(event);

		new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里会阻塞
				// TODO 是否需要把 BinaryJedisPubSub 缓存？做unsubscribe操作
				jedisCluster.subscribe(new BinaryJedisPubSub() {
					@Override
					public void onMessage(byte[] channel, byte[] message) {
						Object evt = deserialize(channel);
						Object[] args = deserialize(message);
						// 这里一定要call父类的方法
						RedisEventBus.super.doFireEvent(evt, args);
					}
				}, serialize(event));
			}
		}, "RedisSubscriber" + threadIndex.incrementAndGet() + "-thread-").start();

		return set;
	}

	protected byte[] serialize(Object o) {
		try {
			return Hessian2Serialization.serialize(o);
		}
		catch (IOException e) {
			throw new RuntimeException("error to serialize " + o, e);
		}
	}

	protected <V> V deserialize(byte[] buf) {
		try {
			return Hessian2Serialization.deserialize(buf);
		}
		catch (IOException e) {
			throw new RuntimeException("error to deserialize " + Arrays.toString(buf), e);
		}
	}

	/**
	 * 分布式触发事件，通知所有机器的所有订阅
	 */
	@Override
	public void fireEvent(Object event, Object... args) {
		jedisCluster.publish(serialize(event), serialize(args));
	}

	/**
	 * 仅触发当前进程内的订阅事件
	 */
	public void fireEventOnlyCurrentProcess(Object event, Object... args) {
		super.fireEvent(event, args);
	}
}

/**
 * 
 */
package redis.clients.jedis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import redis.clients.util.JedisClusterCRC16;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 12:29:52 PM Jan 4, 2016
 */
public class DefaultSlotMatcher implements SlotMatcher {
	private static class Slot {
		final int bgn;
		final int end;

		Slot(int bgn, int end) {
			this.bgn = bgn;
			this.end = end;
		}
	}

	private static final String DEFAULT_SLOTS = "0-5460 5461-10922 10923-16383";
	private static final Slot DEFAULT_SLOT = new Slot(0, 16383);
	private final List<Slot> slots = new ArrayList<Slot>();

	public DefaultSlotMatcher() {
		this(DEFAULT_SLOTS);
	}

	public DefaultSlotMatcher(String slots) {
		slots = StringUtils.defaultIfBlank(slots, DEFAULT_SLOTS);
		for (String str : slots.split("[,\\s\\t]+")) {
			final String[] arr = str.split("-");

			this.slots.add(new Slot(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])));
		}
	}

	Slot getSlot(String key) {
		final int slot = JedisClusterCRC16.getSlot(key);
		for (Slot s : this.slots) {
			if (slot >= s.bgn && slot <= s.end) {
				return s;
			}
		}
		return DEFAULT_SLOT;
	}

	Slot getSlot(byte[] key) {
		final int slot = JedisClusterCRC16.getSlot(key);
		for (Slot s : this.slots) {
			if (slot >= s.bgn && slot <= s.end) {
				return s;
			}
		}
		return DEFAULT_SLOT;
	}

	@Override
	public boolean match(String... keys) {
		final int keyCount = keys.length;
		if (keyCount > 1) {
			final Slot slot = this.getSlot(keys[0]);
			for (int i = 1; i < keyCount; i++) {
				int nextSlot = JedisClusterCRC16.getSlot(keys[i]);
				if (nextSlot < slot.bgn || nextSlot > slot.end) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean match(byte[]... keys) {
		final int keyCount = keys.length;
		if (keyCount > 1) {
			final Slot slot = this.getSlot(keys[0]);
			for (int i = 1; i < keyCount; i++) {
				int nextSlot = JedisClusterCRC16.getSlot(keys[i]);
				if (nextSlot < slot.bgn || nextSlot > slot.end) {
					return false;
				}
			}
		}
		return true;
	}

}

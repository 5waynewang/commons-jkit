/**
 * 
 */
package commons.cache.facade.redis;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import redis.clients.util.RedisInputStream;

import commons.cache.config.RedisConfig;
import commons.cache.serialization.CacheSerializable;
import commons.serialization.hessian.Hessian2Serialization;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 3:07:34 PM Jul 9, 2015
 */
public abstract class Hessian2JedisFacade extends AbstractRedisFacade implements CacheSerializable {
	public Hessian2JedisFacade(RedisConfig redisConfig) {
		super(redisConfig);
	}

	<O> byte[] serialize(O o) throws IOException {
		return Hessian2Serialization.serialize(o);
	}

	<O> O deserialize(byte[] data) throws IOException {
		return Hessian2Serialization.deserialize(data);
	}

	@Override
	public <K> byte[] serializeKey(K key) throws IOException {
		if (key == null) {
			throw new IllegalArgumentException("key must not be null");
		}
		return serialize(key);
	}

	protected <K> byte[] serializeKeyQuietly(K key) throws IOException {
		if (key == null) {
			return null;
		}
		return serialize(key);
	}

	@Override
	public <K> K deserializeKey(byte[] rawKey) throws IOException, ClassNotFoundException {
		if (rawKey == null) {
			return null;
		}
		return deserialize(rawKey);
	}

	@Override
	public <V> byte[] serializeValue(V value) throws IOException {
		if (value == null) {
			return null;
		}
		return serialize(value);
	}

	@Override
	public <V> V deserializeValue(byte[] rawValue) throws IOException, ClassNotFoundException {
		if (rawValue == null) {
			return null;
		}
		return deserialize(rawValue);
	}

	protected <K> byte[][] serializeKeys(K... keys) throws IOException {
		final byte[][] rawKeys = new byte[keys.length][];
		int i = 0;
		for (K key : keys) {
			final byte[] rawKey = this.serializeKeyQuietly(key);

			if (rawKey == null) {
				throw new IllegalArgumentException("must not contains null key");
			}

			rawKeys[i++] = rawKey;
		}
		return rawKeys;
	}

	protected <V> byte[][] serializeValues(V... values) throws IOException {
		final byte[][] rawValues = new byte[values.length][];
		int i = 0;
		for (V value : values) {
			final byte[] rawValue = this.serializeValue(value);

			if (rawValue == null) {
				throw new IllegalArgumentException("must not contains null value");
			}

			rawValues[i++] = rawValue;
		}
		return rawValues;
	}

	protected <K, V> Map<K, V> toGetMap(K[] keys, List<byte[]> rawValues) throws ClassNotFoundException, IOException {
		if (rawValues == null || rawValues.isEmpty()) {
			return new HashMap<K, V>();
		}

		final Map<K, V> results = new HashMap<K, V>();
		for (int i = 0; i < keys.length; i++) {
			results.put(keys[i], (V) this.deserializeValue(rawValues.get(i)));
		}
		return results;
	}

	protected <K> Map<K, Long> toIncrMap(final K[] keys, final List<byte[]> rawValues) {
		if (rawValues == null || rawValues.isEmpty()) {
			return new HashMap<K, Long>();
		}

		RedisInputStream ris = null;

		final Map<K, Long> results = new HashMap<K, Long>();
		for (int i = 0; i < keys.length; i++) {
			try {
				final byte[] rawValue = rawValues.get(i);
				if (rawValue == null || rawValue.length == 0) {
					continue;
				}
				final int length = rawValue.length;
				final int size = 2 + length;
				final byte[] buf = new byte[size];
				System.arraycopy(rawValue, 0, buf, 0, length);
				buf[length] = '\r';
				buf[length + 1] = '\n';

				ris = new RedisInputStream(new ByteArrayInputStream(buf));
				results.put(keys[i], ris.readLongCrLf());
			}
			finally {
				IOUtils.closeQuietly(ris);
			}
		}
		return results;
	}
}

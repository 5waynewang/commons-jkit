/**
 * 
 */
package commons.cache.facade.redis;

import java.io.IOException;

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

}

/**
 * 
 */
package commons.cache.serialization;

import java.io.IOException;

import commons.serialization.hessian.Hessian2Serialization;
import redis.clients.jedis.Protocol;
import redis.clients.util.SafeEncoder;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 1:39:55 PM Sep 11, 2016
 */
public class Hessian2Serializer implements CacheSerializable {
	<O> byte[] serialize(O o) throws IOException {
		return Hessian2Serialization.serialize(o);
	}

	<O> O deserialize(byte[] data) throws IOException {
		return Hessian2Serialization.deserialize(data);
	}

	@Override
	public byte[] serializeKey(String key) throws IOException {
		if (key == null) {
			throw new IllegalArgumentException("key must not be null");
		}
		return SafeEncoder.encode((String) key);
	}

	@Override
	public String deserializeKey(byte[] rawKey) throws IOException {
		if (rawKey == null) {
			return null;
		}
		return new String(rawKey, Protocol.CHARSET);
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

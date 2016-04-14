/**
 * 
 */
package commons.cache.serialization;

import java.io.IOException;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:42:04 AM Dec 24, 2015
 */
public interface CacheSerializable {
	byte[] serializeKey(String key) throws IOException;

	String deserializeKey(byte[] rawKey) throws IOException;

	<V> byte[] serializeValue(V value) throws IOException;

	<V> V deserializeValue(byte[] rawValue) throws IOException, ClassNotFoundException;

}

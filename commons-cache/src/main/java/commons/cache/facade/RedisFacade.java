/**
 * 
 */
package commons.cache.facade;

import java.util.List;
import java.util.Set;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 5:13:53 PM Nov 14, 2015
 */
public interface RedisFacade extends CacheFacade {
	<K, V> V getSet(K key, V value);

	<K, V> V getSetQuietly(K key, V value);

	<K, V> List<V> lrange(K key, long start, long end);

	<K, V> List<V> lrangeQuietly(K key, long start, long end);

	<K> Long llen(K key);

	<K> Long llenQuietly(K key);

	<K, V> Long rpush(K key, V... value);

	<K, V> Long rpushQuietly(K key, V... value);

	<K, V> Long lpush(K key, V... value);

	<K, V> Long lpushQuietly(K key, V... value);

	<K, V> V lpop(K key);

	<K, V> V lpopQuietly(K key);

	<K, V> V rpop(K key);

	<K, V> V rpopQuietly(K key);

	/**
	 * timeout=0 阻塞等待
	 * 
	 * @param timeout
	 * @param keys
	 * @return
	 */
	<K, V> V brpop(int timeout, K... keys);

	<K, V> V blpop(int timeout, K... keys);

	<K, V> Boolean sadd(K key, V... value);

	<K, V> Boolean saddQuietly(K key, V... value);

	<K, V> Boolean srem(K key, V... value);

	<K, V> Boolean sremQuietly(K key, V... value);

	<K, V> Set<V> smembers(K key);

	<K, V> Set<V> smembersQuietly(K key);

	<K> Long scard(K key);

	<K> Long scardQuietly(K key);
}
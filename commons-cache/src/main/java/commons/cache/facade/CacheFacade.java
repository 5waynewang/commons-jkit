/**
 * 
 */
package commons.cache.facade;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import commons.cache.operation.CasOperation;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 5:29:29 PM Jun 3, 2015
 */
public interface CacheFacade {

	<K, V> V get(K key);

	<K, V> V getQuietly(K key);

	<K, V> Map<K, V> mget(K... keys);

	<K, V> Map<K, V> mgetQuietly(K... keys);

	<K, V> void set(K key, V value);

	<K, V> void setQuietly(K key, V value);

	<K, V> void set(K key, V value, long timeout);

	<K, V> void setQuietly(K key, V value, long timeout);

	<K, V> void set(K key, V value, long timeout, TimeUnit unit);

	<K, V> void setQuietly(K key, V value, long timeout, TimeUnit unit);

	<K> void delete(K key);

	<K> void deleteQuietly(K key);

	<K> void delete(Collection<K> keys);

	<K> void deleteQuietly(Collection<K> keys);

	<K> Boolean expire(K key, long timeout, TimeUnit unit);

	<K> Boolean expireQuietly(K key, long timeout, TimeUnit unit);

	<K> Long incr(K key, long delta);

	<K> Long incrQuietly(K key, long delta);

	<K> Long incr(K key, long delta, long timeout, TimeUnit unit);

	<K> Long incrQuietly(K key, long delta, long timeout, TimeUnit unit);

	<K, V> Boolean cas(K key, CasOperation<V> casOperation);

	<K, V> Boolean cas(K key, CasOperation<V> casOperation, long timeout, TimeUnit unit);
}

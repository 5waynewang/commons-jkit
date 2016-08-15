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

	<V> V get(String key);

	<V> V getQuietly(String key);

	<V> Map<String, V> mget(String... keys);

	<V> Map<String, V> mgetQuietly(String... keys);

	<V> void set(String key, V value);

	<V> void setQuietly(String key, V value);

	<V> void set(String key, V value, long timeout);

	<V> void setQuietly(String key, V value, long timeout);

	<V> void set(String key, V value, long timeout, TimeUnit unit);

	<V> void setQuietly(String key, V value, long timeout, TimeUnit unit);

	/**
	 * 返回受影响的行数
	 */
	Integer delete(String key);

	Integer deleteQuietly(String key);
	
//	void delete(String[] key);
//
//	void deleteQuietly(String[] key);

	Integer delete(Collection<String> keys);

	Integer deleteQuietly(Collection<String> keys);

	Boolean expire(String key, long timeout, TimeUnit unit);

	Boolean expireQuietly(String key, long timeout, TimeUnit unit);
	
	/**
	 * <pre>
	 * 如果生存时间设置成功，返回 true 。
	 * 当 key 不存在或没办法设置生存时间，返回 false 。
	 * </pre>
	 * 
	 * @param key
	 * @param unixTimeMillis
	 * @return
	 */
	Boolean expireAt(String key, long unixTimeMillis);
	
	Boolean expireAtQuietly(String key, long unixTimeMillis);

	Long incr(String key, long delta);

	Long incrQuietly(String key, long delta);

	Long incr(String key, long delta, long timeout, TimeUnit unit);

	Long incrQuietly(String key, long delta, long timeout, TimeUnit unit);
	
	Long decr(String key, long delta);

	Long decrQuietly(String key, long delta);

	Long decr(String key, long delta, long timeout, TimeUnit unit);

	Long decrQuietly(String key, long delta, long timeout, TimeUnit unit);

	<V> Boolean cas(String key, CasOperation<V> casOperation);

	<V> Boolean cas(String key, CasOperation<V> casOperation, long timeout, TimeUnit unit);
	
	Boolean exists(String key);
	
	Boolean existsQuietly(String key);
	
	void destroy();
}

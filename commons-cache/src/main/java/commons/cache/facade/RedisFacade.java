/**
 * 
 */
package commons.cache.facade;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 5:13:53 PM Nov 14, 2015
 */
public interface RedisFacade extends CacheFacade {

	/**
	 * {@link #setnx(String, Object, long, TimeUnit)}
	 */
	<V> Boolean setnx(String key, V value);

	/**
	 * <pre>
	 * SETNX 是『SET if Not eXists』(如果不存在，则 SET)的简写。
	 * 设置成功，返回 1 。
	 * 设置失败，返回 0 。
	 * </pre>
	 * 
	 * @param key
	 * @param value
	 * @param timeout
	 * @param unit
	 * @return
	 */
	<V> Boolean setnx(String key, V value, long timeout, TimeUnit unit);

	/**
	 * {@link #setnx(String, Object, long, TimeUnit)}
	 */
	<V> Boolean setnxQuietly(String key, V value);

	/**
	 * {@link #setnx(String, Object, long, TimeUnit)}
	 */
	<V> Boolean setnxQuietly(String key, V value, long timeout, TimeUnit unit);

	<V> V getSet(String key, V value);

	<V> V getSetQuietly(String key, V value);

	<V> List<V> lrange(String key, long start, long end);

	<V> List<V> lrangeQuietly(String key, long start, long end);

	/**
	 * <pre>
	 * LINDEX key index
	 * 返回列表 key 中，下标为 index 的元素。
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 * 如果 key 不是列表类型，返回一个错误。
	 * 时间复杂度：
	 * O(N)， N 为到达下标 index 过程中经过的元素数量。
	 * 因此，对列表的头元素和尾元素执行 LINDEX 命令，复杂度为O(1)。
	 * 返回值:
	 * 列表中下标为 index 的元素。
	 * 如果 index 参数的值不在列表的区间范围内(out of range)，返回 nil 。
	 * </pre>
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	<V> V lindex(String key, int index);

	/**
	 * {@link #lindex(String, int)}
	 */
	<V> V lindexQuietly(String key, int index);

	Long llen(String key);

	Long llenQuietly(String key);

	<V> Long rpush(String key, V... value);

	<V> Long rpushQuietly(String key, V... value);

	<V> Long lpush(String key, V... value);

	<V> Long lpushQuietly(String key, V... value);

	<V> V lpop(String key);

	<V> V lpopQuietly(String key);

	<V> V rpop(String key);

	<V> V rpopQuietly(String key);

	/**
	 * timeout=0 阻塞等待
	 * 
	 * @param timeout
	 * @param keys
	 * @return
	 */
	<V> V brpop(int timeout, String... keys);

	<V> V blpop(int timeout, String... keys);

	<V> Boolean sadd(String key, V... value);

	<V> Boolean saddQuietly(String key, V... value);

	<V> Boolean srem(String key, V... value);

	<V> Boolean sremQuietly(String key, V... value);

	<V> Set<V> smembers(String key);

	<V> Set<V> smembersQuietly(String key);

	Long scard(String key);

	Long scardQuietly(String key);

	Map<String, Long> mincr(String... keys);

	Map<String, Long> mincrQuietly(String... keys);

	<V> void hset(String key, String field, V value);

	<V> void hsetQuietly(String key, String field, V value);

	<V> void hmset(String key, Map<String, V> value);

	<V> void hmsetQuietly(String key, Map<String, V> value);

	<V> V hget(String key, String field);

	<V> V hgetQuietly(String key, String field);

	<V> Map<String, V> hgetAll(String key);

	<V> Map<String, V> hgetAllQuietly(String key);

	<V> Long hincr(String key, String field, long value);

	<V> Long hincrQuietly(String key, String field, long value);

	<V> Map<String, V> hmget(String key, String... fields);

	<V> Map<String, V> hmgetQuietly(String key, String... fields);

	Map<String, Long> hmincr(String key, String... fields);

	Map<String, Long> hmincrQuietly(String key, String... fields);

	void hdel(String key, String... fields);

	void hdelQuietly(String key, String... fields);

	Boolean hexists(String key, String field);

	Boolean hexistsQuietly(String key, String field);

	<V> Set<V> hkeys(String key);

	<V> Set<V> hkeysQuietly(String key);

	<V> List<V> hvals(String key);

	<V> List<V> hvalsQuietly(String key);

	Long hlen(String key);

	Long hlenQuietly(String key);

	Long zadd(String key, Map<String, Double> scoreMembers);

	Long zaddQuietly(String key, Map<String, Double> scoreMembers);

	Long zremrangeByRank(String key, long start, long end);

	Long zremrangeByRankQuietly(String key, long start, long end);

	Set<String> zrange(String key, long start, long end);

	Set<String> zrangeQuietly(String key, long start, long end);

	Boolean setNoIncr(String key, long delta);

	Boolean setNoIncrQuietly(String key, long delta);

	Boolean setNoIncr(String key, long delta, long timeout, TimeUnit unit);

	Boolean setNoIncrQuietly(String key, long delta, long timeout, TimeUnit unit);

	Long getNoIncr(String key);

	Long getNoIncrQuietly(String key);
}

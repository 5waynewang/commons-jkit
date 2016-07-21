/**
 * 
 */
package commons.cache.facade.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import commons.cache.facade.RedisFacade;
import commons.cache.operation.CasOperation;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 8:19:09 PM Apr 21, 2016
 */
public class RedisFacadeWrapper implements RedisFacade {

	private volatile RedisFacade wrapper;

	public RedisFacadeWrapper(RedisFacade wrapper) {
		setWrapper(wrapper);
	}

	public void setWrapper(RedisFacade wrapper) {
		this.wrapper = wrapper;
	}

	public RedisFacade getWrapper() {
		return wrapper;
	}

	@Override
	public <V> V get(String key) {
		return wrapper.get(key);
	}

	@Override
	public <V> V getQuietly(String key) {
		return wrapper.getQuietly(key);
	}

	@Override
	public <V> Map<String, V> mget(String... keys) {
		return wrapper.mget(keys);
	}

	@Override
	public <V> Map<String, V> mgetQuietly(String... keys) {
		return wrapper.mgetQuietly(keys);
	}

	@Override
	public <V> void set(String key, V value) {
		wrapper.set(key, value);
	}

	@Override
	public <V> void setQuietly(String key, V value) {
		wrapper.setQuietly(key, value);
	}

	@Override
	public <V> void set(String key, V value, long timeout) {
		wrapper.set(key, value, timeout);
	}

	@Override
	public <V> void setQuietly(String key, V value, long timeout) {
		wrapper.setQuietly(key, value, timeout);
	}

	@Override
	public <V> void set(String key, V value, long timeout, TimeUnit unit) {
		wrapper.set(key, value, timeout, unit);
	}

	@Override
	public <V> void setQuietly(String key, V value, long timeout, TimeUnit unit) {
		wrapper.setQuietly(key, value, timeout, unit);
	}

	@Override
	public void delete(String key) {
		wrapper.delete(key);
	}

	@Override
	public void deleteQuietly(String key) {
		wrapper.deleteQuietly(key);
	}

	@Override
	public void delete(Collection<String> keys) {
		wrapper.delete(keys);
	}

	@Override
	public void deleteQuietly(Collection<String> keys) {
		wrapper.deleteQuietly(keys);
	}

	@Override
	public Boolean expire(String key, long timeout, TimeUnit unit) {
		return wrapper.expire(key, timeout, unit);
	}

	@Override
	public Boolean expireQuietly(String key, long timeout, TimeUnit unit) {
		return wrapper.expire(key, timeout, unit);
	}

	@Override
	public Boolean expireAt(String key, long unixTimeMillis) {
		return wrapper.expireAt(key, unixTimeMillis);
	}

	@Override
	public Boolean expireAtQuietly(String key, long unixTimeMillis) {
		return wrapper.expireAtQuietly(key, unixTimeMillis);
	}

	@Override
	public Long incr(String key, long delta) {
		return wrapper.incr(key, delta);
	}

	@Override
	public Long incrQuietly(String key, long delta) {
		return wrapper.incrQuietly(key, delta);
	}

	@Override
	public Long incr(String key, long delta, long timeout, TimeUnit unit) {
		return wrapper.incr(key, delta, timeout, unit);
	}

	@Override
	public Long incrQuietly(String key, long delta, long timeout, TimeUnit unit) {
		return wrapper.incrQuietly(key, delta, timeout, unit);
	}

	@Override
	public Long decr(String key, long delta) {
		return wrapper.decr(key, delta);
	}

	@Override
	public Long decrQuietly(String key, long delta) {
		return wrapper.decrQuietly(key, delta);
	}

	@Override
	public Long decr(String key, long delta, long timeout, TimeUnit unit) {
		return wrapper.decr(key, delta, timeout, unit);
	}

	@Override
	public Long decrQuietly(String key, long delta, long timeout, TimeUnit unit) {
		return wrapper.decrQuietly(key, delta, timeout, unit);
	}

	@Override
	public <V> Boolean cas(String key, CasOperation<V> casOperation) {
		return wrapper.cas(key, casOperation);
	}

	@Override
	public <V> Boolean cas(String key, CasOperation<V> casOperation, long timeout, TimeUnit unit) {
		return wrapper.cas(key, casOperation, timeout, unit);
	}

	@Override
	public Boolean exists(String key) {
		return wrapper.exists(key);
	}

	@Override
	public Boolean existsQuietly(String key) {
		return wrapper.existsQuietly(key);
	}

	@Override
	public <V> Boolean setnx(String key, V value) {
		return wrapper.setnx(key, value);
	}

	@Override
	public <V> Boolean setnx(String key, V value, long timeout, TimeUnit unit) {
		return wrapper.setnx(key, value, timeout, unit);
	}

	@Override
	public <V> Boolean setnxQuietly(String key, V value) {
		return wrapper.setnxQuietly(key, value);
	}

	@Override
	public <V> Boolean setnxQuietly(String key, V value, long timeout, TimeUnit unit) {
		return wrapper.setnxQuietly(key, value, timeout, unit);
	}

	@Override
	public <V> V getSet(String key, V value) {
		return wrapper.getSet(key, value);
	}

	@Override
	public <V> V getSetQuietly(String key, V value) {
		return wrapper.getSetQuietly(key, value);
	}

	@Override
	public <V> List<V> lrange(String key, long start, long end) {
		return wrapper.lrange(key, start, end);
	}

	@Override
	public <V> List<V> lrangeQuietly(String key, long start, long end) {
		return wrapper.lrangeQuietly(key, start, end);
	}

	@Override
	public <V> V lindex(String key, int index) {
		return wrapper.lindex(key, index);
	}

	@Override
	public <V> V lindexQuietly(String key, int index) {
		return wrapper.lindexQuietly(key, index);
	}

	@Override
	public Long llen(String key) {
		return wrapper.llen(key);
	}

	@Override
	public Long llenQuietly(String key) {
		return wrapper.llenQuietly(key);
	}

	@Override
	public <V> Long rpush(String key, V... value) {
		return wrapper.rpush(key, value);
	}

	@Override
	public <V> Long rpushQuietly(String key, V... value) {
		return wrapper.rpushQuietly(key, value);
	}

	@Override
	public <V> Long lpush(String key, V... value) {
		return wrapper.lpush(key, value);
	}

	@Override
	public <V> Long lpushQuietly(String key, V... value) {
		return wrapper.lpushQuietly(key, value);
	}

	@Override
	public <V> V lpop(String key) {
		return wrapper.lpop(key);
	}

	@Override
	public <V> V lpopQuietly(String key) {
		return wrapper.lpopQuietly(key);
	}

	@Override
	public <V> V rpop(String key) {
		return wrapper.rpop(key);
	}

	@Override
	public <V> V rpopQuietly(String key) {
		return wrapper.rpopQuietly(key);
	}

	@Override
	public <V> V brpop(int timeout, String... keys) {
		return wrapper.brpop(timeout, keys);
	}

	@Override
	public <V> V blpop(int timeout, String... keys) {
		return wrapper.blpop(timeout, keys);
	}

	@Override
	public <V> Boolean sadd(String key, V... value) {
		return wrapper.sadd(key, value);
	}

	@Override
	public <V> Boolean saddQuietly(String key, V... value) {
		return wrapper.saddQuietly(key, value);
	}

	@Override
	public <V> Boolean srem(String key, V... value) {
		return wrapper.srem(key, value);
	}

	@Override
	public <V> Boolean sremQuietly(String key, V... value) {
		return wrapper.sremQuietly(key, value);
	}

	@Override
	public <V> Set<V> smembers(String key) {
		return wrapper.smembers(key);
	}

	@Override
	public <V> Set<V> smembersQuietly(String key) {
		return wrapper.smembersQuietly(key);
	}

	@Override
	public Long scard(String key) {
		return wrapper.scard(key);
	}

	@Override
	public Long scardQuietly(String key) {
		return wrapper.scardQuietly(key);
	}

	@Override
	public Map<String, Long> mincr(String... keys) {
		return wrapper.mincr(keys);
	}

	@Override
	public Map<String, Long> mincrQuietly(String... keys) {
		return wrapper.mincrQuietly(keys);
	}

	@Override
	public <V> void hset(String key, String field, V value) {
		wrapper.hset(key, field, value);
	}

	@Override
	public <V> void hsetQuietly(String key, String field, V value) {
		wrapper.hsetQuietly(key, field, value);
	}

	@Override
	public <V> void hmset(String key, Map<String, V> value) {
		wrapper.hmset(key, value);
	}

	@Override
	public <V> void hmsetQuietly(String key, Map<String, V> value) {
		wrapper.hmsetQuietly(key, value);
	}

	@Override
	public <V> V hget(String key, String field) {
		return wrapper.hget(key, field);
	}

	@Override
	public <V> V hgetQuietly(String key, String field) {
		return wrapper.hgetQuietly(key, field);
	}

	@Override
	public <V> Map<String, V> hgetAll(String key) {
		return wrapper.hgetAll(key);
	}

	@Override
	public <V> Map<String, V> hgetAllQuietly(String key) {
		return wrapper.hgetAllQuietly(key);
	}

	@Override
	public <V> Long hincr(String key, String field, long value) {
		return wrapper.hincr(key, field, value);
	}

	@Override
	public <V> Long hincrQuietly(String key, String field, long value) {
		return wrapper.hincrQuietly(key, field, value);
	}

	@Override
	public <V> Map<String, V> hmget(String key, String... fields) {
		return wrapper.hmget(key, fields);
	}

	@Override
	public <V> Map<String, V> hmgetQuietly(String key, String... fields) {
		return wrapper.hmgetQuietly(key, fields);
	}

	@Override
	public Map<String, Long> hmincr(String key, String... fields) {
		return wrapper.hmincr(key, fields);
	}

	@Override
	public Map<String, Long> hmincrQuietly(String key, String... fields) {
		return wrapper.hmincrQuietly(key, fields);
	}

	@Override
	public void hdel(String key, String... fields) {
		wrapper.hdel(key, fields);
	}

	@Override
	public void hdelQuietly(String key, String... fields) {
		wrapper.hdelQuietly(key, fields);
	}

	@Override
	public Boolean hexists(String key, String field) {
		return wrapper.hexists(key, field);
	}

	@Override
	public Boolean hexistsQuietly(String key, String field) {
		return wrapper.hexistsQuietly(key, field);
	}

	@Override
	public <V> Set<V> hkeys(String key) {
		return wrapper.hkeys(key);
	}

	@Override
	public <V> Set<V> hkeysQuietly(String key) {
		return wrapper.hkeysQuietly(key);
	}

	@Override
	public <V> List<V> hvals(String key) {
		return wrapper.hvals(key);
	}

	@Override
	public <V> List<V> hvalsQuietly(String key) {
		return wrapper.hvalsQuietly(key);
	}

	@Override
	public Long hlen(String key) {
		return wrapper.hlen(key);
	}

	@Override
	public Long hlenQuietly(String key) {
		return wrapper.hlenQuietly(key);
	}

	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers) {
		return wrapper.zadd(key, scoreMembers);
	}

	@Override
	public Long zaddQuietly(String key, Map<String, Double> scoreMembers) {
		return wrapper.zaddQuietly(key, scoreMembers);
	}

	@Override
	public Long zremrangeByRank(String key, long start, long end) {
		return wrapper.zremrangeByRank(key, start, end);
	}

	@Override
	public Long zremrangeByRankQuietly(String key, long start, long end) {
		return wrapper.zremrangeByRankQuietly(key, start, end);
	}

	@Override
	public Set<String> zrange(String key, long start, long end) {
		return wrapper.zrange(key, start, end);
	}

	@Override
	public Set<String> zrangeQuietly(String key, long start, long end) {
		return wrapper.zrangeQuietly(key, start, end);
	}

	@Override
	public Boolean setNoIncr(String key, long delta) {
		return wrapper.setNoIncr(key, delta);
	}

	@Override
	public Boolean setNoIncrQuietly(String key, long delta) {
		return wrapper.setNoIncrQuietly(key, delta);
	}

	@Override
	public Boolean setNoIncr(String key, long delta, long timeout, TimeUnit unit) {
		return wrapper.setNoIncr(key, delta, timeout, unit);
	}

	@Override
	public Boolean setNoIncrQuietly(String key, long delta, long timeout, TimeUnit unit) {
		return wrapper.setNoIncrQuietly(key, delta, timeout, unit);
	}

	@Override
	public Long getNoIncr(String key) {
		return wrapper.getNoIncr(key);
	}

	@Override
	public Long getNoIncrQuietly(String key) {
		return wrapper.getNoIncrQuietly(key);
	}

	@Override
	public void destroy() {
		wrapper.destroy();
	}
}

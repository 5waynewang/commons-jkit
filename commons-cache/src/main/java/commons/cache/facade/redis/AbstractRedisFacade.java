/**
 * 
 */
package commons.cache.facade.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import commons.cache.config.RedisConfig;
import commons.cache.facade.RedisFacade;
import commons.cache.operation.CasOperation;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 3:09:17 PM Jul 9, 2015
 */
public abstract class AbstractRedisFacade implements RedisFacade {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected final RedisConfig redisConfig;

	public AbstractRedisFacade(RedisConfig redisConfig) {
		this.redisConfig = redisConfig;
	}

	public void destroy() {
	}

	@Override
	public <V> V getQuietly(String key) {
		try {
			return this.get(key);
		}
		catch (Exception e) {
			logger.warn("redis:get", e);
			return null;
		}
	}

	@Override
	public <V> Map<String, V> mgetQuietly(String... keys) {
		try {
			return this.mget(keys);
		}
		catch (Exception e) {
			logger.warn("redis:mget", e);
			return null;
		}
	}

	@Override
	public <V> V getSetQuietly(String key, V value) {
		try {
			return this.getSet(key, value);
		}
		catch (Exception e) {
			logger.warn("redis:getSet", e);
			return null;
		}
	}

	@Override
	public <V> void setQuietly(String key, V value) {
		try {
			this.set(key, value);
		}
		catch (Exception e) {
			logger.warn("redis:set", e);
		}
	}

	@Override
	public <V> void set(String key, V value, long timeout) {
		this.set(key, value, timeout, TimeUnit.MILLISECONDS);
	}

	@Override
	public <V> void setQuietly(String key, V value, long timeout) {
		this.setQuietly(key, value, timeout, TimeUnit.MILLISECONDS);
	}

	@Override
	public <V> void setQuietly(String key, V value, long timeout, TimeUnit unit) {
		try {
			this.set(key, value, timeout, unit);
		}
		catch (Exception e) {
			logger.warn("redis:set", e);
		}
	}

	@Override
	public <V> Boolean setnxQuietly(String key, V value) {
		try {
			return this.setnx(key, value);
		}
		catch (Exception e) {
			logger.warn("redis:setnx", e);
			return null;
		}
	}

	@Override
	public <V> Boolean setnxQuietly(String key, V value, long timeout, TimeUnit unit) {
		try {
			return this.setnx(key, value, timeout, unit);
		}
		catch (Exception e) {
			logger.warn("redis:setnx", e);
			return null;
		}
	}

	@Override
	public <V> Boolean cas(final String key, final CasOperation<V> casOperation) {
		final int timeout = this.redisConfig.getProtocolTimeoutMillis();

		return cas(key, casOperation, timeout, TimeUnit.MILLISECONDS);
	}

	@Override
	public void deleteQuietly(String key) {
		try {
			this.delete(key);
		}
		catch (Exception e) {
			logger.warn("redis:del", e);
		}
	}

	@Override
	public void deleteQuietly(Collection<String> keys) {
		try {
			this.delete(keys);
		}
		catch (Exception e) {
			logger.warn("redis:del", e);
		}
	}

	@Override
	public Boolean expireQuietly(String key, long timeout, TimeUnit unit) {
		try {
			return this.expire(key, timeout, unit);
		}
		catch (Exception e) {
			logger.warn("redis:expire", e);
			return null;
		}
	}

	@Override
	public Long incrQuietly(String key, long delta) {
		try {
			return this.incr(key, delta);
		}
		catch (Exception e) {
			logger.warn("redis:incr", e);
			return null;
		}
	}

	@Override
	public Long incrQuietly(String key, long delta, long timeout, TimeUnit unit) {
		try {
			return this.incr(key, delta, timeout, unit);
		}
		catch (Exception e) {
			logger.warn("redis:incr", e);
			return null;
		}
	}

	@Override
	public Long decrQuietly(String key, long delta) {
		try {
			return this.decr(key, delta);
		}
		catch (Exception e) {
			logger.warn("redis:decr", e);
			return null;
		}
	}

	@Override
	public Long decrQuietly(String key, long delta, long timeout, TimeUnit unit) {
		try {
			return this.decr(key, delta, timeout, unit);
		}
		catch (Exception e) {
			logger.warn("redis:decr", e);
			return null;
		}
	}

	@Override
	public <V> List<V> lrangeQuietly(String key, long start, long end) {
		try {
			return this.lrange(key, start, end);
		}
		catch (Exception e) {
			logger.warn("redis:lrange", e);
			return null;
		}
	}

	@Override
	public <V> V lindexQuietly(String key, int index) {
		try {
			return this.lindex(key, index);
		}
		catch (Exception e) {
			logger.warn("redis:lindex", e);
			return null;
		}
	}

	@Override
	public Long llenQuietly(String key) {
		try {
			return this.llen(key);
		}
		catch (Exception e) {
			logger.warn("redis:llen", e);
			return null;
		}
	}

	@Override
	public <V> Long rpushQuietly(String key, V... values) {
		try {
			return this.rpush(key, values);
		}
		catch (Exception e) {
			logger.warn("redis:rpush", e);
			return null;
		}
	}

	@Override
	public <V> Long lpushQuietly(String key, V... values) {
		try {
			return this.lpush(key, values);
		}
		catch (Exception e) {
			logger.warn("redis:lpush", e);
			return null;
		}
	}

	@Override
	public <V> V lpopQuietly(String key) {
		try {
			return this.lpop(key);
		}
		catch (Exception e) {
			logger.warn("redis:lpop", e);
			return null;
		}
	}

	@Override
	public <V> V rpopQuietly(String key) {
		try {
			return this.rpop(key);
		}
		catch (Exception e) {
			logger.warn("redis:rpop", e);
			return null;
		}
	}

	@Override
	public <V> Boolean saddQuietly(String key, V... values) {
		try {
			return this.sadd(key, values);
		}
		catch (Exception e) {
			logger.warn("redis:sadd", e);
			return null;
		}
	}

	@Override
	public <V> Boolean sremQuietly(String key, V... values) {
		try {
			return this.srem(key, values);
		}
		catch (Exception e) {
			logger.warn("redis:srem", e);
			return null;
		}
	}

	@Override
	public <V> Set<V> smembersQuietly(String key) {
		try {
			return this.smembers(key);
		}
		catch (Exception e) {
			logger.warn("redis:smembers", e);
			return null;
		}
	}

	@Override
	public Long scardQuietly(String key) {
		try {
			return this.scard(key);
		}
		catch (Exception e) {
			logger.warn("redis:scard", e);
			return null;
		}
	}

	@Override
	public Map<String, Long> mincrQuietly(String... keys) {
		try {
			return this.mincr(keys);
		}
		catch (Exception e) {
			logger.warn("redis:mincr", e);
			return null;
		}
	}

	@Override
	public <V> void hsetQuietly(String key, String field, V value) {
		try {
			this.hset(key, field, value);
		}
		catch (Exception e) {
			logger.warn("redis:hset", e);
		}
	}

	@Override
	public <V> V hgetQuietly(String key, String field) {
		try {
			return this.hget(key, field);
		}
		catch (Exception e) {
			logger.warn("redis:hget", e);
			return null;
		}
	}

	@Override
	public <V> Map<String, V> hgetAllQuietly(String key) {
		try {
			return this.hgetAll(key);
		}
		catch (Exception e) {
			logger.warn("redis:hgetAll", e);
			return null;
		}
	}

	@Override
	public <V> Long hincrQuietly(String key, String field, long value) {
		try {
			return this.hincr(key, field, value);
		}
		catch (Exception e) {
			logger.warn("redis:hincr", e);
			return null;
		}
	}

	@Override
	public <V> void hmsetQuietly(String key, Map<String, V> value) {
		try {
			this.hmset(key, value);
		}
		catch (Exception e) {
			logger.warn("redis:hmset", e);
		}
	}

	@Override
	public <V> Map<String, V> hmgetQuietly(String key, String... fields) {
		try {
			return this.hmget(key, fields);
		}
		catch (Exception e) {
			logger.warn("redis:hmget", e);
			return null;
		}
	}

	@Override
	public Map<String, Long> hmincrQuietly(String key, String... fields) {
		try {
			return this.hmincr(key, fields);
		}
		catch (Exception e) {
			logger.warn("redis:hmincr", e);
			return null;
		}
	}

	@Override
	public void hdelQuietly(String key, String... fields) {
		try {
			this.hdel(key, fields);
		}
		catch (Exception e) {
			logger.warn("redis:hdel", e);
		}
	}

	@Override
	public Boolean hexistsQuietly(String key, String field) {
		try {
			return this.hexists(key, field);
		}
		catch (Exception e) {
			logger.warn("redis:hexists", e);
			return null;
		}
	}

	@Override
	public <V> Set<V> hkeysQuietly(String key) {
		try {
			return this.hkeys(key);
		}
		catch (Exception e) {
			logger.warn("redis:hkeys", e);
			return null;
		}
	}

	@Override
	public <V> List<V> hvalsQuietly(String key) {
		try {
			return this.hvals(key);
		}
		catch (Exception e) {
			logger.warn("redis:hvals", e);
			return null;
		}
	}

	@Override
	public Long hlenQuietly(String key) {
		try {
			return this.hlen(key);
		}
		catch (Exception e) {
			logger.warn("redis:hlen", e);
			return null;
		}
	}

	@Override
	public Long zaddQuietly(String key, Map<String, Double> scoreMembers) {
		try {
			return this.zadd(key, scoreMembers);
		}
		catch (Exception e) {
			logger.warn("redis:zadd", e);
			return null;
		}
	}

	@Override
	public Long zremrangeByRankQuietly(String key, long start, long end) {
		try {
			return this.zremrangeByRank(key, start, end);
		}
		catch (Exception e) {
			logger.warn("redis:zremrangeByRank", e);
			return null;
		}
	}

	@Override
	public Set<String> zrangeQuietly(String key, long start, long end) {
		try {
			return this.zrange(key, start, end);
		}
		catch (Exception e) {
			logger.warn("redis:zrange", e);
			return null;
		}
	}

	@Override
	public Boolean existsQuietly(String key) {
		try {
			return this.exists(key);
		}
		catch (Exception e) {
			logger.warn("redis:exists", e);
			return null;
		}
	}

	@Override
	public Boolean expireAtQuietly(String key, long unixTimeMillis) {
		try {
			return this.expireAt(key, unixTimeMillis);
		}
		catch (Exception e) {
			logger.warn("redis:expireAt", e);
			return null;
		}
	}

	@Override
	public Boolean setNoIncrQuietly(String key, long delta) {
		try {
			return this.setNoIncr(key, delta);
		}
		catch (Exception e) {
			logger.warn("redis:set", e);
			return null;
		}
	}

	@Override
	public Boolean setNoIncrQuietly(String key, long delta, long timeout, TimeUnit unit) {
		try {
			return this.setNoIncr(key, delta, timeout, unit);
		}
		catch (Exception e) {
			logger.warn("redis:set", e);
			return null;
		}
	}

	@Override
	public Long getNoIncrQuietly(String key) {
		try {
			return this.getNoIncr(key);
		}
		catch (Exception e) {
			logger.warn("redis:get", e);
			return null;
		}
	}
}

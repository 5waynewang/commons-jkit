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
	public <K, V> V getQuietly(K key) {
		try {
			return this.get(key);
		}
		catch (Exception e) {
			logger.warn("redis:get", e);
			return null;
		}
	}

	@Override
	public <K, V> Map<K, V> mgetQuietly(K... keys) {
		try {
			return this.mget(keys);
		}
		catch (Exception e) {
			logger.warn("redis:mget", e);
			return null;
		}
	}

	@Override
	public <K, V> V getSetQuietly(K key, V value) {
		try {
			return this.getSet(key, value);
		}
		catch (Exception e) {
			logger.warn("redis:getSet", e);
			return null;
		}
	}

	@Override
	public <K, V> void setQuietly(K key, V value) {
		try {
			this.set(key, value);
		}
		catch (Exception e) {
			logger.warn("redis:set", e);
		}
	}

	@Override
	public <K, V> void set(K key, V value, long timeout) {
		this.set(key, value, timeout, TimeUnit.MILLISECONDS);
	}

	@Override
	public <K, V> void setQuietly(K key, V value, long timeout) {
		this.setQuietly(key, value, timeout, TimeUnit.MILLISECONDS);
	}

	@Override
	public <K, V> void setQuietly(K key, V value, long timeout, TimeUnit unit) {
		try {
			this.set(key, value, timeout, unit);
		}
		catch (Exception e) {
			logger.warn("redis:set", e);
		}
	}

	@Override
	public <K, V> Boolean cas(final K key, final CasOperation<V> casOperation) {
		final int timeout = this.redisConfig.getProtocolTimeoutMillis();

		return cas(key, casOperation, timeout, TimeUnit.MILLISECONDS);
	}

	@Override
	public <K> void deleteQuietly(K key) {
		try {
			this.delete(key);
		}
		catch (Exception e) {
			logger.warn("redis:del", e);
		}
	}

	@Override
	public <K> void deleteQuietly(Collection<K> keys) {
		try {
			this.delete(keys);
		}
		catch (Exception e) {
			logger.warn("redis:del", e);
		}
	}

	@Override
	public <K> Boolean expireQuietly(K key, long timeout, TimeUnit unit) {
		try {
			return this.expire(key, timeout, unit);
		}
		catch (Exception e) {
			logger.warn("redis:expire", e);
			return null;
		}
	}

	@Override
	public <K> Long incrQuietly(K key, long delta) {
		try {
			return this.incr(key, delta);
		}
		catch (Exception e) {
			logger.warn("redis:incr", e);
			return null;
		}
	}

	@Override
	public <K> Long incrQuietly(K key, long delta, long timeout, TimeUnit unit) {
		try {
			return this.incr(key, delta, timeout, unit);
		}
		catch (Exception e) {
			logger.warn("redis:incr", e);
			return null;
		}
	}

	@Override
	public <K, V> List<V> lrangeQuietly(K key, long start, long end) {
		try {
			return this.lrange(key, start, end);
		}
		catch (Exception e) {
			logger.warn("redis:lrange", e);
			return null;
		}
	}

	@Override
	public <K> Long llenQuietly(K key) {
		try {
			return this.llen(key);
		}
		catch (Exception e) {
			logger.warn("redis:llen", e);
			return null;
		}
	}

	@Override
	public <K, V> Long rpushQuietly(K key, V... values) {
		try {
			return this.rpush(key, values);
		}
		catch (Exception e) {
			logger.warn("redis:rpush", e);
			return null;
		}
	}

	@Override
	public <K, V> Long lpushQuietly(K key, V... values) {
		try {
			return this.lpush(key, values);
		}
		catch (Exception e) {
			logger.warn("redis:lpush", e);
			return null;
		}
	}

	@Override
	public <K, V> V lpopQuietly(K key) {
		try {
			return this.lpop(key);
		}
		catch (Exception e) {
			logger.warn("redis:lpop", e);
			return null;
		}
	}

	@Override
	public <K, V> V rpopQuietly(K key) {
		try {
			return this.rpop(key);
		}
		catch (Exception e) {
			logger.warn("redis:rpop", e);
			return null;
		}
	}

	@Override
	public <K, V> Boolean saddQuietly(K key, V... values) {
		try {
			return this.sadd(key, values);
		}
		catch (Exception e) {
			logger.warn("redis:sadd", e);
			return null;
		}
	}

	@Override
	public <K, V> Boolean sremQuietly(K key, V... values) {
		try {
			return this.srem(key, values);
		}
		catch (Exception e) {
			logger.warn("redis:srem", e);
			return null;
		}
	}

	@Override
	public <K, V> Set<V> smembersQuietly(K key) {
		try {
			return this.smembers(key);
		}
		catch (Exception e) {
			logger.warn("redis:smembers", e);
			return null;
		}
	}

	@Override
	public <K> Long scardQuietly(K key) {
		try {
			return this.scard(key);
		}
		catch (Exception e) {
			logger.warn("redis:scard", e);
			return null;
		}
	}

	@Override
	public <K> Map<K, Long> mincrQuietly(K... keys) {
		try {
			return this.mincr(keys);
		}
		catch (Exception e) {
			logger.warn("redis:mincr", e);
			return null;
		}
	}

	@Override
	public <K, F, V> void hsetQuietly(K key, F field, V value) {
		try {
			this.hset(key, field, value);
		}
		catch (Exception e) {
			logger.warn("redis:hset", e);
		}
	}

	@Override
	public <K, F, V> V hgetQuietly(K key, F field) {
		try {
			return this.hget(key, field);
		}
		catch (Exception e) {
			logger.warn("redis:hget", e);
			return null;
		}
	}

	@Override
	public <K, F, V> Long hincrQuietly(K key, F field, long value) {
		try {
			return this.hincr(key, field, value);
		}
		catch (Exception e) {
			logger.warn("redis:hincr", e);
			return null;
		}
	}

	@Override
	public <K, F, V> Map<F, V> hmgetQuietly(K key, F... fields) {
		try {
			return this.hmget(key, fields);
		}
		catch (Exception e) {
			logger.warn("redis:hmget", e);
			return null;
		}
	}

	@Override
	public <K, F> Map<F, Long> hmincrQuietly(K key, F... fields) {
		try {
			return this.hmincr(key, fields);
		}
		catch (Exception e) {
			logger.warn("redis:hmincr", e);
			return null;
		}
	}

	@Override
	public <K, F> void hdelQuietly(K key, F... fields) {
		try {
			this.hdel(key, fields);
		}
		catch (Exception e) {
			logger.warn("redis:hdel", e);
		}
	}

	@Override
	public <K, F> Boolean hexistsQuietly(K key, F field) {
		try {
			return this.hexists(key, field);
		}
		catch (Exception e) {
			logger.warn("redis:hexists", e);
			return null;
		}
	}
}

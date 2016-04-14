/**
 * 
 */
package commons.cache.facade.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.util.SafeEncoder;
import commons.cache.config.RedisConfig;
import commons.cache.exception.CacheException;
import commons.cache.exception.CancelCasException;
import commons.cache.operation.CasOperation;
import commons.lang.ObjectUtils;
import commons.lang.concurrent.NamedThreadFactory;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 3:07:34 PM Jul 9, 2015
 */
public class JedisFacade extends Hessian2JedisFacade {
	public JedisFacade(RedisConfig redisConfig) {
		super(redisConfig);
		this.init();
	}

	private JedisPoolConfig poolConfig;
	private JedisPool jedisPool;
	private ThreadPoolExecutor redisCasExecutor;

	@Override
	public <V> V get(String key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			return this.deserializeValue(resource.get(rawKey));
		}
		catch (Exception e) {
			throw new CacheException("redis:get", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> Map<String, V> mget(String... keys) {
		final Jedis resource = this.getResource();
		try {
			final byte[][] rawKeys = this.serializeKeys(keys);

			final List<byte[]> rawValues = resource.mget(rawKeys);

			return this.toGetMap(keys, rawValues);
		}
		catch (Exception e) {
			throw new CacheException("redis:mget", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> V getSet(String key, V value) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.serializeValue(value);

			return this.deserializeValue(resource.getSet(rawKey, rawValue));
		}
		catch (Exception e) {
			throw new CacheException("redis:getSet", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> void set(String key, V value) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.serializeValue(value);

			if (rawValue == null) {
				resource.del(rawKey);
			}
			else {
				resource.set(rawKey, rawValue);
			}
		}
		catch (Exception e) {
			throw new CacheException("redis:set", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> Boolean setnx(String key, V value) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.serializeValue(value);

			if (rawValue != null) {
				return resource.setnx(rawKey, rawValue) == 1;
			}

			return Boolean.FALSE;
		}
		catch (Exception e) {
			throw new CacheException("redis:setnx", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> Boolean setnx(String key, V value, long timeout, TimeUnit unit) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.serializeValue(value);

			if (rawValue != null) {
				final boolean result = (resource.setnx(rawKey, rawValue) == 1);
				if (result) {
					resource.expire(rawKey, (int) unit.toSeconds(timeout));
				}
				return result;
			}

			return Boolean.FALSE;
		}
		catch (Exception e) {
			throw new CacheException("redis:setnx", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> void set(String key, V value, long timeout, TimeUnit unit) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.serializeValue(value);

			if (rawValue == null) {
				resource.del(rawKey);
			}
			else {
				resource.setex(rawKey, (int) unit.toSeconds(timeout), rawValue);
			}
		}
		catch (Exception e) {
			throw new CacheException("redis:setex", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	protected <K, V> Boolean cas0(final String key, final CasOperation<V> casOperation) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			resource.watch(rawKey);

			final V value;
			try {
				final byte[] oldRawValue = resource.get(rawKey);
				final V oldValue = this.deserializeValue(oldRawValue);

				value = casOperation.getNewValue(oldValue);
			}
			catch (CancelCasException e) {
				return Boolean.TRUE;
			}

			final byte[] rawValue = this.serializeValue(value);

			final Transaction t = resource.multi();

			if (rawValue == null) {
				t.del(rawKey);
			}
			else {
				t.set(rawKey, rawValue);
			}

			return t.exec() != null;
		}
		catch (Exception e) {
			throw new CacheException("redis:cas", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> Boolean cas(final String key, final CasOperation<V> casOperation, final long timeout, final TimeUnit unit) {
		final AtomicBoolean abortStatus = new AtomicBoolean(false);
		final FutureTask<Boolean> task = new FutureTask<Boolean>(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				int tries = 0;
				while (!abortStatus.get() && (tries++ < casOperation.casMaxTries())) {
					final Boolean result = cas0(key, casOperation);
					if (result != null && result) {
						return result;
					}
				}
				return Boolean.FALSE;
			}
		});

		try {
			getRedisCasExecutor().submit(task);
			return task.get(timeout, unit);
		}
		catch (CacheException ce) {
			throw ce;
		}
		catch (TimeoutException te) {
			abortStatus.set(true);
			task.cancel(true);
			return null;
		}
		catch (Exception e) {
			abortStatus.set(true);
			task.cancel(true);
			throw new CacheException("redis:cas", e);
		}
	}

	@Override
	public void delete(String key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			resource.del(rawKey);
		}
		catch (Exception e) {
			throw new CacheException("redis:del", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public void delete(Collection<String> keys) {
		if (keys == null || keys.isEmpty()) {
			return;
		}
		final Jedis resource = this.getResource();
		try {
			final byte[][] rawKeys = this.serializeKeys(keys.toArray(new String[] {}));

			resource.del(rawKeys);
		}
		catch (Exception e) {
			throw new CacheException("redis:del", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Boolean expire(String key, long timeout, TimeUnit unit) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Long result = resource.expire(rawKey, (int) unit.toSeconds(timeout));

			return result != null && result > 0;
		}
		catch (Exception e) {
			throw new CacheException("redis:expire", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> Boolean sadd(String key, V... values) {
		if (ObjectUtils.hasNull(values)) {
			throw new IllegalArgumentException("values must not contain null");
		}

		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawValues = this.serializeValues(values);

			final Long result = resource.sadd(rawKey, rawValues);

			return result != null && result > 0;
		}
		catch (Exception e) {
			throw new CacheException("redis:sadd", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> Boolean srem(String key, V... values) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawValues = this.serializeValues(values);

			final Long result = resource.srem(rawKey, rawValues);

			return result != null && result > 0;
		}
		catch (Exception e) {
			throw new CacheException("redis:srem", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> Set<V> smembers(String key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Set<byte[]> results = resource.smembers(rawKey);
			if (results == null || results.isEmpty()) {
				return new HashSet<V>();
			}
			final Set<V> values = new HashSet<V>(results.size());
			for (byte[] result : results) {
				V v = (V) deserializeValue(result);
				if (v != null) {
					values.add(v);
				}
			}

			return values;
		}
		catch (Exception e) {
			throw new CacheException("redis:smembers", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Long scard(String key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			return resource.scard(rawKey);
		}
		catch (Exception e) {
			throw new CacheException("redis:scard", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Long incr(String key, long delta) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			return resource.incrBy(rawKey, delta);
		}
		catch (Exception e) {
			throw new CacheException("redis:incr", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Long incr(String key, long delta, long timeout, TimeUnit unit) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Long result = resource.incrBy(rawKey, delta);

			if (result != null && result == delta) {
				resource.expire(rawKey, (int) unit.toSeconds(timeout));
			}
			return result;
		}
		catch (Exception e) {
			throw new CacheException("redis:incr", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Long decr(String key, long delta) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			return resource.decrBy(rawKey, delta);
		}
		catch (Exception e) {
			throw new CacheException("redis:decr", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Long decr(String key, long delta, long timeout, TimeUnit unit) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Long result = resource.decrBy(rawKey, delta);

			if (result != null && result == delta) {
				resource.expire(rawKey, (int) unit.toSeconds(timeout));
			}
			return result;
		}
		catch (Exception e) {
			throw new CacheException("redis:decr", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> List<V> lrange(String key, long start, long end) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final List<byte[]> results = resource.lrange(rawKey, start, end);
			if (results == null || results.isEmpty()) {
				return new ArrayList<V>(0);
			}
			final List<V> values = new ArrayList<V>(results.size());
			for (byte[] result : results) {
				V v = (V) deserializeValue(result);
				if (v != null) {
					values.add(v);
				}
			}

			return values;
		}
		catch (Exception e) {
			throw new CacheException("redis:lrange", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Long llen(String key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			return resource.llen(rawKey);
		}
		catch (Exception e) {
			throw new CacheException("redis:llen", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> Long rpush(String key, V... values) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawValues = this.serializeValues(values);

			return resource.rpush(rawKey, rawValues);
		}
		catch (Exception e) {
			throw new CacheException("redis:rpush", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> Long lpush(String key, V... values) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawValues = this.serializeValues(values);

			return resource.lpush(rawKey, rawValues);
		}
		catch (Exception e) {
			throw new CacheException("redis:lpush", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> V lpop(String key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = resource.lpop(rawKey);

			return (V) ((rawValue == null) ? null : this.deserializeValue(rawValue));
		}
		catch (Exception e) {
			throw new CacheException("redis:lpop", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> V rpop(String key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = resource.rpop(rawKey);

			return (V) ((rawValue == null) ? null : this.deserializeValue(rawValue));
		}
		catch (Exception e) {
			throw new CacheException("redis:rpop", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> V brpop(int timeout, String... keys) {
		final Jedis resource = this.getResource();
		try {
			final byte[][] rawKeys = this.serializeKeys(keys);

			final List<byte[]> rawValues = resource.brpop(timeout, rawKeys);
			if (rawValues == null || rawValues.isEmpty()) {
				return null;
			}

			return (V) this.deserializeValue(rawValues.get(1));
		}
		catch (Exception e) {
			throw new CacheException("redis:bpop", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> V blpop(int timeout, String... keys) {
		final Jedis resource = this.getResource();
		try {
			final byte[][] rawKeys = this.serializeKeys(keys);

			final List<byte[]> rawValues = resource.blpop(timeout, rawKeys);
			if (rawValues == null || rawValues.isEmpty()) {
				return null;
			}

			return (V) this.deserializeValue(rawValues.get(1));
		}
		catch (Exception e) {
			throw new CacheException("redis:bpop", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	Jedis getResource() {
		return this.getJedisPool().getResource();
	}

	void returnResource(Jedis resource) {
		resource.close();
	}

	@Override
	public void destroy() {
		if (this.jedisPool != null) {
			this.jedisPool.destroy();
		}
		if (this.redisCasExecutor != null) {
			this.redisCasExecutor.shutdown();
		}
	}

	void init() {
		this.setJedisPoolConfig();
		this.setJedisPool();

		final int corePoolSize = Runtime.getRuntime().availableProcessors();
		this.redisCasExecutor = new ThreadPoolExecutor(//
				corePoolSize, //
				corePoolSize, //
				1000 * 60, //
				TimeUnit.MILLISECONDS, //
				new LinkedBlockingQueue<Runnable>(1000), //
				new NamedThreadFactory("RedisCasThread_"));
	}

	void setJedisPoolConfig() {
		final JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(this.redisConfig.getMaxTotal());
		poolConfig.setMaxIdle(this.redisConfig.getMaxIdle());
		poolConfig.setMaxWaitMillis(this.redisConfig.getMaxWaitMillis());
		poolConfig.setMinEvictableIdleTimeMillis(this.redisConfig.getMinEvictableIdleTimeMillis());
		poolConfig.setNumTestsPerEvictionRun(this.redisConfig.getNumTestsPerEvictionRun());
		poolConfig.setTimeBetweenEvictionRunsMillis(this.redisConfig.getTimeBetweenEvictionRunsMillis());
		this.poolConfig = poolConfig;
	}

	void setJedisPool() {
		final String host = this.redisConfig.getHost();
		final int port = this.redisConfig.getPort();
		final int timeout = this.redisConfig.getProtocolTimeoutMillis();
		final String password = this.redisConfig.getPassword();
		final int database = this.redisConfig.getDatabase();

		this.jedisPool = new JedisPool(this.poolConfig, host, port, timeout, password, database);

		if (logger.isInfoEnabled()) {
			logger.info("connect to Redis {}:{}, use DB:{}", host, port, database);
		}
	}

	JedisPool getJedisPool() {
		return jedisPool;
	}

	ThreadPoolExecutor getRedisCasExecutor() {
		return redisCasExecutor;
	}

	@Override
	public Map<String, Long> mincr(String... keys) {
		final Jedis resource = this.getResource();
		try {
			final byte[][] rawKeys = this.serializeKeys(keys);

			final List<byte[]> rawValues = resource.mget(rawKeys);

			return this.toIncrMap(keys, rawValues);
		}
		catch (Exception e) {
			throw new CacheException("redis:mincr", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> void hset(String key, String field, V value) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawField = this.serializeKey(field);

			final byte[] rawValue = this.serializeValue(value);

			resource.hset(rawKey, rawField, rawValue);
		}
		catch (Exception e) {
			throw new CacheException("redis:hset", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> V hget(String key, String field) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawField = this.serializeKey(field);

			final byte[] rawValue = resource.hget(rawKey, rawField);

			return this.deserializeValue(rawValue);
		}
		catch (Exception e) {
			throw new CacheException("redis:hget", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> Map<String, V> hgetAll(String key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Map<byte[], byte[]> rawValues = resource.hgetAll(rawKey);
			if (rawValues == null || rawValues.isEmpty()) {
				return new HashMap<String, V>();
			}

			final Map<String, V> results = new HashMap<String, V>();
			for (Map.Entry<byte[], byte[]> entry : rawValues.entrySet()) {
				final String field = this.deserializeKey(entry.getKey());
				final V value = this.deserializeValue(entry.getValue());

				results.put(field, value);
			}

			return results;
		}
		catch (Exception e) {
			throw new CacheException("redis:hgetAll", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> Long hincr(String key, String field, long value) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawField = this.serializeKey(field);

			return resource.hincrBy(rawKey, rawField, value);
		}
		catch (Exception e) {
			throw new CacheException("redis:hincr", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> void hmset(String key, Map<String, V> value) {
		if (value == null || value.isEmpty()) {
			return;
		}
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>();
			for (Map.Entry<String, V> entry : value.entrySet()) {
				final byte[] rawField = this.serializeKey(entry.getKey());
				if (rawField == null) {
					throw new IllegalArgumentException("must not contains null field");
				}

				final byte[] rawValue = this.serializeValue(entry.getValue());
				if (rawValue == null) {
					throw new IllegalArgumentException("must not contains null value");
				}

				hash.put(rawField, rawValue);
			}

			resource.hmset(rawKey, hash);
		}
		catch (Exception e) {
			throw new CacheException("redis:hmset", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> Map<String, V> hmget(String key, String... fields) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawFields = this.serializeKeys(fields);

			final List<byte[]> rawValues = resource.hmget(rawKey, rawFields);

			return this.toGetMap(fields, rawValues);
		}
		catch (Exception e) {
			throw new CacheException("redis:hmget", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Map<String, Long> hmincr(String key, String... fields) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawFields = this.serializeKeys(fields);

			final List<byte[]> rawValues = resource.hmget(rawKey, rawFields);

			return this.toIncrMap(fields, rawValues);
		}
		catch (Exception e) {
			throw new CacheException("redis:hmincr", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public void hdel(String key, String... fields) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawFields = this.serializeKeys(fields);

			resource.hdel(rawKey, rawFields);
		}
		catch (Exception e) {
			throw new CacheException("redis:hdel", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Boolean hexists(String key, String field) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawField = this.serializeKey(field);

			return resource.hexists(rawKey, rawField);
		}
		catch (Exception e) {
			throw new CacheException("redis:hexists", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> Set<V> hkeys(String key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Set<byte[]> rawValues = resource.hkeys(rawKey);
			final Set<V> results = new HashSet<V>();
			for (byte[] rawValue : rawValues) {
				results.add((V) this.deserializeValue(rawValue));
			}
			return results;
		}
		catch (Exception e) {
			throw new CacheException("redis:hkeys", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <V> List<V> hvals(String key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final List<byte[]> rawValues = resource.hvals(rawKey);
			final List<V> results = new ArrayList<V>();
			for (byte[] rawValue : rawValues) {
				results.add((V) this.deserializeValue(rawValue));
			}
			return results;
		}
		catch (Exception e) {
			throw new CacheException("redis:hvals", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Long hlen(String key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			return resource.hlen(rawKey);
		}
		catch (Exception e) {
			throw new CacheException("redis:hlen", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers) {
		if (scoreMembers == null || scoreMembers.isEmpty()) {
			return 0L;
		}
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Map<byte[], Double> rawScoreMembers = new HashMap<byte[], Double>();
			for (Map.Entry<String, Double> entry : scoreMembers.entrySet()) {
				final byte[] rawField = this.serializeKey(entry.getKey());

				rawScoreMembers.put(rawField, entry.getValue());
			}

			return resource.zadd(rawKey, rawScoreMembers, ZAddParams.zAddParams());
		}
		catch (Exception e) {
			throw new CacheException("redis:zadd", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Long zremrangeByRank(String key, long start, long end) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			return resource.zremrangeByRank(rawKey, start, end);
		}
		catch (Exception e) {
			throw new CacheException("redis:zremrangeByRank", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Set<String> zrange(String key, long start, long end) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Set<byte[]> rawFields = resource.zrange(rawKey, start, end);

			final Set<String> fields = new LinkedHashSet<String>();

			for (byte[] rawField : rawFields) {
				final String field = this.deserializeKey(rawField);

				fields.add(field);
			}

			return fields;
		}
		catch (Exception e) {
			throw new CacheException("redis:zremrangeByRank", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Boolean exists(String key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			return resource.exists(rawKey);
		}
		catch (Exception e) {
			throw new CacheException("redis:exists", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Boolean expireAt(String key, long unixTimeMillis) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			/**
			 * 如果生存时间设置成功，返回 1 。 当 key 不存在或没办法设置生存时间，返回 0 。
			 */
			final Long result = resource.expireAt(rawKey, unixTimeMillis);

			return result != null && result > 0;
		}
		catch (Exception e) {
			throw new CacheException("redis:expireAt", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Boolean setNoIncr(String key, long delta) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final String result = resource.set(rawKey, SafeEncoder.encode(String.valueOf(delta)));

			return StringUtils.equalsIgnoreCase(result, "ok");
		}
		catch (Exception e) {
			throw new CacheException("redis:set", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Boolean setNoIncr(String key, long delta, long timeout, TimeUnit unit) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final String result = resource.setex(rawKey, (int) unit.toSeconds(timeout),
					SafeEncoder.encode(String.valueOf(delta)));

			return StringUtils.equalsIgnoreCase(result, "ok");
		}
		catch (Exception e) {
			throw new CacheException("redis:set", e);
		}
		finally {
			this.returnResource(resource);
		}
	}

	@Override
	public Long getNoIncr(String key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = resource.get(rawKey);
			if (ArrayUtils.isEmpty(rawValue)) {
				return null;
			}

			return Long.valueOf(new String(rawValue, Protocol.CHARSET));
		}
		catch (Exception e) {
			throw new CacheException("redis:get", e);
		}
		finally {
			this.returnResource(resource);
		}
	}
}

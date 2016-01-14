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

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.sortedset.ZAddParams;

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
	public <K, V> V get(K key) {
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
	public <K, V> Map<K, V> mget(K... keys) {
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
	public <K, V> V getSet(K key, V value) {
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
	public <K, V> void set(K key, V value) {
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
	public <K, V> Boolean setnx(K key, V value) {
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
	public <K, V> void set(K key, V value, long timeout, TimeUnit unit) {
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

	protected <K, V> Boolean cas0(final K key, final CasOperation<V> casOperation) {
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
	public <K, V> Boolean cas(final K key, final CasOperation<V> casOperation, final long timeout, final TimeUnit unit) {
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
	public <K> void delete(K key) {
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
	public <K> void delete(Collection<K> keys) {
		if (keys == null || keys.isEmpty()) {
			return;
		}
		final Jedis resource = this.getResource();
		try {
			final byte[][] rawKeys = this.serializeKeys(keys.toArray());

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
	public <K> Boolean expire(K key, long timeout, TimeUnit unit) {
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
	public <K, V> Boolean sadd(K key, V... values) {
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
	public <K, V> Boolean srem(K key, V... values) {
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
	public <K, V> Set<V> smembers(K key) {
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
	public <K> Long scard(K key) {
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
	public <K> Long incr(K key, long delta) {
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
	public <K> Long incr(K key, long delta, long timeout, TimeUnit unit) {
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
	public <K> Long decr(K key, long delta) {
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
	public <K> Long decr(K key, long delta, long timeout, TimeUnit unit) {
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
	public <K, V> List<V> lrange(K key, long start, long end) {
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
	public <K> Long llen(K key) {
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
	public <K, V> Long rpush(K key, V... values) {
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
	public <K, V> Long lpush(K key, V... values) {
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
	public <K, V> V lpop(K key) {
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
	public <K, V> V rpop(K key) {
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
	public <K, V> V brpop(int timeout, K... keys) {
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
	public <K, V> V blpop(int timeout, K... keys) {
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
	public <K> Map<K, Long> mincr(K... keys) {
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
	public <K, F, V> void hset(K key, F field, V value) {
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
	public <K, F, V> V hget(K key, F field) {
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
	public <K, F, V> Map<F, V> hgetAll(K key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Map<byte[], byte[]> rawValues = resource.hgetAll(rawKey);
			if (rawValues == null || rawValues.isEmpty()) {
				return new HashMap<F, V>();
			}

			final Map<F, V> results = new HashMap<F, V>();
			for (Map.Entry<byte[], byte[]> entry : rawValues.entrySet()) {
				final F field = this.deserializeKey(entry.getKey());
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
	public <K, F, V> Long hincr(K key, F field, long value) {
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
	public <K, F, V> void hmset(K key, Map<F, V> value) {
		if (value == null || value.isEmpty()) {
			return;
		}
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>();
			for (Map.Entry<F, V> entry : value.entrySet()) {
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
	public <K, F, V> Map<F, V> hmget(K key, F... fields) {
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
	public <K, F> Map<F, Long> hmincr(K key, F... fields) {
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
	public <K, F> void hdel(K key, F... fields) {
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
	public <K, F> Boolean hexists(K key, F field) {
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
	public <K, V> Set<V> hkeys(K key) {
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
	public <K, V> List<V> hvals(K key) {
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
	public <K, F> Long hlen(K key) {
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
	public <K, F> Long zadd(K key, Map<F, Double> scoreMembers) {
		if (scoreMembers == null || scoreMembers.isEmpty()) {
			return 0L;
		}
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Map<byte[], Double> rawScoreMembers = new HashMap<byte[], Double>();
			for (Map.Entry<F, Double> entry : scoreMembers.entrySet()) {
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
	public <K, F> Long zremrangeByRank(K key, long start, long end) {
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
	public <K, F> Set<F> zrange(K key, long start, long end) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Set<byte[]> rawFields = resource.zrange(rawKey, start, end);

			final Set<F> fields = new LinkedHashSet<F>();

			for (byte[] rawField : rawFields) {
				final F field = this.deserializeKey(rawField);

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
}

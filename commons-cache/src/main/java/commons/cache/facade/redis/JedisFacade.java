/**
 * 
 */
package commons.cache.facade.redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import commons.cache.config.RedisConfig;
import commons.cache.exception.CacheException;
import commons.cache.exception.CancelCasException;
import commons.cache.operation.CasOperation;
import commons.lang.ObjectUtils;
import commons.lang.concurrent.NamedThreadFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 3:07:34 PM Jul 9, 2015
 */
public abstract class JedisFacade extends AbstractRedisFacade {
	public JedisFacade(RedisConfig redisConfig) {
		super(redisConfig);
		this.init();
	}

	private JedisPoolConfig poolConfig;
	private JedisPool jedisPool;
	private ThreadPoolExecutor redisCasExecutor;

	abstract public <K> byte[] serializeKey(K key) throws IOException;

	abstract public <K> K deserializeKey(byte[] rawKey) throws IOException, ClassNotFoundException;

	abstract public <V> byte[] serializeValue(V value) throws IOException;

	abstract public <V> V deserializeValue(byte[] rawValue) throws IOException, ClassNotFoundException;

	public <K> byte[][] serializeKeys(K... keys) throws IOException {
		final byte[][] rawKeys = new byte[keys.length][];
		int i = 0;
		for (K key : keys) {
			rawKeys[i++] = this.serializeKey(key);
		}
		return rawKeys;
	}

	public <V> byte[][] serializeValues(V... values) throws IOException {
		final byte[][] rawValues = new byte[values.length][];
		int i = 0;
		for (V value : values) {
			rawValues[i++] = this.serializeValue(value);
		}
		return rawValues;
	}

	protected void registerClass(Class<?> clazz) {
	}

	@Override
	public <K, V> V get(K key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			return this.deserializeValue(resource.get(rawKey));
		} catch (Exception e) {
			throw new CacheException("redis:get", e);
		} finally {
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
		} catch (Exception e) {
			throw new CacheException("redis:getSet", e);
		} finally {
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
			} else {
				resource.set(rawKey, rawValue);
			}
		} catch (Exception e) {
			throw new CacheException("redis:set", e);
		} finally {
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
			} else {
				resource.setex(rawKey, (int) unit.toSeconds(timeout), rawValue);
			}
		} catch (Exception e) {
			throw new CacheException("redis:setex", e);
		} finally {
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
			} catch (CancelCasException e) {
				return Boolean.TRUE;
			}

			final byte[] rawValue = this.serializeValue(value);

			final Transaction t = resource.multi();

			if (rawValue == null) {
				t.del(rawKey);
			} else {
				t.set(rawKey, rawValue);
			}

			return t.exec() != null;
		} catch (Exception e) {
			throw new CacheException("redis:cas", e);
		} finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <K, V> Boolean cas(final K key, final CasOperation<V> casOperation, final long timeout,
			final TimeUnit unit) {
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
		} catch (CacheException ce) {
			throw ce;
		} catch (TimeoutException te) {
			abortStatus.set(true);
			task.cancel(true);
			return null;
		} catch (Exception e) {
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
		} catch (Exception e) {
			throw new CacheException("redis:del", e);
		} finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <K> void delete(Collection<K> keys) {
		final Jedis resource = this.getResource();
		try {
			final byte[][] rawKey = new byte[keys.size()][];
			int i = 0;
			for (K key : keys) {
				rawKey[i++] = this.serializeKey(key);
			}

			resource.del(rawKey);
		} catch (Exception e) {
			throw new CacheException("redis:del", e);
		} finally {
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
		} catch (Exception e) {
			throw new CacheException("redis:expire", e);
		} finally {
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

			final byte[] rawValue = this.serializeValue(values);

			final Long result = resource.sadd(rawKey, rawValue);

			return result != null && result > 0;
		} catch (Exception e) {
			throw new CacheException("redis:sadd", e);
		} finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <K, V> Boolean srem(K key, V... values) {
		if (ObjectUtils.hasNull(values)) {
			throw new IllegalArgumentException("values must not contain null");
		}

		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.serializeValue(values);

			final Long result = resource.srem(rawKey, rawValue);

			return result != null && result > 0;
		} catch (Exception e) {
			throw new CacheException("redis:srem", e);
		} finally {
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
		} catch (Exception e) {
			throw new CacheException("redis:smembers", e);
		} finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <K> Long scard(K key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			return resource.scard(rawKey);
		} catch (Exception e) {
			throw new CacheException("redis:scard", e);
		} finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <K> Long incr(K key, long delta) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			return resource.incrBy(rawKey, delta);
		} catch (Exception e) {
			throw new CacheException("redis:incr", e);
		} finally {
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
		} catch (Exception e) {
			throw new CacheException("redis:incr", e);
		} finally {
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
				return new ArrayList<V>();
			}
			final List<V> values = new ArrayList<V>(results.size());
			for (byte[] result : results) {
				V v = (V) deserializeValue(result);
				if (v != null) {
					values.add(v);
				}
			}

			return values;
		} catch (Exception e) {
			throw new CacheException("redis:lrange", e);
		} finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <K> Long llen(K key) {
		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			return resource.llen(rawKey);
		} catch (Exception e) {
			throw new CacheException("redis:llen", e);
		} finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <K, V> Long rpush(K key, V... values) {
		if (ObjectUtils.hasNull(values)) {
			throw new IllegalArgumentException("values must not contain null");
		}

		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawValue = this.serializeValues(values);

			return resource.rpush(rawKey, rawValue);
		} catch (Exception e) {
			throw new CacheException("redis:rpush", e);
		} finally {
			this.returnResource(resource);
		}
	}

	@Override
	public <K, V> Long lpush(K key, V... values) {
		if (ObjectUtils.hasNull(values)) {
			throw new IllegalArgumentException("values must not contain null");
		}

		final Jedis resource = this.getResource();
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.serializeValue(values);

			return resource.lpush(rawKey, rawValue);
		} catch (Exception e) {
			throw new CacheException("redis:lpush", e);
		} finally {
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
		} catch (Exception e) {
			throw new CacheException("redis:lpop", e);
		} finally {
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
		} catch (Exception e) {
			throw new CacheException("redis:rpop", e);
		} finally {
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
		} catch (Exception e) {
			throw new CacheException("redis:bpop", e);
		} finally {
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
		} catch (Exception e) {
			throw new CacheException("redis:bpop", e);
		} finally {
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
}

/**
 * 
 */
package commons.cache.facade.redis;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.JedisClusterCRC16;
import redis.clients.util.RedisInputStream;
import commons.cache.config.RedisConfig;
import commons.cache.exception.CacheException;
import commons.cache.operation.CasOperation;
import commons.lang.ObjectUtils;
import commons.lang.quickbean.Entry;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 3:07:34 PM Jul 9, 2015
 */
public class JedisClusterFacade extends Hessian2JedisFacade {
	public JedisClusterFacade(RedisConfig redisConfig) {
		super(redisConfig);
		this.init();
	}

	private JedisPoolConfig poolConfig;
	private JedisCluster jedisCluster;

	protected <K> byte[][] serializeKeys(K... keys) throws IOException {
		final byte[][] rawKeys = new byte[keys.length][];
		int i = 0;
		for (K key : keys) {
			rawKeys[i++] = this.serializeKey(key);
		}
		return rawKeys;
	}

	protected <V> byte[][] serializeValues(V... values) throws IOException {
		final byte[][] rawValues = new byte[values.length][];
		int i = 0;
		for (V value : values) {
			rawValues[i++] = this.serializeValue(value);
		}
		return rawValues;
	}

	@Override
	public <K, V> V get(K key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			return this.deserializeValue(this.jedisCluster.get(rawKey));
		}
		catch (Exception e) {
			throw new CacheException("redis:get", e);
		}
	}

	@Override
	public <K, V> V getSet(K key, V value) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.serializeValue(value);

			return this.deserializeValue(this.jedisCluster.getSet(rawKey, rawValue));
		}
		catch (Exception e) {
			throw new CacheException("redis:getSet", e);
		}
	}

	@Override
	public <K, V> void set(K key, V value) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.serializeValue(value);

			if (rawValue == null) {
				this.jedisCluster.del(rawKey);
			}
			else {
				this.jedisCluster.set(rawKey, rawValue);
			}
		}
		catch (Exception e) {
			throw new CacheException("redis:set", e);
		}
	}

	@Override
	public <K, V> void set(K key, V value, long timeout, TimeUnit unit) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.serializeValue(value);

			if (rawValue == null) {
				this.jedisCluster.del(rawKey);
			}
			else {
				this.jedisCluster.setex(rawKey, (int) unit.toSeconds(timeout), rawValue);
			}
		}
		catch (Exception e) {
			throw new CacheException("redis:setex", e);
		}
	}

	@Override
	public <K, V> Boolean cas(final K key, final CasOperation<V> casOperation, final long timeout, final TimeUnit unit) {
		throw new UnsupportedOperationException("Redis Cluster can not support the operation");
	}

	@Override
	public <K> void delete(K key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			this.jedisCluster.del(rawKey);
		}
		catch (Exception e) {
			throw new CacheException("redis:del", e);
		}
	}

	@Override
	public <K> void delete(Collection<K> keys) {
		try {
			final byte[][] rawKey = new byte[keys.size()][];
			int i = 0;
			for (K key : keys) {
				rawKey[i++] = this.serializeKey(key);
			}

			this.jedisCluster.del(rawKey);
		}
		catch (Exception e) {
			throw new CacheException("redis:del", e);
		}
	}

	@Override
	public <K> Boolean expire(K key, long timeout, TimeUnit unit) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Long result = this.jedisCluster.expire(rawKey, (int) unit.toSeconds(timeout));

			return result != null && result > 0;
		}
		catch (Exception e) {
			throw new CacheException("redis:expire", e);
		}
	}

	@Override
	public <K, V> Boolean sadd(K key, V... values) {
		if (ObjectUtils.hasNull(values)) {
			throw new IllegalArgumentException("values must not contain null");
		}

		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawValue = this.serializeValues(values);

			final Long result = this.jedisCluster.sadd(rawKey, rawValue);

			return result != null && result > 0;
		}
		catch (Exception e) {
			throw new CacheException("redis:sadd", e);
		}
	}

	@Override
	public <K, V> Boolean srem(K key, V... values) {
		if (ObjectUtils.hasNull(values)) {
			throw new IllegalArgumentException("values must not contain null");
		}

		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawValue = this.serializeValues(values);

			final Long result = this.jedisCluster.srem(rawKey, rawValue);

			return result != null && result > 0;
		}
		catch (Exception e) {
			throw new CacheException("redis:srem", e);
		}
	}

	@Override
	public <K, V> Set<V> smembers(K key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Set<byte[]> results = this.jedisCluster.smembers(rawKey);
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
	}

	@Override
	public <K> Long scard(K key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			return this.jedisCluster.scard(rawKey);
		}
		catch (Exception e) {
			throw new CacheException("redis:scard", e);
		}
	}

	@Override
	public <K> Long incr(K key, long delta) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			return this.jedisCluster.incrBy(rawKey, delta);
		}
		catch (Exception e) {
			throw new CacheException("redis:incr", e);
		}
	}

	@Override
	public <K> Long incr(K key, long delta, long timeout, TimeUnit unit) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Long result = this.jedisCluster.incrBy(rawKey, delta);

			if (result != null && result == delta) {
				this.jedisCluster.expire(rawKey, (int) unit.toSeconds(timeout));
			}
			return result;
		}
		catch (Exception e) {
			throw new CacheException("redis:incr", e);
		}
	}

	@Override
	public <K, V> List<V> lrange(K key, long start, long end) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final List<byte[]> results = this.jedisCluster.lrange(rawKey, start, end);
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
		}
		catch (Exception e) {
			throw new CacheException("redis:lrange", e);
		}
	}

	@Override
	public <K> Long llen(K key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			return this.jedisCluster.llen(rawKey);
		}
		catch (Exception e) {
			throw new CacheException("redis:llen", e);
		}
	}

	@Override
	public <K, V> Long rpush(K key, V... values) {
		if (ObjectUtils.hasNull(values)) {
			throw new IllegalArgumentException("values must not contain null");
		}

		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawValue = this.serializeValues(values);

			return this.jedisCluster.rpush(rawKey, rawValue);
		}
		catch (Exception e) {
			throw new CacheException("redis:rpush", e);
		}
	}

	@Override
	public <K, V> Long lpush(K key, V... values) {
		if (ObjectUtils.hasNull(values)) {
			throw new IllegalArgumentException("values must not contain null");
		}

		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawValue = this.serializeValues(values);

			return this.jedisCluster.lpush(rawKey, rawValue);
		}
		catch (Exception e) {
			throw new CacheException("redis:lpush", e);
		}
	}

	@Override
	public <K, V> V lpop(K key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.jedisCluster.lpop(rawKey);

			return (V) ((rawValue == null) ? null : this.deserializeValue(rawValue));
		}
		catch (Exception e) {
			throw new CacheException("redis:lpop", e);
		}
	}

	@Override
	public <K, V> V rpop(K key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.jedisCluster.rpop(rawKey);

			return (V) ((rawValue == null) ? null : this.deserializeValue(rawValue));
		}
		catch (Exception e) {
			throw new CacheException("redis:rpop", e);
		}
	}

	@Override
	public <K, V> V brpop(int timeout, K... keys) {
		try {
			final byte[][] rawKeys = this.serializeKeys(keys);

			final List<byte[]> rawValues = this.jedisCluster.brpop(timeout, rawKeys);
			if (rawValues == null || rawValues.isEmpty()) {
				return null;
			}

			return (V) this.deserializeValue(rawValues.get(1));
		}
		catch (Exception e) {
			throw new CacheException("redis:bpop", e);
		}
	}

	@Override
	public <K, V> V blpop(int timeout, K... keys) {
		try {
			final byte[][] rawKeys = this.serializeKeys(keys);

			final List<byte[]> rawValues = this.jedisCluster.blpop(timeout, rawKeys);
			if (rawValues == null || rawValues.isEmpty()) {
				return null;
			}

			return (V) this.deserializeValue(rawValues.get(1));
		}
		catch (Exception e) {
			throw new CacheException("redis:bpop", e);
		}
	}

	@Override
	public void destroy() {
	}

	void init() {
		this.setJedisPoolConfig();
		this.setJedisPool();
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
		final String clusters = this.redisConfig.getClusters();
		if (StringUtils.isBlank(clusters)) {
			throw new IllegalArgumentException("redis.clusters must not be blank");
		}

		final int connectionTimeout = this.redisConfig.getConnectionTimeout();
		final int soTimeout = this.redisConfig.getSoTimeout();
		final int maxRedirections = this.redisConfig.getMaxRedirections();

		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		for (String str : clusters.split("[,\\s\\t]+")) {
			final String[] arr = str.split(":");

			nodes.add(new HostAndPort(arr[0], Integer.parseInt(arr[1])));
		}

		this.jedisCluster = new JedisCluster(nodes, connectionTimeout, soTimeout, maxRedirections, this.poolConfig);
		if (logger.isInfoEnabled()) {
			logger.info("connect to Redis Cluster {}", clusters);
		}
	}

	@Override
	public <K> Map<K, Long> mincr(K... keys) {
		if (ObjectUtils.hasNull(keys)) {
			throw new IllegalArgumentException("keys must not contain null");
		}

		try {
			final byte[][] rawKeys = this.serializeKeys(keys);
			if (keys.length == 1) {
				return this.mincr0(keys, rawKeys);
			}
			/**
			 * 绕过 redis.clients.jedis.JedisClusterCommand.run(int keyCount, String... keys)
			 */
			final Map<Integer, Entry<List<K>, List<byte[]>>> slotKeyTable = new HashMap<Integer, Entry<List<K>, List<byte[]>>>();

			for (int i = 0; i < keys.length; i++) {
				final int slot = JedisClusterCRC16.getSlot(rawKeys[i]);
				if (!slotKeyTable.containsKey(slot)) {
					slotKeyTable.put(slot,
							new Entry<List<K>, List<byte[]>>(new ArrayList<K>(), new ArrayList<byte[]>()));
				}
				final Entry<List<K>, List<byte[]>> entry = slotKeyTable.get(slot);
				entry.getKey().add(keys[i]);
				entry.getValue().add(rawKeys[i]);
			}

			final Map<K, Long> results = new HashMap<K, Long>();

			for (Entry<List<K>, List<byte[]>> entry : slotKeyTable.values()) {
				results.putAll(this.mincr0((K[]) entry.getKey().toArray(), (byte[][]) entry.getValue().toArray()));
			}
			return results;
		}
		catch (Exception e) {
			throw new CacheException("redis:mincr", e);
		}
	}

	<K> Map<K, Long> mincr0(K[] keys, byte[][] rawKeys) {
		try {
			final List<byte[]> rawValues = this.jedisCluster.mget(rawKeys);
			if (rawValues == null || rawValues.isEmpty()) {
				return Collections.emptyMap();
			}

			RedisInputStream ris = null;

			final Map<K, Long> results = new HashMap<K, Long>();
			for (int i = 0; i < keys.length; i++) {
				try {
					final int length = rawValues.get(i).length;
					final int size = 2 + length;
					final byte[] buf = new byte[size];
					System.arraycopy(rawValues.get(i), 0, buf, 0, length);
					buf[length] = '\r';
					buf[length + 1] = '\n';

					ris = new RedisInputStream(new ByteArrayInputStream(buf));
					results.put(keys[i], ris.readLongCrLf());
				}
				finally {
					IOUtils.closeQuietly(ris);
				}
			}
			return results;
		}
		catch (Exception e) {
			throw new CacheException("redis:mincr", e);
		}
	}
}

/**
 * 
 */
package commons.cache.facade.redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.DefaultSlotMatcher;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.SlotMatcher;
import redis.clients.util.JedisClusterCRC16;

import com.google.common.collect.Lists;
import commons.cache.config.RedisConfig;
import commons.cache.exception.CacheException;
import commons.cache.operation.CasOperation;
import commons.lang.ObjectUtils;

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
	private SlotMatcher slotMatcher;
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
	public <K, V> Map<K, V> mget(K... keys) {
		try {
			final byte[][] rawKeys = this.serializeKeys(keys);
			if (keys.length == 1) {
				return this.toGetMap(keys, this.jedisCluster.mget(rawKeys));
			}
			/**
			 * 绕过 redis.clients.jedis.JedisClusterCommand.run(int keyCount, String... keys)
			 */
			final List<Object[]> slotsList = this.splitKeyBySlot(keys, rawKeys);
			final Map<K, V> results = new HashMap<K, V>();

			for (Object[] slots : slotsList) {
				final K[] ks = (K[]) ((List<K>) slots[0]).toArray();
				final byte[][] rks = ((List<byte[]>) slots[1]).toArray(new byte[][] {});
				final List<byte[]> rvs = this.jedisCluster.mget(rks);
				if (rvs == null || rvs.isEmpty()) {
					continue;
				}

				final Map<K, V> result = this.toGetMap(ks, rvs);
				results.putAll(result);
			}

			return results;
		}
		catch (Exception e) {
			throw new CacheException("redis:mget", e);
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
			final byte[][] rawKeys = new byte[keys.size()][];
			int i = 0;
			for (K key : keys) {
				rawKeys[i++] = this.serializeKey(key);
			}
			if (rawKeys.length == 1) {
				this.jedisCluster.del(rawKeys);
				return;
			}
			/**
			 * 绕过 redis.clients.jedis.JedisClusterCommand.run(int keyCount, String... keys)
			 */
			final List<Object[]> slotsList = this.splitKeyBySlot(keys.toArray(), rawKeys);

			for (Object[] slots : slotsList) {
				final byte[][] rks = ((List<byte[]>) slots[1]).toArray(new byte[][] {});

				this.jedisCluster.del(rks);
			}
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

		final String slots = this.redisConfig.getSlots();
		// TODO 通过 jedis.clusterNodes() 获取，参考 JedisClusterInfoCache
		this.slotMatcher = new DefaultSlotMatcher(slots);

		this.jedisCluster = new JedisCluster(nodes, connectionTimeout, soTimeout, maxRedirections, this.poolConfig);
		if (logger.isInfoEnabled()) {
			logger.info("connect to Redis Cluster {}, slots: {}", clusters, slots);
		}
	}

	@Override
	public <K> Map<K, Long> mincr(K... keys) {
		try {
			final byte[][] rawKeys = this.serializeKeys(keys);
			if (keys.length == 1) {
				return this.toIncrMap(keys, this.jedisCluster.mget(rawKeys));
			}
			/**
			 * 绕过 redis.clients.jedis.JedisClusterCommand.run(int keyCount, String... keys)
			 */
			final List<Object[]> slotsList = this.splitKeyBySlot(keys, rawKeys);
			final Map<K, Long> results = new HashMap<K, Long>();

			for (Object[] slots : slotsList) {
				final K[] ks = (K[]) ((List<K>) slots[0]).toArray();
				final byte[][] rks = ((List<byte[]>) slots[1]).toArray(new byte[][] {});
				final List<byte[]> rvs = this.jedisCluster.mget(rks);
				if (rvs == null || rvs.isEmpty()) {
					continue;
				}

				results.putAll(this.toIncrMap(ks, rvs));
			}

			return results;
		}
		catch (Exception e) {
			throw new CacheException("redis:mincr", e);
		}
	}

	<K> List<Object[]> splitKeyBySlot(K[] keys, byte[][] rawKeys) {
		final Map<byte[], Object[]> slotsTable = new HashMap<byte[], Object[]>();
		slotsTable.put(rawKeys[0], new Object[] { Lists.newArrayList(keys[0]), Lists.newArrayList(rawKeys[0]) });

		if (logger.isDebugEnabled()) {
			logger.debug("key: {}, slot: {}", keys[0], JedisClusterCRC16.getSlot(rawKeys[0]));
		}

		out: for (int i = 1; i < rawKeys.length; i++) {
			if (logger.isDebugEnabled()) {
				logger.debug("key: {}, slot: {}", keys[i], JedisClusterCRC16.getSlot(rawKeys[i]));
			}

			for (Map.Entry<byte[], Object[]> entry : slotsTable.entrySet()) {
				if (this.slotMatcher.match(entry.getKey(), rawKeys[i])) {
					((List<K>) entry.getValue()[0]).add(keys[i]);
					((List<byte[]>) entry.getValue()[1]).add(rawKeys[i]);
					continue out;
				}
			}
			slotsTable.put(rawKeys[i], new Object[] { Lists.newArrayList(keys[i]), Lists.newArrayList(rawKeys[i]) });
		}

		return new ArrayList<Object[]>(slotsTable.values());
	}

	@Override
	public <K, F, V> void hset(K key, F field, V value) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawField = this.serializeKey(key);

			final byte[] rawValue = this.serializeValue(key);

			this.jedisCluster.hset(rawKey, rawField, rawValue);
		}
		catch (Exception e) {
			throw new CacheException("redis:hset", e);
		}
	}

	@Override
	public <K, F, V> V hget(K key, F field) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawField = this.serializeKey(key);

			final byte[] rawValue = this.jedisCluster.hget(rawKey, rawField);

			return this.deserializeValue(rawValue);
		}
		catch (Exception e) {
			throw new CacheException("redis:hget", e);
		}
	}

	@Override
	public <K, F, V> Long hincr(K key, F field, long value) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawField = this.serializeKey(key);

			return this.jedisCluster.hincrBy(rawKey, rawField, value);
		}
		catch (Exception e) {
			throw new CacheException("redis:hincr", e);
		}
	}

	@Override
	public <K, F, V> Map<F, V> hmget(K key, F... fields) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawFields = this.serializeKeys(fields);
			if (rawFields.length == 1) {
				return this.toGetMap(fields, this.jedisCluster.mget(rawFields));
			}
			/**
			 * 绕过 redis.clients.jedis.JedisClusterCommand.run(int keyCount, String... keys)
			 */
			final List<Object[]> slotsList = this.splitKeyBySlot(fields, rawFields);
			final Map<F, V> results = new HashMap<F, V>();

			for (Object[] slots : slotsList) {
				final F[] ks = (F[]) ((List<F>) slots[0]).toArray();
				final byte[][] rks = ((List<byte[]>) slots[1]).toArray(new byte[][] {});
				final List<byte[]> rvs = this.jedisCluster.hmget(rawKey, rks);
				if (rvs == null || rvs.isEmpty()) {
					continue;
				}

				final Map<F, V> result = this.toGetMap(ks, rvs);
				results.putAll(result);
			}

			return results;
		}
		catch (Exception e) {
			throw new CacheException("redis:hmget", e);
		}
	}

	@Override
	public <K, F> Map<F, Long> hmincr(K key, F... fields) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawFields = this.serializeKeys(fields);
			if (fields.length == 1) {
				return this.toIncrMap(fields, this.jedisCluster.mget(rawFields));
			}
			/**
			 * 绕过 redis.clients.jedis.JedisClusterCommand.run(int keyCount, String... keys)
			 */
			final List<Object[]> slotsList = this.splitKeyBySlot(fields, rawFields);
			final Map<F, Long> results = new HashMap<F, Long>();

			for (Object[] slots : slotsList) {
				final F[] ks = (F[]) ((List<F>) slots[0]).toArray();
				final byte[][] rks = ((List<byte[]>) slots[1]).toArray(new byte[][] {});
				final List<byte[]> rvs = this.jedisCluster.hmget(rawKey, rks);
				if (rvs == null || rvs.isEmpty()) {
					continue;
				}

				results.putAll(this.toIncrMap(ks, rvs));
			}

			return results;
		}
		catch (Exception e) {
			throw new CacheException("redis:hmincr", e);
		}
	}

	@Override
	public <K, F> void hdel(K key, F... fields) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawFields = this.serializeKeys(fields);
			if (fields.length == 1) {
				this.jedisCluster.hdel(rawKey, rawFields);
				return;
			}
			/**
			 * 绕过 redis.clients.jedis.JedisClusterCommand.run(int keyCount, String... keys)
			 */
			final List<Object[]> slotsList = this.splitKeyBySlot(fields, rawFields);

			for (Object[] slots : slotsList) {
				final byte[][] rks = ((List<byte[]>) slots[1]).toArray(new byte[][] {});

				this.jedisCluster.hdel(rawKey, rks);
			}
		}
		catch (Exception e) {
			throw new CacheException("redis:hdel", e);
		}
	}

	@Override
	public <K, F> Boolean hexists(K key, F field) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawField = this.serializeKey(field);

			return this.jedisCluster.hexists(rawKey, rawField);
		}
		catch (Exception e) {
			throw new CacheException("redis:hexists", e);
		}
	}
}

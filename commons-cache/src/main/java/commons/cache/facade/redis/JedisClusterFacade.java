/**
 * 
 */
package commons.cache.facade.redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import redis.clients.jedis.DefaultSlotMatcher;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.SlotMatcher;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.util.JedisClusterCRC16;
import redis.clients.util.SafeEncoder;

import com.google.common.collect.Lists;

import commons.cache.config.RedisConfig;
import commons.cache.exception.CacheException;
import commons.cache.operation.CasOperation;

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

	@Override
	public <V> V get(String key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			return this.deserializeValue(this.jedisCluster.get(rawKey));
		}
		catch (Exception e) {
			throw new CacheException("redis:get", e);
		}
	}

	@Override
	public <V> Map<String, V> mget(String... keys) {
		try {
			final byte[][] rawKeys = this.serializeKeys(keys);
			if (keys.length == 1) {
				return this.toGetMap(keys, this.jedisCluster.mget(rawKeys));
			}
			/**
			 * 绕过 redis.clients.jedis.JedisClusterCommand.run(int keyCount, String... keys)
			 */
			final List<Pair<List<String>, List<byte[]>>> slotsList = this.splitKeyBySlot(keys, rawKeys);
			final Map<String, V> results = new HashMap<String, V>();

			for (Pair<List<String>, List<byte[]>> slots : slotsList) {
				final String[] ks = slots.getLeft().toArray(new String[] {});
				final byte[][] rks = slots.getRight().toArray(new byte[][] {});
				final List<byte[]> rvs = this.jedisCluster.mget(rks);
				if (rvs == null || rvs.isEmpty()) {
					continue;
				}

				final Map<String, V> result = this.toGetMap(ks, rvs);
				results.putAll(result);
			}

			return results;
		}
		catch (Exception e) {
			throw new CacheException("redis:mget", e);
		}
	}

	@Override
	public <V> V getSet(String key, V value) {
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
	public <V> void set(String key, V value) {
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
	public <V> void set(String key, V value, long timeout, TimeUnit unit) {
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
	public <V> Boolean setnx(String key, V value, long timeout, TimeUnit unit) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.serializeValue(value);

			if (rawValue != null) {
				final boolean result = (this.jedisCluster.setnx(rawKey, rawValue) == 1);
				if (result) {
					this.jedisCluster.expire(rawKey, (int) unit.toSeconds(timeout));
				}
				return result;
			}

			return Boolean.FALSE;
		}
		catch (Exception e) {
			throw new CacheException("redis:setnx", e);
		}
	}

	@Override
	public <V> Boolean setnx(String key, V value) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.serializeValue(value);

			if (rawValue != null) {
				return this.jedisCluster.setnx(rawKey, rawValue) == 1;
			}

			return Boolean.FALSE;
		}
		catch (Exception e) {
			throw new CacheException("redis:setnx", e);
		}
	}

	@Override
	public <V> Boolean cas(final String key, final CasOperation<V> casOperation, final long timeout, final TimeUnit unit) {
		throw new UnsupportedOperationException("Redis Cluster can not support the operation");
	}

	@Override
	public void delete(String key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			this.jedisCluster.del(rawKey);
		}
		catch (Exception e) {
			throw new CacheException("redis:del", e);
		}
	}

	@Override
	public void delete(Collection<String> keys) {
		if (keys == null || keys.isEmpty()) {
			return;
		}
		try {
			final byte[][] rawKeys = this.serializeKeys(keys.toArray(new String[] {}));
			if (rawKeys.length == 1) {
				this.jedisCluster.del(rawKeys);
				return;
			}
			/**
			 * 绕过 redis.clients.jedis.JedisClusterCommand.run(int keyCount, String... keys)
			 */
			final List<Pair<List<String>, List<byte[]>>> slotsList = this.splitKeyBySlot(keys.toArray(new String[] {}),
					rawKeys);

			for (Pair<List<String>, List<byte[]>> slots : slotsList) {
				final byte[][] rks = slots.getRight().toArray(new byte[][] {});

				this.jedisCluster.del(rks);
			}
		}
		catch (Exception e) {
			throw new CacheException("redis:del", e);
		}
	}

	@Override
	public Boolean expire(String key, long timeout, TimeUnit unit) {
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
	public <V> Boolean sadd(String key, V... values) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawValues = this.serializeValues(values);

			final Long result = this.jedisCluster.sadd(rawKey, rawValues);

			return result != null && result > 0;
		}
		catch (Exception e) {
			throw new CacheException("redis:sadd", e);
		}
	}

	@Override
	public <V> Boolean srem(String key, V... values) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawValues = this.serializeValues(values);

			final Long result = this.jedisCluster.srem(rawKey, rawValues);

			return result != null && result > 0;
		}
		catch (Exception e) {
			throw new CacheException("redis:srem", e);
		}
	}

	@Override
	public <V> Set<V> smembers(String key) {
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
	public Long scard(String key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			return this.jedisCluster.scard(rawKey);
		}
		catch (Exception e) {
			throw new CacheException("redis:scard", e);
		}
	}

	@Override
	public Long incr(String key, long delta) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			return this.jedisCluster.incrBy(rawKey, delta);
		}
		catch (Exception e) {
			throw new CacheException("redis:incr", e);
		}
	}

	@Override
	public Long incr(String key, long delta, long timeout, TimeUnit unit) {
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
	public Long decr(String key, long delta) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			return this.jedisCluster.decrBy(rawKey, delta);
		}
		catch (Exception e) {
			throw new CacheException("redis:decr", e);
		}
	}

	@Override
	public Long decr(String key, long delta, long timeout, TimeUnit unit) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Long result = this.jedisCluster.decrBy(rawKey, delta);

			if (result != null && result == delta) {
				this.jedisCluster.expire(rawKey, (int) unit.toSeconds(timeout));
			}
			return result;
		}
		catch (Exception e) {
			throw new CacheException("redis:decr", e);
		}
	}

	@Override
	public <V> List<V> lrange(String key, long start, long end) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final List<byte[]> results = this.jedisCluster.lrange(rawKey, start, end);
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
	}

	@Override
	public <V> V lindex(String key, int index) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.jedisCluster.lindex(rawKey, index);

			return deserializeValue(rawValue);
		}
		catch (Exception e) {
			throw new CacheException("redis:lindex", e);
		}
	}

	@Override
	public Long llen(String key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			return this.jedisCluster.llen(rawKey);
		}
		catch (Exception e) {
			throw new CacheException("redis:llen", e);
		}
	}

	@Override
	public <V> Long rpush(String key, V... values) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawValues = this.serializeValues(values);

			return this.jedisCluster.rpush(rawKey, rawValues);
		}
		catch (Exception e) {
			throw new CacheException("redis:rpush", e);
		}
	}

	@Override
	public <V> Long lpush(String key, V... values) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawValues = this.serializeValues(values);

			return this.jedisCluster.lpush(rawKey, rawValues);
		}
		catch (Exception e) {
			throw new CacheException("redis:lpush", e);
		}
	}

	@Override
	public <V> V lpop(String key) {
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
	public <V> V rpop(String key) {
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
	public <V> V brpop(int timeout, String... keys) {
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
	public <V> V blpop(int timeout, String... keys) {
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
		if (this.jedisCluster != null) {
			try {
				this.jedisCluster.close();
			}
			catch (IOException e) {
				logger.warn("Error to close jedisCluster", e);
			}
		}
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
	public Map<String, Long> mincr(String... keys) {
		try {
			final byte[][] rawKeys = this.serializeKeys(keys);
			if (keys.length == 1) {
				return this.toIncrMap(keys, this.jedisCluster.mget(rawKeys));
			}
			/**
			 * 绕过 redis.clients.jedis.JedisClusterCommand.run(int keyCount, String... keys)
			 */
			final List<Pair<List<String>, List<byte[]>>> slotsList = this.splitKeyBySlot(keys, rawKeys);
			final Map<String, Long> results = new HashMap<String, Long>();

			for (Pair<List<String>, List<byte[]>> slots : slotsList) {
				final String[] ks = slots.getLeft().toArray(new String[] {});
				final byte[][] rks = slots.getRight().toArray(new byte[][] {});
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

	List<Pair<List<String>, List<byte[]>>> splitKeyBySlot(String[] keys, byte[][] rawKeys) {
		final Map<byte[], Pair<List<String>, List<byte[]>>> slotsTable = new HashMap<byte[], Pair<List<String>, List<byte[]>>>();
		final List<String> left0 = Lists.newArrayList(keys[0]);
		final List<byte[]> right0 = Lists.newArrayList(rawKeys[0]);
		slotsTable.put(rawKeys[0], Pair.of(left0, right0));

		if (logger.isDebugEnabled()) {
			logger.debug("key: {}, slot: {}", keys[0], JedisClusterCRC16.getSlot(rawKeys[0]));
		}

		out: for (int i = 1; i < rawKeys.length; i++) {
			if (logger.isDebugEnabled()) {
				logger.debug("key: {}, slot: {}", keys[i], JedisClusterCRC16.getSlot(rawKeys[i]));
			}

			for (Map.Entry<byte[], Pair<List<String>, List<byte[]>>> entry : slotsTable.entrySet()) {
				if (this.slotMatcher.match(entry.getKey(), rawKeys[i])) {
					entry.getValue().getLeft().add(keys[i]);
					entry.getValue().getRight().add(rawKeys[i]);
					continue out;
				}
			}
			final List<String> left = Lists.newArrayList(keys[i]);
			final List<byte[]> right = Lists.newArrayList(rawKeys[i]);
			slotsTable.put(rawKeys[i], Pair.of(left, right));
		}

		return new ArrayList<Pair<List<String>, List<byte[]>>>(slotsTable.values());
	}

	@Override
	public <V> void hset(String key, String field, V value) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawField = this.serializeKey(field);

			final byte[] rawValue = this.serializeValue(value);

			this.jedisCluster.hset(rawKey, rawField, rawValue);
		}
		catch (Exception e) {
			throw new CacheException("redis:hset", e);
		}
	}

	@Override
	public <V> V hget(String key, String field) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawField = this.serializeKey(field);

			final byte[] rawValue = this.jedisCluster.hget(rawKey, rawField);

			return this.deserializeValue(rawValue);
		}
		catch (Exception e) {
			throw new CacheException("redis:hget", e);
		}
	}

	@Override
	public <V> Map<String, V> hgetAll(String key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Map<byte[], byte[]> rawValues = this.jedisCluster.hgetAll(rawKey);
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
	}

	@Override
	public <V> Long hincr(String key, String field, long value) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawField = this.serializeKey(field);

			return this.jedisCluster.hincrBy(rawKey, rawField, value);
		}
		catch (Exception e) {
			throw new CacheException("redis:hincr", e);
		}
	}

	@Override
	public <V> void hmset(String key, Map<String, V> value) {
		if (value == null || value.isEmpty()) {
			return;
		}
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

			this.jedisCluster.hmset(rawKey, hash);
		}
		catch (Exception e) {
			throw new CacheException("redis:hmset", e);
		}
	}

	@Override
	public <V> Map<String, V> hmget(String key, String... fields) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawFields = this.serializeKeys(fields);

			return this.toGetMap(fields, this.jedisCluster.hmget(rawKey, rawFields));
		}
		catch (Exception e) {
			throw new CacheException("redis:hmget", e);
		}
	}

	@Override
	public Map<String, Long> hmincr(String key, String... fields) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawFields = this.serializeKeys(fields);

			return this.toIncrMap(fields, this.jedisCluster.hmget(rawKey, rawFields));
		}
		catch (Exception e) {
			throw new CacheException("redis:hmincr", e);
		}
	}

	@Override
	public void hdel(String key, String... fields) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[][] rawFields = this.serializeKeys(fields);

			this.jedisCluster.hdel(rawKey, rawFields);
		}
		catch (Exception e) {
			throw new CacheException("redis:hdel", e);
		}
	}

	@Override
	public Boolean hexists(String key, String field) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawField = this.serializeKey(field);

			return this.jedisCluster.hexists(rawKey, rawField);
		}
		catch (Exception e) {
			throw new CacheException("redis:hexists", e);
		}
	}

	@Override
	public <V> Set<V> hkeys(String key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Set<byte[]> rawValues = this.jedisCluster.hkeys(rawKey);
			final Set<V> results = new HashSet<V>();
			for (byte[] rawValue : rawValues) {
				results.add((V) this.deserializeValue(rawValue));
			}
			return results;
		}
		catch (Exception e) {
			throw new CacheException("redis:hkeys", e);
		}
	}

	@Override
	public <V> List<V> hvals(String key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Collection<byte[]> rawValues = this.jedisCluster.hvals(rawKey);
			final List<V> results = new ArrayList<V>();
			for (byte[] rawValue : rawValues) {
				results.add((V) this.deserializeValue(rawValue));
			}
			return results;
		}
		catch (Exception e) {
			throw new CacheException("redis:hvals", e);
		}
	}

	@Override
	public Long hlen(String key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			return this.jedisCluster.hlen(rawKey);
		}
		catch (Exception e) {
			throw new CacheException("redis:hlen", e);
		}
	}

	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers) {
		if (scoreMembers == null || scoreMembers.isEmpty()) {
			return 0L;
		}
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Map<byte[], Double> rawScoreMembers = new HashMap<byte[], Double>();
			for (Map.Entry<String, Double> entry : scoreMembers.entrySet()) {
				final byte[] rawField = this.serializeKey(entry.getKey());

				rawScoreMembers.put(rawField, entry.getValue());
			}

			return this.jedisCluster.zadd(rawKey, rawScoreMembers, ZAddParams.zAddParams());
		}
		catch (Exception e) {
			throw new CacheException("redis:zadd", e);
		}
	}

	@Override
	public Long zremrangeByRank(String key, long start, long end) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			return this.jedisCluster.zremrangeByRank(rawKey, start, end);
		}
		catch (Exception e) {
			throw new CacheException("redis:zremrangeByRank", e);
		}
	}

	@Override
	public Set<String> zrange(String key, long start, long end) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Set<byte[]> rawFields = this.jedisCluster.zrange(rawKey, start, end);

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
	}

	@Override
	public Boolean exists(String key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			return this.jedisCluster.exists(rawKey);
		}
		catch (Exception e) {
			throw new CacheException("redis:exists", e);
		}
	}

	@Override
	public Boolean expireAt(String key, long unixTimeMillis) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final Long result = this.jedisCluster.expireAt(rawKey, unixTimeMillis);

			return result != null && result > 0;
		}
		catch (Exception e) {
			throw new CacheException("redis:expireAt", e);
		}
	}

	@Override
	public Boolean setNoIncr(String key, long delta) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final String result = this.jedisCluster.set(rawKey, SafeEncoder.encode(String.valueOf(delta)));

			return StringUtils.equalsIgnoreCase(result, "ok");
		}
		catch (Exception e) {
			throw new CacheException("redis:set", e);
		}
	}

	@Override
	public Boolean setNoIncr(String key, long delta, long timeout, TimeUnit unit) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final String result = this.jedisCluster.setex(rawKey, (int) unit.toSeconds(timeout),
					SafeEncoder.encode(String.valueOf(delta)));

			return StringUtils.equalsIgnoreCase(result, "ok");
		}
		catch (Exception e) {
			throw new CacheException("redis:set", e);
		}
	}

	@Override
	public Long getNoIncr(String key) {
		try {
			final byte[] rawKey = this.serializeKey(key);

			final byte[] rawValue = this.jedisCluster.get(rawKey);
			if (ArrayUtils.isEmpty(rawValue)) {
				return null;
			}

			return Long.valueOf(new String(rawValue, Protocol.CHARSET));
		}
		catch (Exception e) {
			throw new CacheException("redis:get", e);
		}
	}
}

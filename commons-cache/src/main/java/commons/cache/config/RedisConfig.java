/**
 * 
 */
package commons.cache.config;

import java.io.Serializable;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 5:20:06 PM Nov 14, 2015
 */
public class RedisConfig implements Serializable {
	private static final long serialVersionUID = 2152399835231022772L;
	private String host = "localhost";
	private int port = 6379;
	private int database = 0;
	private int protocolTimeoutMillis = 20000;
	private String password;

	// redis clusters 地址 逗号或空格分隔
	private String clusters;
	private int connectionTimeout = 20000;
	private int soTimeout = 5000;
	private int maxRedirections = 3;

	// 连接池最大连接数
	private int maxTotal = 50;
	// 连接池中最多可空闲的连接数
	private int maxIdle = 5;
	// 连接池中连接用完时，新的请求获取连接的等待时间
	private int maxWaitMillis = 10000;
	// 连接池中连接可空闲的时间，如果空闲连接已达标，那么超过这个时间连接将关闭
	private int minEvictableIdleTimeMillis = 300000;
	/**
	 * <pre>
	 * 设定在进行后台对象清理时，每次检查几个连接。
	 * 如果numTestsPerEvictionRun>=0，则取numTestsPerEvictionRun和池内的连接数 的较小值作为每次检测的连接数。
	 * 如果numTestsPerEvictionRun<0，则每次检查的连接数是检查时池内连接的总数乘以这个值的负倒数再向上取整的结果。
	 * </pre>
	 */
	private int numTestsPerEvictionRun = 3;
	// 每隔多少时间去检测一次空闲连接是否超时，默认值为-1，即不开启
	private int timeBetweenEvictionRunsMillis = 60000;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public int getProtocolTimeoutMillis() {
		return protocolTimeoutMillis;
	}

	public void setProtocolTimeoutMillis(int protocolTimeoutMillis) {
		this.protocolTimeoutMillis = protocolTimeoutMillis;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public int getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public int getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}

	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public int getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public String getClusters() {
		return clusters;
	}

	public void setClusters(String clusters) {
		this.clusters = clusters;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public int getMaxRedirections() {
		return maxRedirections;
	}

	public void setMaxRedirections(int maxRedirections) {
		this.maxRedirections = maxRedirections;
	}

}

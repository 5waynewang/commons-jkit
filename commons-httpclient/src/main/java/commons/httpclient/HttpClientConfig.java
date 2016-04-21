package commons.httpclient;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 8:05:15 PM Jul 28, 2014
 */
public interface HttpClientConfig {

	String HTTP_CHARSET = System.getProperty("http.charset", "UTF-8");
	
	/**
	 * 连接超时时间
	 */
	int HTTP_CONN_TIMEOUT = Integer.parseInt(System.getProperty("http.conn.timeout", "5000"));

	/**
	 * 请求超时时间
	 */
	int HTTP_SO_TIMEOUT = Integer.parseInt(System.getProperty("http.so.timeout", "30000"));

	boolean HTTP_KEEPALIVE = Boolean.parseBoolean(System.getProperty("http.keepalive", "true"));
	/**
	 * 服务端断掉连接后，客户端需重建连接 该参数表示这些 CLOSE_WAIT 状态的连接能保持多长时间，超过这个时间则重新创建连接
	 */
	int HTTP_KEEPIDLE_DURATION = Integer.parseInt(System.getProperty("http.keepidle.duration", "5000"));

	@Deprecated
	boolean HTTP_STALE_CHECK = Boolean.parseBoolean(System.getProperty("http.stale.check", "true"));
	
	int VALIDATE_AFTER_INACTIVITY = Integer.parseInt(System.getProperty("http.validateAfterInactivity.ms", "300000"));

	int HTTP_MAX_TOTAL_CONN = Integer.parseInt(System.getProperty("http.max.total.conn", "100"));

	int HTTP_MAX_CONN_PER_ROUTE = Integer.parseInt(System.getProperty("http.max.conn.per.route", "10"));

}

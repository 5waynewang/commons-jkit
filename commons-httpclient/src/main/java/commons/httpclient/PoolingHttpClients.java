package commons.httpclient;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 8:05:04 PM Jul 28, 2014
 */
public class PoolingHttpClients {
	private static final Log log = LogFactory.getLog(PoolingHttpClients.class);

	private final static PoolingHttpClientConnectionManager connManager;

	private final static CloseableHttpClient httpClient;

	private final static RequestConfig requestConfig;

	private final static ResponseHandler<HttpInvokeResult> responseHandler = new DefaultResponseHandler();

	static {
		connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(HttpClientConfig.HTTP_MAX_TOTAL_CONN);
		connManager.setDefaultMaxPerRoute(HttpClientConfig.HTTP_MAX_CONN_PER_ROUTE);

		final ConnectionConfig.Builder connectionConfigBuilder = ConnectionConfig.custom();
		connectionConfigBuilder.setCharset(Charset.forName(HttpClientConfig.HTTP_CHARSET));
		connManager.setDefaultConnectionConfig(connectionConfigBuilder.build());

		final SocketConfig.Builder socketConfigBuilder = SocketConfig.custom();
		socketConfigBuilder.setTcpNoDelay(true);
		socketConfigBuilder.setSoKeepAlive(HttpClientConfig.HTTP_KEEPALIVE);
		socketConfigBuilder.setSoTimeout(HttpClientConfig.HTTP_SO_TIMEOUT);
		connManager.setDefaultSocketConfig(socketConfigBuilder.build());

		final HttpClientBuilder httpClientBuilderBuilder = HttpClientBuilder.create();
		httpClientBuilderBuilder.setConnectionManager(connManager);
		// 增加 keep alive 策略
		httpClientBuilderBuilder.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				return HttpClientConfig.HTTP_KEEPIDLE_DURATION;
			}
		});
		// 增加 redirect 策略
		httpClientBuilderBuilder.setRedirectStrategy(new DefaultRedirectStrategy() {
			@Override
			protected boolean isRedirectable(String method) {
				if (HttpPost.METHOD_NAME.equalsIgnoreCase(method)) {
					return true;
				}
				return super.isRedirectable(method);
			}
		});
		httpClient = httpClientBuilderBuilder.build();

		final RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
		requestConfigBuilder.setConnectTimeout(HttpClientConfig.HTTP_CONN_TIMEOUT);
		requestConfigBuilder.setSocketTimeout(HttpClientConfig.HTTP_SO_TIMEOUT);
		requestConfig = requestConfigBuilder.build();
	}

	public static CloseableHttpClient getDefaultHttpclient() {
		return httpClient;
	}

	public static RequestConfig getDefaultRequestConfig() {
		return requestConfig;
	}

	public static HttpInvokeResult get(String url) {
		return get(url, 0);
	}

	public static HttpInvokeResult get(String url, long timeout) {
		return get(url, timeout, null);
	}

	public static HttpInvokeResult get(String url, Collection<Header> headers) {
		return get(url, 0, headers);
	}

	public static HttpInvokeResult get(String url, long timeout, Collection<Header> headers) {
		return invoke(createHttpGet(url, headers), timeout);
	}

	static HttpGet createHttpGet(String url, Collection<Header> headers) {
		final HttpGet httpGet = new HttpGet(url);
		addHeaders(headers, httpGet);
		return httpGet;
	}

	public static HttpInvokeResult proxyGet(String url, String proxy) {
		return proxyGet(url, 0, proxy);
	}

	public static HttpInvokeResult proxyGet(String url, long timeout, String proxy) {
		return proxyGet(url, timeout, null, null);
	}

	public static HttpInvokeResult proxyGet(String url, long timeout, Collection<Header> headers,
			String proxy) {
		return invoke(createHttpGet(url, headers), timeout, null);
	}

	static void addHeaders(Collection<Header> headers, HttpRequestBase request) {
		if (headers == null || headers.isEmpty()) {
			return;
		}
		for (final Header header : headers) {
			if (header == null) {
				continue;
			}
			request.addHeader(header);
		}
	}

	@SuppressWarnings("unchecked")
	public static HttpInvokeResult post(String url) {
		return post(url, Collections.EMPTY_MAP, 0);
	}

	public static HttpInvokeResult post(String url, Map<String, Object> params) {
		return post(url, params, 0);
	}

	public static HttpInvokeResult post(String url, Map<String, Object> params, long timeout) {
		return post(url, params, timeout, null);
	}

	public static HttpInvokeResult post(String url, Map<String, Object> params,
			Collection<Header> headers) {
		return post(url, params, 0, headers);
	}

	@SuppressWarnings("unchecked")
	public static HttpInvokeResult post(String url, Map<String, Object> params, long timeout,
			Collection<Header> headers) {
		if (params == null || params.isEmpty()) {
			return post(url, Collections.EMPTY_LIST, timeout, headers);
		}
		final Collection<NameValuePair> nvps = new ArrayList<NameValuePair>(params.size());
		for (final Map.Entry<String, Object> entry : params.entrySet()) {
			if (entry.getValue() == null) {
				nvps.add(new BasicNameValuePair(entry.getKey(), null));
			}
			else if (entry.getValue() instanceof Object[]) {
				for (Object v : (Object[]) entry.getValue()) {
					if (v == null) {
						nvps.add(new BasicNameValuePair(entry.getKey(), null));
					}
					else {
						nvps.add(new BasicNameValuePair(entry.getKey(), v.toString()));
					}
				}
			}
			else {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
		}
		return post(url, nvps, timeout, headers);
	}

	public static HttpInvokeResult post(String url, Collection<NameValuePair> params) {
		return post(url, params);
	}

	public static HttpInvokeResult post(String url, Collection<NameValuePair> params, long timeout) {
		return post(url, params, timeout, null);
	}

	public static HttpInvokeResult post(String url, Collection<NameValuePair> params,
			Collection<Header> headers) {
		return post(url, params, 0, headers);
	}

	public static HttpInvokeResult post(String url, Collection<NameValuePair> params, long timeout,
			Collection<Header> headers) {
		final HttpPost httpPost = new HttpPost(url);

		if (params != null && !params.isEmpty()) {
			try {
				HttpEntity entity = new UrlEncodedFormEntity(params,
						Charset.forName(HttpClientConfig.HTTP_CHARSET));

				httpPost.setEntity(entity);
			}
			catch (RuntimeException e) {
				log.error("Error to setEntity", e);
				throw e;
			}
		}

		addHeaders(headers, httpPost);

		return invoke(httpPost, timeout);
	}

	public static HttpInvokeResult post(String url, byte[] body) {
		return post(url, body, 0);
	}

	public static HttpInvokeResult post(String url, byte[] body, long timeout) {
		return post(url, body, 0, body.length, timeout);
	}

	public static HttpInvokeResult post(String url, byte[] body, int off, int len) {

		return post(url, body, off, len, 0);
	}

	public static HttpInvokeResult post(String url, byte[] body, int off, int len, long timeout) {
		final HttpPost httpPost = new HttpPost(url);

		httpPost.setEntity(new ByteArrayEntity(body, off, len, ContentType.APPLICATION_JSON));

		return invoke(httpPost, timeout);
	}

	static HttpInvokeResult invoke(HttpRequestBase request, long timeout) {
		return invoke(request, timeout, null);
	}

	static HttpInvokeResult invoke(HttpRequestBase request, long timeout, String proxy) {
		final String url = request.getURI().toString();
		if (log.isDebugEnabled()) {
			log.debug("invoke url:" + url);
		}

		HttpInvokeResult result;

		final RequestConfig.Builder builder = RequestConfig.copy(getDefaultRequestConfig());
		if (timeout > 0) {
			builder.setSocketTimeout((int) timeout);
		}
		if (proxy != null && !"".equals(proxy)) {
			final String[] addr = proxy.split(":");
			final int port = addr.length == 1 ? 80 : Integer.parseInt(addr[1]);
			builder.setProxy(new HttpHost(addr[0], port));
		}
		request.setConfig(builder.build());

		try {
			result = getDefaultHttpclient().execute(request, responseHandler);
			if (result.getException() != null) {
				request.abort();
				log.error("请求失败,statusCode=" + result.getStatusCode() + ",url=" + url + ","
						+ result.getException().getMessage());
			}
			result.setUrl(request.getURI().toString());
			request.releaseConnection();
			return result;
		}
		catch (final Throwable e) {
			request.abort();
			log.error("请求失败,url=" + url + "," + e.getMessage());
			result = new HttpInvokeResult();
			result.setUrl(url);
			result.setException(e);
			result.setReason(e.getMessage());
			return result;
		}
		finally {
			request.reset();
		}
	}

}

/**
 * 
 */
package commons.httpclient;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

/**
 * <pre>
 *
 * </pre>
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 6:00:05 PM Sep 16, 2015
 */
public class SSLHttpClientTest {

	@Test
	public void testPost() throws Exception {
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,
				new TrustStrategy() {
					//信任所有
					public boolean isTrusted(X509Certificate[] chain, String authType)
							throws CertificateException {
						return true;
					}
				}).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

		HttpGet request = new HttpGet("https://www.baidu.com");
		request.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.125 Safari/537.36");

		final CloseableHttpResponse response = httpClient.execute(request);

		System.out.println(response.getStatusLine().getStatusCode());

		System.out.println(EntityUtils.toString(response.getEntity()));
	}

	@Test
	public void testGet2() {
		try {

			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,
					new TrustStrategy() {
						@Override
						public boolean isTrusted(X509Certificate[] chain, String authType)
								throws CertificateException {
							return true;
						}
					}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			HttpPost httppost = new HttpPost("https://community.apache.org/contributors/");
			MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
			//params
			//mEntityBuilder.addTextBody("userName", "1234");

			httppost.setEntity(mEntityBuilder.build());
			//httppost.addHeader("Content-Type", "Application/JSON");

			int timeOut = 1000 * 50;
			// set Timeout
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(timeOut).setConnectTimeout(timeOut)
					.setSocketTimeout(timeOut).build();
			httppost.setConfig(requestConfig);
			// get responce
			HttpResponse responce = httpClient.execute(httppost);
			// get http status code
			int status = responce.getStatusLine().getStatusCode();
			System.out.println("request code:" + status);
			String resultString = null;
			if (status == HttpStatus.SC_OK) {
				// get result data
				HttpEntity entity = responce.getEntity();
				resultString = EntityUtils.toString(entity);
			}
			System.out.println(resultString);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}

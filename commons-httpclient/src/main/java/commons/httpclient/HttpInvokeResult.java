package commons.httpclient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 8:05:22 PM Jul 28, 2014
 */
public class HttpInvokeResult {
	private static final String FORMAT = "URL:[%s],StatusCode:[%s],Reason[%s]";

	private HttpResponse response;
	private String url = null;
	private int statusCode = -1;
	private String reason = null;
	private Throwable e = null;

	private byte[] responseBody;

	public void setStatusCode(final int statusCode) {
		this.statusCode = statusCode;
	}

	public void setReason(final String reasonPhrase) {
		this.reason = reasonPhrase;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	/**
	 * see {@link #getResponseBodyAsString()}
	 * @return
	 */
	@Deprecated
	public String getContent() {
		return getResponseBodyAsString();
	}

	public HttpEntity getHttpEntity() {
		final HttpResponse response = getResponse();

		return response == null ? null : response.getEntity();
	}

	public byte[] getResponseBody() {
		return this.responseBody;
	}

	public void setResponseBody(byte[] responseBody) {
		this.responseBody = responseBody;
	}

	/**
	 * <pre>
	 * convert response bytes to String
	 * </pre>
	 * @return
	 */
	public String getResponseBodyAsString() {
		final byte[] reponseBody = this.getResponseBody();
		if (reponseBody == null) {
			return null;
		}

		return new String(reponseBody, getResponseCharset());
	}
	
	/**
	 * <pre>
	 * convert response bytes to InputStream
	 * </pre>
	 * @return
	 */
	public InputStream getResponseBodyAsStream() {
		final byte[] reponseBody = this.getResponseBody();
		if (reponseBody == null) {
			return new ByteArrayInputStream(new byte[] {});
		}

		return new ByteArrayInputStream(reponseBody);
	}

	public ContentType getResponseContentType() {
		final HttpResponse response = getResponse();

		return response == null ? null : ContentType.get(response.getEntity());
	}

	public Charset getResponseCharset() {
		return getResponseCharset(Charset.forName(HttpClientConfig.HTTP_CHARSET));
	}

	public Charset getResponseCharset(Charset chartset) {
		final ContentType contentType = getResponseContentType();

		return contentType == null ? chartset : contentType.getCharset();
	}

	public String getReason() {
		return this.reason;
	}

	public void setException(final Throwable e) {
		this.e = e;
	}

	public Throwable getException() {
		return this.e;
	}

	public boolean isSuccess() {
		return this.statusCode == HttpStatus.SC_OK;
	}

	@Override
	public String toString() {
		return String.format(FORMAT, getUrl(), getStatusCode(), getReason());
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	HttpResponse getResponse() {
		return response;
	}

	public void setResponse(final HttpResponse response) {
		this.response = response;
	}

	public String getHeader(final String name) {
		return getLastHeader(name);
	}

	public String getLastHeader(final String name) {
		final HttpResponse response = getResponse();
		if (response == null) {
			return null;
		}
		final Header header = response.getLastHeader(name);

		return header != null ? header.getValue() : null;
	}

	public String getFirstHeader(final String name) {
		final HttpResponse response = getResponse();
		if (response == null) {
			return null;
		}
		final Header header = response.getFirstHeader(name);

		return header != null ? header.getValue() : null;
	}

	public Header[] getHeaders(final String name) {
		final HttpResponse response = getResponse();

		return response == null ? null : response.getHeaders(name);
	}
}

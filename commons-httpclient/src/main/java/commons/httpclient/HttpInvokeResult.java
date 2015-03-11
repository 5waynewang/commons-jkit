package commons.httpclient;

import org.apache.http.HttpResponse;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 8:05:22 PM Jul 28, 2014
 */
public class HttpInvokeResult {

	public static final int SC_OK = 200;

	public static final int SC_NOT_MODIFIED = 304;

	public static final int SC_NOT_FOUND = 404;

	public static final int SC_SERVICE_UNAVAILABLE = 503;

	private HttpResponse response;
	private String url = null;
	private int statusCode = -1;
	private String content = null;
	private String reason = null;
	private Throwable e = null;

	public void setStatusCode(final int statusCode) {
		this.statusCode = statusCode;
	}

	public void setReason(final String reasonPhrase) {
		this.reason = reasonPhrase;
	}

	public HttpInvokeResult setContent(final String content) {
		this.content = content;
		return this;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public String getContent() {
		return this.content;
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

	public boolean isOK() {
		return this.statusCode == SC_OK;
	}

	String FORMAT = "URL:[%s],StatusCode:[%s],Content[%s],Reason[%s]";

	@Override
	public String toString() {
		return String.format(this.FORMAT, this.url, this.statusCode, this.content, this.reason);
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public void setResponse(final HttpResponse response) {
		this.response = response;
	}

	public String getHeader(final String name) {
		return this.response != null ? this.response.getLastHeader(name).getValue() : null;
	}

}

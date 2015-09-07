package commons.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 8:05:10 PM Jul 28, 2014
 */
class DefaultResponseHandler implements ResponseHandler<HttpInvokeResult> {
	@Override
	public HttpInvokeResult handleResponse(final HttpResponse response) {
		final HttpInvokeResult result = new HttpInvokeResult();
		result.setResponse(response);
		try {
			final StatusLine statusLine = response.getStatusLine();
			final HttpEntity entity = response.getEntity();

			result.setStatusCode(statusLine.getStatusCode());
			result.setReason(statusLine.getReasonPhrase());

			if (statusLine.getStatusCode() >= 300) {
				EntityUtils.consume(entity);
			}

			result.setResponseBody(EntityUtils.toByteArray(entity));
		}
		catch (final Throwable e) {
			result.setReason(e.getMessage());
			result.setException(e);
		}

		return result;

	}
}

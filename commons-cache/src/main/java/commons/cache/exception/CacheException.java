/**
 * 
 */
package commons.cache.exception;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 6:06:11 PM Nov 14, 2015
 */
public class CacheException extends RuntimeException {
	private static final long serialVersionUID = 3796605462770129042L;

	public CacheException() {
		super();
	}

	public CacheException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheException(String message) {
		super(message);
	}

	public CacheException(Throwable cause) {
		super(cause);
	}

}

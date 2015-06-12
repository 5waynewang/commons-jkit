/**
 * 
 */
package com.google.code.fqueue.exception;

/**
 * <pre>
 *
 * </pre>
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:22:34 AM Jun 12, 2015
 */
public class FSQueueClosedException extends RuntimeException {

	private static final long serialVersionUID = -462989057011968484L;

	public FSQueueClosedException() {
		super();
	}

	public FSQueueClosedException(String message, Throwable cause) {
		super(message, cause);
	}

	public FSQueueClosedException(String message) {
		super(message);
	}

	public FSQueueClosedException(Throwable cause) {
		super(cause);
	}
}

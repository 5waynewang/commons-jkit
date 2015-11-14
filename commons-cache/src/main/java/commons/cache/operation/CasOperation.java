/**
 * 
 */
package commons.cache.operation;

import commons.cache.exception.CancelCasException;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 7:07:54 PM Nov 14, 2015
 */
public interface CasOperation<T> {
	/**
	 * cas最大重试次数，0-不控制
	 * 
	 * @return
	 */
	int casMaxTries();

	T getNewValue(T currentValue) throws CancelCasException;
}

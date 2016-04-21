/**
 * 
 */
package commons.eventbus;

import java.util.concurrent.Executor;

import commons.eventbus.closure.ClosureExt;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 6:15:10 PM Apr 15, 2016
 */
public interface EventBus {
	Runnable registerObserver(Executor exec, String event, ClosureExt closure);

	Runnable registerObserver(Executor exec, String event, Object target, String methodName);

	void fireEvent(String event, Object... args);
}

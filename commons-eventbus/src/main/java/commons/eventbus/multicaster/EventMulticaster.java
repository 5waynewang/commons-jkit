/**
 * 
 */
package commons.eventbus.multicaster;

import commons.eventbus.closure.EventListener;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:21:05 AM Jul 27, 2016
 */
public interface EventMulticaster {
	void addListener(Object event, EventListener listener);

	void removeListener(Object event, EventListener listener);

	void removeListeners(Object event);

	void multicastEvent(Object event, Object... args);
}

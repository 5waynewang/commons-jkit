/**
 * 
 */
package commons.eventbus.manager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import commons.eventbus.DefaultEventBus;
import commons.eventbus.EventBus;
import commons.eventbus.closure.ClosureExt;

/**
 * <pre>
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:29:31 AM Jul 21, 2016
 */
public class DefaultEventBusManager {
	private final EventBus eventBus;
	private final Executor exec;

	private final ConcurrentHashMap</*event*/Object, ConcurrentHashMap<ClosureExt, Runnable>> eventListenerTab = new ConcurrentHashMap<>();

	public DefaultEventBusManager() {
		this(new DefaultEventBus(true));
	}

	public DefaultEventBusManager(EventBus eventBus) {
		this(eventBus, Executors.newSingleThreadExecutor());
	}

	public DefaultEventBusManager(Executor exec) {
		this(new DefaultEventBus(true), exec);
	}

	public DefaultEventBusManager(EventBus eventBus, Executor exec) {
		this.eventBus = eventBus;
		this.exec = exec;
	}

	public void fireEvent(Object event, Object... args) {
		eventBus.fireEvent(event, args);
	}

	public void registerListener(Object event, ClosureExt listener) {
		final Runnable runnable = eventBus.registerObserver(exec, event, listener);

		ConcurrentHashMap<ClosureExt, Runnable> listeners = eventListenerTab.get(event);
		if (listeners == null) {
			listeners = new ConcurrentHashMap<>();
			ConcurrentHashMap<ClosureExt, Runnable> old = eventListenerTab.putIfAbsent(event, listeners);
			if (old != null) {
				listeners = old;
			}
		}

		// 这个listener已经订阅过了
		// 覆盖订阅，关闭老的
		final Runnable old = listeners.putIfAbsent(listener, runnable);
		if (old != null) {
			old.run();
		}
	}

	public void removeListener(Object event, ClosureExt listener) {
		final ConcurrentHashMap<ClosureExt, Runnable> listeners = eventListenerTab.get(event);
		if (listeners != null) {
			final Runnable runnable = listeners.remove(listener);
			if (runnable != null) {
				runnable.run();
			}
		}
	}
}

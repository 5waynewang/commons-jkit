/**
 * 
 */
package commons.eventbus.multicaster;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import commons.eventbus.DefaultEventBus;
import commons.eventbus.EventBus;
import commons.eventbus.closure.ClosureExt;
import commons.eventbus.closure.EventListener;

/**
 * <pre>
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:29:31 AM Jul 21, 2016
 */
public class DefaultEventMulticaster implements EventMulticaster {
	private final EventBus eventBus;
	private final Executor exec;

	private final ConcurrentHashMap</*event*/Object, ConcurrentHashMap<ClosureExt, Runnable>> eventListenerTab = new ConcurrentHashMap<>();

	public DefaultEventMulticaster() {
		this(new DefaultEventBus(true));
	}

	public DefaultEventMulticaster(EventBus eventBus) {
		this(eventBus, Executors.newSingleThreadExecutor());
	}

	public DefaultEventMulticaster(Executor exec) {
		this(new DefaultEventBus(true), exec);
	}

	public DefaultEventMulticaster(EventBus eventBus, Executor exec) {
		this.eventBus = eventBus;
		this.exec = exec;
	}

	@Override
	public void multicastEvent(Object event, Object... args) {
		eventBus.fireEvent(event, args);
	}

	@Override
	public void addListener(Object event, EventListener listener) {
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

	@Override
	public void removeListener(Object event, EventListener listener) {
		if (listener == null) {
			return;
		}
		listener.setCanceled(true);
		
		final ConcurrentHashMap<ClosureExt, Runnable> listeners = eventListenerTab.get(event);
		if (listeners != null) {
			final Runnable runnable = listeners.remove(listener);
			if (runnable != null) {
				runnable.run();
			}
		}
	}

	@Override
	public void removeListeners(Object event) {
		final ConcurrentHashMap<ClosureExt, Runnable> listeners = eventListenerTab.remove(event);
		if (listeners != null) {
			for (Map.Entry<ClosureExt, Runnable> entry : listeners.entrySet()) {
				entry.getKey().setCanceled(true);
				entry.getValue().run();
			}
		}
	}
}

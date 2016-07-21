/**
 * 
 */
package commons.eventbus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import commons.eventbus.closure.ClosureExt;
import commons.eventbus.closure.Functor;
import commons.eventbus.closure.FunctorAsync;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 6:47:30 PM Apr 15, 2016
 */
public class DefaultEventBus implements EventBus {

	private final Log log = LogFactory.getLog(getClass());

	private static final AtomicInteger index = new AtomicInteger(0);

	private ExecutorService mainExecutor = null;

	private static class ClosureExSet {
		// 主执行器(mainExecutor)是单线程，所以这里是线程安全的
		private final Map<UUID, ClosureExt> closures;

		private ClosureExSet(boolean isOrdered) {
			if (isOrdered) {
				this.closures = new LinkedHashMap<>();
			}
			else {
				this.closures = new HashMap<>();
			}
		}

		public void add(UUID uuid, ClosureExt closure) {
			closures.put(uuid, closure);
		}

		public void remove(UUID uuid) {
			closures.remove(uuid);
		}

		public void execute(Object... args) {
			for (ClosureExt closure : closures.values()) {
				closure.execute(args);
			}
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (ClosureExt closure : closures.values()) {
				sb.append(closure.toString());
				sb.append(";");
			}

			return sb.toString();
		}
	}

	private Map<Object, ClosureExSet> closureSet = new HashMap<Object, ClosureExSet>();

	private boolean isOrdered;// ClosureExSet 是否有序

	public DefaultEventBus() {
		this(false);
	}

	public DefaultEventBus(boolean isOrdered) {
		this.isOrdered = isOrdered;
		this.mainExecutor = Executors.newFixedThreadPool(1, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "EventBusMainThread" + index.getAndIncrement());
			}
		});
	}

	private ClosureExSet getOrCreateClosureExSet(Object event) {
		ClosureExSet set = closureSet.get(event);

		if (null == set) {
			set = new ClosureExSet(isOrdered);
			closureSet.put(event, set);
		}

		return set;
	}

	private ClosureExSet getClosureExSet(Object event) {
		return closureSet.get(event);
	}

	private void doRegisterObserver(final Object event, final UUID id, final ClosureExt closure) {
		getOrCreateClosureExSet(event).add(id, closure);
	}

	private void doRemoveObserver(final Object event, final UUID id) {
		ClosureExSet set = getClosureExSet(event);

		if (null != set) {
			set.remove(id);
		}
	}

	@Override
	public Runnable registerObserver(final Executor exec, final Object event, final ClosureExt closure) {

		final FunctorAsync async = new FunctorAsync();
		async.setExecutor(exec);
		async.setImpl(closure);

		final UUID id = UUID.randomUUID();

		this.mainExecutor.submit(new Runnable() {
			@Override
			public void run() {
				doRegisterObserver(event, id, async);
			}
		});

		return new Runnable() {
			@Override
			public void run() {
				//	set canceled flag
				closure.setCanceled(true);
				//	and remove registered observer
				mainExecutor.submit(new Runnable() {
					@Override
					public void run() {
						doRemoveObserver(event, id);
					}
				});
			}
		};
	}

	@Override
	public Runnable registerObserver(final Executor exec, final Object event, final Object target,
			final String methodName) {

		return registerObserver(exec, event, new Functor(target, methodName));
	}

	private void doFireEvent(final Object event, final Object... args) {
		ClosureExSet set = this.getClosureExSet(event);
		if (null != set) {
			set.execute(args);
		}
		else {
			if (log.isDebugEnabled()) {
				log.debug("event [" + event + "] not found any matched closure!");
			}
		}
	}

	@Override
	public void fireEvent(final Object event, final Object... args) {
		this.mainExecutor.submit(new Runnable() {
			@Override
			public void run() {
				doFireEvent(event, args);
			}
		});
	}

	public int getPendingTaskCount() {
		if (this.mainExecutor instanceof ThreadPoolExecutor) {
			BlockingQueue<Runnable> queue = ((ThreadPoolExecutor) mainExecutor).getQueue();
			return queue.size();
		}
		else {
			throw new RuntimeException("Internal Erro : mainExecutor is !NOT! ThreadPoolExecutor class");
		}
	}

	private Map<Object, String> doGetAllEvents() {
		Map<Object, String> ret = new HashMap<Object, String>();
		for (Map.Entry<Object, ClosureExSet> entry : this.closureSet.entrySet()) {
			ret.put(entry.getKey(), entry.getValue().toString());
		}

		return ret;
	}

	public Map<Object, String> getAllEvents() throws InterruptedException, ExecutionException {
		return this.mainExecutor.submit(new Callable<Map<Object, String>>() {
			@Override
			public Map<Object, String> call() throws Exception {
				return doGetAllEvents();
			}
		}).get();
	}

}

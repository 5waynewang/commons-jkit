/**
 * 
 */
package commons.eventbus.closure;

import java.util.concurrent.Executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:37:35 AM Apr 18, 2016
 */
public class FunctorAsync implements ClosureExt {

	private static final Log log = LogFactory.getLog(FunctorAsync.class);

	private Executor executor;
	private ClosureExt impl;

	@Override
	public String toString() {
		return null != impl ? impl.toString() : "functorAsync(null)";
	}

	public void execute(final Object... args) {
		executor.execute(new Runnable() {

			public void run() {
				try {
					impl.execute(args);
				}
				catch (Exception e) {
					log.error("execute:", e);
				}
			}
		});
	}

	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public ClosureExt getImpl() {
		return impl;
	}

	public void setImpl(ClosureExt impl) {
		this.impl = impl;
	}

	public void setCanceled(boolean canceled) {
		this.impl.setCanceled(canceled);
	}

}

/**
 * 
 */
package commons.eventbus.closure;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 1:17:01 PM Jul 21, 2016
 */
public abstract class EventListenerAdapter implements ClosureExt {
	private boolean canceled;

	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
}

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
 * @since 6:46:11 PM Apr 15, 2016
 */
public interface ClosureExt {
	void execute(Object... args);

	void setCanceled(boolean canceled);
}

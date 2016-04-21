/**
 * 
 */
package commons.eventbus.closure;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:35:41 AM Apr 18, 2016
 */
public class Functor implements ClosureExt {

	private static final Log log = LogFactory.getLog(Functor.class);

	private boolean canceled = false;
	private Object target = null;
	private Method method = null;

	public Functor(Object target, String methodName) {
		this.target = target;
		if (null == this.target) {
			throw new RuntimeException(" target is null.");
		}

		Method[] methods = null;
		Class<?> itr = target.getClass();
		while (!itr.equals(Object.class)) {
			methods = (Method[]) ArrayUtils.addAll(itr.getDeclaredMethods(), methods);
			itr = itr.getSuperclass();
		}
		for (Method methodItr : methods) {
			if (methodItr.getName().equals(methodName)) {
				methodItr.setAccessible(true);
				this.method = methodItr;
			}
		}
		if (null == this.method) {
			throw new RuntimeException("method [" + target.getClass() + "." + methodName + "] !NOT! exist.");
		}
	}

	public void execute(Object... args) {
		if (!canceled) {
			try {
				method.invoke(this.target, args);
			}
			catch (IllegalArgumentException e) {
				log.error("execute", e);
			}
			catch (IllegalAccessException e) {
				log.error("execute", e);
			}
			catch (InvocationTargetException e) {
				log.error("execute", e);
			}
		}
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(this.target);
		sb.append(".");
		sb.append(this.method.getName());

		if (this.canceled) {
			sb.append("[canceled]");
		}
		return sb.toString();
	}
}

/**
 * 
 */
package commons.lang;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <pre>
 * copy from com.mongodb.util.UniqueList
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 7:54:06 PM Apr 9, 2015
 */
public class UniqueList<T> extends ArrayList<T> {

	private static final long serialVersionUID = -5142543069290180113L;

	@Override
	public boolean add(T t) {
		if (contains(t))
			return false;
		return super.add(t);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean added = false;
		for (T t : c)
			added = add(t) || added;
		return added;
	}
}

/**
 * 
 */
package commons.serialization.hessian.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.IOExceptionWrapper;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 2:22:02 PM Nov 4, 2015
 */
public class SortDeserializer extends AbstractDeserializer {
	@Override
	public Class<?> getType() {
		return Sort.class;
	}

	@Override
	public Object readMap(AbstractHessianInput in) throws IOException {
		int ref = in.addRef(null);

		List<Order> orders = new ArrayList<>();

		while (!in.isEnd()) {
			String key = in.readString();

			if (key.equals("value")) {
				while (!in.isEnd()) {
					orders.add((Order) in.readObject());
				}
			}
			else {
				in.readObject();
			}
		}

		in.readMapEnd();

		Object value = create(orders);

		in.setRef(ref, value);

		return value;
	}

	private Object create(List<Order> orders) throws IOException {
		if (orders == null) {
			throw new IOException(getType().getName() + " expects name.");
		}

		try {
			return new Sort(orders);
		}
		catch (Exception e) {
			throw new IOExceptionWrapper(e);
		}
	}

	@Override
	public Object readObject(AbstractHessianInput in, Object[] fields) throws IOException {
		String[] fieldNames = (String[]) fields;

		int ref = in.addRef(null);

		List<Order> orders = new ArrayList<>();

		for (int i = 0; i < fieldNames.length; i++) {
			String key = fieldNames[i];

			if (key.equals("value")) {
				while (!in.isEnd()) {
					orders.add((Order) in.readObject());
				}
			}
			else {
				in.readObject();
			}
		}

		Object value = create(orders);

		in.setRef(ref, value);

		return value;
	}
}

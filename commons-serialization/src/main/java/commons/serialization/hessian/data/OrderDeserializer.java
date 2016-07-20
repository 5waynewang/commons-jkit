/**
 * 
 */
package commons.serialization.hessian.data;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.NullHandling;

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
public class OrderDeserializer extends AbstractDeserializer {
	private Constructor _constructor;

	public OrderDeserializer() {
		try {
			_constructor = getType().getDeclaredConstructor(Direction.class, String.class, boolean.class,
					NullHandling.class);
			_constructor.setAccessible(true);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Class<?> getType() {
		return Sort.Order.class;
	}

	@Override
	public Object readMap(AbstractHessianInput in) throws IOException {
		int ref = in.addRef(null);

		String direction = null, property = null, nullHandling = null;
		boolean isIgnoreCase = false;

		while (!in.isEnd()) {
			String key = in.readString();

			if (key.equals("value"))
				direction = in.readString();
			else
				in.readString();
		}

		in.readMapEnd();

		Object value = create(direction, property, isIgnoreCase, nullHandling);

		in.setRef(ref, value);

		return value;
	}

	private Object create(String direction, String property, boolean isIgnoreCase, String nullHandling)
			throws IOException {
		if (property == null) {
			throw new IOException(getType().getName() + " expects name.");
		}

		try {
			return _constructor.newInstance(//
					isNullValue(direction) ? null : Direction.fromStringOrNull(direction), //
					property, //
					isIgnoreCase, //
					isNullValue(nullHandling) ? null : NullHandling.valueOf(nullHandling));
		}
		catch (Exception e) {
			throw new IOExceptionWrapper(e);
		}
	}

	boolean isNullValue(String value) {
		return value == null;
	}

	@Override
	public Object readObject(AbstractHessianInput in, Object[] fields) throws IOException {
		String[] fieldNames = (String[]) fields;

		int ref = in.addRef(null);

		String direction = null, property = null, nullHandling = null;
		boolean isIgnoreCase = false;

		for (int i = 0; i < fieldNames.length; i++) {
			String key = fieldNames[i];

			if (key.equals("value")) {
				direction = in.readString();
				property = in.readString();
				isIgnoreCase = in.readBoolean();
				nullHandling = in.readString();
			}
			else {
				in.readObject();
			}
		}

		Object value = create(direction, property, isIgnoreCase, nullHandling);

		in.setRef(ref, value);

		return value;
	}
}

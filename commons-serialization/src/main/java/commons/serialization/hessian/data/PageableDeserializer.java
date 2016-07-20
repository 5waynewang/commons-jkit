/**
 * 
 */
package commons.serialization.hessian.data;

import java.io.IOException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
public class PageableDeserializer extends AbstractDeserializer {
	Class<? extends Pageable> cl;

	public PageableDeserializer(Class<? extends Pageable> cl) {
		this.cl = cl;
	}

	@Override
	public Class<?> getType() {
		return cl;
	}

	@Override
	public Object readMap(AbstractHessianInput in) throws IOException {
		int ref = in.addRef(null);

		int page = 0, size = 0;
		Sort sort = null;

		while (!in.isEnd()) {
			String key = in.readString();

			if (key.equals("value")) {
				page = in.readInt();
				size = in.readInt();
				sort = (Sort) in.readObject();
			}
			else {
				in.readObject();
			}
		}

		in.readMapEnd();

		Object value = create(page, size, sort);

		in.setRef(ref, value);

		return value;
	}

	private Object create(int page, int size, Sort sort) throws IOException {
		try {
			return new PageRequest(page, size, sort);
		}
		catch (Exception e) {
			throw new IOExceptionWrapper(e);
		}
	}

	@Override
	public Object readObject(AbstractHessianInput in, Object[] fields) throws IOException {
		String[] fieldNames = (String[]) fields;

		int ref = in.addRef(null);

		int page = 0, size = 0;
		Sort sort = null;

		for (int i = 0; i < fieldNames.length; i++) {
			String key = fieldNames[i];

			if (key.equals("value")) {
				page = in.readInt();
				size = in.readInt();
				sort = (Sort) in.readObject();
			}
			else {
				in.readObject();
			}
		}

		Object value = create(page, size, sort);

		in.setRef(ref, value);

		return value;
	}
}

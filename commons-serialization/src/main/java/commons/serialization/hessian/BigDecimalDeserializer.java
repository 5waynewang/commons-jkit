/**
 * 
 */
package commons.serialization.hessian;

import java.io.IOException;
import java.math.BigDecimal;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;

/**
 * <pre>
 *
 * </pre>
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 2:22:02 PM Nov 4, 2015
 */
public class BigDecimalDeserializer extends AbstractDeserializer {
	@Override
	public Class<?> getType() {
		return BigDecimal.class;
	}

	@Override
	public Object readMap(AbstractHessianInput in) throws IOException {
		int ref = in.addRef(null);

		Double initValue = null;

		while (!in.isEnd()) {
			String key = in.readString();

			if (key.equals("value"))
				initValue = in.readDouble();
			else
				in.readString();
		}

		in.readMapEnd();

		if (initValue == null) {
			throw new IOException(getType().getName() + " expects name.");
		}

		Object value = BigDecimal.valueOf(initValue);

		in.setRef(ref, value);

		return value;
	}

	@Override
	public Object readObject(AbstractHessianInput in, Object[] fields) throws IOException {
		String[] fieldNames = (String[]) fields;

		int ref = in.addRef(null);

		Double initValue = null;

		for (int i = 0; i < fieldNames.length; i++) {
			String key = fieldNames[i];

			if (key.equals("value"))
				initValue = in.readDouble();
			else
				in.readObject();
		}

		if (initValue == null) {
			throw new IOException(getType().getName() + " expects name.");
		}

		Object value = BigDecimal.valueOf(initValue);

		in.setRef(ref, value);

		return value;
	}
}

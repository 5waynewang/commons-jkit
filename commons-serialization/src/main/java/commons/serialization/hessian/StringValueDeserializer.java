/**
 * 
 */
package commons.serialization.hessian;

import java.io.IOException;
import java.lang.reflect.Constructor;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.IOExceptionWrapper;

/**
 * Deserializing a string valued object
 */
public class StringValueDeserializer extends AbstractDeserializer {
	private Class _cl;
	private Constructor _constructor;

	public StringValueDeserializer(Class cl) {
		try {
			_cl = cl;
			_constructor = cl.getConstructor(new Class[] { String.class });
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Class getType() {
		return _cl;
	}

	@Override
	public Object readMap(AbstractHessianInput in) throws IOException {
		String value = null;

		while (!in.isEnd()) {
			String key = in.readString();

			if (key.equals("value"))
				value = in.readString();
			else
				in.readObject();
		}

		in.readMapEnd();

		Object object = create(value);

		in.addRef(object);

		return object;
	}

	@Override
	public Object readObject(AbstractHessianInput in, Object[] fieldNames) throws IOException {
		String value = null;

		for (int i = 0; i < fieldNames.length; i++) {
			if ("value".equals(fieldNames[i]))
				value = in.readString();
			else
				in.readObject();
		}

		Object object = create(value);

		in.addRef(object);

		return object;
	}

	private Object create(String value) throws IOException {
		if (value == null) {
			throw new IOException(_cl.getName() + " expects name.");
		}

		try {
			return _constructor.newInstance(new Object[] { value });
		}
		catch (Exception e) {
			throw new IOExceptionWrapper(e);
		}
	}
}

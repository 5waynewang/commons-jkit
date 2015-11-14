/**
 * 
 */
package commons.serialization.hessian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.ExtSerializerFactory;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

/**
 * <pre>
 *
 * </pre>
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 5:09:58 AM Sep 22, 2015
 */
public class Hessian2Serialization {
	static final SerializerFactory serializerFactory = SerializerFactory.createDefault();

	static {
		final ExtSerializerFactory extFactory = new ExtSerializerFactory();
		extFactory.addSerializer(java.math.BigDecimal.class, new BigDecimalSerializer());

		extFactory.addDeserializer(java.math.BigDecimal.class, new BigDecimalDeserializer());

		serializerFactory.addFactory(extFactory);
	}
	
	static AbstractHessianInput getAndCreateInput(InputStream is) {
		final Hessian2Input input = new Hessian2Input(is);
		input.setSerializerFactory(serializerFactory);
		return input;
	}
	
	static AbstractHessianOutput getAndCreateOutput(OutputStream os) {
		final Hessian2Output output = new Hessian2Output(os);
		output.setSerializerFactory(serializerFactory);
		return output;
	}

	@SuppressWarnings("unchecked")
	public static <O> O deserialize(byte[] buf) throws IOException {
		final ByteArrayInputStream is = new ByteArrayInputStream(buf);
		final AbstractHessianInput input = getAndCreateInput(is);
		try {
			return (O) input.readObject();
		}
		finally {
			try {
				is.close();
			}
			catch (IOException ignore) {
			}
		}
	}

	public static <O> byte[] serialize(O o) throws IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final AbstractHessianOutput output = getAndCreateOutput(os);
		try {
			output.writeObject(o);
			output.flush();
			return os.toByteArray();
		}
		finally {
			try {
				os.close();
			}
			catch (IOException ignore) {
			}
		}
	}
}

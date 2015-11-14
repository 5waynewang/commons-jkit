/**
 * 
 */
package commons.serialization.hessian;

import java.io.IOException;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;

/**
 * <pre>
 *
 * </pre>
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 2:22:02 PM Nov 4, 2015
 */
public class BigDecimalSerializer extends AbstractSerializer {
	@Override
	public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
		if (obj == null)
			out.writeNull();
		else {
			Class<?> cl = obj.getClass();

			if (out.addRef(obj)) return;

			int ref = out.writeObjectBegin(cl.getName());

			if (ref < -1) {
				out.writeString("value");
				out.writeDouble(((java.math.BigDecimal) obj).doubleValue());
				out.writeMapEnd();
			}
			else {
				if (ref == -1) {
					out.writeInt(1);
					out.writeString("value");
					out.writeObjectBegin(cl.getName());
				}

				out.writeDouble(((java.math.BigDecimal) obj).doubleValue());
			}
		}
	}
}

/**
 * 
 */
package commons.serialization.hessian.data;

import java.io.IOException;

import org.springframework.data.domain.Sort;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 2:22:02 PM Nov 4, 2015
 */
public class OrderSerializer extends AbstractSerializer {
	@Override
	public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
		if (obj == null)
			out.writeNull();
		else {
			Class<?> cl = obj.getClass();

			if (out.addRef(obj)) return;

			int ref = out.writeObjectBegin(cl.getName());

			Sort.Order order = (Sort.Order) obj;

			if (ref < -1) {
				out.writeString("value");
				write(out, order);
				out.writeMapEnd();
			}
			else {
				if (ref == -1) {
					out.writeInt(1);
					out.writeString("value");
					out.writeObjectBegin(cl.getName());
				}

				write(out, order);
			}
		}
	}

	void write(AbstractHessianOutput out, Sort.Order order) throws IOException {
		if (order.getDirection() == null) {
			out.writeNull();
		}
		else {
			out.writeString(order.getDirection().name());
		}
		out.writeString(order.getProperty());
		out.writeBoolean(order.isIgnoreCase());
		
		if (order.getNullHandling() == null) {
			out.writeNull();
		}
		else {
			out.writeString(order.getNullHandling().name());
		}
	}
}

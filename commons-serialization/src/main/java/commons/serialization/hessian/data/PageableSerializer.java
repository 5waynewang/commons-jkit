/**
 * 
 */
package commons.serialization.hessian.data;

import java.io.IOException;

import org.springframework.data.domain.Pageable;

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
public class PageableSerializer extends AbstractSerializer {
	@Override
	public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
		if (obj == null)
			out.writeNull();
		else {
			Class<?> cl = obj.getClass();

			if (out.addRef(obj)) return;

			int ref = out.writeObjectBegin(cl.getName());

			Pageable dest = (Pageable) obj;

			if (ref < -1) {
				out.writeString("value");
				write(out, dest);
				out.writeMapEnd();
			}
			else {
				if (ref == -1) {
					out.writeInt(1);
					out.writeString("value");
					out.writeObjectBegin(cl.getName());
				}

				write(out, dest);
			}
		}
	}

	void write(AbstractHessianOutput out, Pageable dest) throws IOException {
		out.writeInt(dest.getPageNumber());
		out.writeInt(dest.getPageSize());
		out.writeObject(dest.getSort());
	}
}

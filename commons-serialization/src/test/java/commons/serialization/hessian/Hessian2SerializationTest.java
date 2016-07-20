/**
 * 
 */
package commons.serialization.hessian;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.data.domain.Sort.Order;

import commons.serialization.hessian.data.OrderDeserializer;
import commons.serialization.hessian.data.OrderSerializer;
import commons.serialization.hessian.data.PageableDeserializer;
import commons.serialization.hessian.data.PageableSerializer;
import commons.serialization.hessian.data.SortDeserializer;
import commons.serialization.hessian.data.SortSerializer;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:33:05 AM Jul 20, 2016
 */
public class Hessian2SerializationTest {

	@Test
	public void testBigDecimal() throws Exception {
		final BigDecimal bd = new BigDecimal("123456.789");

		final byte[] buf = Hessian2Serialization.serialize(bd);

		final BigDecimal exp = (BigDecimal) Hessian2Serialization.deserialize(buf);

		Assert.assertTrue(bd.compareTo(exp) == 0);
	}

	@Test
	public void testBigInteger() throws Exception {
		final BigInteger bd = new BigInteger("123456789");

		final byte[] buf = Hessian2Serialization.serialize(bd);

		System.out.println(new String(buf));

		System.out.println(new String(Hessian2Serialization.serialize(bd)));

		final BigInteger exp = (BigInteger) Hessian2Serialization.deserialize(buf);

		Assert.assertTrue(bd.compareTo(exp) == 0);
	}

	@Test
	public void testSort_Order() throws Exception {
		Hessian2Serialization.addSerializer(Sort.Order.class, new OrderSerializer());
		Hessian2Serialization.addDeserializer(Sort.Order.class, new OrderDeserializer());

		final Sort.Order[] orders = new Sort.Order[3];
		orders[0] = new Sort.Order(Sort.Direction.ASC, "order_num", null);
		orders[1] = new Sort.Order(null, "order_num", null);
		orders[2] = new Sort.Order(Sort.Direction.DESC, "create_time", NullHandling.NULLS_LAST);

		final byte[] buf = Hessian2Serialization.serialize(orders);

		//		System.out.println(new String(buf));

		final Sort.Order[] expectOrders = Hessian2Serialization.deserialize(buf);

		for (int i = 0; i < orders.length; i++) {
			Assert.assertTrue(orders[i].equals(expectOrders[i]));
		}
	}

	@Test
	public void testSort() throws Exception {
		Hessian2Serialization.addSerializer(Sort.Order.class, new OrderSerializer());
		Hessian2Serialization.addDeserializer(Sort.Order.class, new OrderDeserializer());
		Hessian2Serialization.addSerializer(Sort.class, new SortSerializer());
		Hessian2Serialization.addDeserializer(Sort.class, new SortDeserializer());

		final Sort.Order[] orders = new Sort.Order[3];
		orders[0] = new Sort.Order(Sort.Direction.ASC, "order_num", null);
		orders[0] = null;
		orders[2] = new Sort.Order(Sort.Direction.DESC, "create_time", NullHandling.NULLS_LAST);

		final Sort sort = new Sort(orders);

		final byte[] buf = Hessian2Serialization.serialize(sort);

		final Sort expectSort = Hessian2Serialization.deserialize(buf);

		final Iterator<Order> iterator = expectSort.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			Order expect = iterator.next();

			Assert.assertTrue(orders[i] == expect || orders[i].equals(expect));
			++i;
		}
	}

	@Test
	public void testPageable() throws Exception {
		Hessian2Serialization.addSerializer(Sort.Order.class, new OrderSerializer());
		Hessian2Serialization.addDeserializer(Sort.Order.class, new OrderDeserializer());
		Hessian2Serialization.addSerializer(Sort.class, new SortSerializer());
		Hessian2Serialization.addDeserializer(Sort.class, new SortDeserializer());
		Hessian2Serialization.addSerializer(PageRequest.class, new PageableSerializer());
		Hessian2Serialization.addDeserializer(PageRequest.class, new PageableDeserializer(PageRequest.class));

		Pageable pageable;

		pageable = new PageRequest(0, 100, Direction.DESC, "order_num");

		Assert.assertEquals(pageable, Hessian2Serialization.deserialize(Hessian2Serialization.serialize(pageable)));

		pageable = new PageRequest(0, 100,
				new Sort(new Order(Direction.ASC, "order_num"), new Order(Direction.DESC, "update_time")));

		Assert.assertEquals(pageable, Hessian2Serialization.deserialize(Hessian2Serialization.serialize(pageable)));
	}
}

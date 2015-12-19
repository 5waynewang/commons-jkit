/**
 * 
 */
package commons.lang;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:19:19 AM Dec 17, 2015
 */
public class BeanUtilsTest {

	@Test
	public void testCopyPropertiesToBean() throws Exception {
		final Map<String, Object> map = new HashMap<>();
		map.put("name", "lucifer");
		map.put("age", 30);
		map.put("birthday", DateUtils.parseDate("20121212", "yyyyMMdd"));

		final Bean bean = new Bean();
		bean.setMemo("memo");
		BeanUtils.copyProperties(bean, map);

		System.out.println(ToStringBuilder.reflectionToString(bean));
	}

	@Test
	public void testCopyPropertiesToMap() throws Exception {
		final Bean bean = new Bean();
		bean.setName("lucifer");
		bean.setAge(30);
		bean.setBirthday(DateUtils.parseDate("20121212", "yyyyMMdd"));

		final Map<String, Object> map = new HashMap<>();

		BeanUtils.copyProperties(map, bean);

		System.out.println(JSON.toJSONString(map));
	}

	public class Bean {
		private String name;
		private Integer age;
		private Date birthday;
		private String memo;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public Date getBirthday() {
			return birthday;
		}

		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

	}
}

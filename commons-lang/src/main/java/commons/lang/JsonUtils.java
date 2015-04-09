package commons.lang;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:24:26 AM Nov 25, 2013
 */
public class JsonUtils {

	private static Log LOG = LogFactory.getLog(JsonUtils.class);

	public static final SimpleDateFormatSerializer ST = new SimpleDateFormatSerializer(
			DateUtils.yyyy$MM$dd$HH$mm$ss);

	/**
	 * 获取SerializeConfig对象
	 * 
	 * <pre>
	 * 1.所有的日期按照yyyy-MM-dd HH:mm:ss 格式输出
	 * </pre>
	 * 
	 * @return
	 */
	public static SerializeConfig defaultSerializeConfig() {
		SerializeConfig config = new SerializeConfig();
		config.put(java.util.Date.class, JsonUtils.ST);
		config.put(java.util.Calendar.class, JsonUtils.ST);
		config.put(java.sql.Date.class, JsonUtils.ST);
		config.put(java.sql.Timestamp.class, JsonUtils.ST);
		return config;
	}

	/**
	 * 目标对象转换成JSON格式的字符串
	 * 
	 * <pre>
	 * 1.忽略目标对象的属性，其内部对象的属性不去管
	 * 2.所有的日期按照yyyy-MM-dd HH:mm:ss 格式输出
	 * </pre>
	 * 
	 * @param dest
	 * @param excludeProperties 忽略的属性名称
	 * @return
	 */
	public static String toJSONStringExclude(final Object dest, final String[] excludeProperties) {
		if (dest == null) {
			LOG.warn("The dest object is null");
			return null;
		}
		PropertyFilter filter = new PropertyFilter() {
			public boolean apply(Object source, String name, Object value) {
				if (dest.getClass() != source.getClass()) {
					return true;
				}
				if (ArrayUtils.contains(excludeProperties, name)) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("exclude class [" + source.getClass() + "], name [" + name + "]");
					}
					return false;
				}
				return true;
			}
		};
		SerializeWriter out = new SerializeWriter();
		JSONSerializer serializer = new JSONSerializer(out, defaultSerializeConfig());
		serializer.getPropertyFilters().add(filter);
		serializer.write(dest);
		return out.toString();
	}

	/**
	 * 目标对象转换成JSON格式的字符串
	 * 
	 * <pre>
	 * 1.忽略对象的属性，根据对象内容忽略其属性
	 * 2.所有的日期按照yyyy-MM-dd HH:mm:ss 格式输出
	 * </pre>
	 * 
	 * @param dest
	 * @param excludes <类, 属性名> 忽略类(key值), value中包含的属性除外
	 * @return
	 */
	public static String toJSONStringExclude(final Object dest,
			final Map<Class<?>, String[]> excludes) {
		PropertyFilter filter = new PropertyFilter() {
			public boolean apply(Object source, String name, Object value) {

				boolean result = ClassUtils.containsAssignableClass(excludes.keySet(),
						source.getClass());

				if (!result) {
					return true;
				}

				List<String[]> values = ClassUtils.getAssignableValues(excludes, source.getClass());

				for (String[] array : values) {
					if (ArrayUtils.contains(array, name)) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("exclude class [" + source.getClass() + "], name [" + name
									+ "]");
						}
						return false;
					}
				}
				return true;
			}
		};
		SerializeWriter out = new SerializeWriter();
		JSONSerializer serializer = new JSONSerializer(out, defaultSerializeConfig());
		serializer.getPropertyFilters().add(filter);
		serializer.write(dest);
		return out.toString();
	}

	/**
	 * 目标对象转换成JSON格式的字符串
	 * 
	 * <pre>
	 * 1.所有的日期按照yyyy-MM-dd HH:mm:ss 格式输出
	 * </pre>
	 * 
	 * @param dest
	 * @return
	 */
	public static String toJSONString(final Object dest) {
		return JSON.toJSONString(dest, defaultSerializeConfig());
	}
}

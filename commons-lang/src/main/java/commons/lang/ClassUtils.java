/**
 * 
 */
package commons.lang;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:22:24 AM Nov 25, 2013
 */
public class ClassUtils {

	public static final Class<?>[] NUMBERS = { Byte.class, Short.class, Integer.class, Long.class,
			Float.class, Double.class };

	/**
	 * 判断obj是否为基本对象类型
	 * 
	 * <pre>
	 * java.lang.Boolean/java.lang.Character/java.lang.Byte/java.lang.Short/java.lang.Integer/java.lang.Long/java.lang.Float/java.lang.Double
	 * </pre>
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isBasicType(Object obj) {
		return obj != null
				&& (obj instanceof Boolean || obj instanceof Character || obj instanceof Byte
						|| obj instanceof Short || obj instanceof Integer || obj instanceof Long
						|| obj instanceof Float || obj instanceof Double);
	}

	/**
	 * 判断clazz是否为基本对象类型
	 * 
	 * <pre>
	 * java.lang.Boolean/java.lang.Character/java.lang.Byte/java.lang.Short/java.lang.Integer/java.lang.Long/java.lang.Float/java.lang.Double
	 * </pre>
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isBasicType(Class<?> clazz) {
		return clazz != null
				&& (clazz == Boolean.class || clazz == Character.class || clazz == Byte.class
						|| clazz == Short.class || clazz == Integer.class || clazz == Long.class
						|| clazz == Float.class || clazz == Double.class);
	}

	/**
	 * 基本类型或者字符串
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isBasicTypeOrString(Class<?> clazz) {
		return isBasicType(clazz) || clazz == String.class;
	}

	/**
	 * 判断obj是否为java.lang.String类型
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isString(Object obj) {
		return obj != null && (obj instanceof String);
	}

	/**
	 * 判断clazz是否为java.lang.String类型
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isString(Class<?> clazz) {
		return clazz == String.class;
	}

	/**
	 * 判断clses 中是否存在 clazz类或其父类(包括接口)
	 * 
	 * @param clses
	 * @param clazz
	 * @return
	 */
	public static boolean containsAssignableClass(Collection<Class<?>> clses, Class<?> clazz) {
		for (Class<?> cls : clses) {
			if (cls.isAssignableFrom(clazz)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断clses 中是否存在 clazz类或其父类(包括接口)
	 * 
	 * @param clses
	 * @param clazz
	 * @return
	 */
	public static boolean containsAssignableClass(Class<?>[] clses, Class<?> clazz) {
		for (Class<?> cls : clses) {
			if (cls.isAssignableFrom(clazz)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据key从Map中获取value值, 只要是cls类型或其父类(包含接口)都符合条件
	 * 
	 * @param dest
	 * @param cls
	 * @return java.util.LinkedList
	 */
	public static <E> List<E> getAssignableValues(Map<Class<?>, E> dest, Class<?> cls) {
		List<E> result = new LinkedList<E>();
		for (Class<?> key : dest.keySet()) {
			if (key != null && key.isAssignableFrom(cls)) {
				result.add(dest.get(key));
			}
		}
		return result;
	}
}

/**
 * 
 */
package commons.lang;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:24:47 AM Nov 25, 2013
 */
public class ObjectUtils {

	/**
	 * 是否存在null值
	 * 
	 * @param objects
	 * @return
	 */
	public static boolean hasNull(Object... objects) {
		if (objects == null) {
			return true;
		}
		for (Object obj : objects) {
			if (obj == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <pre>
	 *  ObjectUtils.toString(null)         = null
	 *  ObjectUtils.toString("")           = ""
	 *  ObjectUtils.toString("bat")        = "bat"
	 *  ObjectUtils.toString(Boolean.TRUE) = "true"
	 * </pre>
	 * 
	 * @param obj
	 * @return
	 */
	public static String toStringIfNotNull(Object obj) {
		return obj == null ? null : obj.toString();
	}
}

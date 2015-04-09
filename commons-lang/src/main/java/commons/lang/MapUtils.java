/**
 * 
 */
package commons.lang;

import java.util.Collection;
import java.util.Map;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 1:53:32 PM Apr 30, 2014
 */
public class MapUtils {

	/**
	 * <pre>
	 * </pre>
	 * 
	 * @param map
	 * @param keys
	 */
	public static <K, V> void remove(Map<K, V> map, Collection<K> keys) {
		if (map == null || map.isEmpty()) {
			return;
		}
		if (keys == null || keys.isEmpty()) {
			return;
		}
		for (K key : keys) {
			if (key == null) {
				continue;
			}
			map.remove(key);
		}
	}

	/**
	 * <pre>
	 * </pre>
	 * 
	 * @param map
	 * @param keys
	 */
	public static <K, V> void remove(Map<K, V> map, K[] keys) {
		if (map == null || map.isEmpty()) {
			return;
		}
		if (keys == null || keys.length == 0) {
			return;
		}
		for (K key : keys) {
			if (key == null) {
				continue;
			}
			map.remove(key);
		}
	}
}

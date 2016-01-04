/**
 * 
 */
package redis.clients.jedis;

/**
 * <pre>
 * 判断是否在同一node上
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 12:27:01 PM Jan 4, 2016
 */
public interface SlotMatcher {

	boolean match(String... keys);

	boolean match(byte[]... keys);
}

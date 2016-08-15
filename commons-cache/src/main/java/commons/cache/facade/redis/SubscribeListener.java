/**
 * 
 */
package commons.cache.facade.redis;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 12:19:17 PM Aug 15, 2016
 */
public interface SubscribeListener<M> {
	void onMessage(String topic, M message);
}

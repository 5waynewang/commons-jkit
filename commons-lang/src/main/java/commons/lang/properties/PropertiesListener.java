/**
 * 
 */
package commons.lang.properties;

import java.util.concurrent.Executor;

/**
 * Properties文件变更时通知
 * 
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:25:15 AM Nov 25, 2013
 */
public interface PropertiesListener {
	/**
	 * 接收到变更内容
	 * 
	 * @param content
	 */
	public void receive(String content);

	/**
	 * 接收到变更使用的线程池, 返回null则在默认的线程池里执行
	 * 
	 * @return
	 */
	public Executor getExecutor();
}

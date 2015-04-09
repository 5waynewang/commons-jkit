/**
 * 
 */
package commons.lang.properties;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:25:20 AM Nov 25, 2013
 */
public class PropertiesListenerTask implements Runnable {

	private static final Log LOG = LogFactory.getLog(PropertiesListenerTask.class);

	private String path;
	private PropertiesListener listener;
	private long lastModified;
	private String encoding;

	public PropertiesListenerTask(String path, long lastModified, PropertiesListener listener, String encoding) {
		this.path = path;
		this.lastModified = lastModified;
		this.listener = listener;
		this.encoding = encoding;
	}

	@Override
	public void run() {
		try {
			final File file = new File(path);
			if (!file.exists()) {
				LOG.error("can not find Properties[" + path + "]");
				return;
			}

			final long lastModified = file.lastModified();
			// 未更新过
			if (lastModified <= this.lastModified) {
				return;
			}

			if (LOG.isInfoEnabled()) {
				LOG.info("Properties[" + path + "] has changed");
			}

			final String content = FileUtils.readFileToString(file, this.encoding);

			if (listener.getExecutor() != null) {
				listener.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						listener.receive(content);
					}
				});
			}
			else {
				listener.receive(content);
			}

			if (LOG.isInfoEnabled()) {
				LOG.info("Success to notify changed Properties[" + path + "]");
			}

			this.lastModified = lastModified;
		}
		catch (Exception e) {
			LOG.error("Error to listener Properties[" + path + "]\r\n", e);
		}
	}

}

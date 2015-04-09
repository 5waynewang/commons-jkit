/**
 * 
 */
package commons.lang.properties;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import commons.lang.concurrent.NamedThreadFactory;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:25:27 AM Nov 25, 2013
 */
public class PropertiesUtils {

	public static final int DEFAULT_INTERVAL_SECONDS = 5 * 30;

	public static Properties loadProperties(String path) {
		return loadProperties(path, null);
	}

	public static Properties loadProperties(String path, PropertiesListener listener) {
		return loadProperties(path, listener, DEFAULT_INTERVAL_SECONDS);
	}

	public static Properties loadProperties(String path, PropertiesListener listener, String encoding) {
		return loadProperties(path, listener, DEFAULT_INTERVAL_SECONDS, encoding);
	}

	public static Properties loadProperties(String path, PropertiesListener listener, int intervalSeconds) {
		return loadProperties(path, listener, intervalSeconds, "UTF-8");
	}

	public static Properties loadProperties(String path, PropertiesListener listener, int intervalSeconds, String encoding) {
		final Properties properties = new Properties();

		final File file = new File(path);
		final String content;
		try {
			content = FileUtils.readFileToString(file, encoding);
		}
		catch (IOException e) {
			// 抛出异常，不能影响上层业务
			throw new RuntimeException("Error to read File[" + path + "]\r\n", e);
		}
		try {
			properties.load(new StringReader(content));
		}
		catch (IOException e) {
			// 抛出异常，不能影响上层业务
			throw new RuntimeException("Error to load [" + content + "] to Properties\r\n", e);
		}

		if (listener != null) {
			final long lastModified = file.lastModified();
			final int interval;
			if (intervalSeconds <= 0) {
				interval = DEFAULT_INTERVAL_SECONDS;
			}
			else {
				interval = intervalSeconds;
			}

			Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("PropertiesListeners"))
					.scheduleAtFixedRate(new PropertiesListenerTask(path, lastModified, listener, encoding), interval,
							interval, TimeUnit.SECONDS);
		}

		return properties;
	}

	public static String getProperty(Properties properties, String key) {
		return properties.getProperty(key);
	}

	public static String getProperty(Properties properties, String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public static Byte getPropertyForByte(Properties properties, String key) {
		final String value = getProperty(properties, key);
		return StringUtils.isBlank(value) ? null : Byte.valueOf(value);
	}

	public static Byte getPropertyForByte(Properties properties, String key, Byte defaultValue) {
		final String value = getProperty(properties, key);
		return StringUtils.isBlank(value) ? defaultValue : Byte.valueOf(value);
	}

	public static Short getPropertyForShort(Properties properties, String key) {
		final String value = getProperty(properties, key);
		return StringUtils.isBlank(value) ? null : Short.valueOf(value);
	}

	public static Short getPropertyForShort(Properties properties, String key, Short defaultValue) {
		final String value = getProperty(properties, key);
		return StringUtils.isBlank(value) ? defaultValue : Short.valueOf(value);
	}

	public static Integer getPropertyForInteger(Properties properties, String key) {
		final String value = getProperty(properties, key);
		return StringUtils.isBlank(value) ? null : Integer.valueOf(value);
	}

	public static Integer getPropertyForInteger(Properties properties, String key, Integer defaultValue) {
		final String value = getProperty(properties, key);
		return StringUtils.isBlank(value) ? defaultValue : Integer.valueOf(value);
	}

	public static Long getPropertyForLong(Properties properties, String key) {
		final String value = getProperty(properties, key);
		return StringUtils.isBlank(value) ? null : Long.valueOf(value);
	}

	public static Long getPropertyForLong(Properties properties, String key, Long defaultValue) {
		final String value = getProperty(properties, key);
		return StringUtils.isBlank(value) ? defaultValue : Long.valueOf(value);
	}

	public static Float getPropertyForFloat(Properties properties, String key) {
		final String value = getProperty(properties, key);
		return StringUtils.isBlank(value) ? null : Float.valueOf(value);
	}

	public static Float getPropertyForFloat(Properties properties, String key, Float defaultValue) {
		final String value = getProperty(properties, key);
		return StringUtils.isBlank(value) ? defaultValue : Float.valueOf(value);
	}

	public static Double getPropertyForDouble(Properties properties, String key) {
		final String value = getProperty(properties, key);
		return StringUtils.isBlank(value) ? null : Double.valueOf(value);
	}

	public static Double getPropertyForDouble(Properties properties, String key, Double defaultValue) {
		final String value = getProperty(properties, key);
		return StringUtils.isBlank(value) ? defaultValue : Double.valueOf(value);
	}

	public static Boolean getPropertyForBoolean(Properties properties, String key) {
		final String value = getProperty(properties, key);
		return StringUtils.isBlank(value) ? null : Boolean.valueOf(value);
	}

	public static Boolean getPropertyForBoolean(Properties properties, String key, Boolean defaultValue) {
		final String value = getProperty(properties, key);
		return StringUtils.isBlank(value) ? defaultValue : Boolean.valueOf(value);
	}
}

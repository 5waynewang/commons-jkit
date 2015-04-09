/**
 * 
 */
package wcommons.lang;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:26:08 AM Nov 25, 2013
 */
public class FileUtilsTests {

	@Test
	public void testUpdateCharset() throws Exception {
		final File file = new File("E:\\configkeeper\\configkeeper-server");
		this.updateCharset(file);
	}

	public void updateCharset(File file) throws Exception {
		if (file.isDirectory()) {
			final File[] files = file.listFiles();
			for (File f : files) {
				this.updateCharset(f);
			}
		}
		else if (file.isFile()
				&& FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("java")) {
			System.out.println(file.getAbsolutePath());
			final String text = FileUtils.readFileToString(file, "GBK");
			FileUtils.writeStringToFile(file, text, "UTF-8");
		}
	}
}

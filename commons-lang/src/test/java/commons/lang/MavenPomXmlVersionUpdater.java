/**
 * 
 */
package commons.lang;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.ArrayUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:17:25 AM Jun 28, 2016
 */
public class MavenPomXmlVersionUpdater {

	static final String rootDir = "E:\\github\\commons-jkit";
	static final String newVersion = "0.20-SNAPSHOT";

	public static void main(String[] args) throws Exception {
		updateVersion(new File(rootDir));
	}

	static void updateVersion(File file) throws Exception {
		if (file.isFile()) {
			if (file.getName().equals("pom.xml")) {
				updateVersion0(file);
			}
		}
		else if (file.isDirectory()) {
			final File[] files = file.listFiles();
			if (ArrayUtils.isNotEmpty(files)) {
				for (File f : files) {
					updateVersion(f);
				}
			}
		}
	}

	static void updateVersion0(File file) throws Exception {
		//写XML文件要用到  
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//允许名字空间  
		factory.setNamespaceAware(true);
		//允许验证  
		factory.setValidating(true);
		//获得DocumentBuilder的一个实例  
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new ErrorHandler() {
			@Override
			public void warning(SAXParseException exception) throws SAXException {
			}

			@Override
			public void fatalError(SAXParseException exception) throws SAXException {
				System.err.println(exception);
			}

			@Override
			public void error(SAXParseException exception) throws SAXException {
				System.err.println(exception);
			}
		});
		//解析文档，并获得一个Document实例  
		Document doc = builder.parse(file);

		//获得根节点project  
		Element project = doc.getDocumentElement();

		NodeList children = project.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeName().equals("version")) {
				final String oldVersion = node.getTextContent();

				node.setTextContent(newVersion);

				System.out.println(file.getAbsolutePath() + "\t" + oldVersion + " -> " + newVersion);
			}
			else if (node.getNodeName().equals("parent")) {
				NodeList children2 = node.getChildNodes();
				for (int j = 0; j < children2.getLength(); j++) {
					Node node2 = children2.item(j);
					if (node2.getNodeName().equals("version")) {
						final String oldVersion = node2.getTextContent();

						node2.setTextContent(newVersion);

						System.out.println(file.getAbsolutePath() + "\t" + oldVersion + " -> " + newVersion);
					}
				}
			}
		}
		//创建一个TransformerFactory实例
		TransformerFactory tff = TransformerFactory.newInstance();
		//通过TransformerFactory 得到一个转换器
		Transformer tf = tff.newTransformer();
		//		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		//通过Transformer类的方法 transform(Source xmlSource, Result outputTarget) 
		//将 XML Source 转换为 Result。
		tf.transform(new DOMSource(doc), new StreamResult(file));
	}
}

package repairer;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class XMLDocumentUtil {

	public static Document getXMLDocument(File file) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		Document doc = null;
		try {
			docBuilder = dbf.newDocumentBuilder();
			doc = docBuilder.parse(file);
		}catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		return doc;
	}
}

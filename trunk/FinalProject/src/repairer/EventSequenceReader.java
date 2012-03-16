package repairer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class EventSequenceReader {

	File file;
	
	public EventSequenceReader(File file) {
		this.file = file;
	}
	
	public List<Integer> readEventSequence() {
		List<Integer> eventSequence = new ArrayList<Integer>();
		
		Document doc = XMLDocumentUtil.getXMLDocument(file);
		
		NodeList items = doc.getElementsByTagName("Step");
		
		for (int i = 0; i < items.getLength(); i++) {
			Element stepElement = (Element) items.item(i);
			Element eventElement = (Element) stepElement.getElementsByTagName("EventId").item(0);
			String eventId = eventElement.getFirstChild().getNodeValue();
			eventSequence.add(Integer.valueOf(eventId.substring(1, eventId.length())));
		}
		return eventSequence;
	}
}

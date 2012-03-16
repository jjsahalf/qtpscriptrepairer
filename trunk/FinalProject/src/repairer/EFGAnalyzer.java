package repairer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class EFGAnalyzer {

	private Document doc;
	
	public EFGAnalyzer(File file) {
		doc = XMLDocumentUtil.getXMLDocument(file);
	}
	
	public boolean isEventExisted(int eventId) {
		boolean existed = false;
		NodeList events = doc.getElementsByTagName("Event");
		for(int i = 0; i < events.getLength(); i++) {
			Element eventElement = (Element) events.item(i);
			Element eventIdElement = (Element) eventElement.getElementsByTagName("EventId").item(0);
			String currentEventId = eventIdElement.getFirstChild().getNodeValue();
			if(currentEventId.equals("e" + eventId)) {
				existed = true;
				break;
			}
		}
		return existed;
	}
	
	//should check isEventExisted before checking isEdgeExisted
	public boolean isEdgeExisted(int preEventId, int posEventId) {
		Element row = (Element) doc.getElementsByTagName("Row").item(preEventId);
		Element e = (Element) row.getElementsByTagName("E").item(posEventId);
		if(e.getFirstChild().getNodeValue().equals("0")) {
			return false;
		}
		return true;
	}
	
	public List<Integer> getSucessors(int eventId) {
		List<Integer> sucessors = new ArrayList<Integer>();
		Element row = (Element) doc.getElementsByTagName("Row").item(eventId);
		NodeList elements = row.getElementsByTagName("E");
		for(int i = 0; i < elements.getLength(); i++) {
			Element element = (Element) elements.item(i);
			if(!element.getFirstChild().getNodeValue().equals("0")) {
				sucessors.add(i);
			}
		}
		return sucessors;
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import domain.EventBean;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;



/**
 *
 * @author GXW
 * EFG文件解析类
 */
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class EFGParser {

     /*******************************************************************************************
     * <b>function:</b> 用SAX对EFG文件进行解析，得到Event的集合
     * @param in EFG文件对应的File对象
     * @return EFG中Event的集合，每个Event属性用EventBean进行保存
     */
    public ArrayList<EventBean> getEvents(File in) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        EFGHandler handler = new EFGHandler();
        parser.parse(in, handler);
        return handler.getEfg();
    }

    private class EFGHandler extends DefaultHandler {

        private ArrayList<EventBean> efg;
        private EventBean event = new EventBean();
        private String tag = null;

        @Override
        public void startDocument() throws SAXException {
            efg = new ArrayList<EventBean>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
            tag = qName;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {

            if (tag != null) {
                if ("EventId".equals(tag)) {
                    String data = new String(ch, start + 1, length - 1);
                    int id = Integer.parseInt(data);
                    event.setEventId(id);
                } else if ("WidgetId".equals(tag)) {
                    String data = new String(ch, start + 1, length - 1);
                    int wi = Integer.parseInt(data);
                    event.setWidgetId(wi);
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("Event".equals(qName)) {
                efg.add(event);
                event = new EventBean();
            }
            tag = null;
        }

        public ArrayList<EventBean> getEfg() {
            return efg;
        }
    }
}

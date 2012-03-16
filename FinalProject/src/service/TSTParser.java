/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import domain.TCStepBean;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author GXW
 * 测试用例文件解析类
 */
public class TSTParser {

    /*******************************************************************************************
     * <b>function:</b> 用SAX对测试用例进行解析，得到测试用例中Step的集合
     * @param in 测试用例文件对应的File对象
     * @return 测试用例中Step的集合，每个Step属性用TCStepBean进行保存
     */
    public ArrayList<TCStepBean> getSteps(File in) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        TCStepHandler handler = new TCStepHandler();
        parser.parse(in, handler);
        return handler.getTc();
    }

    private class TCStepHandler extends DefaultHandler {

        private ArrayList<TCStepBean> tc;
        private TCStepBean step = new TCStepBean();
        private String tag = null;

        @Override
        public void startDocument() throws SAXException {
            tc = new ArrayList<TCStepBean>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            tag = qName;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (tag != null) {
                if ("EventId".equals(tag)) {
                    String data = new String(ch, start + 1, length - 1);
                    int id = Integer.parseInt(data);
                    step.setEventId(id);
                } else if ("ReachingStep".equals(tag)) {
                    String data = new String(ch, start, length);
                    boolean rs = Boolean.parseBoolean(tag);
                    step.setReachingStep(rs);
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("Step".equals(qName)) {
                tc.add(step);
                step = new TCStepBean();
            }
            tag = null;
        }

        public ArrayList<TCStepBean> getTc() {
            return tc;
        }
    }
}

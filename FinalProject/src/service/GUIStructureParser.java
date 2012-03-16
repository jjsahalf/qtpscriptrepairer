/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import domain.WidgetBean;
import domain.WindowBean;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author GXW
 * GUIStructure文件解析类
 */
public class GUIStructureParser {

    /*******************************************************************************************
     * <b>function:</b> 用SAX对GUIStructure文件进行解析，得到Widget的集合
     * @param in GUIStructure文件对应的File对象
     * @return GUIStructure中Widget的集合，每个Widget属性用WidgetBean进行保存
     */
    public ArrayList<WidgetBean> getWidgets(File in) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        GUIStructureHandler handler = new GUIStructureHandler();
        parser.parse(in, handler);
        return handler.getGui();
    }

    private class GUIStructureHandler extends DefaultHandler {

        private ArrayList<WidgetBean> gui;
        private ArrayList<String> parents;
        // private ArrayList<WindowBean> windows;
        private WidgetBean widget = new WidgetBean(false);
        //private WindowBean window = new WindowBean();
        private String tag1 = null;
        private String tag2 = null;
        private Boolean tag3 = false;
        private Boolean tag4 = false;
        private int bufContainer = 0;

        @Override
        public void startDocument() throws SAXException {
            gui = new ArrayList<WidgetBean>();
            parents = new ArrayList<String>();
            parents.add("Window");
        }

        @Override
        public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
            tag1 = qName;
            if ("Window".equals(qName)) {
                tag3 = false;
                widget.setIsWindow(true);
            } else if ("Container".equals(qName)) {
                tag4 = true;
                bufContainer++;
                //System.out.println(bufContainer);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (tag4 == false) {
                if (tag1 != null) {
                    if ("Name".equals(tag1)) {
                        tag2 = new String(ch, start, length);
                    } else if ("Value".equals(tag1) && tag3) {
                        if ("ID".equals(tag2)) {
                            widget.setwId(Integer.parseInt(new String(ch, start + 1, length - 1)));
                        } else if ("Class".equals(tag2)) {
                            widget.setwClass(new String(ch, start, length));
                        } else if ("Title".equals(tag2)) {
                            widget.setwTitle(new String(ch, start, length));
                        } else if ("Icon".equals(tag2)) {
                            widget.setwIcon(new String(ch, start, length - 4));
                        }
                    } else if ("Value".equals(tag1) && (!tag3)) {
                        if ("ID".equals(tag2)) {
                            widget.setwTitle(new String(ch, start, length));
                        } else if ("Rootwindow".equals(tag2)) {
                            widget.setRootWindow(Boolean.parseBoolean(new String(ch, start, length)));
                        }
                    }
                }
                widget.setParents(parents.get(parents.size()-1));
            } else {
                if (tag1 != null) {
                    if ("Name".equals(tag1)) {
                        tag2 = new String(ch, start, length);
                    } else if ("Value".equals(tag1) && tag3) {
                        if ("ID".equals(tag2)) {
                            widget.setwId(Integer.parseInt(new String(ch, start + 1, length - 1)));
                        } else if ("Class".equals(tag2)) {
                            widget.setwClass(new String(ch, start, length));
                        } else if ("Title".equals(tag2)) {
                            //System.out.println(new String(ch, start, length));
                            widget.setParents(parents.get(parents.size()-1));
                            while (bufContainer > 0) {
                                parents.add(new String(ch, start, length));
                                bufContainer--;
                            }
                           // for(String a:parents){
                             //   System.out.print(a+"::");
                            //}
                            //System.out.println();
                            widget.setwTitle(new String(ch, start, length));
                        } else if ("Icon".equals(tag2)) {
                            widget.setwIcon(new String(ch, start, length - 4));
                        }
                    } else if ("Value".equals(tag1) && (!tag3)) {
                        if ("ID".equals(tag2)) {
                            widget.setwTitle(new String(ch, start, length));
                        } else if ("Rootwindow".equals(tag2)) {
                            widget.setRootWindow(Boolean.parseBoolean(new String(ch, start, length)));
                        }
                    }
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("Attributes".equals(qName)) {
                gui.add(widget);
                widget = new WidgetBean(false);
                tag3 = true;
                tag4 = false;
                /*if (!tag3) {
                windows.add(window);
                window = new WindowBean();
                tag3 = true;
                } else {
                //  if (("javax.swing.JButton".equals(widget.getwClass())) || ("javax.swing.JRadioButtonMenuItem".equals(widget.getwClass())) || ("javax.swing.JMenu".equals(widget.getwClass())) || ("javax.swing.JMenuItem".equals(widget.getwClass())) || ("javax.swing.JRadioButton".equals(widget.getwClass()))) {
                gui.add(widget);
                widget = new WidgetBean(false);
                // } else {
                //     widget = new WidgetBean();
                //   }
                }*/
            } else if ("Property".equals(qName)) {
                tag2 = null;
            } else if ("Container".equals(qName)) {
                parents.remove(parents.size() - 1);
            }
            tag1 = null;
        }

        public ArrayList<WidgetBean> getGui() {
            return gui;
        }
    }
}

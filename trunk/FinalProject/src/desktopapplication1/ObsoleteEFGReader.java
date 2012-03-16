/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package desktopapplication1;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Yufei Jiang
 */
//这个类的目的是读取旧版本中的EFGxml文件的eid和wid的映射
//这个类的基本逻辑和CurrentEFGReader相同。
//可以直接参考那个类的注释
public class ObsoleteEFGReader extends DefaultHandler {

    public List<EidAndWid> list_of_eid_and_wid = new ArrayList();
    private StringBuffer buf;
    private String str;
    int counter;
    EidAndWid temp_eid_and_wid;

    public ObsoleteEFGReader(){
        counter=0;
    }

    @Override
     public void startDocument() throws SAXException{
        buf=new StringBuffer();
        System.out.println("*******开始解析文档*******");
    }

    @Override
    public void endDocument() throws SAXException{
        System.out.println("*******文档解析结束*******");
    }

    @Override
    public void startPrefixMapping( String prefix, String uri ){
        System.out.println(" 前缀映射: " + prefix +" 开始!"+ " 它的URI是:" + uri);
    }

    @Override
    public void endPrefixMapping( String prefix ){
        System.out.println(" 前缀映射: "+prefix+" 结束!");
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if(qName.equals("EventGraph")){
            Global.list_of_eid_and_wid=this.list_of_eid_and_wid;
            throw new SAXException("aaaaaaaaaaaaa");
        }
        if(qName.equals("EventId")){
            counter=1;
        }
        if(qName.equals("WidgetId")){
            counter=2;
        }
        for (int i = 0; i < atts.getLength(); i++) {
            System.out.println("元素名" + atts.getLocalName(i) + "属性值" + atts.getValue(i));
        }

    }

    @Override
    public void endElement(String namespaceURI, String localName, String fullName) throws SAXException {
        str = buf.toString();
        if(counter==1){
            temp_eid_and_wid=new EidAndWid();
            temp_eid_and_wid.Eid=str.trim();
        }
        if(counter==2){
            temp_eid_and_wid.Wid=str.trim();
            list_of_eid_and_wid.add(temp_eid_and_wid);
            counter=0;
        }
        buf.delete(0, buf.length());
    }

    @Override
    public void characters( char[] chars, int start, int length )throws SAXException{
        //将元素内容累加到StringBuffer中
        buf.append(chars,start,length);
    }
}

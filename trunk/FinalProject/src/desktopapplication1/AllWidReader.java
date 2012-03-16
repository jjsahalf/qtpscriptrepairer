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
//这个类的机制和AllNewWidReader是一样的。只不过这个类读的是旧的GUI xml文件
//每一个函数的作用可以参考AllNewWidReader
public class AllWidReader extends DefaultHandler{
    private StringBuffer buf;
    private String str;
    private List<String> list_of_all_wid=new ArrayList();
    boolean is_id_possible;
    boolean is_next_element_wid;
    boolean is_under_id;
    boolean is_wid_possible;

    public AllWidReader(){
        super();
        is_id_possible=false;
        is_next_element_wid=false;
        is_under_id=false;
    }

    @Override
     public void startDocument() throws SAXException{
        buf=new StringBuffer();
        //System.out.println("*******开始解析文档*******");
    }

    @Override
    public void endDocument() throws SAXException{
        Global.list_of_all_wid=this.list_of_all_wid;
        //System.out.println("*******文档解析结束*******");
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
     public void startElement(String namespaceURI,String localName,String qName,Attributes atts){
        //System.out.println("*******开始解析元素*******");
        if(qName.equals("Name")){
            is_id_possible=true;
        }
        
        if(is_under_id && qName.equals("Value")){
            is_wid_possible=true;
        }
        //System.out.println("元素名"+qName);
    }

    @Override
    public void endElement(String namespaceURI, String localName, String fullName) throws SAXException {
//        buf.trimToSize();
        str = buf.toString();
        if (is_id_possible) {
            if (str.trim().equals("ID")) {
                is_under_id = true;
                is_id_possible = false;
            }
        }

        if (is_wid_possible) {
            if (str.trim().length() >= 2) {
                if (str.trim().charAt(0) == 'w' && isInt(1, str.trim())) {
                    list_of_all_wid.add(str.trim());
                }
            }
        }

        buf.delete(0, buf.length());
    }
        
    public boolean isInt(int first_index,String str){
        String sub_str=str.substring(first_index);
        
        try {
            Integer.parseInt(sub_str);
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
        
        return true;
    }

    @Override
    public void characters( char[] chars, int start, int length )throws SAXException{
        //将元素内容累加到StringBuffer中
        buf.append(chars,start,length);
    }

}

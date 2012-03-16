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

//This class is to read all new wid, as the name of the class has showed.
//we use is class to read the wid in the new GUI xml. we will put it on a static list in Global class.
public class AllNewWidReader extends DefaultHandler{
    private StringBuffer buf;
    private String str;
    private List<String> list_of_all_wid=new ArrayList();
    boolean is_id_possible;
    boolean is_next_element_wid;
    boolean is_under_id;
    boolean is_wid_possible;

    public AllNewWidReader(){
        super();
        is_id_possible=false;
        is_next_element_wid=false;
        is_under_id=false;
    }

    @Override
     public void startDocument() throws SAXException{
        buf=new StringBuffer();
    }

    @Override
    public void endDocument() throws SAXException{
        Global.list_of_all_new_wid=this.list_of_all_wid;
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
        //if the qName equals Name, then the next elment may be the id.
        //we use is_id_possible as a flag, we make it true.
        //if is_id_possible is ture, then that is to say we are parsing the element under the id.
        if(qName.equals("Name")){
            is_id_possible=true;
        }

        //if the place we are parsing is under the id
        //and we encounter the "value"
        //that is to say the element may be wid.
        //then we set the is_wid_possible true.
        if(is_under_id && qName.equals("Value")){
            is_wid_possible=true;
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String fullName) throws SAXException {
        //如果is_id_possible是真，而且发现当中所含的内容是ID。
        //那么我们知道应该开始新一轮的ID搜索过程，is_under_id置为真
        //而同时把is_id_possible已经被消费，我们把它恢复到原始状态，设置为false
        str = buf.toString();
        if (is_id_possible) {
            if (str.trim().equals("ID")) {
                is_under_id = true;
                is_id_possible = false;
            }
        }

        //如果is_wid_possible为真，说明下一个元素有可能是wid
        //接下来我们对我们读到的内容进行字符串匹配。
        //如果第一位为w同时以一个整形数字结束，那么我们就认为它是wid。并且把它加入到列表里
        if (is_wid_possible) {
            if (str.trim().length() >= 2) {
                if (str.trim().charAt(0) == 'w' && isInt(1, str.trim())) {
                    list_of_all_wid.add(str.trim());
                }
            }
        }

        buf.delete(0, buf.length());
    }

    //这个函数用来判断字符串的最后一位是否为一个数字
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

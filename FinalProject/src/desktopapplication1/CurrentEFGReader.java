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
//这个类用来读取现在新的EFG xml 使用数据类型WidAndEid来将一组对应的Wid和Eid放在这个数据类型中
//而再把这个数据类型放入由WidAndEid组成的链表当中，供后面用来查找
//这个查找过程是这样的，其他类会找到我们需要的wid，然后把这里读到的WidAndEid链表来当做字典查找正确的Eid
public class CurrentEFGReader extends DefaultHandler{
    public List<EidAndWid> list_of_eid_and_wid = new ArrayList();
    private StringBuffer buf;
    private String str;
    int counter;
    EidAndWid temp_eid_and_wid;

    public CurrentEFGReader(){
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

        //因为EFGxml文件包括两个部分，第一部分是Eid和Wid的映射，第二部分是事件流图，
        //我们需要的信息是第一部分，所以第二部分一旦开始就应该停止解析
        //而第二部分开始的标识是出现EventGraph
        //我们遇到这个字符就通过抛出异常的方法来停止解析
        if(qName.equals("EventGraph")){
            Global.current_list_of_eid_and_wid=this.list_of_eid_and_wid;
            throw new SAXException("aaaaaaaaaaaaa");
        }
        //counter实际上是一个flag，
        //我们这里生产的数字会在结束元素的方法中使用
        //当遇到EventID字符时，我们让counter=1
       //这预示着我们当遇到这个字符以后接下来就会遇到Eid
        if(qName.equals("EventId")){
            counter=1;
        }

       //当遇到WidgetID字符时，我们让counter=2
       //这预示着我们当遇到这个字符以后接下来就会遇到Wid
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

        //这里消费我们之前生成的counter值
        //如果counter为1，那么我们知道现在读到的是Eid，
        //我们把它放在temp_eid_and_wid.Eid中
        if(counter==1){
            temp_eid_and_wid=new EidAndWid();
            temp_eid_and_wid.Eid=str.trim();
        }
       //如果counter为2，那么我们知道现在读到的是Wid，
        //我们把它放在temp_eid_and_wid.Wid中
        //并且把这个temp_eid_and_wid放到链表中
        //并且把counter重置成0
        //为下一轮解析做准备
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

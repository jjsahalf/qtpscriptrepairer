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
//在对脚本的事件序列做修复时，我们实际上面临了两步，
//第一步是先检查旧的事件序列的中是否有事件不存在
//如果不存在我们将其标识为e-1
//第二步我们才检查是否有已经不存在的follow关系
//而恰恰在第一步中，我们不能简单的用wid与eid的映射来判断
//因为有很多的事件被删除了，所以变化的不仅有eid
//还有wid，所以我们wid此时此刻便不再是一个控件的主键，因为它已不具有唯一性
//我们于是构造了一个数据结构
//它包含新旧版本中控件的wid与eid的表示
//这个想法类似于数据库中用多个字段来共同构成一个主键
//这样我们就可以在检查一个事件是否存在时有了依赖的对象
//这个类就是根据QTP脚本的信息来查找出一个控件在Guitar中的两个恒定信息：class 和title/icon
//并据此在之后的处理中进一步将其丰富，包括新旧的eid，wid
//并将其放入链表中。
public class WidgetsLocator extends DefaultHandler {

    private StringBuffer buf;
    private String str;
    List<String> list_of_wid;
    boolean is_under_wid;
    boolean flag_for_value_of_title;
    boolean flag_for_value_of_class;
    boolean does_need_icon;
    boolean flag_for_value_of_icon;



    String class_name;
    String title_or_icon;
    String wid;

    public WidgetsLocator() {
        list_of_wid = Global.list_of_wid;
        is_under_wid = false;
        flag_for_value_of_class = false;
        flag_for_value_of_title =false;
        does_need_icon=false;
        flag_for_value_of_icon=false;


    }

    @Override
    public void startDocument() throws SAXException {
        buf = new StringBuffer();
        System.out.println("*******开始CheckGUI*******");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("*******GUICheck结束*******");
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) {
        System.out.println(" 前缀映射: " + prefix + " 开始!" + " 它的URI是:" + uri);
    }

    @Override
    public void endPrefixMapping(String prefix) {
        System.out.println(" 前缀映射: " + prefix + " 结束!");
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
 
    }

    @Override
    public void endElement(String namespaceURI, String localName, String fullName) throws SAXException {

        str = buf.toString();
        if (is_under_wid == false) {
            for (String current_wid : list_of_wid) {
                if (str.trim().equals(current_wid)) {
                    is_under_wid = true;
                    wid=current_wid;
                    break;
                }
            }
        }


        if(flag_for_value_of_class){
            flag_for_value_of_class=false;
            class_name=str.trim();
        }

        if(flag_for_value_of_title){
            flag_for_value_of_title=false;
            if(str.trim() == null || "".equals(str.trim())){
                does_need_icon=true;
            }else{
                title_or_icon=str.trim();
                //关键：这里将一个已构造好的新的GuitarClassAndTitle数据类型加入到链表中。
                Global.list_of_widgets.add(new GuitarClassAndTitle(class_name,title_or_icon,wid));
                is_under_wid=false;
            }
        }

        if(flag_for_value_of_icon){
            flag_for_value_of_icon=false;
            does_need_icon=false;

            title_or_icon=str.trim();
            //关键：这里将一个已构造好的新的GuitarClassAndTitle数据类型加入到链表中。
            Global.list_of_widgets.add(new GuitarClassAndTitle(class_name,title_or_icon,wid));
            is_under_wid=false;

        }

        if(does_need_icon){
            if(str.trim().equals("Icon")){
                flag_for_value_of_icon=true;
            }
        }

        if (is_under_wid && str.trim().equals("Class")) {
            flag_for_value_of_class = true;
        }

        if(is_under_wid && str.trim().equals("Title")){
            flag_for_value_of_title=true;
        }



        if (flag_for_value_of_class) {
            if (is_under_wid == true) {
            }
        }
        buf.delete(0, buf.length());
    }

    @Override
    public void characters(char[] chars, int start, int length) throws SAXException {
        //将元素内容累加到StringBuffer中
        buf.append(chars, start, length);
    }

}

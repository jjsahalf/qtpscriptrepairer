/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package desktopapplication1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Yufei Jiang
 */
//这个类和WidgetsLocator有很大的关系，是WidgetsLocator类的下游
//背景信息在WidgetsLocator类中已有介绍
//WidgetsLocator为所有旧版本中的控件用GuitarClassAndTitle数据类型做了封装
//但是WidgetsLocator这个类只完成了这项工作的一部分，
//它只是为所有的控件生成了一个相应的数据类型对象，并且仅仅对Class，title和old wid三个字段做了填写
//而其中的new_wid,new_eid未赋值
//而这个类就是来进行这项工作的。
//他所依据的主要信息时新版本的GUIxml
public class WidAndEidFinder extends DefaultHandler{
    private StringBuffer buf;
    private String str;
    boolean is_under_wid;
    boolean flag_for_value_of_class;
    boolean does_need_check_title;
    boolean flag_for_value_of_title;
    boolean does_need_icon;
    boolean flag_for_value_of_icon;
    String file_name_of_new_guitar_gui_file;
    String current_wid;
    int under_wid_counter_out;
    List<GuitarClassAndTitle> list_of_same_right_class;
    //接下来要做的是进新的gui.xml把所有的新的wid都找出来，
    //然后再有一个类把所有的新wid转为新的eid

    public WidAndEidFinder(String file_name_of_new_guitar_gui_file){
        is_under_wid = false;
        flag_for_value_of_class = false;
        does_need_check_title=false;
        flag_for_value_of_title=false;
        does_need_icon=false;
        this.file_name_of_new_guitar_gui_file=file_name_of_new_guitar_gui_file;
        //current_wid=new String();
    }

    public void findAllWidForNewGuitarGuiXML(){
        SAXParserFactory sf4;
        SAXParser sp4;
        AllNewWidReader all_new_wid_reader=new AllNewWidReader();
        try {
            sf4= SAXParserFactory.newInstance();
            sp4= sf4.newSAXParser();

            sp4.parse(new InputSource(this.file_name_of_new_guitar_gui_file), all_new_wid_reader);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     @Override
    public void startDocument() throws SAXException {
        buf = new StringBuffer();
        this.findAllWidForNewGuitarGuiXML();
        this.list_of_same_right_class=new ArrayList();
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

    //在这里的解析中，我们实际上是在利用一个控件在旧GUIxml中的信息来判断出这个控件在新GUIxml
    //中的wid是什么。换句话说，这个控件在旧的GUIxml中的wid此时对我们来说帮不上什么忙
    //我们可以依靠的只有这个控件的classname和title
    //又显而易见，有许多titile重名的控件，我们显见要对其进行classname和title的双重检查才可以最终确定一个控件
    //下面的过程就是利用各种布尔的标记值来进行双重判断的过程
    @Override
    public void endElement(String namespaceURI, String localName, String fullName) throws SAXException {
        str = buf.toString();
        //从循环头中可以看到，我们是对list_of_all_new_wid进行遍历
        //对每一个wid都进行双重检查
        //第一步便是确认我们的解析流已经遇到了一个wid
        //遇到之后的标识便是令is_under_wid为真
        if (str.trim().length() >= 2) {
            if (is_under_wid == false) {
                for (String wid : Global.list_of_all_new_wid) {
                    if (str.trim().equals(wid)) {
                        is_under_wid = true;
                        under_wid_counter_out = 17;//下11层到icon
                        current_wid = wid;//总是手握当前wid，一旦找到就可以停下来，那么我们就有了正确的wid
                        break;
                    }
                }
            }
        }

        //得到了这个控件的title，那么可以定位到一个具体的控件
        //完成了对控件数据结构的空缺字段的填写
        //唯一要注意的是有能这个控件没有title
        //那么我们就读取它的icon
        //其机制和标记值的设置方法与title的解析方法相同
        if (flag_for_value_of_title) {
            flag_for_value_of_title = false;
            if (str.trim() == null || "".equals(str.trim())) {
                does_need_icon = true;
            } else {
                for (GuitarClassAndTitle guitar_class_and_title : list_of_same_right_class) {
                    if (guitar_class_and_title.title_or_icon.equals(str.trim())) {
                        if (Global.list_of_widgets.get(Global.list_of_widgets.indexOf(guitar_class_and_title)).new_wid.equals("w-1")) {
                            Global.list_of_widgets.get(Global.list_of_widgets.indexOf(guitar_class_and_title)).new_wid = current_wid;
                            this.list_of_same_right_class=new ArrayList();
                            break;
                        }
                    }
                }
            }
        }

        //如果需要检查title
        //那么先看到title标识出现
        //标识flag_for_value_of_title为真
        //为下一轮解析title做准备
        if (does_need_check_title) {
            if (str.trim().equals("Title")) {
                does_need_check_title=false;
                flag_for_value_of_title = true;
            }
        }

        //这一步读入class，并且判断它是否需要进一步判断其title或icon
        //如果是，设置does_need_check_title为真
        if(flag_for_value_of_class){
            flag_for_value_of_class=false;
            for(GuitarClassAndTitle guitar_c_a_t:Global.list_of_widgets){
                if(guitar_c_a_t.class_name.equals(str.trim())&& guitar_c_a_t.new_wid.equals("w-1")){
                    does_need_check_title=true;
                    list_of_same_right_class.add(guitar_c_a_t);
                }
            }
        }




        if(flag_for_value_of_icon){
            flag_for_value_of_icon=false;
            for (GuitarClassAndTitle guitar_class_and_title : list_of_same_right_class) {
                if (guitar_class_and_title.title_or_icon.equals(str.trim())) {
                    if (Global.list_of_widgets.get(Global.list_of_widgets.indexOf(guitar_class_and_title)).new_wid.equals("w-1")) {
                        Global.list_of_widgets.get(Global.list_of_widgets.indexOf(guitar_class_and_title)).new_wid = current_wid;
                        this.list_of_same_right_class=new ArrayList();
                        break;
                    }
                }
            }
        }

        if(does_need_icon){
            if(str.trim().equals("Icon")){
                does_need_icon=false;
                flag_for_value_of_icon=true;
            }
        }

        //如果我们正在对一个wid的子xml树进行解析，且遇到了class标识
        //那么我们知道下一个解析的字符是这个控件所属的类型
        //应该在下一轮解析中读取这个字符
        //具体来说就是设置flag_for_value_of_class为真
        if (is_under_wid && str.trim().equals("Class")) {
            flag_for_value_of_class = true;
        }



        under_wid_counter_out--;

        if (under_wid_counter_out == 0) {
            is_under_wid = false;
            current_wid = null;
            does_need_icon = false;
        }
        buf.delete(0, buf.length());

    }

    @Override
    public void characters(char[] chars, int start, int length) throws SAXException {
        //将元素内容累加到StringBuffer中
        buf.append(chars, start, length);
    }
}

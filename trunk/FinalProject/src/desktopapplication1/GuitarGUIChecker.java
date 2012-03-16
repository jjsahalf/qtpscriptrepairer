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
//这个类是整个系统中逻辑较为复杂的一个类，做了许多细节的处理
//但是目的是比较明确的，类的名字直译过来就是Guitar的GUIxml文件中的wid的查找器
//这个类就是把我们从QTP源脚本中解析出来的控件关键字在GUIxml中进行查找它所对应的wid
public class GuitarGUIChecker extends DefaultHandler{

    private StringBuffer buf;
    private String str;
    boolean flag_of_key_word[];//2实际上是keyword个数减一，(Notepad,File.New),
    boolean flag_for_value_of_class_or_icon;
    boolean flag_for_value_of_title;
    boolean flag_for_value_of_icon;
    boolean does_need_icon;
    boolean is_ID;
    boolean is_name;
    boolean is_under_wid;
    boolean is_window_attributes;
    boolean is_window_id;
    int under_wid_counter_out;

    String current_wid;
    //int depth;
    List<String> list_of_key_word=new ArrayList();
    int number_of_new;
    int depth[];//2实际上是keyword个数减一，(Notepad,File,New)
    int cursor_of_key_word=0;
    List<String> list_of_wid;



    public GuitarGUIChecker(){
        super();
        depth=new int[Global.list_of_current_one_line_title.size()-1];//2实际上是keyword个数减一，(Notepad,File,New)
        flag_of_key_word=new boolean[Global.list_of_current_one_line_title.size()-1];//2实际上是keyword个数减一，(Notepad,File.New)
        for(int i=0;i<flag_of_key_word.length;i++){
            System.out.println(flag_of_key_word.length);
            flag_of_key_word[i]=false;
        }

        for(int i=0; i<depth.length;i++){
            depth[i]=0;
        }


        //这里有大量的flag，在下面有大量的逻辑要用到
        flag_for_value_of_class_or_icon=false;
        is_ID=false;
        is_name=false;
        is_under_wid=false;
        number_of_new=0;
        is_window_attributes=false;
        is_window_id=false;
        list_of_wid=new ArrayList();
        flag_for_value_of_title=false;
        flag_for_value_of_icon=false;
        does_need_icon=false;
        under_wid_counter_out=5;
       
    }





    @Override
     public void startDocument() throws SAXException{
        buf=new StringBuffer();
        System.out.println("*******开始CheckGUI*******");
    }

    @Override
    public void endDocument() throws SAXException{
        System.out.println("*******GUICheck结束*******");
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
        //如果我们在解析window属性的过程中，遇到了value值
        //我们就知道接下来一个属性值是window的id
        //置is_window_id为真
        //让下面的解析进行读取
        if(is_window_attributes && qName.equals("Value")){
            is_window_id=true;
        }

        //如果我们在解析流中遇到了Window字符，那么我们接下来就会遇到一些Window的相关属性
        //所以我们将is_window_attributes设置为true
        //让接下来的解析能够知道我们目前正在解析window的相关属性
        if(qName.equals("Window")){
            //System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
            is_window_attributes=true;
        }
        
        for(int i=0;i<list_of_key_word.size()-1;i++){
            if(flag_of_key_word[i]){
                depth[i]++;
            }
        }

    }

    @Override
     public void endElement(String namespaceURI,String localName,String fullName )throws SAXException{

        str = buf.toString();

    //因为在QTP的容器中控件树是单根的，以notepad为例，一定是从notepad开始
    //一个弹出的dialog的QTP表示依然是从notepad开始
    //notepad->dialog window->button
    //但是在Guitar中，其容器/控件树是多根的，即有好几个棵树，
    //每个window有属性rootWindow来标识其是否是跟窗口
    //但在xml层次结构上，一个弹出窗口和根窗口是平等的
    //这也成为转换时的一个障碍。
    //我们解决这个不同通过了两个步骤
    //1.如果在QTP脚本中，其语句出现了一个弹出窗口（notepad->dialog window->button）
    //我们删除掉第一个notepad，使其变成dialog window-》button的层次结构
    //这样，它的根就变成了这个具体的dialog window
    //使预处理后的QTP脚本变成多根的、
    //这一部分可以参见WidAndEidFinder的deleteDumbWindow方法
    //我们这里就是直接根据window的id来进入深度搜索。
    //比如我们的一个button是在弹出窗口名为pop中的，这个程序的rootWindow是notepad
    //那么我们当看见notepad这个id时，直接跳过notepad子树中的所有内容进入下一棵
    //树进行匹配
    //如果是pop窗口，那么我们就进入继续寻找
        if (is_window_id) {
            if(str.trim().equals(list_of_key_word.get(cursor_of_key_word))){

                System.out.println("找到了Window");
                System.out.println(str.trim());

                depth[0]=5;
                flag_of_key_word[cursor_of_key_word]=true;
                cursor_of_key_word++;
                
                is_window_id=false;
                is_window_attributes=false;
                
            }
        }



        //接下来的思路其实与上面类似
        //先在高层次上进行匹配以节约时间
        //比如对于notepad-》menu file-》menu item new
        //我们在进入notepad后，先匹配menu file层次上的xml子结构
        //因为经常会出现某一层次同名的情况
        //但因为xml是天然的树结构，我们可以使用一个计数器counter来计算我们深入了多少层
        //当进入一个层次时，我们立刻建立这个计数器
        //每开始一个xml标签，counter加一，遇到一个xml结束标签时减一
        //这样在这个层次（menu file）开始后，counter会一直大于0（进入到更深的层次结构）
        //这样我们此时就可以寻找menu item new
        //而当counter归0，证明我们已经遇到了menu file的xml结束标签
        //如果此时依然没有找到menu item new
        //那就说明这个menu file中不含这个菜单项
        //程序中一定是有其他同名的菜单
        //而当counter归零后，我们的寻找目标向上跳一级，重新回到menu file层次
        //继续寻找menu file
        if (flag_of_key_word[0]) {
            //手握wid的部分
            if (str.trim().length() >= 2) {
                if (is_under_wid == false) {
                    for (String wid : Global.list_of_all_wid) {
                        if (str.trim().equals(wid)) {
                            //System.out.println(str.trim());
                            is_under_wid = true;
                            under_wid_counter_out = 17;//下11层到icon
                            current_wid = wid;//总是手握当前wid，一旦找到就可以停下来，那么我们就有了正确的wid
                            break;
                        }
                    }
                }
            }

            //具体查找的部分
            //由于流解析时可利用的函数主要就两个
            //一个在标签开始时触发事件
            //一个在标签结束时触发事件
            //并且SAX流解析的特质使解析一个元素时，不知道在解析这个元素前发生了什么
            //我们为了让这仅有的两个函数在不同的时候做出不同的动作
            //我们通过设置各种布尔标记值使得上一次解析的有用信息可以传递到下一次
            //并且可以使方法根据布尔标记值的不同表现出不同的行为
            //具体机制更详细的说明可以参考WidAndEidFinder中的具体介绍
            if (flag_for_value_of_class_or_icon || flag_for_value_of_title) {
                if (flag_for_value_of_title) {
                    if (str.trim() == null || "".equals(str.trim())) {
                        does_need_icon = true;
                    }
                }

                if (str.trim().equals(list_of_key_word.get(cursor_of_key_word))
                        || str.trim().endsWith(list_of_key_word.get(cursor_of_key_word))
                        || (does_need_icon && str.trim().startsWith(list_of_key_word.get(cursor_of_key_word)))) {

                    if (!(current_wid == null)) {
                        list_of_wid.add(current_wid);
                    }

                    if (cursor_of_key_word < list_of_key_word.size() - 1) {
                        flag_of_key_word[cursor_of_key_word] = true;
                        depth[cursor_of_key_word] = 4;
                        cursor_of_key_word++;

                    } else {
                        //解析结束
                        for(String str:list_of_wid){
                            Global.list_of_wid.add(str);
                        }
                       
                        throw new SAXException("stop of parsing");
                    }
                }


                if (flag_for_value_of_class_or_icon) {
                    flag_for_value_of_class_or_icon = false;
                }

                if (flag_for_value_of_title) {
                    flag_for_value_of_title = false;
                }


            }
            if (is_under_wid && (str.trim().equals("Class") || str.trim().equals("Icon"))) {
                //System.out.println("找到了title，flag_for_value_of_title已被置为真");
                flag_for_value_of_class_or_icon = true;
                //System.out.println(str);
            }
            if (str.trim().equals("Title")) {
                flag_for_value_of_title = true;
            }

            //深度增减的部分
            for (int i = 0; i < list_of_key_word.size() - 1; i++) {
                if (flag_of_key_word[i]) {
                    depth[i]--;
                    if (depth[i] == 0) {
                        System.out.println("should not always enter this section");
                        flag_of_key_word[i] = false;
                        cursor_of_key_word--;
                    }
                }
            }

            under_wid_counter_out--;

            if (under_wid_counter_out == 0) {
                is_under_wid = false;
                current_wid = null;
                does_need_icon = false;
            }
        }

        buf.delete(0, buf.length());

    }

    @Override
    public void characters( char[] chars, int start, int length )throws SAXException{
        //将元素内容累加到StringBuffer中
        buf.append(chars,start,length);
    }




}

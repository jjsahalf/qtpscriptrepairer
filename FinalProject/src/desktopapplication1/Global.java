/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package desktopapplication1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yufei Jiang
 */
//这里是Global类，是全局都要消费都变量，主要是各种读好的链表，它们以都是static
//这样我们不创建Global类的实例就可以使用这些变量，保证了这些变量的唯一性
public class Global {
    public static List<ArrayList> key_word_of_many_lines=new ArrayList();//所有的QTP脚本的所有关键词，它是list的list，它的元素中的list是每一行的关键词
    public static List<EidAndWid> list_of_eid_and_wid = new ArrayList();//一个列表中存放了所有的EFG文件中的wid和eid映射
    public static List<String> list_of_wid=new ArrayList();//这里面是所有的找到的目标wid
    public static List<String> list_of_all_wid=new ArrayList(); //这些就是所有在旧版本GuitarGUI文件中出现过的wid的总列表。
    public static List<String> list_of_all_new_wid=new ArrayList();//这些是所有在新版本的GuitarGUI文件中出现过的wid的总列表
    public static List<String> list_of_eid_for_writing=new ArrayList();//存放准备写入最后的事件序列的所有Eid
    public static List<String> list_of_current_one_line_title=new ArrayList();//现在QTP脚本中一行的脚本中所含的所有的title(控件名)
    public static List<GuitarClassAndTitle> list_of_widgets=new ArrayList();//储存找到的widgets的classname和title或icon
    public static String original_space=new String();//存放所有待修复的QTP工程的文件夹全路径
    public static String target_space=new String();//存放新的QTP工程的文件夹全路径
    public static List<String> list_of_obsolete_scripts=new ArrayList();//所有待修复qtp脚本的路径
    public static String obsolete_gui=new String();//旧的GUI文件的文件全路径
    public static String obsolete_efg=new String();//旧的EFG文件的文件全路径
    public static String new_gui=new String();//新的GUI文件的文件全路径
    public static String new_efg=new String();//新的EFG文件的文件全路径
    public static List<EidAndWid> current_list_of_eid_and_wid=new ArrayList();//现在新的EFGxml中所有的
    //Eid和Wid的映射，以一个链表存放
    public static List<String> list_of_new_wid=new ArrayList();//转换后的wid
}

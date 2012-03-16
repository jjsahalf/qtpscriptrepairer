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
//这个方法的作用是利用之前已经解析好的QTP脚本信息
//通过重复调用GuitarGUIChecker这个类将每一个控件的QTP信息转化为Guitar中的wid
//具体来说，我们之前有1个链表存放了QTP脚本的所有解析好的数据，并用一个数据结构封装好一个控件的信息
//我们这个类便是用这个链表作为数据源
//每读取一个控件在QTP的信息表示，就调用一次GuitarGUIChecker，得到一个wid
//整个循环结束，我们自然就得到了一个QTP脚本转化成的wid序列
public class WidSearcher {

    private StringBuffer buf;
    private String str;
    boolean flag_of_key_word[] = new boolean[3];
    boolean flag_for_value_of_title;
    boolean is_ID;
    boolean is_name;
    boolean is_under_wid;
    public List<EidAndWid> list_of_eid_and_wid = new ArrayList();
    int depth;
    int number_of_new;

    public WidSearcher(){
        super();
        for (int i = 0; i < flag_of_key_word.length; i++) {
            System.err.println(flag_of_key_word.length);
            flag_of_key_word[i] = false;
        }
        flag_for_value_of_title = false;
        is_ID = false;
        is_name = false;
        is_under_wid = false;
        depth = 0;

        number_of_new = 0;
    }

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
    //使预处理后的QTP脚本变成多根的
    //这个函数就是干的这件事
    //这个函数以后有扩展的必要。因为子窗口不只有javadialog
    //如果发现了新的可以直接在我们的if代码中增加新的类型
        public void deleteDumbWindow(List<QTPClassAndTitle> list) { 
        int i,j;
        try {
            for (i = 0; i < list.size(); i++) {
                if (!(list.get(i).type_of_class==null)) {
                    if (list.get(i).type_of_class.equals("JavaDialog")) {
                        for (j = 0; j < i; j++) {
                            list.remove(j);
                        }
                        break;
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("break point");
        }

    }

    public void searchWid() {
        int number_of_qtp_sentence = Global.key_word_of_many_lines.size();
        List<QTPClassAndTitle> key_word_of_one_line;
         //这里对每一个QTP语句进行一次循环，但这里只进行了语句数减1次，进行这里减一是因为最后一句是关掉程序
        //所以最后一句不用解析。
        for (int i = 0; i < number_of_qtp_sentence - 1; i++) {
            key_word_of_one_line = readOneLine(i);

            this.deleteDumbWindow(key_word_of_one_line);


            List<String> title_of_one_line = new ArrayList();
            for (QTPClassAndTitle qtp_c_a_t : key_word_of_one_line) {
                title_of_one_line.add(qtp_c_a_t.title_in_qtp);
            }

            Global.list_of_current_one_line_title = title_of_one_line;
            GuitarGUIChecker guitar_gui_checker = new GuitarGUIChecker();
            guitar_gui_checker.list_of_key_word = title_of_one_line;

            SAXParserFactory sf_wid_searcher;
            SAXParser sp_wid_searcher;

            try {
                sf_wid_searcher = SAXParserFactory.newInstance();
                sp_wid_searcher = sf_wid_searcher.newSAXParser();

                sp_wid_searcher.parse(new InputSource(Global.obsolete_gui), guitar_gui_checker);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //先确定定位window，直接关键字定位

        }

    }

    public List<QTPClassAndTitle> readOneLine(int index_of_line) {
        return Global.key_word_of_many_lines.get(index_of_line);
    }



}

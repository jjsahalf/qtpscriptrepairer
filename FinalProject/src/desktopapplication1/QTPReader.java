/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package desktopapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 *
 * @author Yufei Jiang
 */
//这个类是QTP脚本读取器，
//这个类把QTP脚本进行解析，并且把必要的元素放在相应的链表中。
public class QTPReader {
        /**
     * @param args the command line arguments
     */
    private List<String> line_list;//这个
    private List<QTPClassAndTitle> key_word_list;//这个list中每个元素是一个自定义数据类型，每个数据类型存储了一个控件
    //在QTP的表示信息，包括QTP中的控件类型和QTP中的控件名
    private StringBuffer buf_of_class;
    List<ArrayList> key_word_of_many_lines=new ArrayList();//这个list的每一个元素是一个list，每一个list是一行的QTP
    //关键词(控件类型和控件名)，而这些list的list就当然的成为了所有行的QTP关键词


    public QTPReader(){
        line_list=new ArrayList();
        buf_of_class=new StringBuffer();
    }

    //把一个QTP脚本的脚本语言逐行读入。
    //用一个list来存储一个QTP脚本的全部内容，
    //这个list的每一个元素是一个String
    //每一个String存储了QTP脚本的一行
    public void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                line_list.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    //这一个方法多次调用findKeyWordForOneLine，把这个子方法得到的一行的QTP脚本中的title和class的list
    //放在一个以list为元素的list里
    //这样就得到了一个QTP脚本中的所有有效信息，并且把他们以易识别易查找的方法进行存储。
    public void findKeyWordForManyLines(){
        
        for(String line : line_list){
            ArrayList<QTPClassAndTitle> key_word_of_one_line=findKeyWordForOneLine(line);
            //这里就是把刚刚得到的一行的title和class的list放入list的list中。
            key_word_of_many_lines.add(key_word_of_one_line);
            for(QTPClassAndTitle c_a_t: key_word_of_one_line){
                System.out.println(c_a_t.type_of_class);
                System.out.println(c_a_t.title_in_qtp);

            }
            System.out.println("一行结束");
        }
        //最终赋值给Global类中的静态变量，方便以后使用
        Global.key_word_of_many_lines=this.key_word_of_many_lines;


    }

    //这个方法逐行解析QTP脚本，把每一行的解析结果都放在一个list里
    public ArrayList findKeyWordForOneLine(String line){  //这里的line就是脚本语言的一句
        ArrayList<QTPClassAndTitle> key_word_of_one_line = new ArrayList();
        int number_of_quotation = 0;
        String key_word = "";
        boolean is_in_class=true;
        int cursor_of_class_and_title=0;

        key_word_of_one_line.add(new QTPClassAndTitle());

        //下面的内容看似内容比较大，但是实际上都做的是字符串处理的工作
        //我们通过识别引号，左右括号和@符号，来把QTP脚本每一行的内容切割成一个个词，并且根据
        //其所在位置决定它是控件的classname还是title还是应该停止解析

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '"') {
                number_of_quotation++;
                if (number_of_quotation % 2 == 0) {
                    key_word_of_one_line.get(cursor_of_class_and_title).title_in_qtp=key_word;
                    key_word="";
                    key_word_of_one_line.add(new QTPClassAndTitle());
                    cursor_of_class_and_title++;
                }else{
                    while(line.charAt(i+1)!='"'){
                        key_word+=line.charAt(i+1);
                        i++;
                    }
                }
            }
            if(line.charAt(i)=='@'){
                key_word_of_one_line.remove(key_word_of_one_line.size()-1);
                break;
            }
        }

        //int number_of_class_and_title=key_word_of_one_line.size();
        cursor_of_class_and_title=0;

        for (int i = 0; i < line.length(); i++) {

            if (line.charAt(i) == '(') {
                is_in_class = false;
                String type_of_class = buf_of_class.toString();

                key_word_of_one_line.get(cursor_of_class_and_title).type_of_class = type_of_class;
                buf_of_class.delete(0, buf_of_class.length());
                cursor_of_class_and_title++;
            }
            if (is_in_class) {
                buf_of_class.append(line.charAt(i));
            }
            if (line.charAt(i) == '.') {
                is_in_class = true;
            }
            if(line.charAt(i)=='@'){
                buf_of_class.delete(0, buf_of_class.length());
                break;
            }
        }
        return key_word_of_one_line;
    }
}

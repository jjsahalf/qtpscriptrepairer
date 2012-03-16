/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package desktopapplication1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yufei Jiang
 */
//正如其名字所显示的那样，这个类用来把已经生成好的事件序列按照Guitar事件序列的格式写到文件中
public class GuitarEventWriter {

    private String step;
    private String end_step;
    private String event_id;
    private String end_event_id;
    private String reaching_step;
    private String xml_title;
    private String start_tag;
    private String end_tag;
    private List<String> list_of_eid_for_writing;


    public GuitarEventWriter(){
        //这里面把一定要写入的xml格式固定元素先用一些字符串存储起来
        step = "    <Step>\n";
        end_step = "    </Step>\n";
        event_id = "        <EventId>";
        end_event_id = "</EventId>\n";
        reaching_step = "        <ReachingStep>false</ReachingStep>\n";
        xml_title="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
        start_tag="<TestCase>\n";
        end_tag="</TestCase>\n";




    }
    //把我们手中的wid序列根据Global.list_of_eid_and_wid中的内容映射成eid的序列，为写入做最后准备
    public void mapper(){
        for (String wid : Global.list_of_wid) {
            for (EidAndWid eid_and_wid : Global.list_of_eid_and_wid) {//如果旧的wid有对应eid
                if (wid.equals(eid_and_wid.Wid)) {
                    for (GuitarClassAndTitle guitar_c_a_t : Global.list_of_widgets) {//那就把widgets中的旧eid修改掉
                        if (wid.equals(guitar_c_a_t.old_wid)) {
                            guitar_c_a_t.old_eid = eid_and_wid.Eid;
                        }
                    }
                }
            }
        }

        for (GuitarClassAndTitle guitar_c_a_t : Global.list_of_widgets) {
            for (EidAndWid eid_and_wid : Global.current_list_of_eid_and_wid) {
                if(guitar_c_a_t.new_wid.equals(eid_and_wid.Wid)){
                    guitar_c_a_t.new_eid=eid_and_wid.Eid;
                }
            }
        }//已经有了old_wid,old_eid,new_eid
        //用old_wid进去找即可


        for(String wid:Global.list_of_wid){
            for(GuitarClassAndTitle guitar_c_a_t:Global.list_of_widgets){
                if(wid.equals(guitar_c_a_t.old_wid) && !(guitar_c_a_t.old_eid.equals("e-1"))){
                    //Global.list_of_new_wid.add(guitar_c_a_t.new_wid);
                    Global.list_of_eid_for_writing.add(guitar_c_a_t.new_eid);
                }
            }
        }
    }



    //这个方法很简单 ，把eid序列按Guitar格式写到一个新的xml文件里
    public void writer(){
        this.mapper();
        File file = new File("src\\InPool\\t1.xml");
        BufferedWriter bw;


        try {
            bw = new BufferedWriter(new FileWriter(file, false)); //此处true为追加
            bw.write(xml_title);
            bw.write(start_tag);
            for(String eid:Global.list_of_eid_for_writing){
                bw.write(step);
                bw.write(event_id);
                bw.write(eid);
                bw.write(end_event_id);
                bw.write(reaching_step);
                bw.write(end_step);
            }
            bw.write(end_tag);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(GuitarEventWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

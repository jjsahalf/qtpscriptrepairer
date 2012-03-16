/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import domain.EventBean;
import domain.ScriptBean;
import domain.TCStepBean;
import domain.WidgetBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author GXW
 * 工具类
 */
public class Utils {

    /*******************************************************************************************
     * <b>function:</b> Test Case ====> EFG  找出Eid对应的Wid
     * @param tc 测试用例中抓取的Step集合，每个Step属性用TCStepBean保存
     * @param efg EFG中抓取的事件集合，每个事件属性用EventBean保存
     * @return tc中每个Step对应的事件对象的集合，事件对象属性用EventBean保存
     */
    public static ArrayList<EventBean> searchTCInEFG(ArrayList<TCStepBean> tc, ArrayList<EventBean> efg) {

        ArrayList<EventBean> result = new ArrayList<EventBean>();
        int tag;

        for (int i = 0; i < tc.size(); i++) {
            tag = tc.get(i).getEventId();
            for (int j = 0; j < efg.size(); j++) {
                if (efg.get(j).getEventId() == tag) {
                    result.add(efg.get(j));
                    break;
                }
            }
        }
        return result;
    }

    /*******************************************************************************************
     * <b>function:</b> EFG ====> GUIStructure  找出Wid对应的Widget
     * @param tc 测试用例中Step对应的事件对象的集合
     * @param gui GUIStructure中抓取的Widget集合，每个Widget的属性用WidgetBean保存
     * @return tc中没个Step对应的Widget对象，Widget属性用WidgetBean保存
     */
    public static ArrayList<WidgetBean> searchWidgetsInGUIStructure(ArrayList<EventBean> tc, ArrayList<WidgetBean> gui) {

        ArrayList<WidgetBean> result = new ArrayList<WidgetBean>();
        int tag;

        for (int i = 0; i < tc.size(); i++) {
            tag = tc.get(i).getWidgetId();
            for (int j = 0; j < gui.size(); j++) {
                if (gui.get(j).getwId() == tag) {
                    result.add(gui.get(j));
                    break;
                }
            }
        }
        return result;
    }

    /*******************************************************************************************
     * <b>function:</b> 初始化：（1）Convert.txt中存放的是GUIStructure中点击类事件的类名与QTP Script中的格式的对应关系
     *                           （2）读取Convert.txt中的信息
     *                           （3）将对应关系保存到Hashtable中
     * @param path Convert.txt文件的路径
     * @return 事件类名同Script格式的对应关系集合
     */
    public static Hashtable<String, ScriptBean> init(String path) throws FileNotFoundException, IOException, IOException {

        Hashtable<String, ScriptBean> ht = new Hashtable();
        String[] split = new String[3];

        File in = new File(path);
        if (in.exists()) {
            FileInputStream fi = new FileInputStream(in);
            InputStreamReader isr = new InputStreamReader(fi);
            BufferedReader bf = new BufferedReader(isr);
            String nextLine = "";
            while ((nextLine = bf.readLine()) != null) {
                split = nextLine.split(":");
                ScriptBean sb = new ScriptBean();
                sb.setPart1(split[1]);
                sb.setPart2(split[2]);
                ht.put(split[0], sb);
                sb = null;
            }
        }
        return ht;
    }

    /*******************************************************************************************
     * <b>function:</b> 生成脚本内容
     * @param widgets 测试用例（事件流）中每个事件对应的Widget的集合，Widget属性用WidgetBean保存
     * @param ht 事件类名同Script格式的对应关系集合
     * @return 脚本内容
     */
    public static String convertToScript(ArrayList<WidgetBean> widgets, Hashtable ht, String windowName, ArrayList<WidgetBean> gui) {

        String result = "JavaWindow(\"" + windowName + "\")";
        String append = "";
        String parent = "Window";

        for(WidgetBean wb: widgets){
            System.out.println(wb.getwTitle()+":"+wb.getParents());
        }

        for (int i = 0; i < widgets.size(); i++) {
            ScriptBean sb = new ScriptBean();
            if (ht.containsKey(widgets.get(i).getwClass())) {
                sb = (ScriptBean) ht.get(widgets.get(i).getwClass());
            } else {
                int j = gui.indexOf(widgets.get(i));
                while (j >= 0) {
                    if (ht.containsKey(gui.get(j).getwClass())) {
                        sb = (ScriptBean) ht.get(gui.get(j).getwClass());
                        break;
                    }
                    j--;
                }
            }
            if ("javax.swing.JMenu".equals(widgets.get(i).getwClass())) {
                //System.out.println(widgets.get(i).getwTitle()+":"+"\n");
                
                if ("Window".equals(parent)) {
                    append = sb.getPart1() + widgets.get(i).getwTitle() + sb.getPart2();
                    //System.out.println(append);
                    parent = null;
                    parent = widgets.get(i).getwTitle();
                } else if (widgets.get(i).getParents().equals(parent)) {
                    append = sb.getPart1() + widgets.get(i).getwTitle() + sb.getPart2();
                    parent = null;
                    parent = widgets.get(i).getwTitle();
                } else {
                    append = null;
                    parent = null;
                    parent = widgets.get(i).getwTitle();
                    append = sb.getPart1() + widgets.get(i).getwTitle() + sb.getPart2();
                }
            } else {
                if (append != null) {
                    append = append+sb.getPart1() + widgets.get(i).getwTitle() + sb.getPart2() + "\n" + "JavaWindow(\"" + windowName + "\")";
                }else{
                    append = sb.getPart1() + widgets.get(i).getwTitle() + sb.getPart2() + "\n" + "JavaWindow(\"" + windowName + "\")";
                }
                result += append;
                append = null;
                parent = null;
                parent = widgets.get(i).getwTitle();
            }

        }

        result += ".Close";

        return result;
    }
}

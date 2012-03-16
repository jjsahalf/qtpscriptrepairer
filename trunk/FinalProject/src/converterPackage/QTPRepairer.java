package converterPackage;

import domain.EventBean;
import domain.ScriptBean;
import domain.TCStepBean;
import domain.WidgetBean;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.JFrame;
import service.EFGParser;
import service.GUIStructureParser;
import service.TSTParser;
import service.Utils;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author GXW
 */
public class QTPRepairer extends JFrame {


    public void run(String efgPath, String guiPath, String inPath, String outPath) throws FileNotFoundException, IOException {
        try {
            File efgFile = new File(efgPath);
            File guiFile = new File(guiPath);
 
            String path = "src\\converterPackage\\Convert.txt";//Convert.txt文件路径
 
            String windowName = "";

            //解析器对象创建
            EFGParser ep = new EFGParser();
            GUIStructureParser gsp = new GUIStructureParser();
            TSTParser tp = new TSTParser();


            ArrayList<EventBean> efg = ep.getEvents(efgFile);

            /*for(EventBean eb:efg){
            System.out.println(eb.getEventId()+":"+eb.getWidgetId());
            }*/
            ArrayList<TCStepBean> tc = tp.getSteps(new File(inPath));
            ArrayList<WidgetBean> gui = gsp.getWidgets(guiFile);

            for (WidgetBean wb : gui) {
                if (wb.getwTitle() == null) {
                    wb.setwTitle(wb.getwIcon());
                }
                if (wb.isIsWindow() && wb.isRootWindow()) {
                    windowName = wb.getwTitle();
                }
            }

 
            ArrayList<EventBean> tc_efg = Utils.searchTCInEFG(tc, efg);
            ArrayList<WidgetBean> tc_efg_gui = Utils.searchWidgetsInGUIStructure(tc_efg, gui);
            Hashtable<String, ScriptBean> ht = Utils.init(path);
            String scriptResult = Utils.convertToScript(tc_efg_gui, ht, windowName, gui);

            FileOutputStream outStream = null;
            BufferedOutputStream buff = null;

            outStream = new FileOutputStream(new File(outPath));
            buff = new BufferedOutputStream(outStream);
            buff.write(scriptResult.getBytes());
            buff.flush();
            outStream.close();
            buff.close();
            // }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}

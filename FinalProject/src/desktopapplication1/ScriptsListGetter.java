/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopapplication1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yufei Jiang
 */
//我们对QTP工程的处理过程是这样的，
//我们首先根据用户提供的旧的过时的QTP工程的目录将该目录下的所有QTP工程进行拷贝
//之后把内容全部粘贴到用户指定的文件夹下，
//而之后的修复工作将全部在这个新的文件夹中的工程上进行。
//我们有一件事情必须要做的是要在这个新文件夹中自动化寻找到脚本文件
//因为一个工程中有很多的文件，所以我们要自动化查找
//这个类就是帮我们在干这件事情

public class ScriptsListGetter {
    
    public void getScriptsList(){
        refreshFileList(Global.target_space);
    }



//这个方法包括了这个类的主要逻辑
    //我们使用递归的方法逐层深入的寻找脚本文件所在的文件夹(应该在名为Action0的文件夹中)
    //并且进一步的在其中找到正确的文件，（应该以.mts为后缀名）
    //最终得到它的绝对路径，并且将其放入脚本文件绝对路径的list中（因为是批量修复，所以会有很多list）
    //这样之后的读取和重新写入，均根据这个绝对路径来进行。
    public void refreshFileList(String strPath) {

        File dir = new File(strPath);

        File[] files = dir.listFiles();

        if (files == null) {
            return;
        }

        for (int i = 0; i < files.length; i++) {

            if (files[i].isDirectory()) {

                if (!files[i].getAbsolutePath().endsWith("Action0")) {
                    refreshFileList(files[i].getAbsolutePath());
                }

            } else {

                String strFileName = files[i].getAbsolutePath().toLowerCase();

                //System.out.println("---" + strFileName);

                if (files[i].getAbsolutePath().endsWith(".mts")) {
                    Global.list_of_obsolete_scripts.add(files[i].getAbsolutePath());
                }
            }

        }

    }
}

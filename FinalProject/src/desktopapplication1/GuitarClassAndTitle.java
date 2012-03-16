/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package desktopapplication1;

/**
 *
 * @author Yufei Jiang
 */
//这个数据结构是用来存储一个控件在Guitar中的所有形式的表示
public class GuitarClassAndTitle {
    public String class_name;//这个控件在GUIxml中所有标识的classname
    public String title_or_icon;//GUIxml中读取的这个类的title名字，在没有title的情况下存储icon名字
    public String old_wid;//在旧GUIxml文件中存储的这个控件的wid
    public String old_eid;//在旧EFGxml文件中存储的这个控件的eid
    public String new_wid;//在新GUIxml文件中存储的这个控件的wid
    public String new_eid;//在新EFGxml文件中存储的这个控件的wid

    public GuitarClassAndTitle(){

    }
    public GuitarClassAndTitle(String class_name, String title_or_icon,String old_wid){
        this.class_name=class_name;
        this.title_or_icon=title_or_icon;
        this.old_wid=old_wid;
        this.new_wid="w-1";
        this.new_eid="e-1";
        this.old_eid="e-1";
    }
}

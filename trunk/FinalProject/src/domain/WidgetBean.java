/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package domain;

/**
 *
 * @author GXW
 * 保存GUIStructure中每个Widget的属性
 */
public class WidgetBean {

    private int wId;
    private String wClass;
    private String parents;

    public String getParents() {
        return parents;
    }

    public void setParents(String parents) {
        this.parents = parents;
    }

    public WidgetBean(Boolean isWindow){
        this.isWindow = isWindow;
    }

    public WidgetBean(){

    }

    public boolean isIsWindow() {
        return isWindow;
    }

    public void setIsWindow(boolean isWindow) {
        this.isWindow = isWindow;
    }

    public boolean isRootWindow() {
        return rootWindow;
    }

    public void setRootWindow(boolean rootWindow) {
        this.rootWindow = rootWindow;
    }
    private String wTitle;
    private String wIcon;
    private boolean isWindow;
    private boolean rootWindow;

    public String getwIcon() {
        return wIcon;
    }

    public void setwIcon(String wIcon) {
        this.wIcon = wIcon;
    }

    public String getwClass() {
        return wClass;
    }

    public void setwClass(String wClass) {
        this.wClass = wClass;
    }

    public int getwId() {
        return wId;
    }

    public void setwId(int wId) {
        this.wId = wId;
    }

    public String getwTitle() {
        return wTitle;
    }

    public void setwTitle(String wTitle) {
        this.wTitle = wTitle;
    }


}

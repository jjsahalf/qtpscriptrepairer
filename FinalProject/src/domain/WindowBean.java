/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package domain;

/**
 *
 * @author GXW
 */
public class WindowBean {

    private String wId;
    private Boolean rootWindow;

    public Boolean getRootWindow() {
        return rootWindow;
    }

    public void setRootWindow(Boolean rootWindow) {
        this.rootWindow = rootWindow;
    }

    public String getwId() {
        return wId;
    }

    public void setwId(String wId) {
        this.wId = wId;
    }


}

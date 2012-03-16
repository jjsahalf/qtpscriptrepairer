/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package domain;

/**
 *
 * @author GXW
 * 保存测试用例中每个Step的属性
 */
public class TCStepBean {

    private int eventId;
    private boolean reachingStep;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public boolean isReachingStep() {
        return reachingStep;
    }

    public void setReachingStep(boolean reachingStep) {
        this.reachingStep = reachingStep;
    }
}

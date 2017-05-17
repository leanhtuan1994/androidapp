package ttuananhle.android.chatlearningapp.model;

/**
 * Created by leanh on 5/17/2017.
 */

public class Quesion {
    private String text;
    private String time;
    private String fromUserId;
    private String toPresenId;

    public Quesion(){}

    public Quesion(String text, String time, String fromUserId, String toPresenId) {
        this.text = text;
        this.time = time;
        this.fromUserId = fromUserId;
        this.toPresenId = toPresenId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToPresenId() {
        return toPresenId;
    }

    public void setToPresenId(String toPresenId) {
        this.toPresenId = toPresenId;
    }
}

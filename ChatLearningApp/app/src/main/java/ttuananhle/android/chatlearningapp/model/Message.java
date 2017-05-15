package ttuananhle.android.chatlearningapp.model;

import java.util.Date;

/**
 * Created by leanh on 5/9/2017.
 */

public class Message {
    private String fromId;
    private String toId;
    private String text;
    private String time;
    private boolean seen;


    public Message(){

    }
    public Message(String fromId, String toId, String text, String time) {
        this.fromId = fromId;
        this.toId = toId;
        this.text = text;
        this.time = time;
    }

    public Message(String fromId, String toId, String text, String time, boolean seen) {
        this.fromId = fromId;
        this.toId = toId;
        this.text = text;
        this.time = time;
        this.seen = seen;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
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

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}

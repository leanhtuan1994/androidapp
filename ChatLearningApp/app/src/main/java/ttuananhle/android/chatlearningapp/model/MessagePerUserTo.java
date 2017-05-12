package ttuananhle.android.chatlearningapp.model;

/**
 * Created by leanh on 5/10/2017.
 */

public class MessagePerUserTo {
    private String name;
    private String massage;
    private String photoUrl;
    private String time;
    private String toId;

    public MessagePerUserTo(){

    }
    public MessagePerUserTo(String toId, String name, String massage, String photoUrl, String time) {
        this.toId = toId;
        this.name = name;
        this.massage = massage;
        this.photoUrl = photoUrl;
        this.time = time;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

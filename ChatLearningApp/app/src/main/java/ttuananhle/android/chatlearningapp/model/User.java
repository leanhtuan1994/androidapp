package ttuananhle.android.chatlearningapp.model;

/**
 * Created by leanh on 5/4/2017.
 */

public class User {
    private String  id;
    private String  name;
    private String  email;
    private String  password;
    private String  photoUrl;
    private boolean isTeacher = false;
    private String  code;
    private String  team;
    private boolean haveMessage = false;

    public User(){

    }

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.team = "";
    }

    public User(String name, String email){
        this.name = name;
        this.email = email;
        this.team = "";
    }

    public User(String id, String name, String email, String password, String photoUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.photoUrl = photoUrl;
        this.team = "";
    }

    public User(String id, String name, String email, String password, String photoUrl, boolean isTeacher) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.photoUrl = photoUrl;
        this.isTeacher = isTeacher;
        this.team = "";
    }

    public User(String id, String name, String email, String password, String photoUrl, boolean isTeacher, String code) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.photoUrl = photoUrl;
        this.isTeacher = isTeacher;
        this.code = code;
        this.team = "";
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String idTeam) {
        this.team = idTeam;
    }

    public boolean isHaveMessage() {
        return haveMessage;
    }

    public void setHaveMessage(boolean haveMessage) {
        this.haveMessage = haveMessage;
    }

    @Override
    public String toString() {
        return id;
    }
}

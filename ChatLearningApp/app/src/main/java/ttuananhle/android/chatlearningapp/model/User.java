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

    public User(){

    }

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String id, String name, String email, String password, String photoUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.photoUrl = photoUrl;
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
}

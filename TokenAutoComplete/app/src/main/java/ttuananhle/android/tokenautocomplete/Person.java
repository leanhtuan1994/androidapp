package ttuananhle.android.tokenautocomplete;

import java.io.Serializable;

/**
 * Created by leanh on 5/9/2017.
 */

public class Person implements Serializable{
    private String name;
    private String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
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

    @Override
    public String toString() { return name; }
}

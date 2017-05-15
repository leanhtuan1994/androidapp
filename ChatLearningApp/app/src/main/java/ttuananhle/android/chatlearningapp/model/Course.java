package ttuananhle.android.chatlearningapp.model;

import java.util.Date;
import java.util.List;

/**
 * Created by leanh on 5/14/2017.
 */

public class Course {
    private String id;
    private String idTeacher;
    private Date date;

    public Course(String id, String idTeacher, Date date) {
        this.id = id;
        this.idTeacher = idTeacher;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(String idTeacher) {
        this.idTeacher = idTeacher;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

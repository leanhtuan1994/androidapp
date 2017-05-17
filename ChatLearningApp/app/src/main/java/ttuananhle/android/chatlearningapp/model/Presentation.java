package ttuananhle.android.chatlearningapp.model;

import java.util.List;

/**
 * Created by leanh on 5/17/2017.
 */

public class Presentation {
    private String id;
    private String teamId;
    private String name;
    private String time;
    private List<String> listQuestion;
    private float rating;

    public Presentation(){}

    public Presentation(String id, String teamId, String name) {
        this.id = id;
        this.teamId = teamId;
        this.name = name;
    }

    public Presentation(String id, String teamId, String name, String time) {
        this.id = id;
        this.teamId = teamId;
        this.name = name;
        this.time = time;
    }

    public Presentation(String id, String teamId, String name, String time, List<String> listQuestion) {
        this.id = id;
        this.teamId = teamId;
        this.name = name;
        this.time = time;
        this.listQuestion = listQuestion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getListQuestion() {
        return listQuestion;
    }

    public void setListQuestion(List<String> listQuestion) {
        this.listQuestion = listQuestion;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}

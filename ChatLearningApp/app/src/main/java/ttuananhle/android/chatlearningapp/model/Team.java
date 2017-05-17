package ttuananhle.android.chatlearningapp.model;

import java.util.List;

/**
 * Created by leanh on 5/17/2017.
 */

public class Team {
    private String id;
    private String name;
    private List<String> idmember;
    private int score;


    public Team(){

    }

    public Team(String id, String name, List<String> idmember, int score) {
        this.id = id;
        this.name = name;
        this.idmember = idmember;
        this.score = score;
    }

    public Team(String id, String name, List<String> idmember){
        this.id = id;
        this.name = name;
        this.idmember = idmember;
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

    public List<String> getIdmember() {
        return idmember;
    }

    public void setIdmember(List<String> idmember) {
        this.idmember = idmember;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return name;
    }
}

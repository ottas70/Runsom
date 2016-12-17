package ottas70.runningapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ottas on 7.12.2016.
 */

public class User {

    private int id;
    private String username;
    private String password;
    private String email;

    private List<Run> runs;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        runs = new ArrayList<>();
    }

    public User(String password, String username, String email) {
        this.password = password;
        this.username = username;
        this.email = email;
        runs = new ArrayList<>();
    }

    public User(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        runs = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Run> getRuns() {
        return runs;
    }

    public void setRuns(ArrayList<Run> runs) {
        this.runs = runs;
    }
}

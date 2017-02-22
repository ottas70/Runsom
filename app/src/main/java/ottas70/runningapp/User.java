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
    private int money;
    private int weight = -1;
    private int height = -1;
    private String gender;

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

    public User(int id, String username, String password, String email, int money, int weight, int height, String gender) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.money = money;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
        runs = new ArrayList<>();
    }

    public void discountMoney(int price) {
        money = money - price;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getGenderInt(){
        if(gender.equals("Male")){
            return 1;
        }
        return 2;
    }
}

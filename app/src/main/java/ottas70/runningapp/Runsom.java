package ottas70.runningapp;

/**
 * Created by Ottas on 10.12.2016.
 */
public class Runsom {

    private static Runsom instance = new Runsom();

    private User user;

    private Runsom() {
    }

    public static Runsom getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

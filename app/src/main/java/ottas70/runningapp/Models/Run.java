package ottas70.runningapp.Models;

import ottas70.runningapp.Runsom;

/**
 * Created by Ottas on 10.12.2016.
 */

public class Run {

    private int MET_RUNNING = 8;

    private Duration duration;
    private double distance;
    private double averageSpeed;
    private int moneyEarned;
    private String date;
    private String name;
    private String encodedPath;

    public Run(Duration duration, double distance, double averageSpeed, int moneyEarned, String date, String name, String encodedPath) {
        this.duration = duration;
        this.distance = distance;
        this.averageSpeed = averageSpeed;
        this.moneyEarned = moneyEarned;
        this.date = date;
        this.name = name;
        this.encodedPath = encodedPath;
    }

    public static String generateName() {
        return "Run " + (Runsom.getInstance().getUser().getRuns().size() + 1);
    }

    public double getCalories() {
        return (MET_RUNNING * Runsom.getInstance().getUser().getWeight() * duration.getDurationInHours() * 1.0);
    }

    public Duration getDuration() {
        return duration;
    }

    public double getDistance() {
        return distance;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public int getMoneyEarned() {
        return moneyEarned;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEncodedPath() {
        return encodedPath;
    }
}

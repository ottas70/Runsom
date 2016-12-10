package ottas70.runningapp;

/**
 * Created by Ottas on 10.12.2016.
 */

public class Run {

    private Duration duration;
    private double distance;
    private double averageSpeed;
    private int moneyEarned;
    private String date;

    public Run(Duration duration, double distance, double averageSpeed, int moneyEarned,String date) {
        this.duration = duration;
        this.distance = distance;
        this.averageSpeed = averageSpeed;
        this.moneyEarned = moneyEarned;
        this.date = date;
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
}

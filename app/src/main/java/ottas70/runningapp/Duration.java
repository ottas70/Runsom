package ottas70.runningapp;

/**
 * Created by Ottas on 10.12.2016.
 */

public class Duration {

    private int hours;
    private int minutes;
    private int seconds;

    public Duration(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public void addSecond(){
        seconds++;
        if(seconds >= 60){
            seconds = 0;
            minutes++;
            if(minutes >= 60){
                minutes = 0;
                hours++;
            }
        }
    }

    @Override
    public String toString() {
        return ""+String.format("%02d",hours)+":"+String.format("%02d",minutes)+":"+String.format("%02d",seconds);
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

}

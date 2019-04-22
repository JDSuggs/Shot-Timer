package com.hfad.shottimer;

import java.util.ArrayList;

public class Shot {
    public static int COUNTER = 1;
    private int shotNumber;
    private double time;
    private double splitTime = 0;
    private double missedShots = 0;

    public static ArrayList<Shot> shotList = new ArrayList<>();

    public Shot(double time){
        this.time = time;
        this.shotNumber = COUNTER;
        COUNTER += 1;

    }

    public double splitTime( Shot shot2){
        if ((this.time-shot2.time) < 0){
            return 0;
        }else{
            shot2.setSplitTime(this.time-shot2.time);
            return this.time-shot2.time;
        }

    }

    public int getShotNumber() {
        return shotNumber;
    }

    public double getTime() {
        return time;
    }

    @Override
    public String toString() {
        double milliSeconds = getTime();
        return getTimeStr(milliSeconds);
    }

    public static String getTimeStr(double milli) {
        int milliseconds = (int)(milli % 1000);
        int seconds = (int)(Math.floor((milli / 1000) % 60));
        int minutes = (int)(Math.floor((milli / (60 * 1000)) % 60));
        return minutes + ":" + seconds + "." + milliseconds;
    }

    public void setSplitTime(double splitTime) {
        this.splitTime = splitTime;
    }

    public double getSplitTime() {
        return splitTime;
    }

    public double getMissedShots() {
        return missedShots;
    }

    public void setMissedShots(double missedShots) {
        this.missedShots = missedShots;
    }
}

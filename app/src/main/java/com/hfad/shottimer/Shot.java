package com.hfad.shottimer;

import java.util.ArrayList;

public class Shot {
    public static int COUNTER = 1;
    private int shotNumber;
    private double time;

    public static ArrayList<Shot> shotList = new ArrayList<>();

    public Shot(double time){
        this.time = time;
        this.shotNumber = COUNTER;
        COUNTER += 1;
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

    public String getTimeStr(double milli) {
        int milliseconds = (int)(milli % 1000);
        int seconds = (int)(Math.floor((milli / 1000) % 60));
        int minutes = (int)(Math.floor((milli / (60 * 1000)) % 60));
        return minutes + ":" + seconds + "." + milliseconds;
    }
}

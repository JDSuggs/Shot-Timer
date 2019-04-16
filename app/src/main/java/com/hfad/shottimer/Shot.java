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
}

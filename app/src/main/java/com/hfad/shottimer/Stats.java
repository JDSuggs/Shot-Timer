package com.hfad.shottimer;

import java.util.ArrayList;

public class Stats {
    private double penaltyPoints = 0;
    private double sessionShots = 0;
    private double averageDelayBetweenShots= 0;
    private double numMissedShots = 0;
    private double averageMissedShots = 0;
    public static ArrayList<Stats> shotList = new ArrayList<>();

    public Stats(ArrayList<Shot> sessionShots){
        this.sessionShots = Double.valueOf(sessionShots.get(sessionShots.size()-1).getShotNumber());

        for(int x =0; x< sessionShots.size(); x++){
            this.averageDelayBetweenShots += sessionShots.get(x).getSplitTime();
            this.numMissedShots += sessionShots.get(x).getMissedShots();
        }
        this.averageDelayBetweenShots = this.averageDelayBetweenShots/this.sessionShots;
    }
}

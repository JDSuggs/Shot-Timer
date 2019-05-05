package com.hfad.shottimer;

import java.util.ArrayList;
import java.util.Date;

public class Stats {
    public static int COUNTERSTAT = 1;
    private int statNumber;
    private String date;
    private double penaltyPoints = 0;
    private double sessionShots = 0;
    private double averageDelayBetweenShots= 0;
    private double numMissedShots = 0;
    private double averageMissedShots = 0;
    private double totalTime = 0;
    private ArrayList<Shot> sessionShotList;
    public static ArrayList<Stats> statList = new ArrayList<>();


    public Stats(ArrayList<Shot> sessionShots){
        this.sessionShots = Double.valueOf(sessionShots.get(sessionShots.size()-1).getShotNumber());
        double addDelaytemp = 0;
        for(int i =0; i < sessionShots.size(); i++){
            addDelaytemp = sessionShots.get(i).getSplitTime();
            this.averageDelayBetweenShots += addDelaytemp;
            this.numMissedShots += sessionShots.get(i).getMissedShots();
        }
        this.averageDelayBetweenShots = this.averageDelayBetweenShots/this.sessionShots;
        this.sessionShotList = sessionShots;
        this.totalTime = (sessionShots.get(sessionShots.size()-1).getTime());
        this.statNumber = statList.size() + 1;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double d) {
        this.totalTime = d;
    }

    public double getPenaltyPoints() {
        return penaltyPoints;
    }

    public void setPenaltyPoints(double penaltyPoints) {
        this.penaltyPoints = penaltyPoints;
    }

    public double getSessionShots(){
        return this.sessionShots;
    }
    public double getNumMissedShots(){
        return this.numMissedShots;
    }
    public String getAverageDelayBetweenShots(){
        return getTimeStr(this.averageDelayBetweenShots);
    }
    public double getAverageMissedShots(){
        return this.averageMissedShots;
    }
    public double getMissedShots(){
        return this.numMissedShots;
    }

    public static String getTimeStr(double milli) {
        int milliseconds = (int)(milli % 1000);
        int seconds = (int)(Math.floor((milli / 1000) % 60));
        int minutes = (int)(Math.floor((milli / (60 * 1000)) % 60));
        return minutes + ":" + seconds + "." + milliseconds;
    }

    public int getStatNumber() {
        return statNumber;
    }
//    public void setStatNumber() {
//        this.statNumber =  statList.size() +1;
//    }

    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }

    public ArrayList <Shot> getSessionShotList() {
        return sessionShotList;
    }

    public void setSessionShotList(ArrayList <Shot> sessionShotList) {
        this.sessionShotList = sessionShotList;
    }


}

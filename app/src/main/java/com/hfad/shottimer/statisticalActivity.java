package com.hfad.shottimer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class statisticalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView delayShots = findViewById(R.id.resultsAverageDelayBetweenShots);
        TextView avgMissedShot = findViewById(R.id.resultsaverageMissedShots);
        TextView penaltyPoint = findViewById(R.id.resultspenaltyPoints);
        TextView shots = findViewById(R.id.resultssessionShots);

        ArrayList<Shot> sessionShot = (ArrayList<Shot>)(Shot.shotList.clone());
        if (sessionShot.size() == 0){
            delayShots.setText("Not Working");
            avgMissedShot.setText("Not Working");
            penaltyPoint.setText("Not Working");
            shots.setText("Not Working");
        }else{
            Stats stats = new Stats(sessionShot);
            delayShots.setText(String.valueOf(stats.getAverageDelayBetweenShots()));
            avgMissedShot.setText(String.valueOf(stats.getAverageMissedShots()));
            penaltyPoint.setText(String.valueOf(stats.getPenaltyPoints()));
            shots.setText(String.valueOf(stats.getSessionShots()));

        }



    }

}

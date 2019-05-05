package com.hfad.shottimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class statisticalActivity extends AppCompatActivity {

//    private String csv = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final TextView delayShots = findViewById(R.id.resultsAverageDelayBetweenShots);
        final TextView missedShots = findViewById(R.id.resultsaverageMissedShots);
        final TextView penaltyPoint = findViewById(R.id.resultspenaltyPoints);
        final TextView sessionTime = findViewById(R.id.totalTimeResults);
        final TextView shots = findViewById(R.id.resultssessionShots);
        final TextView session = findViewById(R.id.sessionNumber);
        final TextView date = findViewById(R.id.dateNumber);
        FloatingActionButton fab = findViewById(R.id.fab);

        if (Stats.statList.size() > 0) {
            try {
                Bundle bundle = getIntent().getExtras();
                delayShots.setText(String.valueOf(bundle.get("avgDelayBetweenShots")));
                missedShots.setText(String.valueOf(bundle.get("numberMissed")));
                penaltyPoint.setText(String.valueOf(bundle.get("penalty")));
                shots.setText(String.valueOf(bundle.get("totalShots")));
                sessionTime.setText(String.valueOf(bundle.get("totalTime")));
                session.setText(String.valueOf(bundle.get("session")));
                date.setText(String.valueOf(bundle.get("date")).substring(0,10));

            } catch (Exception e) {
                delayShots.setText(Stats.statList.get(0).getAverageDelayBetweenShots());
                missedShots.setText(Double.toString(Stats.statList.get(0).getMissedShots()));
                penaltyPoint.setText(Double.toString(Stats.statList.get(0).getPenaltyPoints()));
                shots.setText(Double.toString(Stats.statList.get(0).getSessionShots()));
                sessionTime.setText(Stats.getTimeStr(Stats.statList.get(0).getTotalTime()));
                session.setText(Integer.toString(Stats.statList.get(0).getStatNumber()));
                date.setText(Stats.statList.get(0).getDate().substring(0,10));
            }
        } else {
            delayShots.setText("-----");
            missedShots.setText("-----");
            penaltyPoint.setText("-----");
            shots.setText("-----");
            sessionTime.setText("-----");
            session.setText("-----");
            date.setText("-----");
        }
//        } finally {
//            delayShots.setText("-----");
//            missedShots.setText("-----");
//            penaltyPoint.setText("-----");
//            shots.setText("-----");
//            sessionTime.setText("-----");
//        }

//        try{
//            ArrayList <Shot> sessionShot = Stats.statList.get(Stats.statList.size()-1).getSessionShotList();
//            Stats stats = Stats.statList.get(Stats.statList.size()-1);
//
//                delayShots.setText(String.valueOf(stats.getAverageDelayBetweenShots()));
//                missedShots.setText(String.valueOf(stats.getMissedShots()));
//                penaltyPoint.setText(String.valueOf(stats.getPenaltyPoints()));
//                shots.setText(String.valueOf(stats.getSessionShots()));
//
//
//
//        }catch (Exception e){


//        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String csv = delayShots.getText().toString() + "," + missedShots.getText().toString()
                        + ","+ penaltyPoint.getText().toString() + "," + sessionTime.getText().toString();
                Intent intent = new Intent(statisticalActivity.this, CreateMessageActivity.class);
                intent.putExtra("CSV", csv);
                startActivity(intent);

            }
        });


    }

}

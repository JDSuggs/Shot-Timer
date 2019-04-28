package com.hfad.shottimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        TextView shots = findViewById(R.id.resultssessionShots);
        FloatingActionButton fab = findViewById(R.id.fab);

        ArrayList<Shot> sessionShot = (ArrayList<Shot>)(Shot.shotList.clone());
        if (sessionShot.size() == 0){
            delayShots.setText("Not Working");
            missedShots.setText("Not Working");
            penaltyPoint.setText("Not Working");
            shots.setText("Not Working");
        }else{
            Stats stats = new Stats(sessionShot);
            delayShots.setText(String.valueOf(stats.getAverageDelayBetweenShots()));
            missedShots.setText(String.valueOf(stats.getMissedShots()));
            penaltyPoint.setText(String.valueOf(stats.getPenaltyPoints()));
            shots.setText(String.valueOf(stats.getSessionShots()));

        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String csv = delayShots.getText().toString() + "," + missedShots.getText().toString()
                        + ","+ penaltyPoint.getText().toString();
                Intent intent = new Intent(statisticalActivity.this, CreateMessageActivity.class);
                intent.putExtra("CSV", csv);
                startActivity(intent);

            }
        });


    }

}

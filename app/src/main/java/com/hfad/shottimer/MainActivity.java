package com.hfad.shottimer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(Color.GRAY);
    }

    public void historicalAct(View view) {
        Intent historical = new Intent(this, historicalActivity.class);
        startActivity(historical);
    }

    public void timerActivity(View view) {
        Intent historical = new Intent(this, timerActivity.class);
        startActivity(historical);
    }

    public void statsActivity(View view) {
        Intent historical = new Intent(this, statisticalActivity.class);
        startActivity(historical);
    }

    public void questionActivity(View view) {
        Intent historical = new Intent(this, questionActivity.class);
        startActivity(historical);
    }
}

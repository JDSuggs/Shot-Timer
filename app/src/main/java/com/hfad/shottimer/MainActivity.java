package com.hfad.shottimer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(Color.GRAY);
        if (checkPermissionFromDevice()) {
        } else {
            requestPermissions();
        }
//        Shot shot1 = new Shot(1.1);
//        Shot shot2 = new Shot(1.2);
//        Shot shot3 = new Shot(1.3);
//        Shot shot4 = new Shot(1.4);
//        Shot.shotList.add(shot1);
//        Shot.shotList.add(shot2);
//        Shot.shotList.add(shot3);
//        Shot.shotList.add(shot4);
    }

    public void historicalAct(View view) {
        Intent historical = new Intent(this, historicalActivity.class);
        startActivity(historical);
    }

    public void timerActivity(View view) {
        if(checkPermissionFromDevice()) {
            Intent historical = new Intent(this, timerActivity.class);
            startActivity(historical);
        } else {
            requestPermissions();
        }
    }

    public void statsActivity(View view) {
        Intent historical = new Intent(this, statisticalActivity.class);
        startActivity(historical);
    }

    public void questionActivity(View view) {
        Intent historical = new Intent(this, questionActivity.class);
        startActivity(historical);
    }

    public boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED && record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO}
                ,REQUEST_PERMISSION_CODE);
    }

}

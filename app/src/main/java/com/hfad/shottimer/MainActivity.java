package com.hfad.shottimer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_PERMISSION_CODE = 1000;
    private FirebaseAuth mAuth;
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(Color.GRAY);
        mAuth = FirebaseAuth.getInstance();
        if (checkPermissionFromDevice()) {
        } else {
            requestPermissions();
        }
    }
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        mAuth.signOut();
                        Shot.shotList.clear();
                        Stats.statList.clear();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                    }
                }).create().show();
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

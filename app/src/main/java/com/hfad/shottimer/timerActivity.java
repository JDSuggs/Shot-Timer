package com.hfad.shottimer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.hfad.shottimer.Stats.statList;


public class timerActivity extends AppCompatActivity {
    Button btnRecord, btnStopRecording, btnFinish, btnNew;
    //    String pathSaved = "";
    MediaPlayer mediaPlayer;
    final int REQUEST_PERMISSION_CODE = 1000;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    MediaRecorder mRecorder;
    Thread thread;
    long oldTime = 0;
    long newTime = 0;
    long startTime = 0;
    public static int finished = 0;

    final Runnable updater = new Runnable() {
        public void run() {
            recordShots();
        }
    };
    final Handler handler = new Handler();

    RecyclerView recyclerView;
    ShotRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Shot.COUNTER = 1;
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShotRecyclerAdapter(this, Shot.shotList);
        recyclerView.setAdapter(adapter);

        //Buttons for Playing and stopping
        btnFinish = findViewById(R.id.finish);
        btnRecord = findViewById(R.id.start);
        btnNew = findViewById(R.id.new1);
        btnStopRecording = findViewById(R.id.stop);
        if (finished == 1) {
            btnRecord.setEnabled(false);
            btnStopRecording.setEnabled(false);
        }

        if(checkPermissionFromDevice()){
            btnRecord.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (thread == null) {
                        thread = new Thread() {
                            public void run() {
                                oldTime = 0;
                                while (thread != null) {
                                    try {
                                        Thread.sleep(20);
                                    } catch (InterruptedException e) {
                                    }
                                    handler.post(updater);
                                }
                            }
                        };
                        thread.start();
                    }
////                    pathSaved = Environment.getExternalStorageDirectory().getAbsolutePath()
////                            +"/"+ UUID.randomUUID().toString()+"_audio_record.3gp";
                    btnStopRecording.setEnabled(true);
                    Toast.makeText(timerActivity.this, "Recording", Toast.LENGTH_SHORT).show();
                }

            });
            btnStopRecording.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    thread=null;
                    btnStopRecording.setEnabled(false);
                    btnRecord.setEnabled(false);
                }
            });
            btnFinish.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if (thread != null) {
                        thread = null;
                    }
                    if (Shot.shotList.size() != 0){
                        finish();
                    }
                }
            });
            btnNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (thread != null) {
                        thread = null;
                    }
                    if (Shot.shotList.size() != 0){
                        finish();
                    }
                    Shot.shotList.clear();
                    btnStopRecording.setEnabled(true);
                    btnRecord.setEnabled(true);
                    finished = 0;
                    Shot.COUNTER = 1;
                    adapter.notifyDataSetChanged();
                }
            });
        }else{
            requestPermissions();
        }
    }

    @Override
    public void onBackPressed() {
        if ((finished == 0) && (Shot.shotList.size() != 0)){
            new AlertDialog.Builder(this)
                    .setTitle("Finish Session")
                    .setMessage("Finish the session? \nMisses may not be recorded later.")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {

                            finish();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }).create().show();
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public void finish() {
        //Crate Statistics object upon ending the session.
        if (finished == 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY/ HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            Stats tempStat = new Stats(Shot.shotList);
            tempStat.setDate(currentDateandTime);
            statList.add(0,tempStat);
            String userId = mAuth.getCurrentUser().getUid();
            DocumentReference docRef = mDb.collection("users").document(userId)
                    .collection("statList").document(userId)
                    .collection("stats").document();
            docRef.set(tempStat);
            Stats.COUNTERSTAT++;
            adapter.notifyDataSetChanged();
            finished = 1;
            btnStopRecording.setEnabled(false);
            btnRecord.setEnabled(false);
        }
    }

    public void onResume() {
        super.onResume();
        startRecorder();
    }

    public void onPause() {
        super.onPause();
        stopRecorder();
    }

    public void startRecorder() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            } catch (java.io.IOException ioe) {
            } catch (java.lang.SecurityException e) {
            }
            try {
                mRecorder.start();
            } catch (java.lang.SecurityException e) {
            }
        }
    }

    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void recordShots() {
        double db = decibels();
        if (db > 70) {
            newTime = getCurrentTime();
            double timeDiff = (timeDifference(oldTime,newTime));
            if (oldTime == 0) {
                startTime = newTime;
                Shot shot = new Shot((double)(timeDifference(startTime, newTime)));
                Shot.shotList.add(shot);
                adapter.notifyDataSetChanged();
                oldTime = newTime;
            }
            else if (timeDiff > 50) {
                Shot shot = new Shot((double)(timeDifference(startTime, newTime)));
                Shot.shotList.add(shot);
                adapter.notifyDataSetChanged();
                oldTime = newTime;
            }
        }
    }

    public double decibels() {
        return 20 * Math.log10(getAmplitude() / getAmplitudeFactor());
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return (mRecorder.getMaxAmplitude());
        else
            return 0;
    }

    public double getAmplitudeFactor() {
        double factor = .8;
        return factor;
    }

    public String getTimeStr(long milli, long startTime) {
        milli = milli - startTime;
        int milliseconds = (int)(milli % 1000);
        int seconds = (int)(Math.floor((milli / 1000) % 60));
        int minutes = (int)(Math.floor((milli / (60 * 1000)) % 60));
        return minutes + ":" + seconds + "." + milliseconds;
    }

    public long getCurrentTime() {
        return  System.currentTimeMillis();
    }

    public double timeDifference(double oldTime, double newTime) {
        return newTime - oldTime;
    }

//    private void setupMRecorder() {
//        mRecorder = new MediaRecorder();
//        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//        mRecorder.setOutputFile(pathSaved);
//        TextView text = findViewById(R.id.pathSaved);
//        text.setText(this.pathSaved);
//    }

//    private void setupMediaRecorder() {
//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//        mediaRecorder.setOutputFile(pathSaved);
//        TextView text = findViewById(R.id.pathSaved);
//        text.setText(this.pathSaved);
//    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO}
                ,REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    public boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED && record_audio_result == PackageManager.PERMISSION_GRANTED;
    }
}

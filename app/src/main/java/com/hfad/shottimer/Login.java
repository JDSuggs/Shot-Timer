package com.hfad.shottimer;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    private ConstraintLayout mLoggedInGroup;
    private ConstraintLayout mLoggedOutGroup;
    private TextView mNameLabel;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);


        mLoggedInGroup = findViewById(R.id.logged_in_group);
        mLoggedOutGroup = findViewById(R.id.logged_out_group);
        mNameLabel = findViewById(R.id.hello);
        mName = findViewById(R.id.name);
        mEmailField = findViewById(R.id.email);
        mPasswordField = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Shot.shotList.clear();
        Stats.statList.clear();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);


    }

    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLoggedInGroup.setVisibility(View.GONE);
        mLoggedOutGroup.setVisibility(View.VISIBLE);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            mLoggedOutGroup.setVisibility(View.GONE);
            mLoggedInGroup.setVisibility(View.VISIBLE);
            DocumentReference docRef = mDb.collection("users").document(mAuth.getCurrentUser().getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            mNameLabel.setText(document.get("name").toString());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

//            mNameLabel.setText(d.);
//            mNameLabel.setText((CharSequence) mDb.collection("users").document(mAuth.getCurrentUser().getUid()));
//            mNameLabel.setText(mAuth.getCurrentUser().getEmail());
        } else {
            mLoggedInGroup.setVisibility(View.GONE);
            mLoggedOutGroup.setVisibility(View.VISIBLE);
        }
    }

    public void signOut(View view) {
        mAuth.signOut();
        updateUI(null);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    final Runnable r = new Runnable() {
        public void run() {
            Intent intent = new Intent(getBaseContext(),MainActivity.class);
            startActivity(intent);
        }
    };

    final Handler h = new Handler();

    public void signIn(View view) {
        if (!validateForm()) {
            return;
        }

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, user.getEmail());
                            updateUI(user);
                            String userId = mAuth.getCurrentUser().getUid();
                            mDb.collection("users").document(userId)
                                    .collection("statList").document(userId)
                                    .collection("stats").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String delay = (String) document.get("averageDelayBetweenShots");
                                            Double avgMissed = (Double) document.get("averageMissedShots");
                                            String date = (String) document.get("date");
                                            Double missed = (Double) document.get("missedShots");
                                            Double numMissed = (Double) document.get("numMissedShots");
                                            Double penalty = (Double) document.get("penaltyPoints");
                                            Double sesShots = (Double) document.get("sessionShots");
                                            Long statNum = (Long) document.get("statNumber");
                                            Double time = (Double) document.get("totalTime");

                                            Stats stat = new Stats(delay,avgMissed,date,missed,numMissed,penalty,sesShots,statNum,time);
                                            Stats.statList.add(0,stat);
                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                        }
                                        Collections.sort(Stats.statList, new Comparator<Stats>() {
                                            @Override
                                            public int compare(Stats o1, Stats o2) {
                                                return Integer.compare(o2.getStatNumber(),(o1.getStatNumber()));
                                            }
                                        });
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                            h.postDelayed(r, 1000);
                        } else {
                            // If sign in fails, display a message to the user.
                            Exception e = task.getException();
                            Log.w(TAG, "signInWithEmail:failure", e);
                            Toast.makeText(Login.this, "Login failed: " + e.getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void createAccount(View view) {
        if (!validateForm()) {
            return;
        }

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User u = new User(mName.getText().toString(), user.getUid());
                            mDb.collection("users").document(mAuth.getCurrentUser()
                                    .getUid()).set(u)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                            updateUI(user);
                            h.postDelayed(r, 1000);
                        } else {
                            // If registration fails, display a message to the user.
                            Exception e = task.getException();
                            Log.w(TAG, "createUserWithEmail:failure", e);
                            Toast.makeText(Login.this, "Registration failed: " + e.getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }
                });
    }

//    public void launchFirestoreActivity(View view) {
//        startActivity(new Intent(this, DisplayActivity.class));
//    }
//
//    public void launchStorageActivity(View view) {
//        startActivity(new Intent(this, StorageActivity.class));
//    }
}

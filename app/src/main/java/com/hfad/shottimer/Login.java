package com.hfad.shottimer;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    private FirebaseAuth mAuth;

    private ConstraintLayout mLoggedInGroup;
    private ConstraintLayout mLoggedOutGroup;
    private TextView mNameLabel;
    private EditText mEmailField;
    private EditText mPasswordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoggedInGroup = findViewById(R.id.logged_in_group);
        mLoggedOutGroup = findViewById(R.id.logged_out_group);
        mNameLabel = findViewById(R.id.hello);
        mEmailField = findViewById(R.id.email);
        mPasswordField = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

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
//            mNameLabel.setText(String.format(getResources().getString(R.string.hello), currentUser.getEmail()));
            mNameLabel.setText(mAuth.getCurrentUser().getEmail());
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
                            updateUI(user);
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
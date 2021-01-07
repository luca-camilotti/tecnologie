package com.example.firebaseapp;

//import android.support.v7.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final String TAG = "FIREBASETEST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        final EditText txt_email = (EditText) findViewById(R.id.editTextEmail);
        final EditText txt_pwd = (EditText) findViewById(R.id.editTextPassword);

        /* Register */
        Button btn_sign = (Button) findViewById(R.id.btnsign);
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser(txt_email.getText().toString(), txt_pwd.getText().toString());
            }
        });

        /* Sign out */
        Button btn_logout = (Button) findViewById(R.id.btnlogoff);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() != null) {
                    Log.d(TAG, "user "+mAuth.getCurrentUser().getEmail()+" logoff");
                    Toast.makeText(getApplicationContext(), "user "+mAuth.getCurrentUser().getEmail()+" logoff", Toast.LENGTH_LONG).show();
                    mAuth.signOut();
                }
                else {
                    Log.d(TAG, "user already logged out");
                    Toast.makeText(getApplicationContext(), "user already logged out", Toast.LENGTH_LONG).show();
                }

            }
        });

        /* Sign in */
        Button btn_login = (Button) findViewById(R.id.btnlogin);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null) {
                    Log.d(TAG, "user already logged: " + currentUser.getEmail());
                    Toast.makeText(getApplicationContext(), "user already logged: " + currentUser.getEmail(), Toast.LENGTH_LONG).show();
                }
                else
                    loginUser(txt_email.getText().toString(), txt_pwd.getText().toString());
            }
        });
    }

    private void loginUser(String email, String pwd) {
        if(!(email.contains("@") && email.length() > 0 && pwd.length() > 0)) {
            Log.d(TAG, "cannot login user "+email+", pwd "+pwd+", please insert valid email and pwd");
            Toast.makeText(getApplicationContext(), "cannot register user "+email+", pwd "+pwd+", please insert valid email and pwd", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Log.d(TAG, "FirebaseAuth ERROR! cannot login user " + email + ", pwd " + pwd);
                            Toast.makeText(getApplicationContext(), "FirebaseAuth ERROR! cannot login user " + email + ", pwd " + pwd, Toast.LENGTH_LONG).show();
                        }
                        else {
                            Log.d(TAG, "FirebaseAuth login successful! user " + email + ", pwd " + pwd);
                            Toast.makeText(getApplicationContext(), "FirebaseAuth login successful! user " + email + ", pwd " + pwd, Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void createUser(String email, String pwd) {
        if(!(email.contains("@") && email.length() > 4 && pwd.length() >= 6)) {
            Log.d(TAG, "cannot register user "+email+", pwd "+pwd+", please insert valid email (> 4 char) and pwd (>= 6 char)");
            Toast.makeText(getApplicationContext(), "cannot register user "+email+", pwd "+pwd+", please insert valid email (> 4 char) and pwd (>= 6 char)", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    Log.d(TAG, "FirebaseAuth ERROR! cannot register user " + email + ", pwd " + pwd);
                    Toast.makeText(getApplicationContext(), "FirebaseAuth ERROR! cannot register user " + email + ", pwd " + pwd+" (should be at least 6 char long)", Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d(TAG, "FirebaseAuth registration successful! user " + email + ", pwd " + pwd);
                    Toast.makeText(getApplicationContext(), "FirebaseAuth registration successful! user " + email + ", pwd " + pwd, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
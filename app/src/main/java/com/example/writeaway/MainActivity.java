package com.example.writeaway;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //widgets

    Button loginBtn, createAccountBtn;
    private EditText email, pass;

    //Firebase Auth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        createAccountBtn = findViewById(R.id.create_account);

        createAccountBtn.setOnClickListener(v -> {
            //We replaced v -> (lambda) instead of OnClick... [Both works are same]
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
        });
        //Login
        loginBtn = findViewById(R.id.email_signin);
        email =  findViewById(R.id.email);
        pass = findViewById(R.id.password);

        //Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(v-> {

            logEmailPassUser(
                    email.getText().toString().trim(),
                    pass.getText().toString().trim()
            );

            }
        );

    }

    private void logEmailPassUser(
         String email, String pwd
    ){
        //Checking for empty texts
        if(!TextUtils.isEmpty(email)
        && !TextUtils.isEmpty(pwd)
        ){
            firebaseAuth.signInWithEmailAndPassword(
                    email,
                    pwd
            ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    Intent i = new Intent(MainActivity.this, JournalListActivity.class);
                    startActivity(i);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Wrong Password or Id", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
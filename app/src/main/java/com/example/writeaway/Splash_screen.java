package com.example.writeaway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class Splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Thread thread = new Thread(){
            public void run(){
                try{
                    sleep(1900);
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    Intent i = new Intent(Splash_screen.this, MainActivity.class);
                    startActivity(i);

                }
            }
        }; thread.start();

    }
}
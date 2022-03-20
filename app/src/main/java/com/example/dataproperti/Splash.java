package com.example.dataproperti;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(Splash.this, SignIn.class);
                    startActivity(intent);

                }
            }
        };
        timerThread.start();

        getWindow().setExitTransition(new Fade());

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }
}
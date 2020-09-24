package com.sai.newmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.sai.newmap.dao.ApplicationDataPreferences;

public class SlpashScreenActivity extends AppCompatActivity {

    ApplicationDataPreferences applicationDataPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_slpash_screen);




        applicationDataPreferences = ApplicationDataPreferences.getInstance(this);

        Thread t = new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*Log.i("splash", "onCreate:pendingIntent "+pendingIntent);
                startAlarm();*/
                Intent intent;
                if(applicationDataPreferences.getUserId() != null){
                    intent = new Intent(SlpashScreenActivity.this, MapsActivity.class);
                }else{
                    intent = new Intent(SlpashScreenActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        };
        t.start();
    }
}

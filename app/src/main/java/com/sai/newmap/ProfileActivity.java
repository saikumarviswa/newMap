package com.sai.newmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.sai.newmap.dao.ApplicationDataPreferences;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView userImage;
    TextView userId,userName,userMobile;
    Button logOutButton;
    ApplicationDataPreferences applicationDataPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        applicationDataPreferences = ApplicationDataPreferences.getInstance(ProfileActivity.this);

        userImage = (CircleImageView)findViewById(R.id.userImage);
        userId = (TextView)findViewById(R.id.userId);
        userName = (TextView)findViewById(R.id.userName);
        userMobile = (TextView)findViewById(R.id.userMobile);
        logOutButton = (Button) findViewById(R.id.logOutButton);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applicationDataPreferences.setUserImage(null);
                applicationDataPreferences.setUserId(null);
                applicationDataPreferences.setUserEmail(null);
                applicationDataPreferences.setUserId(null);
                applicationDataPreferences.setUserName(null);
                applicationDataPreferences.setUserMobile(null);
                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });


        if(applicationDataPreferences.getUserImage() != null){
            Glide.with(ProfileActivity.this)
                    .load(applicationDataPreferences.getUserImage())
                    .into(userImage);
        }

        userId.setText(applicationDataPreferences.getUserId());
        userName.setText(applicationDataPreferences.getUserName());
        if(applicationDataPreferences.getUserMobile() != null){
            userMobile.setText(applicationDataPreferences.getUserMobile());
        }else {
            userMobile.setText(applicationDataPreferences.getUserEmail());
        }


    }
}

package com.sai.newmap.dao;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationDataPreferences {

    private static ApplicationDataPreferences instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ApplicationDataPreferences(Context context){
        sharedPreferences = context.getSharedPreferences("ApplicationPreferences",0);
        editor = sharedPreferences.edit();
    }

    public static ApplicationDataPreferences getInstance(Context context){
        if(instance == null)
            instance = new ApplicationDataPreferences(context);
        return instance;
    }

    public void setUserId(String userId){
        editor.putString("userId", userId);
        editor.apply();
    }

    public String getUserId(){
        return sharedPreferences.getString("userId", null);
    }

    public void setUserName(String userName){
        editor.putString("userName", userName);
        editor.apply();
    }

    public String getUserName(){
        return sharedPreferences.getString("userName", null);
    }

    public void setUserMobile(String userMobile){
        editor.putString("userMobile", userMobile);
        editor.apply();
    }

    public String getUserMobile(){
        return sharedPreferences.getString("userMobile", null);
    }

    public void setUserEmail(String userEmail){
        editor.putString("userEmail", userEmail);
        editor.apply();
    }

    public String getUserEmail(){
        return sharedPreferences.getString("userEmail", null);
    }

    public void setUserImage(String userImage){
        editor.putString("userImage", userImage);
        editor.apply();
    }

    public String getUserImage(){
        return sharedPreferences.getString("userImage", null);
    }

}

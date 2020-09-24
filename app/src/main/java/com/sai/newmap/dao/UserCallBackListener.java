package com.sai.newmap.dao;

import com.sai.newmap.model.User;

import java.util.ArrayList;

public interface UserCallBackListener {

    public void successCallback(ArrayList<User> userArrayList);
    public void errorCallback(String errorMessage);

}

package com.sai.newmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sai.newmap.dao.ApplicationDataPreferences;
import com.sai.newmap.dao.UserCallBackListener;
import com.sai.newmap.dao.UserDao;
import com.sai.newmap.model.User;

import java.util.ArrayList;

public class LoginFragment  extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    Button loginButto;
    UserDao userDao = new UserDao();
    EditText mobileNumber;
    EditText password;
    Context context;
    ApplicationDataPreferences applicationDataPreferences;
    SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;

    public static LoginFragment newInstance(Context context)
    {

        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.log_in_fragment, container, false);

        loginButto = (Button)view.findViewById(R.id.logInButton);
        mobileNumber = (EditText)view.findViewById(R.id.mobileNumber);
        password = (EditText)view.findViewById(R.id.password);

        context = view.getContext();

        applicationDataPreferences = ApplicationDataPreferences.getInstance(context);

        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(context)
                .enableAutoManage((FragmentActivity) context,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton=(SignInButton)view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });



        loginButto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userDao.userLogin(mobileNumber.getText().toString(), password.getText().toString(), new UserCallBackListener() {
                    @Override
                    public void successCallback(ArrayList<User> userArrayList) {

                        Log.d("main", "successCallback:userArrayList.get(0).getWork() " + userArrayList.get(0).getUserId()+userArrayList.get(0).getUserMobileNumber());

                        if (userArrayList.size() > 0) {

                            applicationDataPreferences.setUserId(userArrayList.get(0).getUserId());
                            applicationDataPreferences.setUserName(userArrayList.get(0).getUserName());
                            applicationDataPreferences.setUserMobile(userArrayList.get(0).getUserMobileNumber());

                            Intent intent = new Intent(context, MapsActivity.class);
                            startActivity(intent);
                            ((FragmentActivity) context).finish();

                        }else {
                            Toast.makeText(context,"Please Enter Valid Mobile Number And Password",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void errorCallback(String errorMessage) {

                    }


                });
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            gotoProfile();
        }else{
            Toast.makeText(context,"Sign in cancel", Toast.LENGTH_LONG).show();
        }
    }

    private void gotoProfile(){
        Intent intent=new Intent(context,MapsActivity.class);
        intent.putExtra("hello","hello");
        startActivity(intent);
        ((FragmentActivity) context).finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

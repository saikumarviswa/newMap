package com.sai.newmap;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.sai.newmap.dao.UserCallBackListener;
import com.sai.newmap.dao.UserDao;
import com.sai.newmap.model.User;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RegisterFragment extends Fragment {

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }
    Context context;
    String mVerificationId;
    EditText otpEditText;
    FirebaseAuth mAuth;
    Button submitOtpButton;
    Button registerButton;
    EditText UserNameEditText;
    EditText registerMobileNumber;
    EditText createPassword;
    EditText conformPassword;
    LinearLayout registerLayout;
    LinearLayout otpLayout;
    UserDao userDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);

        context = view.getContext();

        registerButton = (Button)view.findViewById(R.id.registerButton);
        submitOtpButton = (Button)view.findViewById(R.id.submitOtpButton);
        UserNameEditText = (EditText)view.findViewById(R.id.UserNameEditText);
        registerMobileNumber = (EditText)view.findViewById(R.id.registerMobileNumber);
        createPassword = (EditText)view.findViewById(R.id.createPassword);
        conformPassword = (EditText)view.findViewById(R.id.conformPassword);
        otpEditText = (EditText)view.findViewById(R.id.otpEdit);
        registerLayout = (LinearLayout)view.findViewById(R.id.registerLayout);
        otpLayout = (LinearLayout)view.findViewById(R.id.otpLayout);

        mAuth = FirebaseAuth.getInstance();
        userDao = new UserDao();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent(view.getContext(), CompleteYourProfileActivity.class);
                startActivity(intent);*/



                if(!UserNameEditText.getText().toString().isEmpty() && !registerMobileNumber.getText().toString().isEmpty() && !createPassword.getText().toString().isEmpty()) {
                    if(createPassword.getText().toString().equals(conformPassword.getText().toString())) {

                        userDao.getExistingUser(registerMobileNumber.getText().toString(), new UserCallBackListener() {
                            @Override
                            public void successCallback(ArrayList<User> userArrayList) {
                                if(userArrayList.size() <= 0){
                                    sendVerificationCode(registerMobileNumber.getText().toString());
                                    registerLayout.setVisibility(View.GONE);
                                    registerButton.setVisibility(View.GONE);
                                    otpLayout.setVisibility(View.VISIBLE);
                                }else {
                                    Toast.makeText(context,"User With This Mobile Number Already Exists",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void errorCallback(String errorMessage) {

                            }
                        });

                    }else {
                        Toast.makeText(context,"Password mismatch",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(context,"PLease Fill All The Required Fields",Toast.LENGTH_LONG).show();
                }

            }
        });

        submitOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifyVerificationCode(otpEditText.getText().toString());

            }
        });

        return view;
    }


    private void sendVerificationCode(String mobile) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+mobile,
                120,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks

        );


    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code

            if (code != null) {
                //verifying the code
                otpEditText.setText(code);
                verifyVerificationCode(code);
                Log.d("main", "onVerificationCompleted: "+code);
            }

        }


        @Override
        public void onVerificationFailed(FirebaseException e) {
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);


        //signing the user
        signInWithPhoneAuthCredential(credential);
        Log.d("main", "verifyVerificationCode: "+code+" "+credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Random r = new Random();
                            int low = 100000;
                            int high = 999999;
                            int result = r.nextInt(high-low) + low;


                            User user = new User();
                            user.setUserMobileNumber(registerMobileNumber.getText().toString());
                            user.setPassword(createPassword.getText().toString());
                            user.setUserName(UserNameEditText.getText().toString());



                            userDao.addUser(user, new UserCallBackListener() {
                                @Override
                                public void successCallback(ArrayList<User> userArrayList) {

                                    Toast.makeText(context,"RegistrationSuccessfull",Toast.LENGTH_LONG).show();


                                }



                                @Override
                                public void errorCallback(String errorMessage) {

                                }
                            });






                        }else {
                            Toast.makeText(context,"Please Enter Valid OTP",Toast.LENGTH_LONG).show();
                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                        }

                    }
                });
    }




}

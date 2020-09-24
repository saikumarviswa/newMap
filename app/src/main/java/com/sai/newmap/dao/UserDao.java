package com.sai.newmap.dao;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sai.newmap.model.User;

import java.util.ArrayList;

public class UserDao extends HarVishDao{

    public UserDao(){

    }

    public void addUser(final User user, final UserCallBackListener userCallBackListener){
        try {

            firestore.collection("User")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            ArrayList<User> users = new ArrayList<>();
                            user.setUserId(documentReference.getId());
                            users.add(user);
                            userCallBackListener.successCallback(users);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            userCallBackListener.errorCallback(e.getMessage());

                        }
                    });

        }catch (Exception e){

        }
    }

    public void userLogin(String mobile, String password, final UserCallBackListener userCallBackListener){
        try{

            firestore.collection("User")
                    .whereEqualTo("userMobileNumber",mobile)
                    .whereEqualTo("password",password)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            ArrayList<User> users = new ArrayList<>();
                            for(DocumentSnapshot documentSnapshot : task.getResult()){
                                User user = new User();
                                user = documentSnapshot.toObject(User.class);
                                user.setUserId(documentSnapshot.getId());
                                users.add(user);
                            }

                            userCallBackListener.successCallback(users);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            userCallBackListener.errorCallback(e.getMessage());

                        }
                    });

        }catch (Exception e){

        }
    }

    public void getExistingUser(String mobile, final UserCallBackListener userCallBackListener){
        try{

            firestore.collection("User")
                    .whereEqualTo("userMobileNumber",mobile)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            ArrayList<User> users = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                User user =documentSnapshot.toObject(User.class);
                                user.setUserId(documentSnapshot.getId());
                                users.add(user);
                            }

                            userCallBackListener.successCallback(users);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            userCallBackListener.errorCallback(e.getMessage());
                        }
                    });

        }catch (Exception e){

        }
    }

}

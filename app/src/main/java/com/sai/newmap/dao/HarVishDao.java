package com.sai.newmap.dao;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class HarVishDao {

    protected FirebaseFirestore firestore;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    public HarVishDao(){
        firestore = FirebaseFirestore.getInstance();
    }

}

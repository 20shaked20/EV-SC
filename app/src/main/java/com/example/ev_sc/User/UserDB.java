package com.example.ev_sc.User;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserDB {

    final private String TAG = "UserDB";


    public HashMap<String, Object> MapUser(String email, String pass, String FirstName, String LastName, String UserName, String Phone, String Permission) {
        HashMap<String, Object> user = new HashMap<>();

        user.put("email", email);
        user.put("pass", pass);
        user.put("FirstName", FirstName);
        user.put("LastName", LastName);
        user.put("UserName", UserName);
        user.put("Phone", Phone);
        user.put("Permission", Permission);

        Log.d(TAG, "User mapped to: " + "\n" + user);
        return user;
    }
}

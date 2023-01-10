package com.example.ev_sc.User;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserDB {

    final private String TAG = "UserDB";


    public HashMap<String,String> MapUser(String email, String pass, String FirstName, String LastName, String UserName, String Phone, String Permission) {
        HashMap<String, String> user = new HashMap<>();

        user.put("email", email);
        user.put("pass", pass);
        user.put("FirstName", FirstName);
        user.put("LastName", LastName);
        user.put("UserName", UserName);
        user.put("Phone", Phone);
        user.put("Permission", Permission);

        Log.d(TAG,"User mapped to: " + "\n" + user);
        return user;
    }

    /**
     * this method parses a firebase user document into a user object.
     *
     * @param doc a document containing the user data from the firestore database.
     * @return Object Of user Type.
     */
    public UserObj GetUserFromDatabase(DocumentSnapshot doc) {
        //parser from firebase to object
        String f_name = doc.getString("FirstName");
        String l_name = doc.getString("LastName");
        Double d_permission = doc.getDouble("Permission");
        String phone = doc.getString("Phone");
        String user_name = doc.getString("UserName");

        Integer permission = d_permission.intValue(); // stupid trick to bypass firebase restrictions

        return new UserObj(f_name, l_name, user_name, phone, doc.getId(), permission);
    }

}

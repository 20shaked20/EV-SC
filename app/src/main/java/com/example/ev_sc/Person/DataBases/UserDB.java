package com.example.ev_sc.Person.DataBases;

import android.util.Log;

import com.example.ev_sc.Person.UserObj;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserDB {

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    final private String TAG = "UserDB";

    /**
     * this method adds a new user to the database.
     *
     * @param User user Object
     */
    public void AddUserToDatabase(UserObj User) {
        DocumentReference documentReference = fStore.collection("users").document(User.getID());
        Map<String, Object> user = new HashMap<>();

        user.put("FirstName", User.getFirst_name());
        user.put("LastName", User.getLast_name());
        user.put("UserName", User.getUsername());
        user.put("Phone", User.getPhone_number());
        user.put("Permission", User.getPermissions());

        //Check//
        documentReference.set(user).addOnSuccessListener(unused -> Log.d(TAG, "user Profile is created for " + User.getID()));
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
        Double permission = doc.getDouble("Permission");
        String phone = doc.getString("Phone");
        String user_name = doc.getString("UserName");

        return new UserObj(f_name, l_name, user_name, phone, doc.getId());
    }

}

package com.example.ev_sc.Backend.DataLayer;

import android.util.Log;

import java.util.HashMap;

public class UserDB {

    final private String TAG = "UserDB";

    /**
     * this method gets details about user and maps it to a map.
     *
     * @param email      email of user
     * @param pass       password of user
     * @param FirstName  first name of the user
     * @param LastName   last name of the user
     * @param UserName   username
     * @param Phone      phone
     * @param Permission permission (1 - admin, 0 - user )
     * @return mapped user details.
     */
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

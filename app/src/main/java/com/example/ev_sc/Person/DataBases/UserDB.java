package com.example.ev_sc.Person.DataBases;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.ev_sc.Person.PersonObj;
import com.example.ev_sc.Person.UserObj;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserDB {

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    public void AddUserToDatabase(UserObj User)
    {
        DocumentReference documentReference = fStore.collection("users").document(User.getID());
        Map<String,Object> user = new HashMap<>();

        user.put("FirstName", User.getFirst_name());
        user.put("LastName", User.getLast_name());
        user.put("UserName", User.getUsername());
        user.put("Phone", User.getPhone_number());
        user.put("Permission", User.getPermissions());

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: user Profile is created for "+User.getID());
            }
        });

    }
}

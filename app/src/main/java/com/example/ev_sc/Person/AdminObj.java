package com.example.ev_sc.Person;

import androidx.annotation.NonNull;

public class AdminObj implements PersonObj {
    private String First_name;
    private String Last_name;
    private final int Permissions = 1;
    private final String UID;

    public AdminObj(String first_name, String last_name, String UID) {
        this.Last_name = last_name;
        this.First_name = first_name;
        this.UID = UID;
    }

    @Override
    public String getFirst_name() {
        return First_name;
    }

    @Override
    public String getLast_name() {
        return Last_name;
    }

    @Override
    public String getID() {
        return this.UID;
    }

    @Override
    public int getPermissions() {
        return Permissions;
    }

    @Override
    public void setFirst_name(String first_name) {
        this.First_name = first_name;
    }

    @Override
    public void setLast_name(String last_name) {
        this.Last_name = last_name;
    }

    @NonNull
    public String toString(){ // override toString method to better represent admin data (logging etc.)
        return "Full Name: " + this.getFirst_name() + this.getLast_name() +  "\n" +
              //  "Username: " + this.getUsername() +"\n" +
               // "Phone Number: " + this.getPhone_number() + "\n" +
                "Permissions: " + this.getPermissions() + "\n" +
                "User ID: " + this.getID() + "\n";
    }

}

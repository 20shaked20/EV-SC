package com.example.ev_sc.Person;

import androidx.annotation.NonNull;

public class UserObj implements PersonObj {
    private String First_name;
    private String Last_name;
    private String username;
    private final int Permissions = 0;
    private String phone_number;
    private final String UID;

    public UserObj(String first_name, String last_name, String username, String phone_number, String UID) {
        this.Last_name = last_name;
        this.First_name = first_name;
        this.username = username;
        this.phone_number = phone_number;
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

    /**
     * Get method to receive the phone number of the user.
     * @return String representing the phone number of the user.
     */
    public String getPhone_number() {
        return phone_number;
    }

    /**
     * Get method to receive the username of the user.
     * @return String representing the username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Change the username method.
     * @param username String with the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Change the phone method.
     * @param phone_number String with the new phone number
     */
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @NonNull
    public String toString(){ // override toString method to better represent user data (logging etc.)
        return "Full Name: " + this.getFirst_name() + this.getLast_name() +  "\n" +
                "Username: " + this.getUsername() +"\n" +
                "Phone Number: " + this.getPhone_number() + "\n" +
                "Permissions: " + this.getPermissions() + "\n" +
                "User ID: " + this.getID() + "\n";
    }
}

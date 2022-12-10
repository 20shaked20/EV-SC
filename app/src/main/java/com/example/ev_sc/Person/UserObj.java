package com.example.ev_sc.Person;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.UUID;

public class UserObj implements PersonObj, Parcelable {
    private String First_name;
    private String Last_name;
    private String username;
    private int Permissions;
    private String phone_number;
    private String UID;

    public UserObj(String first_name, String last_name, String username, String phone_number, String UID, int permissions) {
        this.Last_name = last_name;
        this.First_name = first_name;
        this.username = username;
        this.phone_number = phone_number;
        this.UID = UID;
        this.Permissions = permissions;
    }

    public UserObj(UserObj user) {
        this.First_name = user.getFirst_name();
        this.Last_name = user.getLast_name();
        this.username = user.getUsername();
        this.Permissions = user.getPermissions();
        this.phone_number = user.getPhone_number();
        this.UID = user.getID();
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
     *
     * @return String representing the phone number of the user.
     */
    public String getPhone_number() {
        return phone_number;
    }

    /**
     * Get method to receive the username of the user.
     *
     * @return String representing the username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Change the username method.
     *
     * @param username String with the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Change the phone method.
     *
     * @param phone_number String with the new phone number
     */
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @NonNull
    public String toString() { // override toString method to better represent user data (logging etc.)
        return "Full Name: " + this.getFirst_name() + this.getLast_name() + "\n" +
                "Username: " + this.getUsername() + "\n" +
                "Phone Number: " + this.getPhone_number() + "\n" +
                "Permissions: " + this.getPermissions() + "\n" +
                "User ID: " + this.getID() + "\n";
    }

    // ===============================================
    // All code below is the implementation of the Parcelable interface
    // ===============================================

    public UserObj(Parcel in) {
        readFromParcel(in);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public UserObj createFromParcel(Parcel in) {
            return new UserObj(in);
        }

        public UserObj[] newArray(int size) {
            return new UserObj[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(First_name);
        dest.writeString(Last_name);
        dest.writeString(username);
        dest.writeString(phone_number);
        dest.writeInt(Permissions);
        dest.writeString(UID);
    }

    private void readFromParcel(Parcel in) {

        First_name = in.readString();
        Last_name = in.readString();
        username = in.readString();
        phone_number = in.readString();
        Permissions = in.readInt();
        UID = in.readString();
    }

    // ===============================================
    // End of Parcelable implementation
    // ===============================================


}

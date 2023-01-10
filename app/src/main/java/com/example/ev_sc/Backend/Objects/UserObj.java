package com.example.ev_sc.Backend.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserObj implements PersonObj, Parcelable {
    private String FirstName;
    private String LastName;
    private String UserName;
    private int Permission;
    private String Phone;
    private String UID;

    public UserObj(String first_name, String last_name, String username, String phone_number, String UID, int permissions) {
        this.LastName = last_name;
        this.FirstName = first_name;
        this.UserName = username;
        this.Phone = phone_number;
        this.UID = UID;
        this.Permission = permissions;
    }

    public UserObj(UserObj user) {
        this.FirstName = user.getFirstName();
        this.LastName = user.getLastName();
        this.UserName = user.getUserName();
        this.Permission = user.getPermission();
        this.Phone = user.getPhone();
        this.UID = user.getID();
    }

    @Override
    public String getFirstName() {
        return FirstName;
    }

    @Override
    public String getLastName() {
        return LastName;
    }

    @Override
    public String getID() {
        return this.UID;
    }

    @Override
    public int getPermission() {
        return Permission;
    }

    @Override
    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    @Override
    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    /**
     * Get method to receive the phone number of the user.
     *
     * @return String representing the phone number of the user.
     */
    public String getPhone() {
        return Phone;
    }

    /**
     * Get method to receive the username of the user.
     *
     * @return String representing the username of the user.
     */
    public String getUserName() {
        return UserName;
    }

    /**
     * Change the username method.
     *
     * @param userName String with the new username
     */
    public void setUserName(String userName) {
        this.UserName = userName;
    }

    /**
     * Change the phone method.
     *
     * @param phone String with the new phone number
     */
    public void setPhone(String phone) {
        this.Phone = phone;
    }

    @NonNull
    public String toString() { // override toString method to better represent user data (logging etc.)
        return "Full Name: " + this.getFirstName() + this.getLastName() + "\n" +
                "Username: " + this.getUserName() + "\n" +
                "Phone Number: " + this.getPhone() + "\n" +
                "Permissions: " + this.getPermission() + "\n" +
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
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(UserName);
        dest.writeString(Phone);
        dest.writeInt(Permission);
        dest.writeString(UID);
    }

    private void readFromParcel(Parcel in) {

        FirstName = in.readString();
        LastName = in.readString();
        UserName = in.readString();
        Phone = in.readString();
        Permission = in.readInt();
        UID = in.readString();
    }

    // ===============================================
    // End of Parcelable implementation
    // ===============================================


}

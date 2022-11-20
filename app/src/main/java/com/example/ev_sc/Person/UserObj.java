package com.example.ev_sc.Person;

public class UserObj implements PersonObj {
    public String First_name;
    public String Last_name;
    public String username;
    public int Permissions = 0;
    public String phone_number;
    public String UID;

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
}

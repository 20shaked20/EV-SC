package com.example.ev_sc.Person;

public class AdminObj implements PersonObj {
    public String First_name = "";
    public String Last_name = "";
    public int Permissions = 1;
    public String UID;

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


}

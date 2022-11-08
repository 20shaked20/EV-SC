package com.example.ev_sc.Person;

public class UserObj implements PersonObj{
    public String First_name;
    public String Last_name;
    public String username;
    public int Permissions =0;
    public String phone_number;

    public UserObj(String first_name, String last_name, String username,String phone_number){
        this.Last_name=last_name;
        this.First_name=first_name;
        this.username=username;
        this.phone_number=phone_number;
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
    public int getPermissions() {
        return Permissions;
    }

    @Override
    public void setFirst_name( String first_name) {
      this.First_name=first_name;
    }

    @Override
    public void setLast_name(String last_name) {
        this.Last_name=last_name;
    }


    public String getPhone_number() {
        return phone_number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}

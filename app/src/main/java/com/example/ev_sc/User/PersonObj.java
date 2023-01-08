package com.example.ev_sc.User;

public interface PersonObj {

    /**
     * Get method to return the first name of the person
     * @return String first name
     */
    public String getFirstName();

    /**
     * Get method to return the last name of the person
     * @return String last name
     */
    public String getLastName();

    /**
     * Get method to return the unique ID given by the registration database to the user.
     * @return String ID
     */
    public String getID();

    /**
     * Get method to return the permission type of the user ( 1 -> admin, 0 user .. )
     * @return 1 for admin 0 for user.
     */
    public int getPermission();

    /**
     * Change the first name of the user
     * @param firstName String containing the new desired first name.
     */
    public void setFirstName(String firstName);

    /**
     * Change the last name of the user
     * @param lastName String containing the new desired last name.
     */
    public void setLastName(String lastName);

}

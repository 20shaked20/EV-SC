package com.example.ev_sc.User;

public interface PersonObj {

    /**
     * Get method to return the first name of the person
     * @return String first name
     */
    public String getFirst_name();

    /**
     * Get method to return the last name of the person
     * @return String last name
     */
    public String getLast_name();

    /**
     * Get method to return the unique ID given by the registration database to the user.
     * @return String ID
     */
    public String getID();

    /**
     * Get method to return the permission type of the user ( 1 -> admin, 0 user .. )
     * @return 1 for admin 0 for user.
     */
    public int getPermissions();

    /**
     * Change the first name of the user
     * @param first_name String containing the new desired first name.
     */
    public void setFirst_name(String first_name);

    /**
     * Change the last name of the user
     * @param last_name String containing the new desired last name.
     */
    public void setLast_name(String last_name);

}

package com.example.ev_sc.Backend.Objects;

public interface PersonObj {

    /**
     * Get method to return the first name of the person
     *
     * @return String first name
     */
    String getFirstName();

    /**
     * Get method to return the last name of the person
     *
     * @return String last name
     */
    String getLastName();

    /**
     * Get method to return the unique ID given by the registration database to the user.
     *
     * @return String ID
     */
    String getID();

    /**
     * Get method to return the permission type of the user ( 1 -> admin, 0 user .. )
     *
     * @return 1 for admin 0 for user.
     */
    int getPermission();

    /**
     * Change the first name of the user
     *
     * @param firstName String containing the new desired first name.
     */
    void setFirstName(String firstName);

    /**
     * Change the last name of the user
     *
     * @param lastName String containing the new desired last name.
     */
    void setLastName(String lastName);

    /**
     * Get method to receive the phone number of the user.
     *
     * @return String representing the phone number of the user.
     */
    String getPhone();

    /**
     * Get method to receive the username of the user.
     *
     * @return String representing the username of the user.
     */
    String getUserName();

    /**
     * Change the username method.
     *
     * @param userName String with the new username
     */
    void setUserName(String userName);

    /**
     * Change the phone method.
     *
     * @param phone String with the new phone number
     */
    void setPhone(String phone);
}

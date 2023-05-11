/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;


public class User {
    
    private int id;
    private String name;
    private String user;
    private String password;
    private String email;

    /**
     * @return the userid
     */
    public int getId() {
        return id;
    }

    /**
     * @param userid the userid to set
     */
    public void setId(int userid) {
        this.id = userid;
    }

    /**
     * @return the firstName
     */
    public String getName() {
        return name;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the lastName
     */
    public String getUser() {
        return user;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the dob
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param dob the dob to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    
    
    
}

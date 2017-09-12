package com.tonny.androidcodetesttonnybaya.home.model;

/**
 * Contact Pojo Object.
 *
 * @author tonnbaya@yahoo.co.uk
 */

public class Contact {
    private String m_firstName;
    private String m_lastName;
    private int m_id;

    public Contact(int id, String firstName, String lastName) {
        this.m_firstName = firstName;
        this.m_lastName = lastName;
        this.m_id = id;
    }

    public String getFirstName() {
        return this.m_firstName;
    }

    public String getLastName() {
        return this.m_lastName;
    }

    public String getInitials(){
        return "SB";
    }

    public int getId(){
        return this.m_id;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

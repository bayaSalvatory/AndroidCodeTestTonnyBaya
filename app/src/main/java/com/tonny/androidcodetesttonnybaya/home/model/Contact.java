package com.tonny.androidcodetesttonnybaya.home.model;

/**
 * Contact Pojo Object.
 *
 * @author tonnbaya@yahoo.co.uk
 */

public class Contact {
    private String m_firstName;
    private String m_lastName;

    public Contact(String firstName, String lastName) {
        this.m_firstName = firstName;
        this.m_lastName = lastName;
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

    @Override
    public String toString() {
        return super.toString();
    }
}

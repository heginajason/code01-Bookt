package com.ust.bookt.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Jace on 3/26/2017.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class User {
    private String displayName;
    private String uid;
    private String contactNumber;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}

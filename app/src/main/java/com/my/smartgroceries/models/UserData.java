package com.my.smartgroceries.models;

public class UserData {
    String uid;
    String name;
    String contact;
    String email;
    Boolean isVendor;
    String refStoreData;

    public UserData(String uid, String name, String contact, String email, Boolean isVendor, String refStoreData) {
        this.uid = uid;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.isVendor = isVendor;
        this.refStoreData = refStoreData;
    }

    public UserData() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getVendor() {
        return isVendor;
    }

    public void setVendor(Boolean vendor) {
        isVendor = vendor;
    }

    public String getRefStoreData() {
        return refStoreData;
    }

    public void setRefStoreData(String refStoreData) {
        this.refStoreData = refStoreData;
    }
}

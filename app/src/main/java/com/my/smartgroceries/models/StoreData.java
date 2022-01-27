package com.my.smartgroceries.models;

public class StoreData {
    String id;
    String name;
    String address;
    String pincode;
    String ownerId;

    public StoreData() {
    }

    public StoreData(String id, String name, String address, String pincode, String ownerId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.pincode = pincode;
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}

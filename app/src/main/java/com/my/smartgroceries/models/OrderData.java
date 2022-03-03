package com.my.smartgroceries.models;

public class OrderData {
    String id;
    String itemtotal;
    String address;
    String storeid;
    String userid;
    String status;
    //Use "OrderList" Child to Add the ArrayList of the Products in the Order

    public OrderData() {
    }
    public OrderData(String id, String itemtotal, String address, String storeid, String userid, String status) {
        this.id = id;
        this.itemtotal = itemtotal;
        this.address = address;
        this.storeid = storeid;
        this.userid = userid;
        this.status = status;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getItemtotal() {
        return itemtotal;
    }
    public void setItemtotal(String itemtotal) {
        this.itemtotal = itemtotal;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getStoreid() {
        return storeid;
    }
    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}

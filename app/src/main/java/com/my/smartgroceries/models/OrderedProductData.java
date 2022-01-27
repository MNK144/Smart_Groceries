package com.my.smartgroceries.models;

public class OrderedProductData {
    String prodrefid;
    int quantity;

    public OrderedProductData(String prodrefid, int quantity) {
        this.prodrefid = prodrefid;
        this.quantity = quantity;
    }

    public OrderedProductData() {
    }

    public String getProdrefid() {
        return prodrefid;
    }

    public void setProdrefid(String prodrefid) {
        this.prodrefid = prodrefid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

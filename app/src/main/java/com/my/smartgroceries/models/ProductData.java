package com.my.smartgroceries.models;

import com.google.firebase.database.Exclude;

public class ProductData {
    String id;
    String name;
    String price;
    String measure;
    String quantity;
    String barcode; //not priority
    String imageurl; //future work

    public ProductData(String id, String name, String price, String measure, String quantity, String barcode, String imageurl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.measure = measure;
        this.quantity = quantity;
        this.barcode = barcode;
        this.imageurl = imageurl;
    }

    public ProductData() {
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }


    @Exclude private int selectedQuantity;
    @Exclude public int getSelectedQuantity() { return selectedQuantity; }
    @Exclude public void setSelectedQuantity(int selectedQuantity) { this.selectedQuantity=selectedQuantity; }

}

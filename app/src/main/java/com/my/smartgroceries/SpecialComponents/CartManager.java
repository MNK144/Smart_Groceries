package com.my.smartgroceries.SpecialComponents;

import com.my.smartgroceries.models.ProductData;
import java.util.ArrayList;
import java.util.List;

public class CartManager {

    //Singleton Class
    private static CartManager obj;
    private CartManager() { orderList=new ArrayList<>();storeid="NULL";total=0; }
    public static CartManager getInstance()
    {
        if(obj==null)
            obj = new CartManager();
        return obj;
    }

    //Main Function
    private List<ProductData> orderList;
    private String storeid;
    public void init(String storeid)
    {
        orderList.clear();
        total=0;
        this.storeid = storeid;
    }
    public List<ProductData> getOrderList()
    {
        return orderList;
    }
    public String getStoreId()
    {
        return storeid;
    }
    public void removeStoreID()
    {
        storeid = "NULL";
        total = 0;
    }
    public boolean isCartEmpty()
    {
        return storeid.equals("NULL");
    }

    //Item Total Management
    private int total;
    public void addToTotal(int n)
    {
        total = total + n;
    }
    public void removeFromTotal(int n)
    {
        total = total - n;
    }
    public int getTotal()
    {
        return total;
    }
}

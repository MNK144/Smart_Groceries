package com.my.smartgroceries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.smartgroceries.SpecialComponents.CartManager;
import com.my.smartgroceries.account.UserManager;
import com.my.smartgroceries.models.OrderData;
import com.my.smartgroceries.models.ProductData;
import com.my.smartgroceries.models.StoreData;

import java.util.List;

public class OrderConfirmActivity extends AppCompatActivity {

    ImageView storeImage;
    TextView storeName,storeAddress;

    TextView itemTotal;
    EditText address;
    String saddress;
    Button orderButton;

    String id;
    CartManager cartManager;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        cartManager = CartManager.getInstance();
        id = cartManager.getStoreId();
        databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_ORDERDATA);

        storeImage = findViewById(R.id.storeimage);
        storeName = findViewById(R.id.storename);
        storeAddress = findViewById(R.id.storeaddr);
        itemTotal = findViewById(R.id.itemtotal);
        address = findViewById(R.id.orderaddr);
        orderButton = findViewById(R.id.placeorderbtn);

        setStoreData();
        itemTotal.setText(CONST.RUPEES_SYMBOL + cartManager.getTotal());
        orderButton.setOnClickListener(view -> {
            saddress = address.getText().toString();
            if(saddress.isEmpty())
                Toast.makeText(getApplicationContext(),"Address is Required",Toast.LENGTH_LONG).show();
            else
            {
                //Send Data to DB
                String id = databaseReference.push().getKey();
                OrderData orderData = new OrderData(id,String.valueOf(cartManager.getTotal()),saddress,cartManager.getStoreId(),UserManager.getUID(),CONST.ORDERSTATUS_PLACED);
                databaseReference.child(id).setValue(orderData).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(),"Order Placed Successfully",Toast.LENGTH_LONG).show();
                        uploadOrderList(id);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Error Placing Order",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        });
    }

    private void setStoreData()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_STOREDATA).child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StoreData storeData = snapshot.getValue(StoreData.class);
                storeName.setText(storeData.getName());
                storeAddress.setText(storeData.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to fetch the store data",Toast.LENGTH_LONG).show();
            }
        });
    }


    private void uploadOrderList(String id)
    {
        List<ProductData> orderList;
        orderList = cartManager.getOrderList();
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(CONST.DB_ORDERDATA).child(id).child(CONST.DB_ORDERDATA_ORDERLIST);
        for(ProductData data : orderList)
        {
            String pid = databaseReference1.push().getKey();
            ProductData pdata = new ProductData(pid,data.getName(),data.getPrice(),data.getMeasure(),String.valueOf(data.getSelectedQuantity()),data.getBarcode(),data.getImageurl());
            databaseReference1.child(pid).setValue(pdata);
        }
        cartManager.init("NULL");
        finish();
    }
}

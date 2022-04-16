package com.my.smartgroceries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.smartgroceries.adapters.OrderViewProductAdapter;
import com.my.smartgroceries.models.OrderData;
import com.my.smartgroceries.models.ProductData;
import com.my.smartgroceries.models.StoreData;
import com.my.smartgroceries.models.UserData;

import java.util.ArrayList;
import java.util.List;

public class VendorOrderViewActivity extends AppCompatActivity {

    String orderid,userid;
    OrderData orderData;
    UserData userData;
    List<ProductData> productList = new ArrayList<>();

    //UI
    TextView custName,custContact,deliveryAddress,itemtotal,orderStatus;
    RecyclerView recyclerView;
    OrderViewProductAdapter adapter;
    Button orderAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_order_view);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=#282a35>" + "View Order Details" + "</font>"));

        custName = findViewById(R.id.custname);
        custContact = findViewById(R.id.custcontact);
        deliveryAddress = findViewById(R.id.orderaddr);
        itemtotal = findViewById(R.id.itemtotal);
        orderStatus = findViewById(R.id.orderstatus);
        recyclerView = findViewById(R.id.orderproductlist);
        orderAction = findViewById(R.id.orderActionBtn);

        orderid = getIntent().getStringExtra("id");
        FirebaseDatabase.getInstance().getReference(CONST.DB_ORDERDATA).child(orderid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderData = snapshot.getValue(OrderData.class);
                userid = orderData.getUserid();
                itemtotal.setText(CONST.RUPEES_SYMBOL+orderData.getItemtotal());
                orderStatus.setText(orderData.getStatus());
                deliveryAddress.setText(orderData.getAddress());
                if(orderData.getStatus().equals(CONST.ORDERSTATUS_DELIVERED)||orderData.getStatus().equals(CONST.ORDERSTATUS_CANCELED))
                {
                    orderAction.setEnabled(false);
                }
                else
                {
                    orderAction.setEnabled(true);
                }
                orderAction.setOnClickListener(view -> {
                    final Dialog dialog = new Dialog(VendorOrderViewActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialogue_update_order_status);
                    Button completed = dialog.findViewById(R.id.dialog_ok);
                    Button cancelled = dialog.findViewById(R.id.dialog_cancel);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_ORDERDATA).child(orderid);
                    completed.setOnClickListener(view1 -> {
                        view1.setEnabled(false);
                        databaseReference.child("status").setValue(CONST.ORDERSTATUS_DELIVERED)
                                .addOnCompleteListener(task -> {
                                    if(!task.isSuccessful())
                                        Toast.makeText(getApplicationContext(),"Failed to Update Data",Toast.LENGTH_SHORT).show();
                                    else
                                    {
                                        view.setEnabled(false);
                                        orderStatus.setText(CONST.ORDERSTATUS_DELIVERED);
                                    }
                                    dialog.dismiss();
                                });
                    });
                    cancelled.setOnClickListener(view1 -> {
                        view1.setEnabled(false);
                        databaseReference.child("status").setValue(CONST.ORDERSTATUS_CANCELED)
                                .addOnCompleteListener(task -> {
                                    if(!task.isSuccessful())
                                        Toast.makeText(getApplicationContext(),"Failed to Update Data",Toast.LENGTH_SHORT).show();
                                    else
                                    {
                                        view.setEnabled(false);
                                        orderStatus.setText(CONST.ORDERSTATUS_CANCELED);
                                    }
                                    dialog.dismiss();
                                });
                    });
                    dialog.show();
                });
                setUserData();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to Load Order Data",Toast.LENGTH_LONG).show();
            }
        });

        //Adding product data list
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(VendorOrderViewActivity.this));
        adapter = new OrderViewProductAdapter(productList,VendorOrderViewActivity.this);
        recyclerView.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference(CONST.DB_ORDERDATA).child(orderid).child(CONST.DB_ORDERDATA_ORDERLIST)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1: snapshot.getChildren())
                        {
                            ProductData productData = snapshot1.getValue(ProductData.class);
                            productList.add(productData);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(),"Failed to Load Order Product Data",Toast.LENGTH_LONG).show();
                    }
                });
    }
    void setUserData()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_USERDATA).child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userData = snapshot.getValue(UserData.class);
                custName.setText("Name: "+userData.getName());
                custContact.setText("Contact: "+userData.getContact());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to fetch the store data",Toast.LENGTH_LONG).show();
            }
        });
    }
}

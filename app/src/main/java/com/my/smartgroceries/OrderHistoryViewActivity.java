package com.my.smartgroceries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
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

public class OrderHistoryViewActivity extends AppCompatActivity {

    String orderid,storeid;
    OrderData orderData;
    StoreData storeData;
    List<ProductData> productList = new ArrayList<>();

    //UI
    ImageView storeImage;
    TextView storeName,storeAddress;
    TextView deliveryAddress,itemtotal,orderStatus;
    RecyclerView recyclerView;
    OrderViewProductAdapter adapter;
    Button orderAction; //If order is pending then 'Cancel', if order is completed then 'Contact Vendor'
    boolean isActionCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_view);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=#282a35>" + "View Order Details" + "</font>"));

        storeImage = findViewById(R.id.storeimage);
        storeName = findViewById(R.id.storename);
        storeAddress = findViewById(R.id.storeaddr);
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
                storeid = orderData.getStoreid();
                itemtotal.setText(CONST.RUPEES_SYMBOL+orderData.getItemtotal());
                orderStatus.setText(orderData.getStatus());
                deliveryAddress.setText(orderData.getAddress());
                storeName.setText(orderData.getStorename());
                if(orderData.getStatus().equals(CONST.ORDERSTATUS_DELIVERED)||orderData.getStatus().equals(CONST.ORDERSTATUS_CANCELED))
                {
                    orderAction.setText("Call Vendor");
                    isActionCall = true;
                }
                else
                {
                    orderAction.setText("Cancel Order");
                    isActionCall = false;
                }
                setStoreData();
                orderAction.setOnClickListener(view -> {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_ORDERDATA).child(orderid);
                    if(isActionCall)
                    {
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(CONST.DB_STOREDATA).child(orderData.getStoreid());
                        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                StoreData storeData = snapshot.getValue(StoreData.class);
                                String refUser = storeData.getOwnerId();
                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference(CONST.DB_USERDATA).child(refUser);
                                databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        UserData userData = snapshot.getValue(UserData.class);
                                        String contact = userData.getContact();

                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:"+contact));
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getApplicationContext(),"Failed to Load Data",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(),"Failed to Load Data",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        final Dialog dialog = new Dialog(OrderHistoryViewActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialogue_remove_item);
                        Button ok = dialog.findViewById(R.id.dialog_ok);
                        Button cancel = dialog.findViewById(R.id.dialog_cancel);
                        TextView title = dialog.findViewById(R.id.title);
                        TextView message = dialog.findViewById(R.id.message);
                        title.setText("Are you sure want to Cancel this Order?");
                        message.setText("Dispatched it will be delivered");
                        cancel.setOnClickListener(view1 -> dialog.dismiss());
                        ok.setOnClickListener(view1 -> {
                            databaseReference.child("status").setValue(CONST.ORDERSTATUS_CANCELED)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(),"Order Has Been Cancelled",Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(),"Failed to Update Data",Toast.LENGTH_SHORT).show();
                                        }
                                        dialog.dismiss();
                                        orderAction.setText("Call Vendor");
                                        isActionCall = true;
                                        orderStatus.setText(CONST.ORDERSTATUS_CANCELED);
                                    });
                        });
                        dialog.show();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to Load Order Data",Toast.LENGTH_LONG).show();
            }
        });

        //Adding product data list
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistoryViewActivity.this));
        adapter = new OrderViewProductAdapter(productList,OrderHistoryViewActivity.this);
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

    private void setStoreData()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_STOREDATA).child(storeid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storeData = snapshot.getValue(StoreData.class);
                storeAddress.setText(storeData.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to fetch the store data",Toast.LENGTH_LONG).show();
            }
        });
    }
}

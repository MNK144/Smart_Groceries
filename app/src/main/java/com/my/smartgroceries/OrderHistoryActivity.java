package com.my.smartgroceries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.smartgroceries.account.UserManager;
import com.my.smartgroceries.adapters.OrderAdapter;
import com.my.smartgroceries.models.OrderData;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    List<OrderData> orderList = new ArrayList<>();
    DatabaseReference databaseReference;
    boolean isVendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=#282a35>" + "Order History" + "</font>"));

        isVendor = getIntent().getBooleanExtra("isVendor",false);

        recyclerView = findViewById(R.id.orderHistoryList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        orderAdapter = new OrderAdapter(orderList,OrderHistoryActivity.this,isVendor);
        recyclerView.setAdapter(orderAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_ORDERDATA);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    OrderData data = snapshot1.getValue(OrderData.class);
                    if(isVendor)
                    {
                        if(data.getStoreid().equals(UserManager.getUserData().getRefStoreData()))
                            orderList.add(data);
                    }
                    else
                    {
                        if(data.getUserid().equals(UserManager.getUID()))
                            orderList.add(data);
                    }
                }
                orderAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to Load Data",Toast.LENGTH_LONG).show();
            }
        });
    }
}

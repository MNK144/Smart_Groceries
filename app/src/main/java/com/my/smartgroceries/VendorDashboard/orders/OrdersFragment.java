package com.my.smartgroceries.VendorDashboard.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.smartgroceries.CONST;
import com.my.smartgroceries.OrderHistoryActivity;
import com.my.smartgroceries.R;
import com.my.smartgroceries.account.UserManager;
import com.my.smartgroceries.adapters.OrderAdapter;
import com.my.smartgroceries.models.OrderData;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    List<OrderData> orderList = new ArrayList<>();
    DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vendor_orders, container, false);

        recyclerView = root.findViewById(R.id.vendorOrders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(orderList, getContext());
        recyclerView.setAdapter(orderAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_ORDERDATA);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    OrderData data = snapshot1.getValue(OrderData.class);
                    if(data.getStoreid().equals(UserManager.getUserData().getRefStoreData()))
                        orderList.add(data);
                }
                orderAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Failed to Load Data",Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }
}
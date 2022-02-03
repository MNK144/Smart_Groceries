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
import com.my.smartgroceries.R;
import com.my.smartgroceries.SpecialComponents.CartManager;
import com.my.smartgroceries.adapters.ProductAdapter;
import com.my.smartgroceries.models.ProductData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreProductsActivity extends AppCompatActivity {
    String id;
    String name;
    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    List<ProductData> dataList = new ArrayList<>();
    HashMap<String,Integer> orderListQuantity = new HashMap<>();

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_products);
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(Html.fromHtml("<font color=#282a35>" + name + "</font>"));

        recyclerView = findViewById(R.id.productlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(StoreProductsActivity.this));
        productAdapter = new ProductAdapter(dataList,StoreProductsActivity.this,id,false);
        recyclerView.setAdapter(productAdapter);

        if(CartManager.getInstance().getStoreId().equals(id)) loadOrderListQuantity();

        databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_PRODUCTDATA).child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    ProductData data = snapshot1.getValue(ProductData.class);
                    String id = data.getId();
                    if(orderListQuantity.containsKey(id))
                        data.setSelectedQuantity(orderListQuantity.get(id));
                    dataList.add(data);
                }
                productAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to fetch the product data",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadOrderListQuantity()
    {
        for(ProductData data: CartManager.getInstance().getOrderList())
        {
            orderListQuantity.put(data.getId(),data.getSelectedQuantity());
        }
    }
}

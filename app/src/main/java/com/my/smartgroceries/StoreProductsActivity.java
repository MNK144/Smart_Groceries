package com.my.smartgroceries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
        //getSupportActionBar().setTitle(Html.fromHtml("<font color=#282a35>" + name + "</font>"));

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
                dataList.clear();
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

        manageActionBar();
    }

    private void loadOrderListQuantity()
    {
        for(ProductData data: CartManager.getInstance().getOrderList())
        {
            orderListQuantity.put(data.getId(),data.getSelectedQuantity());
        }
    }



    //Handling Custom Made ActionBar
    TextView actionbarText;
    EditText actionbarEditText;
    ImageView actionbarSearch;
    boolean flagSearch = true;
    String forSearch = "Search Results for ";
    String searchQuery=CONST.STATUS_NULL;

    private void manageActionBar()
    {
        actionbarText = findViewById(R.id.actionbarMainText);
        actionbarText.setText(name);
        actionbarEditText = findViewById(R.id.actionbarMainEditText);
        actionbarSearch = findViewById(R.id.actionBarSearch);
        actionbarSearch.setOnClickListener(view -> {
            if(flagSearch)
            {
                actionbarText.setVisibility(View.GONE);
                actionbarEditText.setVisibility(View.VISIBLE);
                actionbarSearch.setImageResource(R.drawable.clear);
                actionbarEditText.requestFocus();
            }
            else
            {
                searchQuery=CONST.STATUS_NULL;
                actionbarEditText.setText("");
                actionbarText.setText(name);
                actionbarText.setVisibility(View.VISIBLE);
                actionbarEditText.setVisibility(View.GONE);
                actionbarSearch.setImageResource(R.drawable.search);
                productAdapter.setFilters(searchQuery);
            }
            flagSearch = !flagSearch;
        });
        actionbarEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void performSearch()
    {
        actionbarEditText.clearFocus();
        InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(actionbarEditText.getWindowToken(), 0);

        searchQuery = actionbarEditText.getText().toString();
        actionbarText.setText(forSearch + searchQuery);
        actionbarText.setVisibility(View.VISIBLE);
        actionbarEditText.setVisibility(View.GONE);

        //Trigger to update RecyclerView
        productAdapter.setFilters(searchQuery);
    }
}

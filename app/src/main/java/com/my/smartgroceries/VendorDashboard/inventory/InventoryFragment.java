package com.my.smartgroceries.VendorDashboard.inventory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.my.smartgroceries.AddProductActivity;
import com.my.smartgroceries.CONST;
import com.my.smartgroceries.R;
import com.my.smartgroceries.account.UserManager;
import com.my.smartgroceries.adapters.InventoryAdapter;
import com.my.smartgroceries.models.ProductData;

import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {

    private List<ProductData> inventoryList = new ArrayList<>();
    private InventoryAdapter inventoryAdapter;

    private Button addItem;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vendor_inventory, container, false);

        RecyclerView recyclerView;
        recyclerView = root.findViewById(R.id.inventory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        inventoryAdapter = new InventoryAdapter(inventoryList,getContext());
        recyclerView.setAdapter(inventoryAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_PRODUCTDATA).child(UserManager.getUserData().getRefStoreData());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                inventoryList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    ProductData data = snapshot1.getValue(ProductData.class);
                    inventoryList.add(data);
                }
                inventoryAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Failed to Load Data",Toast.LENGTH_LONG).show();
            }
        });

        addItem = root.findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddProductActivity.class);
                getContext().startActivity(i);
            }
        });

        manageActionBar(root);
        return root;
    }


    //Handling Custom Made ActionBar
    private TextView actionbarText;
    private EditText actionbarEditText;
    private ImageView actionbarSearch;
    private boolean flagSearch = true;
    private String forSearch = "Search Results for ";
    private String searchQuery=CONST.STATUS_NULL;
    private void manageActionBar(View root)
    {
        actionbarText = root.findViewById(R.id.actionbarMainText);
        actionbarText.setText("Store Inventory");
        actionbarEditText = root.findViewById(R.id.actionbarMainEditText);
        actionbarEditText.setHint("Search Inventory");
        actionbarSearch = root.findViewById(R.id.actionBarSearch);
        actionbarSearch.setOnClickListener(view -> {
            if(flagSearch) {
                actionbarText.setVisibility(View.GONE);
                actionbarEditText.setVisibility(View.VISIBLE);
                actionbarSearch.setImageResource(R.drawable.clear);
                actionbarEditText.requestFocus();
            } else {
                searchQuery=CONST.STATUS_NULL;
                actionbarEditText.setText("");
                actionbarText.setText("Store Inventory");
                actionbarText.setVisibility(View.VISIBLE);
                actionbarEditText.setVisibility(View.GONE);
                actionbarSearch.setImageResource(R.drawable.search);
                inventoryAdapter.setFilters(searchQuery);
            }
            flagSearch = !flagSearch;
        });
        actionbarEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
    }
    private void performSearch()
    {
        actionbarEditText.clearFocus();
        InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(actionbarEditText.getWindowToken(), 0);
        searchQuery = actionbarEditText.getText().toString();
        actionbarText.setText(forSearch + searchQuery);
        actionbarText.setVisibility(View.VISIBLE);
        actionbarEditText.setVisibility(View.GONE);
        //Trigger to update RecyclerView
        inventoryAdapter.setFilters(searchQuery);
    }
}
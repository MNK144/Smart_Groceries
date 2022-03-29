package com.my.smartgroceries.CustomerDashboard.home;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.smartgroceries.CONST;
import com.my.smartgroceries.R;
import com.my.smartgroceries.adapters.StoreAdapter;
import com.my.smartgroceries.models.StoreData;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView storelist;
    private StoreAdapter storeAdapter;
    private List<StoreData> dataList = new ArrayList<>();
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_customer_home, container, false);
        storelist = root.findViewById(R.id.storelist);
        storelist.setHasFixedSize(true);
        storelist.setLayoutManager(new LinearLayoutManager(getContext()));
        storeAdapter = new StoreAdapter(dataList,getContext());
        storelist.setAdapter(storeAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_STOREDATA);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()) {
                    StoreData storeData = snapshot1.getValue(StoreData.class);
                    if(storeData.getIsActive())
                        dataList.add(storeData);
                }
                storeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Failed to Fetch Store Details",Toast.LENGTH_LONG).show();
            }
        });

        actionbarText = root.findViewById(R.id.actionbarMainText);
        actionbarEditText = root.findViewById(R.id.actionbarMainEditText);
        actionbarSearch = root.findViewById(R.id.actionBarSearch);
        actionbarEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        actionbarEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
        manageActionBar();

        return root;
    }

    //Handling Custom Made ActionBar
    TextView actionbarText;
    EditText actionbarEditText;
    ImageView actionbarSearch;
    boolean flagSearch = true;
    String forPinCode = "Stores located in ";
    String searchQuery=CONST.STATUS_NULL;

    private void manageActionBar()
    {
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
                actionbarText.setText("All Grocery Stores");
                actionbarText.setVisibility(View.VISIBLE);
                actionbarEditText.setVisibility(View.GONE);
                actionbarSearch.setImageResource(R.drawable.search);
                storeAdapter.setFilters(searchQuery);
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
        InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(actionbarEditText.getWindowToken(), 0);

        searchQuery = actionbarEditText.getText().toString();
        actionbarText.setText(forPinCode + searchQuery);
        actionbarText.setVisibility(View.VISIBLE);
        actionbarEditText.setVisibility(View.GONE);

        //Trigger to update RecyclerView
        storeAdapter.setFilters(searchQuery);
    }
}
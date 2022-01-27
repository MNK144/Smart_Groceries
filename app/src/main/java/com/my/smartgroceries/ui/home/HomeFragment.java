package com.my.smartgroceries.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        storelist = root.findViewById(R.id.storelist);
        storelist.setHasFixedSize(true);
        storelist.setLayoutManager(new LinearLayoutManager(getContext()));
        storeAdapter = new StoreAdapter(dataList,getContext());
        storelist.setAdapter(storeAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_STOREDATA);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()) {
                    StoreData storeData = snapshot1.getValue(StoreData.class);
                    dataList.add(storeData);
                }
                storeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Failed to Fetch Store Details",Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }
}
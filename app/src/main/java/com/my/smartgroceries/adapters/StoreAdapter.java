package com.my.smartgroceries.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.smartgroceries.CONST;
import com.my.smartgroceries.R;
import com.my.smartgroceries.StoreProductsActivity;
import com.my.smartgroceries.models.StoreData;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyHolder> {

    private List<StoreData> dataList;
    private List<StoreData> filteredDataList;
    private boolean isFiltered;
    private Context context;

    public StoreAdapter(List<StoreData> dataList,Context context) {
        this.dataList = dataList;
        this.context = context;
        isFiltered = false;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.storelisting,parent,false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder,final int position) {
        final StoreData data = (isFiltered)?filteredDataList.get(position):dataList.get(position);
        holder.name.setText(data.getName());
        holder.addr.setText(data.getAddress());
        holder.itemView.setOnClickListener(view -> {
            //Handle Store Listing to Product Listing Activity
            Intent i = new Intent(context, StoreProductsActivity.class);
            i.putExtra("id",data.getId());
            i.putExtra("name",data.getName());
            context.startActivity(i);
        });
    }
    @Override
    public int getItemCount() {
        return (isFiltered)?filteredDataList.size():dataList.size();
    }
    class MyHolder extends RecyclerView.ViewHolder
    {
        TextView name,addr;
        ImageView image;
        View itemView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.storeimage);
            name = itemView.findViewById(R.id.storename);
            addr = itemView.findViewById(R.id.storeaddr);
            this.itemView = itemView;
        }
    }

    //Utilities
    public void setFilters(String searchQuerry)
    {
        if(searchQuerry.equals(CONST.STATUS_NULL))
            isFiltered=false;
        else
        {
            isFiltered=true;
            filteredDataList = new ArrayList<>();
            for(StoreData data : dataList)
            {
                if(data.getPincode().contains(searchQuerry))
                    filteredDataList.add(data);
            }
        }
        notifyDataSetChanged();
    }
}

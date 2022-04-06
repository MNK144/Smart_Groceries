package com.my.smartgroceries.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.smartgroceries.CONST;
import com.my.smartgroceries.R;
import com.my.smartgroceries.models.ProductData;

import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.MyHolder> {

    private List<ProductData> dataList;
    private List<ProductData> filteredDataList;
    private boolean isFiltered=false;
    private Context context;

    public InventoryAdapter(List<ProductData> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventorylisting,parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder,final int position) {
        ProductData data = (isFiltered)?filteredDataList.get(position):dataList.get(position);
        holder.name.setText(data.getName());
        holder.measure.setText(data.getMeasure());
        holder.price.setText(CONST.RUPEES_SYMBOL +data.getPrice());
        holder.stock.setText("Stock: "+data.getQuantity());
        holder.edit.setOnClickListener(view -> {

        });
        holder.remove.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return (isFiltered)?filteredDataList.size():dataList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name,measure,price,stock;
        ImageButton edit,remove;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.productimage);
            name = itemView.findViewById(R.id.productname);
            measure = itemView.findViewById(R.id.productmeasure);
            price = itemView.findViewById(R.id.productprice);
            stock = itemView.findViewById(R.id.productQty);
            edit = itemView.findViewById(R.id.inventoryEdit);
            remove = itemView.findViewById(R.id.inventoryRemove);
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
            Log.d("InventoryFilter","Run");
            for(ProductData data : dataList)
            {
                if(data.getName().toLowerCase().contains(searchQuerry.toLowerCase()))
                    filteredDataList.add(data);
            }
        }
        notifyDataSetChanged();
    }

}

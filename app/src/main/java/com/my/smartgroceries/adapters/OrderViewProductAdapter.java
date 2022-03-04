package com.my.smartgroceries.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.smartgroceries.CONST;
import com.my.smartgroceries.R;
import com.my.smartgroceries.SpecialComponents.CartManager;
import com.my.smartgroceries.SpecialComponents.CartUpdateListener;
import com.my.smartgroceries.models.ProductData;

import java.util.ArrayList;
import java.util.List;

public class OrderViewProductAdapter extends RecyclerView.Adapter<OrderViewProductAdapter.MyHolder> {

    private List<ProductData> dataList;
    private Context context;

    public OrderViewProductAdapter(List<ProductData> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderproductlisting,parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder,final int position) {
        ProductData data = dataList.get(position);
        holder.name.setText(data.getName());
        holder.measure.setText(data.getMeasure());
        holder.price.setText(CONST.RUPEES_SYMBOL +data.getPrice());
        holder.producttotal.setText("₹" + data.getPrice() + " x " +data.getQuantity()+ " = ₹" + (Integer.parseInt(data.getPrice())*Integer.parseInt(data.getQuantity())) );
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name,measure,price,producttotal;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.productimage);
            name = itemView.findViewById(R.id.productname);
            measure = itemView.findViewById(R.id.productmeasure);
            price = itemView.findViewById(R.id.productprice);
            producttotal = itemView.findViewById(R.id.producttotal);
        }
    }

}

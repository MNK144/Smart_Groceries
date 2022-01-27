package com.my.smartgroceries.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.my.smartgroceries.R;
import com.my.smartgroceries.models.OrderedProductData;
import com.my.smartgroceries.models.ProductData;
import java.util.HashMap;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {

    List<ProductData> dataList;
    HashMap<Integer,OrderedProductData> orderList = new HashMap<>();
    private Context context;

    public ProductAdapter(List<ProductData> dataList,Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productlisting,parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder,final int position) {
        ProductData data = dataList.get(position);
        holder.name.setText(data.getName());
        holder.measure.setText(data.getMeasure());
        holder.price.setText(data.getPrice()+"₹");

        //Handling Current Qty Selection
        OrderedProductData orderedProductData = orderList.get(position);
        if(!orderList.containsKey(position))
        {
            holder.plus.setVisibility(View.GONE);
            holder.minus.setVisibility(View.GONE);
            holder.qty.setText("Add");
        }
        else
        {
            holder.plus.setVisibility(View.VISIBLE);
            holder.minus.setVisibility(View.VISIBLE);
            holder.qty.setText(orderedProductData.getQuantity());
        }


        holder.add.setOnClickListener(view -> {
            //Action
            if(!orderList.containsKey(position)) {
                OrderedProductData orderedProductData1 = new OrderedProductData(data.getId(),1);
                orderList.put(position, orderedProductData1);
                holder.plus.setVisibility(View.VISIBLE);
                holder.minus.setVisibility(View.VISIBLE);
                holder.qty.setText("1");
            }
        });

        holder.plus.setOnClickListener(view -> {
            OrderedProductData d = orderList.get(position);
            if(d.getQuantity() == Integer.valueOf(data.getQuantity())) {
                Toast.makeText(context,"Can not add more than available in Stock",Toast.LENGTH_SHORT).show();
            } else {
                d.setQuantity(d.getQuantity() + 1);
                holder.qty.setText("" + d.getQuantity());
            }
        });
        holder.minus.setOnClickListener(view -> {
            OrderedProductData d = orderList.get(position);
            d.setQuantity(d.getQuantity()-1);
            if(d.getQuantity()==0) {
                orderList.remove(position);
                holder.plus.setVisibility(View.GONE);
                holder.minus.setVisibility(View.GONE);
                holder.qty.setText("Add");
            } else {
                holder.qty.setText(""+d.getQuantity());
            }

        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name,measure,price,qty;
        View add;
        Button plus,minus;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.productimage);
            name = itemView.findViewById(R.id.productname);
            measure = itemView.findViewById(R.id.productmeasure);
            price = itemView.findViewById(R.id.productprice);
            qty = itemView.findViewById(R.id.productqty);
            add = itemView.findViewById(R.id.productbutton);
            plus = itemView.findViewById(R.id.productplus);
            minus = itemView.findViewById(R.id.productminus);
        }
    }
}

package com.my.smartgroceries.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.my.smartgroceries.OrderHistoryViewActivity;
import com.my.smartgroceries.R;
import com.my.smartgroceries.models.OrderData;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyHolder> {

    List<OrderData> orderList;
    Context context;

    public OrderAdapter(List<OrderData> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlisting,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        OrderData orderData = orderList.get(position);
        holder.orderStoreName.setText(orderData.getStorename());
        holder.orderStatus.setText(orderData.getStatus());
        holder.orderItems.setText("Order Items: "+orderData.getItemcount());
        holder.orderTotal.setText("Order Total: ₹"+orderData.getItemtotal());
        holder.orderViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"Coming Soon...",Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, OrderHistoryViewActivity.class);
                i.putExtra("id",orderData.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView orderStoreName,orderStatus,orderItems,orderTotal;
        Button orderViewButton;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            orderStoreName = itemView.findViewById(R.id.orderStoreName);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderItems = itemView.findViewById(R.id.orderItems);
            orderTotal = itemView.findViewById(R.id.orderTotal);
            orderViewButton = itemView.findViewById(R.id.orderViewBtn);
        }
    }
}

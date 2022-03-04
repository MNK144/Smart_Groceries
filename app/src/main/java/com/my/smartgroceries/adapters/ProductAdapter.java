package com.my.smartgroceries.adapters;

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
import com.my.smartgroceries.models.StoreData;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {

    private List<ProductData> dataList;
    private List<ProductData> orderList;
    private List<ProductData> filteredDataList;
    private boolean isFiltered;
    private String storeid;
    private CartManager cartManager;
    private Context context;

    private boolean isAdapterForCart;
    private CartUpdateListener cartUpdateListener;

    public ProductAdapter(List<ProductData> dataList,Context context,String storeid,boolean isAdapterForCart) {
        this.dataList = dataList;
        this.context = context;
        this.storeid = storeid;
        this.isAdapterForCart = isAdapterForCart;
        cartManager = CartManager.getInstance();
        orderList = cartManager.getOrderList();
    }

    public void setCartUpdateListener(CartUpdateListener cartUpdateListener)
    {
        this.cartUpdateListener = cartUpdateListener;
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
        ProductData data = (isFiltered)?filteredDataList.get(position):dataList.get(position);
        holder.name.setText(data.getName());
        holder.measure.setText(data.getMeasure());
        holder.price.setText(CONST.RUPEES_SYMBOL +data.getPrice());

        //Handling Current Qty Selection
        Log.d("ProductAdapter","Product name="+data.getName()+", selectedQuantity="+data.getSelectedQuantity()+", storeid="+storeid+", CartManagerStoreID="+cartManager.getStoreId());
        if( data.getSelectedQuantity()==0 || (!storeid.equals(cartManager.getStoreId())) )
        {
            holder.plus.setVisibility(View.GONE);
            holder.minus.setVisibility(View.GONE);
            holder.qty.setText("Add");
        }
        else
        {
            holder.plus.setVisibility(View.VISIBLE);
            holder.minus.setVisibility(View.VISIBLE);
            holder.qty.setText(""+data.getSelectedQuantity());
        }


        holder.add.setOnClickListener(view -> {
            //Action
            if(!storeid.equals(cartManager.getStoreId()))
            {
                cartManager.init(storeid);
            }
            if( data.getSelectedQuantity()==0 ) {
                data.setSelectedQuantity(1);
                cartManager.addToTotal(Integer.valueOf(data.getPrice()));
                if (isAdapterForCart) cartUpdateListener.onCartValueChange();
                holder.plus.setVisibility(View.VISIBLE);
                holder.minus.setVisibility(View.VISIBLE);
                holder.qty.setText("1");
                orderList.add(data);
            }
        });

        holder.plus.setOnClickListener(view -> {
            if(data.getSelectedQuantity() == Integer.valueOf(data.getQuantity())) {
                Toast.makeText(context,"Can not add more than available in Stock",Toast.LENGTH_SHORT).show();
            } else {
                data.setSelectedQuantity(data.getSelectedQuantity()+1);
                Log.d("ProductAdd","Item="+data.getName()+", Quantity="+data.getSelectedQuantity());
                cartManager.addToTotal(Integer.valueOf(data.getPrice()));
                if (isAdapterForCart) cartUpdateListener.onCartValueChange();
                holder.qty.setText("" + data.getSelectedQuantity());
            }
        });
        holder.minus.setOnClickListener(view -> {
            data.setSelectedQuantity(data.getSelectedQuantity()-1);
            cartManager.removeFromTotal(Integer.valueOf(data.getPrice()));
            if (isAdapterForCart) cartUpdateListener.onCartValueChange();

            if(data.getSelectedQuantity()==0) {
                orderList.remove(data);
                if(orderList.size()==0) {
                    cartManager.removeStoreID();
                    if(isAdapterForCart) cartUpdateListener.onCartEmpty();
                }

                holder.plus.setVisibility(View.GONE);
                holder.minus.setVisibility(View.GONE);
                holder.qty.setText("Add");
                this.notifyDataSetChanged();
            } else {
                holder.qty.setText(""+data.getSelectedQuantity());

            }

        });
    }

    @Override
    public int getItemCount() {
        return (isFiltered)?filteredDataList.size():dataList.size();
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

    //Utilities
    public void setFilters(String searchQuerry)
    {
        if(searchQuerry.equals(CONST.STATUS_NULL))
            isFiltered=false;
        else
        {
            isFiltered=true;
            filteredDataList = new ArrayList<>();
            Log.d("PA","Run");
            for(ProductData data : dataList)
            {
                if(data.getName().toLowerCase().contains(searchQuerry.toLowerCase()))
                    filteredDataList.add(data);
            }
        }
        notifyDataSetChanged();
    }
}

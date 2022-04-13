package com.my.smartgroceries.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.my.smartgroceries.CONST;
import com.my.smartgroceries.R;
import com.my.smartgroceries.account.UserManager;
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
        final String id = data.getId();
        holder.name.setText(data.getName());
        holder.measure.setText(data.getMeasure());
        holder.price.setText(CONST.RUPEES_SYMBOL +data.getPrice());
        holder.stock.setText("Stock: "+data.getQuantity());

        holder.edit.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialogue_edit_item);
            TextView itemName = dialog.findViewById(R.id.dialog_item);
            Button plus = dialog.findViewById(R.id.dialog_plus);
            Button minus = dialog.findViewById(R.id.dialog_minus);
            Button confirm = dialog.findViewById(R.id.dialog_confirm);
            EditText stock = dialog.findViewById(R.id.dialog_stock);
            itemName.setText(data.getName());
            stock.setText(data.getQuantity());
            stock.setSelection(data.getQuantity().length());

            plus.setOnClickListener(view1 -> {
                stock.setText( ""+(Integer.parseInt(stock.getText().toString())+1) );
                stock.setSelection(data.getQuantity().length());
            });
            minus.setOnClickListener(view1 -> {
                stock.setText( ""+(Integer.parseInt(stock.getText().toString())-1) );
                stock.setSelection(data.getQuantity().length());
            });
            confirm.setOnClickListener(view1 -> {
                view1.setEnabled(false);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_PRODUCTDATA).child(UserManager.getUserData().getRefStoreData()).child(id);
                databaseReference.child("quantity").setValue(stock.getText().toString())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        dialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(context,"Failed to Update Data",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            });
            dialog.show();
        });

        holder.remove.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialogue_remove_item);
            Button ok = dialog.findViewById(R.id.dialog_ok);
            Button cancel = dialog.findViewById(R.id.dialog_cancel);
            TextView message = dialog.findViewById(R.id.message);
            message.setText(data.getName());
            dialog.show();
            cancel.setOnClickListener(view1 -> dialog.dismiss());
            ok.setOnClickListener(view1 -> {
                //Delete Data
                view1.setEnabled(false);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_PRODUCTDATA).child(UserManager.getUserData().getRefStoreData());
                databaseReference.child(id).removeValue()
                        .addOnCompleteListener(task -> {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(context,"Failed to update Data",Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        });
            });
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

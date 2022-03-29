package com.my.smartgroceries.CustomerDashboard.cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.smartgroceries.CONST;
import com.my.smartgroceries.OrderConfirmActivity;
import com.my.smartgroceries.R;
import com.my.smartgroceries.SpecialComponents.CartManager;
import com.my.smartgroceries.SpecialComponents.CartUpdateListener;
import com.my.smartgroceries.StoreProductsActivity;
import com.my.smartgroceries.adapters.ProductAdapter;
import com.my.smartgroceries.models.ProductData;
import com.my.smartgroceries.models.StoreData;
import java.util.List;

public class CartFragment extends Fragment {

    String id;
    String name;
    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    List<ProductData> orderList;

    View storeDataLayout;
    ImageView storeImage;
    TextView storeName, storeAddress;
    TextView itemTotal;
    Button orderButton;

    View cartEmptyLayout,cartPresentLayout;
    CartManager cartManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_customer_cart, container, false);
        cartEmptyLayout = root.findViewById(R.id.cartEmptyLayout);
        cartPresentLayout = root.findViewById(R.id.cartPresentLayout);
        cartManager = CartManager.getInstance();

        if(checkCart()) {
            id = cartManager.getStoreId();
            itemTotal = root.findViewById(R.id.itemtotal);
            storeDataLayout = root.findViewById(R.id.storelayout);
            storeImage = root.findViewById(R.id.storeimage);
            storeName = root.findViewById(R.id.storename);
            storeAddress = root.findViewById(R.id.storeaddr);
            orderButton = root.findViewById(R.id.orderbtn);
            setStoreData();

            itemTotal.setText(CONST.RUPEES_SYMBOL+cartManager.getTotal());
            orderList = cartManager.getOrderList();
            recyclerView = root.findViewById(R.id.cartproducts);
            recyclerView.setHasFixedSize(true);
            productAdapter = new ProductAdapter(orderList, getContext(), id,true);
            productAdapter.setCartUpdateListener(new CartUpdateListener() {
                @Override
                public void onCartEmpty() {
                    checkCart();
                }
                @Override
                public void onCartValueChange() {
                    updateTotal();
                    Log.d("Cart","Updated");
                }
            });
            recyclerView.setAdapter(productAdapter);
            orderButton.setOnClickListener(view -> {
                Intent i = new Intent(getContext(), OrderConfirmActivity.class);
                pauseflag=true;
                startActivity(i);
            });
        }

        return root;
    }

    private boolean checkCart()
    {
        if(cartManager.isCartEmpty())
        {
            cartEmptyLayout.setVisibility(View.VISIBLE);
            cartPresentLayout.setVisibility(View.GONE);
            return false;
        }
        else
        {
            cartPresentLayout.setVisibility(View.VISIBLE);
            cartEmptyLayout.setVisibility(View.GONE);
            return true;
        }
    }

    private void updateTotal()
    {
        itemTotal.setText(CONST.RUPEES_SYMBOL+cartManager.getTotal());
    }


    private void setStoreData()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_STOREDATA).child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StoreData storeData = snapshot.getValue(StoreData.class);
                storeName.setText(storeData.getName());
                storeAddress.setText(storeData.getAddress());
                storeDataLayout.setOnClickListener(view -> {
                    Intent i = new Intent(getContext(),StoreProductsActivity.class);
                    i.putExtra("id",storeData.getId());
                    i.putExtra("name",storeData.getName());
                    startActivity(i);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Failed to fetch the store data",Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean pauseflag = false;

    @Override
    public void onPause() {
        super.onPause();
        if(productAdapter!=null)
            pauseflag = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        checkCart();
        if(productAdapter!=null) {
            productAdapter.notifyDataSetChanged();
            updateTotal();
        }
//        if (pauseflag) {
//            checkCart();
//            productAdapter.notifyDataSetChanged();
//            updateTotal();
//            pauseflag = false;
//        }
    }
}
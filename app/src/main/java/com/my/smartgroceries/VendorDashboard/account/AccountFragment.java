package com.my.smartgroceries.VendorDashboard.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.my.smartgroceries.EditAccountActivity;
import com.my.smartgroceries.EditStoreActivity;
import com.my.smartgroceries.LoginActivity;
import com.my.smartgroceries.OrderHistoryActivity;
import com.my.smartgroceries.R;
import com.my.smartgroceries.SpecialComponents.CartManager;
import com.my.smartgroceries.account.UserManager;

public class AccountFragment extends Fragment {

    Button orderHistory,editstore,editaccount,logout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vendor_account, container, false);

        orderHistory = root.findViewById(R.id.orderhistory);
        orderHistory.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), OrderHistoryActivity.class);
            i.putExtra("isVendor",true);
            startActivity(i);
        });

        editstore = root.findViewById(R.id.editstore);
        editstore.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), EditStoreActivity.class);
            startActivity(i);
        });

        editaccount = root.findViewById(R.id.editaccount);
        editaccount.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), EditAccountActivity.class);
            startActivity(i);
        });

        logout = root.findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            CartManager.getInstance().resetCartManager();
            UserManager.Logout(getContext());
            Intent i = new Intent(getContext(), LoginActivity.class);
            startActivity(i);
            getActivity().finish();
        });

        return root;
    }
}
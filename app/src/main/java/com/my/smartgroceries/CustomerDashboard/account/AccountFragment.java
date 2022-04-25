package com.my.smartgroceries.CustomerDashboard.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.my.smartgroceries.EditAccountActivity;
import com.my.smartgroceries.LoginActivity;
import com.my.smartgroceries.OrderHistoryActivity;
import com.my.smartgroceries.R;
import com.my.smartgroceries.SpecialComponents.CartManager;
import com.my.smartgroceries.TestActivity;
import com.my.smartgroceries.account.UserManager;

public class AccountFragment extends Fragment {

    Button orderHistory,editaccount,logout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_customer_account, container, false);

        orderHistory = root.findViewById(R.id.orderhistory);
        orderHistory.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), OrderHistoryActivity.class);
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
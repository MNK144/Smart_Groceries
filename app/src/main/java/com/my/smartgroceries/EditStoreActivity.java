package com.my.smartgroceries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.smartgroceries.account.UserManager;
import com.my.smartgroceries.models.StoreData;
import com.my.smartgroceries.models.UserData;

public class EditStoreActivity extends AppCompatActivity {

    View layout_name;
    View layout_addr;
    View layout_pincode;
    EditText name;
    EditText addr;
    EditText pincode;
    Button submit;
    FirebaseAuth firebaseAuth;
    StoreData storeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=#282a35>" + getString(R.string.app_name) + "</font>"));

        firebaseAuth = FirebaseAuth.getInstance();
        layout_name = findViewById(R.id.srinputl_storename);
        layout_addr = findViewById(R.id.srinputl_storeaddr);
        layout_pincode = findViewById(R.id.srinputl_storepincode);
        name = findViewById(R.id.sregstorename);
        addr = findViewById(R.id.sregstoreaddr);
        pincode = findViewById(R.id.sregstorepincode);
        submit = findViewById(R.id.regbtn);
        layout_name.setOnClickListener(view -> {
            name.requestFocus();
            name.setSelection(name.getText().length());
        });
        layout_addr.setOnClickListener(view -> {
            addr.requestFocus();
            addr.setSelection(addr.getText().length());
        });
        layout_pincode.setOnClickListener(view -> {
            pincode.requestFocus();
            pincode.setSelection(pincode.getText().length());
        });
        submit.setOnClickListener(view -> {
            //Register Process
            submit.setEnabled(false);
            validateSubmit();
        });

        FirebaseDatabase.getInstance().getReference(CONST.DB_STOREDATA).child(UserManager.getUserData().getRefStoreData())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        storeData = snapshot.getValue(StoreData.class);
                        name.setText(storeData.getName());
                        addr.setText(storeData.getAddress());
                        pincode.setText(storeData.getPincode());
                        submit.setEnabled(true);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(),"Failed to load User Data",Toast.LENGTH_SHORT).show();
                        submit.setEnabled(false);
                    }
                });
    }

    //Firebase Related Operations
    String sname,saddr,spincode;
    DatabaseReference databaseReference;
    private void validateSubmit()
    {
        //Add Some Validation - Pending
        sname = name.getText().toString();
        saddr = addr.getText().toString();
        spincode = pincode.getText().toString();

        if(TextUtils.isEmpty(sname))
            name.setError("This Field Cannot be Empty");
        else if(TextUtils.isEmpty(saddr))
            addr.setError("This Field Cannot be Empty");
        else if(TextUtils.isEmpty(spincode))
            pincode.setError("This Field Cannot be Empty");
        else if(spincode.length()!=6)
            pincode.setError("Invalid Pincode");
        else
            updateData();
    }

    private void updateData()
    {
        storeData.setName(name.getText().toString());
        storeData.setAddress(addr.getText().toString());
        storeData.setPincode(pincode.getText().toString());
        databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_STOREDATA).child(UserManager.getUserData().getRefStoreData());
        databaseReference.setValue(storeData)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(),"Data Updated Successful",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Failed to update User Data",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

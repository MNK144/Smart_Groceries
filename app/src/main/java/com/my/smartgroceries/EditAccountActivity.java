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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.smartgroceries.account.UserManager;
import com.my.smartgroceries.models.UserData;

public class EditAccountActivity extends AppCompatActivity {

    View layout_name;
    View layout_phone;
    EditText name;
    EditText phone;
    EditText email;
    Button submit;
    FirebaseAuth firebaseAuth;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=#282a35>" + getString(R.string.app_name) + "</font>"));

        firebaseAuth = FirebaseAuth.getInstance();
        layout_name = findViewById(R.id.rinputl_name);
        layout_phone = findViewById(R.id.rinputl_phone);
        name = findViewById(R.id.regname);
        phone = findViewById(R.id.regphone);
        email = findViewById(R.id.regemail);
        submit = findViewById(R.id.regbtn);
        layout_name.setOnClickListener(view -> {
            name.requestFocus();
            name.setSelection(name.getText().length());
        });
        layout_phone.setOnClickListener(view -> {
            phone.requestFocus();
            phone.setSelection(phone.getText().length());
        });
        submit.setOnClickListener(view -> {
            //Register Process
            submit.setEnabled(false);
            validateSubmit();
        });

        FirebaseDatabase.getInstance().getReference(CONST.DB_USERDATA).child(UserManager.getUID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userData = snapshot.getValue(UserData.class);
                        name.setText(userData.getName());
                        phone.setText(userData.getContact());
                        email.setText(userData.getEmail());
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
    String sname,scontact;
    private void validateSubmit()
    {
        //Add Some Validation - Pending
        sname = name.getText().toString();
        scontact = phone.getText().toString();

        if(TextUtils.isEmpty(sname))
            name.setError("This Field Cannot be Empty");
        else if(TextUtils.isEmpty(scontact))
            phone.setError("This Field Cannot be Empty");
        else
            updateData();
    }
    private void updateData()
    {
        userData.setName(name.getText().toString());
        userData.setContact(phone.getText().toString());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_USERDATA).child(UserManager.getUID());
        databaseReference.setValue(userData)
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

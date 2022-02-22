package com.my.smartgroceries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.my.smartgroceries.models.StoreData;
import com.my.smartgroceries.models.UserData;

public class RegisterStoreActivity extends AppCompatActivity {

    View layout_name;
    View layout_phone;
    View layout_email;
    View layout_password;
    View layout_storename;
    View layout_storeaddr;
    View layout_storepincode;

    EditText name;
    EditText phone;
    EditText email;
    EditText password;
    EditText storename;
    EditText storeaddr;
    EditText storepincode;

    ImageButton passwordShowButton;
    boolean isPasswordHidden;
    Button register;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_store);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=#282a35>" + getString(R.string.app_name) + "</font>"));
        firebaseAuth = FirebaseAuth.getInstance();

        layout_name = findViewById(R.id.srinputl_name);
        layout_phone = findViewById(R.id.srinputl_phone);
        layout_email = findViewById(R.id.srinputl_email);
        layout_password = findViewById(R.id.srinputl_password);
        layout_storename = findViewById(R.id.srinputl_storename);
        layout_storeaddr = findViewById(R.id.srinputl_storeaddr);
        layout_storepincode = findViewById(R.id.srinputl_storepincode);

        name = findViewById(R.id.sregname);
        phone = findViewById(R.id.sregphone);
        email = findViewById(R.id.sregemail);
        password = findViewById(R.id.sregpass);
        storename = findViewById(R.id.sregstorename);
        storeaddr = findViewById(R.id.sregstoreaddr);
        storepincode = findViewById(R.id.sregstorepincode);

        register = findViewById(R.id.sregbtn);
        passwordShowButton = findViewById(R.id.regpassshow);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        isPasswordHidden = true;

        layout_name.setOnClickListener(view -> {
            name.requestFocus();
            name.setSelection(name.getText().length());
        });
        layout_phone.setOnClickListener(view -> {
            phone.requestFocus();
            phone.setSelection(phone.getText().length());
        });
        layout_email.setOnClickListener(view -> {
            email.requestFocus();
            email.setSelection(email.getText().length());
        });
        layout_password.setOnClickListener(view -> {
            password.requestFocus();
            password.setSelection(password.getText().length());
        });
        layout_storename.setOnClickListener(view -> {
            storename.requestFocus();
            storename.setSelection(storename.getText().length());
        });
        layout_storeaddr.setOnClickListener(view -> {
            storeaddr.requestFocus();
            storeaddr.setSelection(storeaddr.getText().length());
        });
        layout_storepincode.setOnClickListener(view -> {
            storepincode.requestFocus();
            storepincode.setSelection(storepincode.getText().length());
        });

        passwordShowButton.setOnClickListener(view -> {
            if(isPasswordHidden)
            {
                passwordShowButton.setImageDrawable(getResources().getDrawable(R.drawable.password_notvisible,getTheme()));
                password.setInputType(InputType.TYPE_CLASS_TEXT);
                isPasswordHidden = false;
            }
            else
            {
                passwordShowButton.setImageDrawable(getResources().getDrawable(R.drawable.password_visible,getTheme()));
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                isPasswordHidden = true;
            }
            password.setSelection(password.getText().length());
        });

        register.setOnClickListener(view -> {
            //Register Process
            validateRegistration();
        });

    }

    //Firebase Related Operations
    String UID,sid;
    String semail,spwd,sname,scontact;
    String sstorename,sstoreaddr,sstorepincode;
    DatabaseReference databaseReference,storeDatabaseReference;
    private void validateRegistration()
    {
        //Add Some Validation - Pending
        semail = email.getText().toString();
        spwd = password.getText().toString();
        sname = name.getText().toString();
        scontact = phone.getText().toString();
        sstorename = storename.getText().toString();
        sstoreaddr = storeaddr.getText().toString();
        sstorepincode = storepincode.getText().toString();

        if(TextUtils.isEmpty(sname))
            name.setError("This Field Cannot be Empty");
        else if(TextUtils.isEmpty(scontact))
            phone.setError("This Field Cannot be Empty");
        else if(TextUtils.isEmpty(semail))
            email.setError("This Field Cannot be Empty");
        else if(!Patterns.EMAIL_ADDRESS.matcher(semail).matches())
            email.setError("Enter a Valid Email");
        else if(TextUtils.isEmpty(spwd))
            password.setError("This Field Cannot be Empty");
        else if(spwd.length()<6)
            password.setError("Minimum 6 Character Required");
        else if(TextUtils.isEmpty(sstorename))
            storename.setError("This Field Cannot be Empty");
        else if(TextUtils.isEmpty(sstoreaddr))
            storeaddr.setError("This Field Cannot be Empty");
        else if(TextUtils.isEmpty(sstorepincode))
            storepincode.setError("This Field Cannot be Empty");
        else if(sstorepincode.length()!=6)
            storepincode.setError("Invalid Pincode");
        else
            registerUser();

    }
    private void registerUser()
    {
        inProcess(true);
        //Making AuthAccount
        firebaseAuth.createUserWithEmailAndPassword(semail,spwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            UID = firebaseAuth.getUid();
                            Log.d("RegisterUser","New User UID:"+UID);
                            registerStoreData();
                        }
                        else
                        {
                            Toast.makeText(RegisterStoreActivity.this, "Error: " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            inProcess(false);
                        }
                    }
                });
    }
    private void registerStoreData()
    {
        storeDatabaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_STOREDATA);
        sid = storeDatabaseReference.push().getKey();
        StoreData storeData = new StoreData(sid,sstorename,sstoreaddr,sstorepincode,UID,true,CONST.STATUS_NULL);
        storeDatabaseReference.child(sid).setValue(storeData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            //Toast.makeText(RegisterStoreActivity.this,"Registation Successful",Toast.LENGTH_LONG).show();
                            //finish();
                            Log.d("StoreAccountCreation","Store Data Created");
                            registerUserData();
                        }
                        else
                        {
                            if(firebaseAuth.getCurrentUser()!=null) {
                                firebaseAuth.getCurrentUser().delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.d("StoreAccountCreation","Failed to Create Store Data");
                                                Toast.makeText(RegisterStoreActivity.this, "Registration Failed",Toast.LENGTH_LONG).show();
                                                inProcess(false);
                                            }
                                        });
                            }
                            else
                            {
                                Log.d("StoreAccountCreation","Failed to Create Store Data");
                                Toast.makeText(RegisterStoreActivity.this,"Registration Failed",Toast.LENGTH_LONG).show();
                                inProcess(false);
                            }
                        }
                    }
                });
    }
    private void registerUserData()
    {
        UserData userData = new UserData(UID,sname,scontact,semail,true,sid);
        databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_USERDATA);
        databaseReference.child(UID).setValue(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Log.d("StoreAccountCreation","User Data Created");
                            Toast.makeText(RegisterStoreActivity.this,"Registration Successful",Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else
                        {
                            Log.d("StoreAccountCreation","Userdata Creation Failed");
                            storeDatabaseReference.child(sid).removeValue();
                            if(firebaseAuth.getCurrentUser()!=null) {
                                firebaseAuth.getCurrentUser().delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(RegisterStoreActivity.this, "Registration Failed",Toast.LENGTH_LONG).show();
                                                inProcess(false);
                                            }
                                        });
                            }
                            else
                            {
                                Toast.makeText(RegisterStoreActivity.this,"Registration Failed",Toast.LENGTH_LONG).show();
                                inProcess(false);
                            }
                        }
                    }
                });
    }


    private void inProcess(Boolean b)
    {
        if(b) {
            //loading.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        else {
            //loading.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}

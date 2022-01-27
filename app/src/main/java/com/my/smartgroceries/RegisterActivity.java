package com.my.smartgroceries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.my.smartgroceries.R;
import com.my.smartgroceries.models.UserData;

public class RegisterActivity extends AppCompatActivity {

    View layout_name;
    View layout_phone;
    View layout_email;
    View layout_password;

    EditText name;
    EditText phone;
    EditText email;
    EditText password;

    ImageButton passwordShowButton;
    boolean isPasswordHidden;
    Button register;
    TextView storereg;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=#282a35>" + getString(R.string.app_name) + "</font>"));
        firebaseAuth = FirebaseAuth.getInstance();

        layout_name = findViewById(R.id.rinputl_name);
        layout_phone = findViewById(R.id.rinputl_phone);
        layout_email = findViewById(R.id.rinputl_email);
        layout_password = findViewById(R.id.rinputl_password);
        name = findViewById(R.id.regname);
        phone = findViewById(R.id.regphone);
        email = findViewById(R.id.regemail);
        password = findViewById(R.id.regpass);
        register = findViewById(R.id.regbtn);
        storereg = findViewById(R.id.storeregbtn);
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

        storereg.setOnClickListener(view -> {
            //Go to Store Owner Registration
            Intent i = new Intent(RegisterActivity.this,RegisterStoreActivity.class);
            startActivity(i);
            finish();
        });
    }

    //Firebase Related Operations
    String UID;
    String semail,spwd,sname,scontact;
    private void validateRegistration()
    {
        //Add Some Validation
        semail = email.getText().toString();
        spwd = password.getText().toString();
        sname = name.getText().toString();
        scontact = phone.getText().toString();
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
                            registerUserData();
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            inProcess(false);
                        }
                    }
                });
    }
    private void registerUserData()
    {
        UserData userData = new UserData(UID,sname,scontact,semail,false,"NA");
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_USERDATA);
        databaseReference.child(UID).setValue(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this,"Registation Successful",Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else
                        {
                            if(firebaseAuth.getCurrentUser()!=null) {
                                firebaseAuth.getCurrentUser().delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(RegisterActivity.this, "Registration Failed",Toast.LENGTH_LONG).show();
                                                inProcess(false);
                                            }
                                        });
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,"Registration Failed",Toast.LENGTH_LONG).show();
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

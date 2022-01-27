package com.my.smartgroceries;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class RegisterStoreActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_store);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=#282a35>" + getString(R.string.app_name) + "</font>"));

        layout_name = findViewById(R.id.srinputl_name);
        layout_phone = findViewById(R.id.srinputl_phone);
        layout_email = findViewById(R.id.srinputl_email);
        layout_password = findViewById(R.id.srinputl_password);
        name = findViewById(R.id.sregname);
        phone = findViewById(R.id.sregphone);
        email = findViewById(R.id.sregemail);
        password = findViewById(R.id.sregpass);
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
        });

    }
}

package com.my.smartgroceries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {

    EditText email;
    Button forgot;
    TextView signup;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        email = findViewById(R.id.forgotemail);
        forgot = findViewById(R.id.forgotbtn);
        signup = findViewById(R.id.fregbtn);
        firebaseAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle(Html.fromHtml("<font color=#282a35>" + getString(R.string.app_name) + "</font>"));

        forgot.setOnClickListener(view -> {
            String semail;
            semail = email.getText().toString();
            if(!TextUtils.isEmpty(semail))
            {
                firebaseAuth.sendPasswordResetEmail(semail).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Toast.makeText(ForgotActivity.this, "Check your email for password reset link", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else{
                        Toast.makeText(ForgotActivity.this, "Failed to Reset Password", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        signup.setOnClickListener(view -> {
            Intent i = new Intent(ForgotActivity.this,RegisterActivity.class);
            startActivity(i);
            finish();
        });
    }
}

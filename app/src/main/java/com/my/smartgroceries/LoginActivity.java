package com.my.smartgroceries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.smartgroceries.account.UserManager;
import com.my.smartgroceries.models.UserData;

public class LoginActivity extends AppCompatActivity {

    View layout_email;
    View layout_password;
    EditText email;
    EditText password;
    TextView forgot,reg;

    ImageButton passwordShowButton;
    boolean isPasswordHidden;

    Button loginButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        layout_email = findViewById(R.id.inputl_email);
        layout_password = findViewById(R.id.inputl_password);
        email = findViewById(R.id.loginemail);
        password = findViewById(R.id.loginpass);
        passwordShowButton = findViewById(R.id.loginpassshow);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        isPasswordHidden = true;
        loginButton = findViewById(R.id.loginbtn);
        forgot = findViewById(R.id.loginforgot);
        reg = findViewById(R.id.loginregbtn);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=#282a35>" + getString(R.string.app_name) + "</font>"));

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

        //Auth process
        loginButton.setOnClickListener(view -> {
            //Handle Validation Here (It can be optional since its login)
            String semail, spassword;
            semail = email.getText().toString();
            spassword = password.getText().toString();

            // TOD remove hardcoded login auth
            //Login("patel.manank144@gmail.com","Password@123");

            if(!semail.isEmpty() && !spassword.isEmpty())
            {
                Login(semail,spassword);
                //Temp
                /*
                if(semail.equals("patel.manank144@gmail.com") && spassword.equals("Password@123"))
                    Login(semail,spassword);
                else
                {
                    TextView textView=null;
                    textView.setText("Hello");
                }
                */
            }
        });
        forgot.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this,ForgotActivity.class);
            startActivity(i);
        });
        reg.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(i);
        });
    }


    //Auth process
    private String UID;
    private UserData userData;
    public void Login(String email, String pass) {

        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        UID = firebaseAuth.getUid();
                        fetchUserData(UID);
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_LONG).show();
                    }
                });

    }
    private void fetchUserData(final String UID)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_USERDATA).child(UID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    userData = dataSnapshot.getValue(UserData.class);
                    loginSuccess();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Login Failed: Failed to get User Data, Contact Support",Toast.LENGTH_LONG).show();
                    //inProcess(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this,"Login Failed: Failed to get User Data",Toast.LENGTH_LONG).show();
                //inProcess(false);
            }
        });
    }
    private void loginSuccess()
    {
        Log.d("UserManager","Accessed Login Successful");
        UserManager.Login(UID,userData,LoginActivity.this);
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}

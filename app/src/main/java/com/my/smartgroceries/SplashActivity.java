package com.my.smartgroceries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.my.smartgroceries.account.UserManager;
import com.my.smartgroceries.models.UserData;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AlphaAnimation animation = new AlphaAnimation(0,1);
        animation.setDuration(3000);

        ImageView image = findViewById(R.id.splashimg);
        TextView text = findViewById(R.id.splashtxt);

        image.startAnimation(animation);
        text.startAnimation(animation);

        //Get User Data From Database


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
                */
                checkLogin();
            }
        }, 5000);
    }


    private void checkLogin()
    {
        if(UserManager.checkSession(SplashActivity.this))
            fetchUserData(UserManager.getUID());
        else
        {
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            finish();
        }
    }
    private void fetchUserData(final String UID)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_USERDATA).child(UID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    UserData userData = dataSnapshot.getValue(UserData.class);
                    UserManager.setUserData(userData);
                    Log.d("SessionLogin","Login Success");
                    login(userData.isVendorAccount());
                }
                else {
                    Toast.makeText(getApplicationContext(), "Login Failed: Failed to get User Data",Toast.LENGTH_LONG).show();
                    logout();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Login Failed: Failed to get User Data",Toast.LENGTH_LONG).show();
                logout();
            }
        });
    }
    private void login(boolean vendor)
    {
        Intent i;
        if(vendor)
        {
            i = new Intent(getApplicationContext(),VendorHomeActivity.class);
        }
        else
        {
            i = new Intent(getApplicationContext(),HomeActivity.class);
        }
        startActivity(i);
        finish();
    }
    private void logout()
    {
        UserManager.Logout(SplashActivity.this);
        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

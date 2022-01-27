package com.my.smartgroceries;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.my.smartgroceries.R;
import com.my.smartgroceries.models.ProductData;

public class TestActivity extends AppCompatActivity {

    TextView t1,t2,t3,t4;
    Button b1;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_PRODUCTDATA).child(CONST.DB_TESTID);

        t1 = findViewById(R.id.tt1);
        t2 = findViewById(R.id.tt2);
        t3 = findViewById(R.id.tt3);
        t4 = findViewById(R.id.tt4);
        b1 = findViewById(R.id.tb1);

        b1.setOnClickListener(view -> {
            Log.d("TEST","Button Clicked");
            String id = databaseReference.push().getKey();
            //StoreData sd = new StoreData(id,t1.getText().toString(),t2.getText().toString(),t3.getText().toString(),t4.getText().toString(),"NODATA");
            ProductData pd = new ProductData(id,t1.getText().toString(),t2.getText().toString(),t3.getText().toString(),t4.getText().toString(),"NODATA","NODATA");
            databaseReference.child(id).setValue(pd)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                            Toast.makeText(TestActivity.this,"Pushed Successfully",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(TestActivity.this,"Push Failed",Toast.LENGTH_LONG).show();
                    });
        });
    }
}

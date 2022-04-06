package com.my.smartgroceries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.my.smartgroceries.account.UserManager;
import com.my.smartgroceries.models.ProductData;

public class AddProductActivity extends AppCompatActivity {

    View layout_name;
    View layout_price;
    View layout_quantity;
    EditText name,price,measure,quantity;
    Spinner unit;
    Button submit;
    String sname,sprice,smeasure,squantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=#282a35>" + getString(R.string.app_name) + "</font>"));

        layout_name = findViewById(R.id.ainputl_name);
        layout_price = findViewById(R.id.ainputl_price);
        layout_quantity = findViewById(R.id.ainputl_quantity);
        name = findViewById(R.id.AIName);
        price = findViewById(R.id.AIPrice);
        measure = findViewById(R.id.AIMeasure);
        quantity = findViewById(R.id.AIQuantity);
        unit = findViewById(R.id.AIMeasureUnit);
        submit = findViewById(R.id.addItemButton);

        layout_name.setOnClickListener(view -> {
            name.requestFocus();
            name.setSelection(name.getText().length());
        });
        layout_price.setOnClickListener(view -> {
            price.requestFocus();
            price.setSelection(price.getText().length());
        });
        layout_quantity.setOnClickListener(view -> {
            quantity.requestFocus();
            quantity.setSelection(quantity.getText().length());
        });

        submit.setOnClickListener(view -> {
            sname = name.getText().toString();
            sprice = price.getText().toString();
            smeasure = measure.getText().toString() + " " + unit.getSelectedItem().toString();
            squantity = quantity.getText().toString();
            //Log.d("AddProduct","Name:"+sname+", Price:"+sprice+", Measure:"+smeasure+", Quantity:"+squantity);

            if(TextUtils.isEmpty(sname))
                name.setError("This Field Cannot be Empty");
            else if(TextUtils.isEmpty(sprice))
                price.setError("This Field Cannot be Empty");
            else if(TextUtils.isEmpty(smeasure))
                measure.setError("This Field Cannot be Empty");
            else if(TextUtils.isEmpty(squantity))
                quantity.setError("This Field Cannot be Empty");
            else
                addItem();
        });
    }
    private void addItem()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CONST.DB_PRODUCTDATA).child(UserManager.getUserData().getRefStoreData());
        String id = databaseReference.push().getKey();
        ProductData data = new ProductData(id,sname,sprice,smeasure,squantity,"NODATA","NODATA");
        databaseReference.child(id).setValue(data)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(),"Item Added Successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Failed to add Item",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

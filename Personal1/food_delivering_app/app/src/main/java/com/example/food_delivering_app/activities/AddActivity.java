package com.example.food_delivering_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.food_delivering_app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    Bundle bundle;
    String tmp;
    EditText name,price,quantity,url,name1;
    Button uploadd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        name=findViewById(R.id.name);
        price=findViewById(R.id.price);
        name1=findViewById(R.id.name1);
        quantity=findViewById(R.id.quantity);
        url=findViewById(R.id.url);
        uploadd=findViewById(R.id.uploadd);


        Intent in = getIntent();
        bundle = in.getExtras();
        tmp = bundle.getString("name");


        uploadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("CategoryItems").child(tmp);
                Map<String,Object> taskMap = new HashMap<String,Object>();
                taskMap.put("productname", name.getText().toString());
                taskMap.put("productprice", price.getText().toString());
                taskMap.put("name", name1.getText().toString());
                taskMap.put("productquantity", quantity.getText().toString());
                taskMap.put("url", url.getText().toString());
                taskMap.put("availability", "yes");
                rootRef.child(name.getText().toString()).setValue(taskMap);
            }
        });

    }
}
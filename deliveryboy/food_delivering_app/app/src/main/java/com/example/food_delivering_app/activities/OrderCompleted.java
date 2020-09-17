package com.example.food_delivering_app.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivering_app.R;
import com.example.food_delivering_app.adapter.CategoryAdapter;
import com.example.food_delivering_app.adapter.OrderCompletedAdapter;
import com.example.food_delivering_app.adapter.OrderConfirmationAdapter;
import com.example.food_delivering_app.model.Categorymodel;
import com.example.food_delivering_app.model.OrderCompletedmodel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class OrderCompleted extends AppCompatActivity {

    RecyclerView todaysorder;
    ArrayList<OrderCompletedmodel> todayslist = new ArrayList<>();
    String DeviceID;
    public static String name;
    String currentDate;
    SharedPreferences.Editor editor;
    EditText deliveryboyname;
    Button summary,submitname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_completed);
        todaysorder=findViewById(R.id.todaysorder);
        summary=findViewById(R.id.summary);
        deliveryboyname=findViewById(R.id.deliveryboyname);
        submitname=findViewById(R.id.submitname);
        todaysorder.setHasFixedSize(true);
        todaysorder.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        init(currentDate);



        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderCompleted.this,Deliveryboy.class);
                startActivity(intent);
            }
        });


        submitname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                editor.putString("name", deliveryboyname.getText().toString());
                editor.apply();
                SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
                 name= prefs.getString("name", "No name defined");
                Toast.makeText(OrderCompleted.this,name,Toast.LENGTH_LONG).show();

            }
        });
        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        name= prefs.getString("name", "No name defined");
        deliveryboyname.setText(name);

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(OrderCompleted.this, OrderCompleted.class);
        startActivity(i);
    }

    private void init(String date) {
        clearAll();


        DatabaseReference query = FirebaseDatabase.getInstance().getReference("TotalOrders").child(date);

        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    todayslist.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String status = "";
                        if (dataSnapshot.exists()) {
                                status = postSnapshot.child("status").getValue().toString();
                                if (status.equals("Packed")) {
                                    OrderCompletedmodel model = postSnapshot.getValue(OrderCompletedmodel.class);
                                    todayslist.add(model);
                                }
                            }
                        }
                }
                OrderCompletedAdapter OCadapter=new OrderCompletedAdapter(todayslist,getApplicationContext(),OrderCompleted.this);
                todaysorder.setAdapter(OCadapter);
                OCadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }



    private void clearAll() {
        if(todayslist!=null){
            todayslist.clear();
        }
        todayslist = new ArrayList<>();
    }
}

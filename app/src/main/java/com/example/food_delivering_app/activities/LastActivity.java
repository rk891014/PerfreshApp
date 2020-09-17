package com.example.food_delivering_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.food_delivering_app.R;
import com.example.food_delivering_app.adapter.CategoryAdapter;
import com.example.food_delivering_app.adapter.LastAdapter;
import com.example.food_delivering_app.model.Categorymodel;
import com.example.food_delivering_app.model.CustomModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class LastActivity extends AppCompatActivity {

    String DeviceID;
    RecyclerView lastrec;
    String date,time,deviceIdd;
    ArrayList<CustomModel> orderrlist = new ArrayList<>();
    ImageView lastback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);
        lastback=findViewById(R.id.lastback);
        lastrec=findViewById(R.id.lastrec);

         date = getIntent().getStringExtra("date");
         time = getIntent().getStringExtra("time");
        deviceIdd = getIntent().getStringExtra("deviceidd");

        Log.e("deviceIdddd",deviceIdd);

        lastback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lastrec.setHasFixedSize(true);
        lastrec.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        if(date!=null && time!=null){
            init();
        }


    }

    private void init() {
        clearAll();
        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        DatabaseReference query = FirebaseDatabase.getInstance().getReference("TotalOrders").child(date).child(date+"  "+time+" "+deviceIdd)
                .child("orders");

        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                orderrlist.clear();
                String deviceid = "";
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if(postSnapshot.child("DeviceId").getValue()!=null){
                        deviceid = postSnapshot.child("DeviceId").getValue().toString();
                        if (deviceid.equals(DeviceID)) {
                            CustomModel model = postSnapshot.getValue(CustomModel.class);
                            orderrlist.add(model);
                        }
                    }
                }

                LastAdapter Ladapter=new LastAdapter(orderrlist,LastActivity.this);
                Collections.reverse(orderrlist);
                lastrec.setAdapter(Ladapter);
                Ladapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }




    private void clearAll() {
        if(orderrlist!=null){
            orderrlist.clear();
        }
        orderrlist = new ArrayList<>();
    }
}
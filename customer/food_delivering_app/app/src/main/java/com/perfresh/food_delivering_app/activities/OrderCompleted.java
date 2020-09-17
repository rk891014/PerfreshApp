package com.perfresh.food_delivering_app.activities;

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
import android.widget.LinearLayout;
import android.widget.Toast;


import com.perfresh.food_delivering_app.R;
import com.Perfresh.food_delivering_app.adapter.OrderCompletedAdapter;

import com.perfresh.food_delivering_app.model.OrderCompletedmodel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class OrderCompleted extends AppCompatActivity {

    RecyclerView todaysorder,OrdersComp;
    ArrayList<OrderCompletedmodel> todayslist = new ArrayList<>();
    ArrayList<OrderCompletedmodel> prevorderlist = new ArrayList<>();
    String DeviceID;
    String currentDate;
    LinearLayout linear1,linear2;
    ImageView Ocomback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_completed);
        todaysorder=findViewById(R.id.todaysorder);
        OrdersComp=findViewById(R.id.OrdersComp);
        linear1=findViewById(R.id.linear1);
        linear2=findViewById(R.id.linear2);
        Ocomback=findViewById(R.id.Ocomback);

        todaysorder.setHasFixedSize(true);
        todaysorder.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        OrdersComp.setHasFixedSize(true);
        OrdersComp.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        init(currentDate);


        prevoreders(currentDate);


        Ocomback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void prevoreders(String currentDate) {
        prevorderlist.clear();

        for(int i=0;i<4;i++){
            String datee=getlastdate(currentDate);
            if(datee.isEmpty()){
                Toast.makeText(getApplicationContext(),"wait a sec",Toast.LENGTH_LONG).show();
            }else {
                currentDate=datee;
                init2(currentDate);
            }

        }
    }

    private void init2(String date2) {
        DatabaseReference query = FirebaseDatabase.getInstance().getReference("TotalOrders").child(date2);

        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String deviceid = "";
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        deviceid = postSnapshot.child("DeviceId").getValue().toString();
                        if (deviceid.equals(DeviceID)) {
                            OrderCompletedmodel model = postSnapshot.getValue(OrderCompletedmodel.class);
                            prevorderlist.add(model);
                        }
                    }
                    if(prevorderlist.size()!=0){
                        linear2.setLayoutParams(new LinearLayout.LayoutParams(0,0));
                    }
                    OrderCompletedAdapter OCadapter = new OrderCompletedAdapter(prevorderlist, getApplicationContext(), OrderCompleted.this);
                    Collections.reverse(prevorderlist);
                    OrdersComp.setAdapter(OCadapter);
                    OCadapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }

    private void prevorders() {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(OrderCompleted.this, MainActivity.class);
        i.putExtra("vieworders","0");
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
                    String deviceid = "";
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if(postSnapshot.child("DeviceId").getValue()!=null){
                            deviceid = postSnapshot.child("DeviceId").getValue().toString();
                            if (deviceid.equals(DeviceID)) {
                                OrderCompletedmodel model = postSnapshot.getValue(OrderCompletedmodel.class);
                                todayslist.add(model);
                            }
                        }
                    }
                }
                if(todayslist.size()!=0){
                    linear1.setLayoutParams(new LinearLayout.LayoutParams(0,0));
                }
                OrderCompletedAdapter OCadapter=new OrderCompletedAdapter(todayslist,getApplicationContext(),OrderCompleted.this);
                Collections.reverse(todayslist);
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


    public String getlastdate(String date){
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(Calendar.DAY_OF_YEAR,-1);
        String oneDayBefore = df.format(cal.getTime());
        return oneDayBefore;
    }
    private void clearAll() {
        if(todayslist!=null){
            todayslist.clear();
        }
        todayslist = new ArrayList<>();
    }
    private void clearAll2() {
        if(prevorderlist!=null){
            prevorderlist.clear();
        }
        prevorderlist = new ArrayList<>();
    }
}
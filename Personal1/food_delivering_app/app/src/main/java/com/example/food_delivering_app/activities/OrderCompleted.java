package com.example.food_delivering_app.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    ArrayList<OrderCompletedmodel> prevorderlist = new ArrayList<>();
    String DeviceID;
    String currentDate;
    float deltotal1=0;
    ImageView Ocomback;
    EditText deliveryboyname;
    TextView totalrupees,listsizes;
    Button getrupee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_completed);
        todaysorder=findViewById(R.id.todaysorder);
        Ocomback=findViewById(R.id.Ocomback);
        totalrupees=findViewById(R.id.totalrupees);
        deliveryboyname=findViewById(R.id.deliveryboy);
        listsizes=findViewById(R.id.listsize);
        getrupee=findViewById(R.id.getrupee);


        todaysorder.setHasFixedSize(true);
        todaysorder.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        init(currentDate);

        getrupee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deltotal1=0;
                init2(deliveryboyname.getText().toString());
            }
        });


        Ocomback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void init2(final String name) {

        DatabaseReference query = FirebaseDatabase.getInstance().getReference("TotalOrders").child(currentDate);

        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String boyname = "";
                deltotal1=0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            boyname = postSnapshot.child("deliveryboy").getValue().toString();
                            if (boyname.equals(name)) {
                                OrderCompletedmodel model = postSnapshot.getValue(OrderCompletedmodel.class);
                                prevorderlist.add(model);
                                String str = model.getTopay().substring(1);
                                deltotal1 += Float.parseFloat(str);

                        }
                    }
                    totalrupees.setText(String.valueOf(deltotal1));
                    listsizes.setText(String.valueOf(prevorderlist.size()));
                }
                OrderCompletedAdapter OCadapter=new OrderCompletedAdapter(prevorderlist,getApplicationContext(),OrderCompleted.this);
                Collections.reverse(prevorderlist);
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
                    float deltotal=0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                OrderCompletedmodel model = postSnapshot.getValue(OrderCompletedmodel.class);
                                todayslist.add(model);

                                if(model.getTopay()!=null){
                                    String str =model.getTopay().substring(1);
                                    deltotal+=Float.parseFloat(str);
                                }


                    }
                    totalrupees.setText(String.valueOf(deltotal));
                    listsizes.setText(String.valueOf(todayslist.size()));
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

    private void clearAll() {
        if(todayslist!=null){
            todayslist.clear();
        }
        todayslist = new ArrayList<>();
    }
}
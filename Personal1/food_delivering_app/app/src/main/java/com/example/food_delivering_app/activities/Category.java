package com.example.food_delivering_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivering_app.R;
import com.example.food_delivering_app.adapter.CategoryAdapter;
import com.example.food_delivering_app.model.Categorymodel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class Category extends AppCompatActivity {

    Bundle bundle;
    ImageView back;
    public static TextView categorii;
    Button additem;
    String tmp;
    RecyclerView recyclerView;
    ArrayList<Categorymodel> categorylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        recyclerView = findViewById(R.id.caterv);
        back=findViewById(R.id.back);
        categorii=findViewById(R.id.category);
        additem=findViewById(R.id.additem);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        categorylist = new ArrayList<>();


        Intent in = getIntent();
         bundle = in.getExtras();
        tmp = bundle.getString("name");
        Toast.makeText(Category.this, tmp, Toast.LENGTH_SHORT).show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category.super.onBackPressed();
            }
        });
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Category.this, AddActivity.class);
                i.putExtra("name",categorii.getText().toString());
                startActivity(i);
            }
        });

        categorii.setText(tmp);


        if(tmp!=null){
            init();
        }

    }

    private void init() {
        clearAll();


        DatabaseReference query = FirebaseDatabase.getInstance().getReference("CategoryItems").child(tmp);

        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                categorylist.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if(dataSnapshot.exists()) {
                            Categorymodel model = postSnapshot.getValue(Categorymodel.class);
                            categorylist.add(model);
                    }
                }
                CategoryAdapter Cadapter=new CategoryAdapter(categorylist,getApplicationContext(),Category.this);
                Collections.reverse(categorylist);
                recyclerView.setAdapter(Cadapter);
                Cadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }

    private void clearAll() {
        if(categorylist!=null){
            categorylist.clear();
        }
        categorylist = new ArrayList<>();
    }
}

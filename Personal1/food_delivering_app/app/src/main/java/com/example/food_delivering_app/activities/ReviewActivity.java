package com.example.food_delivering_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.food_delivering_app.R;
import com.example.food_delivering_app.adapter.LastAdapter;
import com.example.food_delivering_app.adapter.ReviewAdapter;
import com.example.food_delivering_app.fragments.MyDatabaseHelper;
import com.example.food_delivering_app.model.Categorymodel;
import com.example.food_delivering_app.model.CustomModel;
import com.example.food_delivering_app.model.ReviewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ReviewActivity extends AppCompatActivity {

    public static String productname;
    String productprice,producturl,productquantity,productrating;
    ImageView rimageView1,reviewback;
    TextView productname1,productprice1,productquantity1,rating2,addcart1;
    ArrayList<ReviewModel> reviewlist = new ArrayList<>();
    RecyclerView recreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        rimageView1=findViewById(R.id.rimageView1);
        productname1=findViewById(R.id.productname1);
        productprice1=findViewById(R.id.productprice1);
        productquantity1=findViewById(R.id.productquantity1);
        rating2=findViewById(R.id.rating2);
        addcart1=findViewById(R.id.addcart1);
        reviewback=findViewById(R.id.reviewback);
        recreview=findViewById(R.id.recreview);


        productname = getIntent().getStringExtra("name");
        productprice = getIntent().getStringExtra("price");
        producturl = getIntent().getStringExtra("url");
        productquantity = getIntent().getStringExtra("productquantity");
        productrating = getIntent().getStringExtra("rating");

        Glide.with(getApplicationContext()).load(producturl).thumbnail(0.5f).skipMemoryCache(false).into(rimageView1);
        productname1.setText(productname);
        productprice1.setText(productprice);
        productquantity1.setText(productquantity);
        rating2.setText(productrating);

        reviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addcart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        MyDatabaseHelper myDB = new MyDatabaseHelper(getApplicationContext());
                        myDB.addBook("1",
                                productname,
                                productprice,
                                productquantity,
                                producturl,
                                1);

            }
        });


        recreview.setHasFixedSize(true);
        recreview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        initreview();
    }

    private void initreview() {
        clearAll();
        Toast.makeText(ReviewActivity.this,productname,Toast.LENGTH_LONG).show();
        DatabaseReference query = FirebaseDatabase.getInstance().getReference("reviews").child(productname).child("Product cmnt");

        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                reviewlist.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            ReviewModel model = postSnapshot.getValue(ReviewModel.class);
                            reviewlist.add(model);
                }

                ReviewAdapter Reviewadapter=new ReviewAdapter(reviewlist,ReviewActivity.this);
                Collections.reverse(reviewlist);
                recreview.setAdapter(Reviewadapter);
                Reviewadapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }
    private void clearAll() {
        if(reviewlist!=null){
            reviewlist.clear();
        }
        reviewlist = new ArrayList<>();
    }
}
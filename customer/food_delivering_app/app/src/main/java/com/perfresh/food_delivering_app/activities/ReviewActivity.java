package com.perfresh.food_delivering_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.perfresh.food_delivering_app.R;
import com.bumptech.glide.Glide;

import com.perfresh.food_delivering_app.adapter.ReviewAdapter;
import com.perfresh.food_delivering_app.fragments.MyDatabaseHelper;
import com.perfresh.food_delivering_app.model.ReviewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ReviewActivity extends AppCompatActivity {

    public String productname;
    String productprice,producturl,productquantity,productrating,cuttedprice,discount;
    ImageView rimageView1,reviewback;
    TextView productname1,productprice1,productquantity1,rating2,addcart1,cuttedproductprice1,discount1;
    ArrayList<ReviewModel> reviewlist = new ArrayList<>();
    RecyclerView recreview;
    Button viewcart1;
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
        viewcart1=findViewById(R.id.viewcart1);
        cuttedproductprice1=findViewById(R.id.cuttedproductprice1);
        discount1=findViewById(R.id.discount1);

        productname = getIntent().getStringExtra("name");
        productprice = getIntent().getStringExtra("price");
        producturl = getIntent().getStringExtra("url");
        productquantity = getIntent().getStringExtra("productquantity");
        productrating = getIntent().getStringExtra("rating");
        cuttedprice = getIntent().getStringExtra("cuttedprice");
        discount = getIntent().getStringExtra("discount");



        Glide.with(getApplicationContext()).load(producturl).thumbnail(0.5f).skipMemoryCache(false).into(rimageView1);
        productname1.setText(productname);
        productprice1.setText("₹"+productprice);
        productquantity1.setText(productquantity);
        rating2.setText(productrating);
        discount1.setText(discount+"%\noff");
        cuttedproductprice1.setText("₹"+cuttedprice);
        cuttedproductprice1.setPaintFlags( cuttedproductprice1.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        reviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(discount==null || discount.equals("")){
            discount1.setVisibility(View.GONE);
        }
        if(cuttedprice==null || cuttedprice.equals("")){
            cuttedproductprice1.setVisibility(View.GONE);
        }

        addcart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyDatabaseHelper myDB = new MyDatabaseHelper(getApplicationContext());
                myDB.addBook("1",
                        productname,
                        productprice,
                        productquantity,
                        cuttedprice,discount,
                        producturl,
                        1);

            }
        });
        viewcart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReviewActivity.this, MainActivity.class);
                i.putExtra("vieworders","1");
                startActivity(i);
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
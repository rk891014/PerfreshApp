package com.perfresh.food_delivering_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.perfresh.food_delivering_app.R;


import com.perfresh.food_delivering_app.adapter.ReviewAdapter;
import com.perfresh.food_delivering_app.model.ReviewModel;
import com.perfresh.food_delivering_app.model.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AppReviewActivity extends AppCompatActivity {

    RecyclerView appreviewrec;
    ArrayList<ReviewModel> appreviewlist = new ArrayList<>();
    ImageView apprreviewback;
    EditText appreview;
    ImageView reviewsubmit;
    TextView truefalse3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_review);

        apprreviewback=findViewById(R.id.apprreviewback);
        appreviewrec=findViewById(R.id.reviewrec);
        reviewsubmit=findViewById(R.id.reviewsubmit);
        appreview=findViewById(R.id.appreview);
        truefalse3=findViewById(R.id.truefalse3);
        appreviewrec.setHasFixedSize(true);
        appreviewrec.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        getbio();
        initappreview();

        appreview.setText("");
        reviewsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(truefalse3.getText().toString().equals("nice")){
                    if(!appreview.getText().toString().isEmpty()){
                        addReview();
                    }else {
                        Toast.makeText(AppReviewActivity.this,"you haven't text anything or not registered",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(AppReviewActivity.this,"you are not registered yet",Toast.LENGTH_LONG).show();

                }


            }
        });
        apprreviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void addReview() {
        String DeviceID = Settings.Secure.getString(AppReviewActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("app review");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("comment",appreview.getText().toString());
        hashMap.put("DeviceId",DeviceID);
        if(appreview.getText().toString().length()>11){
            reference.child(DeviceID+appreview.getText().toString().substring(0,9)).setValue(hashMap);
        }else{
            reference.child(DeviceID+appreview.getText().toString()).setValue(hashMap);
        }

        appreview.setText("");

        Toast.makeText(getApplicationContext(),"Thank you for sharing!\nYour feedback helps others make better decisions.",Toast.LENGTH_LONG).show();

    }

    private void initappreview() {
        clearAll();
        DatabaseReference query = FirebaseDatabase.getInstance().getReference("app review");

        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                appreviewlist.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ReviewModel model = postSnapshot.getValue(ReviewModel.class);
                    appreviewlist.add(model);
                }
                Log.e("sizeeeee",String.valueOf(appreviewlist.size()));
                ReviewAdapter Reviewadapter=new ReviewAdapter(appreviewlist,AppReviewActivity.this);
                Collections.reverse(appreviewlist);
                appreviewrec.setAdapter(Reviewadapter);
                Reviewadapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }
    private void clearAll() {
        if(appreviewlist!=null){
            appreviewlist.clear();
        }
        appreviewlist = new ArrayList<>();
    }
    private void getbio() {
        String DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("customer detail").child(DeviceID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    user u = dataSnapshot.getValue(user.class);
                   truefalse3.setText("nice");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("bgh", "onCancelled: called");
            }
        });
    }

}
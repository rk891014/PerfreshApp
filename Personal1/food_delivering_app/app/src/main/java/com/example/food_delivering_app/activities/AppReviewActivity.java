package com.example.food_delivering_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.food_delivering_app.R;
import com.example.food_delivering_app.adapter.AppReviewAdapter;
import com.example.food_delivering_app.adapter.ReviewAdapter;
import com.example.food_delivering_app.model.CustomModel;
import com.example.food_delivering_app.model.ReviewModel;
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
    ImageView reviewback;
    EditText appreview;
    ImageView reviewsubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_review);

        reviewback=findViewById(R.id.reviewback);
        appreviewrec=findViewById(R.id.reviewrec);
        reviewsubmit=findViewById(R.id.reviewsubmit);
        appreview=findViewById(R.id.appreview);
        appreviewrec.setHasFixedSize(true);
        appreviewrec.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        initappreview();

        reviewsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!appreview.getText().toString().isEmpty()){
                    addReview();
                }else {
                    Toast.makeText(AppReviewActivity.this,"you haven't filled anything",Toast.LENGTH_LONG).show();
                }

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
                AppReviewAdapter Reviewadapter2=new AppReviewAdapter(appreviewlist,AppReviewActivity.this);
                Collections.reverse(appreviewlist);
                appreviewrec.setAdapter(Reviewadapter2);
                Reviewadapter2.notifyDataSetChanged();

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
}
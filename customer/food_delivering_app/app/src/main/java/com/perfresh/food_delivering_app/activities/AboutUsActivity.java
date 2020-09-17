package com.perfresh.food_delivering_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.perfresh.food_delivering_app.R;


public class AboutUsActivity extends AppCompatActivity {

    ImageView aboutreviewback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        aboutreviewback=findViewById(R.id.aboutreviewback);
        aboutreviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
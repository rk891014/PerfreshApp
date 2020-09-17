package com.example.food_delivering_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.food_delivering_app.R;
import com.example.food_delivering_app.activities.LastActivity;
import com.example.food_delivering_app.activities.ReviewActivity;
import com.example.food_delivering_app.fragments.MyDatabaseHelper;
import com.example.food_delivering_app.model.Categorymodel;
import com.example.food_delivering_app.model.CustomModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public List<Categorymodel> categorylis;
    public Context context;
    public String DeviceID,name;
    public static float totalrating=0;
    private Activity activity;

    public CategoryAdapter(List<Categorymodel> categorylis, Context context, Activity activity) {
        this.categorylis = categorylis;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_item, parent, false);
        return new CategoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapter.ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.color.colorAccent);
        requestOptions.error(R.color.colorAccent);

        final Categorymodel model = categorylis.get(position);
        holder.productname.setText(model.getProductname());
        holder.productprice.setText("₹ "+model.getProductprice());
        holder.productquantity.setText(model.getProductquantity());
        Glide.with(context).load(categorylis.get(position % categorylis.size()).getUrl()).apply(requestOptions)
                .thumbnail(0.5f).skipMemoryCache(false).into(holder.imageView);

        name=model.getProductname();



        holder.addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });
        if(model.getProductname()!=null) {
            DeviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

            DatabaseReference query = FirebaseDatabase.getInstance().getReference("reviews").child(model.getProductname()).child("Product ratings");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    totalrating = 0;
                    int i = 0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        i++;
                        String s = postSnapshot.child("rating").getValue().toString();
                        Log.e("sdfg", s);
                        totalrating += Float.parseFloat(s);
                        totalrating = totalrating / i;
                    }
                    if (i != 0) {
                        holder.rating1.setText(String.valueOf(totalrating));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

                }
            });
        }
        holder.productreviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ReviewActivity.class);
                intent.putExtra("name", model.getProductname());
                intent.putExtra("price", model.getProductprice());
                intent.putExtra("url", model.getUrl());
                intent.putExtra("productquantity", model.getProductquantity());
                intent.putExtra("rating", holder.rating1.getText());
                activity.startActivityForResult(intent, 1);
            }
        });


    }




    @Override
    public int getItemCount() {
        return categorylis.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productname,productprice,productquantity;
        ImageView imageView;
        Button addcart;
        TextView rating1,productreviews;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productname = (TextView) itemView.findViewById(R.id.productname);
            productprice = (TextView) itemView.findViewById(R.id.productprice);
            productquantity = (TextView) itemView.findViewById(R.id.productquantity);
            imageView = (ImageView) itemView.findViewById(R.id.rimageView);
            addcart=itemView.findViewById(R.id.addcart);
            rating1=itemView.findViewById(R.id.rating1);
            productreviews=itemView.findViewById(R.id.productreviews);
        }
    }
}

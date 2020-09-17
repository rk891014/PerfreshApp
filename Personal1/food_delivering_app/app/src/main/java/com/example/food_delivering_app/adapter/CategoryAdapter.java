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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.food_delivering_app.R;
import com.example.food_delivering_app.activities.Category;
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
import java.util.Map;

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
        holder.discount.setText(model.getDiscount());
        holder.cuttedprice.setText(model.getCuttedprice());
        holder.categoryname.setText(model.getName());
        Glide.with(context).load(categorylis.get(position % categorylis.size()).getUrl()).apply(requestOptions)
                .thumbnail(0.5f).skipMemoryCache(false).into(holder.imageView);
        holder.available.setText(model.getAvailability());
        name=model.getProductname();

        final Map<String,Object> taskMap = new HashMap<String,Object>();
        holder.available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("CategoryItems").child(Category.categorii.getText().toString());

               if(holder.available.getText().toString().equals("yes")){
                   taskMap.put("availability", "no");
                   rootRef.child(model.getProductname()).updateChildren(taskMap);
               }else {
                   taskMap.put("availability", "yes");
                   rootRef.child(model.getProductname()).updateChildren(taskMap);
               }

            }
        });
            if(model.getProductname()!=null){
        DeviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        DatabaseReference query = FirebaseDatabase.getInstance().getReference("reviews").child(model.getProductname()).child("Product ratings");

        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
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

        holder.updateprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("CategoryItems").child(Category.categorii.getText().toString());
                if(!holder.newprice.getText().toString().equals("")){
                    taskMap.put("productprice", holder.newprice.getText().toString());
                    rootRef.child(model.getProductname()).updateChildren(taskMap);
                }
            }
        });

        holder.updatecategoryname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("CategoryItems").child(Category.categorii.getText().toString());
                    taskMap.put("name", holder.categoryname.getText().toString());
                    rootRef.child(model.getProductname()).updateChildren(taskMap);
                }
        });
        holder.updatecuttedprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("CategoryItems").child(Category.categorii.getText().toString());
                    taskMap.put("cuttedprice", holder.cuttedprice.getText().toString());
                    rootRef.child(model.getProductname()).updateChildren(taskMap);
                }
        });
        holder.updatediscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("CategoryItems").child(Category.categorii.getText().toString());
                    taskMap.put("discount", holder.discount.getText().toString());
                    rootRef.child(model.getProductname()).updateChildren(taskMap);
                }
        });

        holder.delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("CategoryItems")
                        .child(Category.categorii.getText().toString()).child(holder.productname.getText().toString()).removeValue();
            }
        });
    }




    @Override
    public int getItemCount() {
        return categorylis.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productname,productprice,productquantity;
        ImageView imageView,delete2;
        Button available;
        TextView rating1,productreviews;
        EditText newprice,cuttedprice,discount,categoryname;
        Button updateprice,updatecuttedprice,updatediscount,updatecategoryname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productname =  itemView.findViewById(R.id.productname);
            productprice =  itemView.findViewById(R.id.productprice);
            productquantity =  itemView.findViewById(R.id.productquantity);
            imageView =  itemView.findViewById(R.id.rimageView);
            available=itemView.findViewById(R.id.available);
            rating1=itemView.findViewById(R.id.rating1);
            productreviews=itemView.findViewById(R.id.productreviews);
            newprice=itemView.findViewById(R.id.newprice);
            updateprice=itemView.findViewById(R.id.updateprice);
            delete2=itemView.findViewById(R.id.delete2);
            cuttedprice=itemView.findViewById(R.id.cuttedprice);
            discount=itemView.findViewById(R.id.discount);
            categoryname=itemView.findViewById(R.id.categoryname);
            updatecuttedprice=itemView.findViewById(R.id.updatecuttedprice);
            updatediscount=itemView.findViewById(R.id.updatediscount);
            updatecategoryname=itemView.findViewById(R.id.updatecategoryname);
        }
    }
}

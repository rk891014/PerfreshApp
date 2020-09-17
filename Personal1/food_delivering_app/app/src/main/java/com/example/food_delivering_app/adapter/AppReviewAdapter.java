package com.example.food_delivering_app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivering_app.R;
import com.example.food_delivering_app.activities.ReviewActivity;
import com.example.food_delivering_app.model.ReviewModel;
import com.example.food_delivering_app.model.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AppReviewAdapter extends RecyclerView.Adapter<AppReviewAdapter.ViewHolder> {

    public List<ReviewModel> reviewlist;
    public Context context;

    public AppReviewAdapter(List<ReviewModel> reviewlist, Context context) {
        this.reviewlist = reviewlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list, parent, false);
        return new AppReviewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ReviewModel modellll=reviewlist.get(position);
        holder.comment.setText(modellll.getComment());

        Log.e("dgafsgadfgs",modellll.getDeviceId()+holder.comment.getText().toString());
        holder.delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.comment.getText().toString().length()>11){
                    FirebaseDatabase.getInstance().getReference().child("app review").child(modellll.getDeviceId()+holder.comment.getText().toString().substring(0,9)).removeValue();
                }
                FirebaseDatabase.getInstance().getReference().child("app review").child(modellll.getDeviceId()+holder.comment.getText().toString()).removeValue();
            }
        });


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("customer detail").child(modellll.getDeviceId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        user u = dataSnapshot.getValue(user.class);
                        holder.name.setText(u.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("bgh", "onCancelled: called");
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView comment,name;
        ImageView delete2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment=itemView.findViewById(R.id.comment);
            name=itemView.findViewById(R.id.name);
            delete2=itemView.findViewById(R.id.delete1);
        }
    }
}

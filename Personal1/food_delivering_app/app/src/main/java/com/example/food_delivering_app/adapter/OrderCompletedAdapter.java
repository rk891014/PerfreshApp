package com.example.food_delivering_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivering_app.R;
import com.example.food_delivering_app.activities.Category;
import com.example.food_delivering_app.activities.LastActivity;
import com.example.food_delivering_app.model.OrderCompletedmodel;
import com.example.food_delivering_app.model.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OrderCompletedAdapter extends RecyclerView.Adapter<OrderCompletedAdapter.ViewHolder> {
    public List<OrderCompletedmodel> todayorderlist;
    public Context context;
    private Activity activity;

    public OrderCompletedAdapter(List<OrderCompletedmodel> todayorderlist, Context context, Activity activity) {
        this.todayorderlist = todayorderlist;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_list_item, parent, false);
        return new OrderCompletedAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final OrderCompletedmodel model=todayorderlist.get(position);
        holder.datetime.setText(model.getDate()+" "+model.getTime());
        holder.totalitems.setText(model.getItemsordered());
        holder.totalprice.setText(model.getTopay());
        holder.status.setText(model.getStatus());
        holder.savedrs2.setText(model.getSaved());
        holder.deliveryboy.setText(model.getDeliveryboy());
        if(model.getStatus().equals("completed")){
            holder.topay.setText("Paid");
            holder.completetime.setText(model.getCompletedtime());
            holder.status.setTextColor(Color.GREEN);
        }

        holder.cardviev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, LastActivity.class);
                intent.putExtra("date", String.valueOf(model.getDate()));
                intent.putExtra("time", String.valueOf(model.getTime()));
                intent.putExtra("Deviceidd", String.valueOf(model.getDeviceId()));
                activity.startActivityForResult(intent, 1);
            }
        });
        if(model.getDeviceId()!=null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("customer detail").child(model.getDeviceId());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        user u = dataSnapshot.getValue(user.class);
                        holder.name2.setText(u.getName());
                        holder.address2.setText(u.getAddress());
                        holder.contactno2.setText(u.getMobileno());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("bgh", "onCancelled: called");
                }
            });
        }
        holder.calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",holder.contactno2.getText().toString(), null)));

            }
        });

        holder.delete21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setTitle("Alert Dialog");
                alertDialog.setMessage("Are you sure u want to delete.");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("TotalOrders")
                                .child(holder.datetime.getText().toString().substring(0,10)).child(holder.datetime.getText().toString().substring(0,10)+" "+
                                holder.datetime.getText().toString().substring(10)+" "+model.getDeviceId()).removeValue();

                    }
                });

                alertDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return todayorderlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView datetime,totalitems,topay,totalprice,status,completetime,savedrs2,deliveryboy,name2,address2,contactno2;
        CardView cardviev;
        ImageView calling,delete21;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            datetime=itemView.findViewById(R.id.datetime);
            totalitems=itemView.findViewById(R.id.totalitems);
            topay=itemView.findViewById(R.id.topay);
            totalprice=itemView.findViewById(R.id.totalprice);
            status=itemView.findViewById(R.id.status);
            completetime=itemView.findViewById(R.id.completetime);
            cardviev=itemView.findViewById(R.id.cardviev);
            savedrs2=itemView.findViewById(R.id.savedrs2);
            deliveryboy=itemView.findViewById(R.id.deliveryboy);

            name2=itemView.findViewById(R.id.name2);
            address2=itemView.findViewById(R.id.address2);
            contactno2=itemView.findViewById(R.id.contactno2);
            calling=itemView.findViewById(R.id.calling);
            delete21=itemView.findViewById(R.id.delete21);
        }
    }
}
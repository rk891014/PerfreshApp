package com.example.food_delivering_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivering_app.R;
import com.example.food_delivering_app.activities.Deliveryboy;
import com.example.food_delivering_app.activities.LastActivity;
import com.example.food_delivering_app.activities.OrderCompleted;
import com.example.food_delivering_app.fragments.MyDatabaseHelper;
import com.example.food_delivering_app.model.OrderCompletedmodel;
import com.example.food_delivering_app.model.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if(model.getStatus()=="completed"){
            holder.topay.setText("Paid");
            holder.completetime.setText(model.getCompletedtime());
        }

        holder.cardviev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, LastActivity.class);
                intent.putExtra("date", String.valueOf(model.getDate()));
                intent.putExtra("time", String.valueOf(model.getTime()));
                intent.putExtra("deviceidd", String.valueOf(model.getDeviceId()));
                activity.startActivityForResult(intent, 1);
            }
        });


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

        holder.shipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!OrderCompleted.name.equals("No name defined") && !OrderCompleted.name.equals("")) {
                    MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                    myDB.addBook(holder.name2.getText().toString(), model.getDeviceId(), holder.address2.getText().toString(),
                            holder.contactno2.getText().toString(), model.getItemsordered(), model.getStatus(),
                            model.getTopay(), model.getDate(), model.getTime(),
                            model.getCompletedtime());

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("TotalOrders")
                            .child(model.getDate()).child(model.getDate() + "  " + model.getTime() + " " + model.getDeviceId());
                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("status", "shipped");
                    taskMap.put("deliveryboy", OrderCompleted.name);
                    rootRef.updateChildren(taskMap);
                    Intent intent = new Intent(context, Deliveryboy.class);
                    activity.startActivityForResult(intent, 1);
                }else{
                    Toast.makeText(context,"fill your name first",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todayorderlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView datetime,totalitems,topay,totalprice,status,completetime,name2,address2,contactno2;
        CardView cardviev;
        Button shipped;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            datetime=itemView.findViewById(R.id.datetime);
            totalitems=itemView.findViewById(R.id.totalitems);
            topay=itemView.findViewById(R.id.topay);
            totalprice=itemView.findViewById(R.id.totalprice);
            status=itemView.findViewById(R.id.status);
            completetime=itemView.findViewById(R.id.completetime);
            cardviev=itemView.findViewById(R.id.cardviev);
            shipped=itemView.findViewById(R.id.shipped);

            name2=itemView.findViewById(R.id.name2);
            address2=itemView.findViewById(R.id.address2);
            contactno2=itemView.findViewById(R.id.contactno2);
        }
    }

}
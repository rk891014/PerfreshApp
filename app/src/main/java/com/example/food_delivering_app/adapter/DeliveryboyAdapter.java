package com.example.food_delivering_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivering_app.R;
import com.example.food_delivering_app.activities.Deliveryboy;
import com.example.food_delivering_app.activities.OrderCompleted;
import com.example.food_delivering_app.fragments.MyDatabaseHelper;
import com.example.food_delivering_app.model.OrderCompletedmodel;
import com.example.food_delivering_app.model.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DeliveryboyAdapter extends RecyclerView.Adapter<DeliveryboyAdapter.ViewHolder> {

    public List<OrderCompletedmodel> todayorderlist1;
    public Context context;
    private Activity activity;
    String currentTime,currentDate;
    Map<String,Object> taskMap = new HashMap<String,Object>();

    public DeliveryboyAdapter(List<OrderCompletedmodel> todayorderlist1, Context context, Activity activity) {
        this.todayorderlist1 = todayorderlist1;
        this.context = context;
        this.activity = activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delivery_list_item, parent, false);
        return new DeliveryboyAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final OrderCompletedmodel model=todayorderlist1.get(position);
        holder.datetime.setText(model.getDate()+" "+model.getTime());
        holder.totalitems.setText(model.getItemsordered());
        holder.totalprice.setText(model.getTopay());
        holder.name2.setText(model.getName());
        holder.address2.setText(model.getAddress());
        holder.contactno2.setText(model.getContactno());
        holder.status.setText("Not Completed");
        Log.e("getstatus",model.getStatus());
        if(model.getStatus().equals("Completed")){
            holder.status.setText("Completed");
            holder.status.setTextColor(Color.GREEN);
            holder.topay.setText("Paid");
            holder.completetime.setText(model.getCompletedtime());
            holder.completed.setVisibility(View.INVISIBLE);
        }



        holder.calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", model.getContactno(), null)));
            }
        });

        Log.e("device1234",model.getDeviceId());
        holder.completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                 currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Delevery Done?");
                builder.setMessage("Are you sure you have completed delivery?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("TotalOrders")
                                .child(model.getDate()).child(model.getDate()+"  "+model.getTime()+" "+model.getDeviceId());
                        taskMap.put("status", "Completed");
                        taskMap.put("completedtime", currentDate+" "+currentTime);
                        rootRef.updateChildren(taskMap);

                        MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                        myDB.updateData(model.getId(),  currentDate+" "+currentTime);
                        holder.status.setText("Completed");
                        holder.status.setTextColor(Color.GREEN);
                        holder.completetime.setText(currentDate+" "+currentTime);
                        holder.topay.setText("Paid");
                        holder.completed.setVisibility(View.INVISIBLE);

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return todayorderlist1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView datetime,totalitems,topay,totalprice,status,completetime,name2,address2,contactno2;
        Button completed;
        ImageView calling;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            datetime=itemView.findViewById(R.id.datetime1);
            totalitems=itemView.findViewById(R.id.totalitems1);
            topay=itemView.findViewById(R.id.topay1);
            totalprice=itemView.findViewById(R.id.totalprice1);
            status=itemView.findViewById(R.id.status1);
            completetime=itemView.findViewById(R.id.completetime1);
            completed=itemView.findViewById(R.id.completed1);

            name2=itemView.findViewById(R.id.name2);
            address2=itemView.findViewById(R.id.address2);
            contactno2=itemView.findViewById(R.id.contactno2);
            calling=itemView.findViewById(R.id.calling);

        }

    }
}

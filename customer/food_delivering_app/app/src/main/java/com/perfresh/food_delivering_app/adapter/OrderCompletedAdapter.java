package com.Perfresh.food_delivering_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.perfresh.food_delivering_app.R;
import com.perfresh.food_delivering_app.activities.LastActivity;
import com.perfresh.food_delivering_app.model.OrderCompletedmodel;

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
        holder.completetime.setText(model.getCompletedtime());
        if(model.getStatus().equals("completed")){
            holder.topay.setText("Paid");
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
    }

    @Override
    public int getItemCount() {
        return todayorderlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView datetime,totalitems,topay,totalprice,status,completetime,savedrs2;
        CardView cardviev;
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
        }
    }
}
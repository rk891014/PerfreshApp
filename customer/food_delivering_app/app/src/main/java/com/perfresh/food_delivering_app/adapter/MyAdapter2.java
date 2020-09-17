package com.Perfresh.food_delivering_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.perfresh.food_delivering_app.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.perfresh.food_delivering_app.activities.Category;
import com.perfresh.food_delivering_app.model.Model2;


import java.util.List;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> {
    public List<Model2> popularlist;
    public Context context;

    public MyAdapter2(List<Model2> popularlist, Context context) {

        this.popularlist = popularlist;
        this.context = context;
        Log.e("listsize", String.valueOf(popularlist.size()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new MyAdapter2.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.color.colorAccent);
        requestOptions.error(R.color.colorAccent);

        final Model2 model = popularlist.get(position);
        holder.username.setText(model.getName());
        Glide.with(context).load(popularlist.get(position % popularlist.size()).getUrl()).apply(requestOptions)
                .thumbnail(0.5f).skipMemoryCache(false).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Category.class);
                intent.putExtra("name", holder.username.getText().toString());
                intent.putExtra("rec", "rec2");
                context.startActivity(intent);
            }
        });
        holder.username.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return popularlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.username);
            imageView = (ImageView) itemView.findViewById(R.id.rimageView);
        }
    }
}
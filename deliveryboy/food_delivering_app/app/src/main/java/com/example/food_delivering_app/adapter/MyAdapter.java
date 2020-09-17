package com.example.food_delivering_app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.food_delivering_app.activities.Category;
import com.example.food_delivering_app.R;
import com.example.food_delivering_app.model.Model;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        private static final String TAG = "recyclerAdapter";

//    ArrayList<Model> imageList = new ArrayList<>();

        public List<Model> imageList;
        public Context context;

        public MyAdapter(List<Model> imageList, Context context) {
            this.imageList = imageList;
            this.context = context;
        Log.e("listsize",String.valueOf(imageList.size()));
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item,parent,false);
            return new ViewHolder(v);
        }

        @SuppressLint("CheckResult")
        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.color.colorAccent);
            requestOptions.error(R.color.colorAccent);

            final Model model = imageList.get(position);
            holder.username.setText(model.getName());
            Glide.with(context).load(imageList.get(position%imageList.size()).getUrl()).apply(requestOptions)
                    .thumbnail(0.5f).skipMemoryCache(false).into(holder.imageView);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Category.class);
                    intent.putExtra("name", holder.username.getText().toString());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder{

            TextView username;
            ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                username=itemView.findViewById(R.id.username);
                imageView=itemView.findViewById(R.id.rimageView);
            }
        }

 }


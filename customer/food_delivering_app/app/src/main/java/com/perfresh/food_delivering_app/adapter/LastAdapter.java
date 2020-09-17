package com.perfresh.food_delivering_app.adapter;

import android.content.Context;
import android.provider.Settings;
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

import com.perfresh.food_delivering_app.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.perfresh.food_delivering_app.model.CustomModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static me.zhanghai.android.materialratingbar.MaterialRatingBar.*;

public class LastAdapter extends RecyclerView.Adapter<LastAdapter.ViewHolder> {

    public List<CustomModel> lastlis;
    public Context context;
    public float ratingg=0;
    public String DeviceID;
    public LastAdapter(List<CustomModel> lastlis, Context context) {
        this.lastlis = lastlis;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.last_list, parent, false);
        return new LastAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.color.colorAccent);
        requestOptions.error(R.color.colorAccent);

        final CustomModel modelll=lastlis.get(position);

        holder.productname.setText(modelll.getProductname());
        holder.productprice.setText("₹ "+modelll.getProductprice());
        holder.productquantity.setText(modelll.getProductquantity());
        holder.quantity.setText(modelll.getQuantity());
        Glide.with(context).load(lastlis.get(position % lastlis.size()).getUrl()).apply(requestOptions)
                .thumbnail(0.5f).skipMemoryCache(false).into(holder.imageView);

        holder.ratingBarr.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener(){

            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                Toast.makeText(context,String.valueOf(rating),Toast.LENGTH_LONG).show();
                ratingg=rating;
            }
        });

        DeviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        holder.submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("reviews").child(holder.productname.getText().toString());
                Map<String,Object> taskMap = new HashMap<String,Object>();
                if(!holder.etreview.getText().toString().equals("") && ratingg==0){
                    ratingg=5;
                    rootRef.child("Product ratings").child(DeviceID).child("rating").setValue(String.valueOf(ratingg));
                    taskMap.put("DeviceId", DeviceID);
                    taskMap.put("comment",holder.etreview.getText().toString());
                    if(holder.etreview.getText().toString().length()>11){
                        rootRef.child("Product cmnt").child(DeviceID+holder.etreview.getText().toString().substring(0,9)).setValue(taskMap);
                    }else {
                        rootRef.child("Product cmnt").child(DeviceID+holder.etreview.getText().toString()).setValue(taskMap);
                    }
                    Toast.makeText(context,"Review saved Successfully",Toast.LENGTH_LONG).show();
                    holder.etreview.setText("");
                }
                else if(holder.etreview.getText().toString().equals("")){
                    Toast.makeText(context,"Plzz Review this Product",Toast.LENGTH_LONG).show();
                }
                else {
                    rootRef.child("Product ratings").child(DeviceID).child("rating").setValue(String.valueOf(ratingg));
                    taskMap.put("DeviceId", DeviceID);
                    taskMap.put("comment",holder.etreview.getText().toString());

                    if(holder.etreview.getText().toString().length()>11){
                        rootRef.child("Product cmnt").child(DeviceID+holder.etreview.getText().toString().substring(0,9)).setValue(taskMap);
                    }else {
                        rootRef.child("Product cmnt").child(DeviceID+holder.etreview.getText().toString()).setValue(taskMap);
                    }

                    Toast.makeText(context,"Review saved Successfully",Toast.LENGTH_LONG).show();
                    holder.etreview.setText("");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lastlis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productname,productprice,productquantity,quantity,total;
        ImageView imageView;
        MaterialRatingBar ratingBarr;
        EditText etreview;
        Button submit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productname = (TextView) itemView.findViewById(R.id.productname);
            productprice = (TextView) itemView.findViewById(R.id.productprice);
            productquantity = (TextView) itemView.findViewById(R.id.productquantity);
            imageView = (ImageView) itemView.findViewById(R.id.rimageView);
            quantity=itemView.findViewById(R.id.quanity);
            ratingBarr=itemView.findViewById(R.id.rating);
            etreview=itemView.findViewById(R.id.etreview);
            submit=itemView.findViewById(R.id.submit);

        }
    }
}

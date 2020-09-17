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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.food_delivering_app.R;
import com.example.food_delivering_app.model.CustomModel;

import java.util.List;

public class OrderConfirmationAdapter extends RecyclerView.Adapter<OrderConfirmationAdapter.ViewHolder>{
    public List<CustomModel> sqlitelist;
    public Context context;
    public OrderConfirmationAdapter(List<CustomModel> sqlitelist, Context context) {

        this.sqlitelist = sqlitelist;
        this.context = context;
        Log.e("listsize", String.valueOf(sqlitelist.size()));
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.confirm_order_list, parent, false);
        return new OrderConfirmationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.color.colorAccent);
        requestOptions.error(R.color.colorAccent);

        final CustomModel model=sqlitelist.get(position);

        holder.productname.setText(model.getProductname());
        holder.productprice.setText("₹ "+model.getProductprice());
        holder.productquantity.setText(model.getProductquantity());
        holder.quantity.setText(model.getQuantity());
        int totall=Integer.parseInt(model.getProductprice())*Integer.parseInt(model.getQuantity());
        holder.total.setText("₹ "+String.valueOf(totall));
        Glide.with(context).load(sqlitelist.get(position % sqlitelist.size()).getUrl()).apply(requestOptions)
                .thumbnail(0.5f).skipMemoryCache(false).into(holder.imageView);



    }
    @Override
    public int getItemCount() {
        return sqlitelist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productname,productprice,productquantity,quantity,total;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productname = (TextView) itemView.findViewById(R.id.productname);
            productprice = (TextView) itemView.findViewById(R.id.productprice);
            productquantity = (TextView) itemView.findViewById(R.id.productquantity);
            imageView = (ImageView) itemView.findViewById(R.id.rimageView);
            quantity=itemView.findViewById(R.id.quanity);
            total=itemView.findViewById(R.id.total);
        }
    }

}

package com.example.food_delivering_app.fragments;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.food_delivering_app.R;
import com.example.food_delivering_app.model.CustomModel;

import java.util.List;

import static com.example.food_delivering_app.fragments.orders_fragment.customAdapter;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{
    private Context context;
    public List<CustomModel> sqlitelist;

    public CustomAdapter(Context context, List<CustomModel> sqlitelist) {
        this.context = context;
        this.sqlitelist = sqlitelist;
        Log.e("sqlitesize",String.valueOf(sqlitelist.size()));

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_cart_item, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final MyDatabaseHelper myDB = new MyDatabaseHelper(context);


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.color.colorAccent);
        requestOptions.error(R.color.colorAccent);

         final CustomModel model=sqlitelist.get(position);

        holder.productname.setText(model.getProductname());
        holder.productprice.setText("₹ "+model.getProductprice());
        holder.productquantity.setText(model.getProductquantity());
        holder.quantity.setText(model.getQuantity());
        holder.btnquamtity.setNumber( model.getQuantity());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                myDB.deleteOneRow(model.getProductId());
                sqlitelist.remove(position);
                customAdapter.notifyDataSetChanged();

                int grandTotal=0;
                int size=sqlitelist.size();
                for(int i=0;i<size;i++){
                    int price=Integer.parseInt(sqlitelist.get(i).getProductprice());
                    int quantity=Integer.parseInt(sqlitelist.get(i).getQuantity());
                    grandTotal+=price*quantity;
                }
                orders_fragment.itemtotal.setText(String.valueOf(grandTotal));
                orders_fragment.gettotalsum();
                Log.e("grandtotal",String.valueOf(grandTotal));
                if (sqlitelist.size() == 0) {
                    orders_fragment.paymentdetail.setVisibility(View.INVISIBLE);
                    orders_fragment.emptycart.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
                    orders_fragment.empty.setText("Your cart is empty!\n" + "Add items to it now.");
                }
            }
        });


        Glide.with(context).load(sqlitelist.get(position % sqlitelist.size()).getUrl()).apply(requestOptions)
                .thumbnail(0.5f).skipMemoryCache(false).into(holder.imageView);


        holder.btnquamtity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                holder.quantity.setText(model.getQuantity());
                model.setQuantity(String.valueOf(newValue));
                myDB.updateData(model.getProductId(),String.valueOf(newValue));
                holder.quantity.setText(model.getQuantity());

                int grandTotal=0;
                int size=sqlitelist.size();
                for(int i=0;i<size;i++){
                    int price=Integer.parseInt(sqlitelist.get(i).getProductprice());
                    int quantity=Integer.parseInt(sqlitelist.get(i).getQuantity());
                    grandTotal+=price*quantity;
                }
                orders_fragment.itemtotal.setText(String.valueOf(grandTotal));
                orders_fragment.gettotalsum();
                Log.e("grandtotal",String.valueOf(grandTotal));
            }
        });

    }

    @Override
    public int getItemCount() {
        return sqlitelist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView productname,productprice,productquantity,quantity;
        ImageView imageView,delete;
        ElegantNumberButton btnquamtity;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productname = (TextView) itemView.findViewById(R.id.productname);
            productprice = (TextView) itemView.findViewById(R.id.productprice);
            productquantity = (TextView) itemView.findViewById(R.id.productquantity);
            imageView = (ImageView) itemView.findViewById(R.id.rimageView);
            btnquamtity = itemView.findViewById(R.id.btnquamtity);
            delete = itemView.findViewById(R.id.delete);
            quantity = itemView.findViewById(R.id.quanity);
            //Animate Recyclerview
//            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
//            mainLayout.setAnimation(translate_anim);
//        }
        }
    }
}

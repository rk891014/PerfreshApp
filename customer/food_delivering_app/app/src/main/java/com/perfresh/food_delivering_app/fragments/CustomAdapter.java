package com.perfresh.food_delivering_app.fragments;

import android.content.Context;
import android.graphics.Paint;
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

import com.perfresh.food_delivering_app.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.perfresh.food_delivering_app.model.CustomModel;


import java.util.List;


import static com.perfresh.food_delivering_app.fragments.orders_fragment.customAdapter;
import static com.perfresh.food_delivering_app.fragments.orders_fragment.cuttedpricee;
import static com.perfresh.food_delivering_app.fragments.orders_fragment.cuttedtopay;
import static com.perfresh.food_delivering_app.fragments.orders_fragment.deliverycharges;
import static com.perfresh.food_delivering_app.fragments.orders_fragment.itemtotal;
import static com.perfresh.food_delivering_app.fragments.orders_fragment.savedrs;
import static com.perfresh.food_delivering_app.fragments.orders_fragment.saving;


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

//        deliverycharges.setText("₹10");
//        topay.setText("₹"+String.valueOf(Float.parseFloat(topay.getText().toString().substring(1))+Float.parseFloat(deliverycharges.getText().toString().substring(1))));
//        deliverycharges.setText("₹20");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.color.colorAccent);
        requestOptions.error(R.color.colorAccent);

         final CustomModel model=sqlitelist.get(position);

        holder.productname.setText(model.getProductname());
        holder.productprice.setText("₹ "+model.getProductprice());
        holder.productquantity.setText(model.getProductquantity());
        holder.quantity.setText(model.getQuantity());
        holder.btnquamtity.setNumber( model.getQuantity());


        holder.cuttedproductprice.setText("₹"+model.getCuttedprice());
        holder.discount.setText(model.getDiscount()+"%\noff");

        if(model.getCuttedprice()==null || model.getCuttedprice().equals("")) {
            holder.cuttedproductprice.setVisibility(View.GONE);
        }
        if(model.getDiscount()==null || model.getDiscount().equals("") || model.getDiscount().equals("0")){
            holder.discount.setVisibility(View.GONE);
        }
        holder.cuttedproductprice.setPaintFlags( holder.cuttedproductprice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);





        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                myDB.deleteOneRow(model.getProductId());
                sqlitelist.remove(position);
                customAdapter.notifyDataSetChanged();

                holder.quantity.setText(model.getQuantity());
                holder.quantity.setText(model.getQuantity());

                int grandTotal=0;
                int size=sqlitelist.size();
                int cuttedgrandTotal = 0;
                for (int i = 0; i < size; i++) {
                    int price = Integer.parseInt(sqlitelist.get(i).getProductprice());
                    int quantity = Integer.parseInt(sqlitelist.get(i).getQuantity());
                    grandTotal += price * quantity;
                }
                for (int i = 0; i < size; i++) {
                    int price,quantity;
                    if(sqlitelist.get(i).getCuttedprice()!=null && !sqlitelist.get(i).getCuttedprice().equals("")){
                        price = Integer.parseInt(sqlitelist.get(i).getCuttedprice());
                        quantity = Integer.parseInt(sqlitelist.get(i).getQuantity());
                    }else{
                        price = Integer.parseInt(sqlitelist.get(i).getProductprice());
                        quantity = Integer.parseInt(sqlitelist.get(i).getQuantity());
                    }

                    cuttedgrandTotal += price * quantity;
                }
                cuttedpricee.setText(String.valueOf(cuttedgrandTotal));
                cuttedpricee.setPaintFlags(cuttedpricee.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                itemtotal.setText(String.valueOf(grandTotal));
                saving=cuttedgrandTotal-grandTotal;
                orders_fragment.gettotalsum();
                orders_fragment.customerdetail();
//                orders_fragment.coindiscountfunc();
                Log.e("rohit",String.valueOf(deliverycharges.getText().toString()));
                savedrs.setText("Saved\n"+"₹"+saving);



                if(cuttedgrandTotal==grandTotal){
                    cuttedpricee.setVisibility(View.GONE);
                    cuttedtopay.setVisibility(View.GONE);
                    savedrs.setVisibility(View.GONE);
                }


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
                int cuttedgrandTotal = 0;
                for (int i = 0; i < size; i++) {
                    int price = Integer.parseInt(sqlitelist.get(i).getProductprice());
                    int quantity = Integer.parseInt(sqlitelist.get(i).getQuantity());
                    grandTotal += price * quantity;
                }
                for (int i = 0; i < size; i++) {
                    int price,quantity;
                    if(sqlitelist.get(i).getCuttedprice()!=null && !sqlitelist.get(i).getCuttedprice().equals("")){
                        price = Integer.parseInt(sqlitelist.get(i).getCuttedprice());
                        quantity = Integer.parseInt(sqlitelist.get(i).getQuantity());
                    }else{
                        price = Integer.parseInt(sqlitelist.get(i).getProductprice());
                        quantity = Integer.parseInt(sqlitelist.get(i).getQuantity());
                    }

                    cuttedgrandTotal += price * quantity;
                }
                cuttedpricee.setText(String.valueOf(cuttedgrandTotal));
                cuttedpricee.setPaintFlags(cuttedpricee.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                itemtotal.setText(String.valueOf(grandTotal));
                saving=cuttedgrandTotal-grandTotal;
                orders_fragment.gettotalsum();
                orders_fragment.customerdetail();
//                orders_fragment.coindiscountfunc();
                Log.e("rohit",String.valueOf(deliverycharges.getText().toString()));
                savedrs.setText("Saved\n"+"₹"+saving);



                if(cuttedgrandTotal==grandTotal){
                    cuttedpricee.setVisibility(View.GONE);
                    cuttedtopay.setVisibility(View.GONE);
                    savedrs.setVisibility(View.GONE);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return sqlitelist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView productname,productprice,productquantity,quantity,cuttedproductprice,discount;
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
            cuttedproductprice= (TextView) itemView.findViewById(R.id.cuttedproductprice);
            discount=itemView.findViewById(R.id.discount);
        }
    }
}

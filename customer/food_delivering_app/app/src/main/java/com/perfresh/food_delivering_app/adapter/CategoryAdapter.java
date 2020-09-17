package com.perfresh.food_delivering_app.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.perfresh.food_delivering_app.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import com.perfresh.food_delivering_app.activities.ReviewActivity;
import com.perfresh.food_delivering_app.fragments.MyDatabaseHelper;
import com.perfresh.food_delivering_app.model.Categorymodel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Dialog mDialog;
    public List<Categorymodel> categorylis;
    public Context context;
    public String DeviceID,name;
    public static float totalrating=0;
    private Activity activity;

    public CategoryAdapter(List<Categorymodel> categorylis, Context context, Activity activity) {
        this.categorylis = categorylis;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_item, parent, false);
        return new CategoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapter.ViewHolder holder, int position) {

        final String cuttedprice,discount;

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.color.colorAccent);
        requestOptions.error(R.color.colorAccent);

        final Categorymodel model = categorylis.get(position);
        holder.productname.setText(model.getProductname());
        holder.productprice.setText("₹"+model.getProductprice());
        holder.productquantity.setText(model.getProductquantity());
        holder.cuttedproductprice.setText("₹"+model.getCuttedprice());
        holder.discount.setText(model.getDiscount()+"%\noff");
        holder.cuttedproductprice.setVisibility(View.VISIBLE);
        holder.discount.setVisibility(View.VISIBLE);
        if(model.getCuttedprice()==null || model.getCuttedprice().equals("")){
            holder.cuttedproductprice.setVisibility(View.GONE);
            cuttedprice="";
        }else {
            cuttedprice=model.getCuttedprice();
        }
        if(model.getDiscount()==null || model.getDiscount().equals("")){
            holder.discount.setVisibility(View.GONE);
            discount="0";
        }else {
            discount=model.getDiscount();
        }
        holder.cuttedproductprice.setPaintFlags( holder.cuttedproductprice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        Glide.with(context).load(categorylis.get(position % categorylis.size()).getUrl()).apply(requestOptions)
                .thumbnail(0.5f).skipMemoryCache(false).into(holder.imageView);

        name=model.getProductname();



        holder.addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                myDB.addBook(model.getProductId(),
                        model.getProductname(),
                        holder.productprice.getText().toString().substring(1),
                        holder.productquantity.getText().toString(),
                        cuttedprice,
                        discount,
                        model.getUrl(),
                        1);


            }
        });
        if(model.getProductname()!=null) {
            DeviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

            DatabaseReference query = FirebaseDatabase.getInstance().getReference("reviews").child(model.getProductname()).child("Product ratings");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    totalrating = 0;
                    int i = 0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        i++;
                        String s = postSnapshot.child("rating").getValue().toString();
                        totalrating += Float.parseFloat(s);
                    }
                    if (i != 0) {
                        totalrating = totalrating / i;
                        holder.rating1.setText(String.valueOf(new DecimalFormat("##.##").format(totalrating)));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

                }
            });
        }
        holder.cardviewww.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ReviewActivity.class);
                intent.putExtra("name", model.getProductname());
                intent.putExtra("price", model.getProductprice());
                intent.putExtra("url", model.getUrl());
                intent.putExtra("productquantity", model.getProductquantity());
                intent.putExtra("rating", holder.rating1.getText());
                intent.putExtra("cuttedprice", model.getCuttedprice());
                intent.putExtra("discount", model.getDiscount());
                activity.startActivityForResult(intent, 1);
            }
        });

        ArrayList<String> arrayList1=new ArrayList<>();
        arrayList1.add("1 kg ₹1000");
        arrayList1.add("2 kg ₹2000");
        arrayList1.add("3 kg ₹3000");
        arrayList1.add("4 kg ₹4000");
        arrayList1.add("5 kg ₹5000");

            holder.spinner.setItems(arrayList1);
            holder.spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                    String quan = item.split("₹")[0];
                    String price = item.split("₹")[1];
                    holder.productquantity.setText(quan);
                    holder.productprice.setText("₹"+price);
                    Snackbar.make(view, "Clicked " + quan+price, Snackbar.LENGTH_LONG).show();
                }
        });

    }




    @Override
    public int getItemCount() {
        return categorylis.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productname,productprice,productquantity,cuttedproductprice,discount;
        ImageView imageView;
        Button addcart;
        TextView rating1;
        CardView cardviewww;
        MaterialSpinner spinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productname = (TextView) itemView.findViewById(R.id.productname);
            productprice = (TextView) itemView.findViewById(R.id.productprice);
            cuttedproductprice= (TextView) itemView.findViewById(R.id.cuttedproductprice);
            productquantity = (TextView) itemView.findViewById(R.id.productquantity);
            imageView = (ImageView) itemView.findViewById(R.id.rimageView);
            addcart=itemView.findViewById(R.id.addcart);
            rating1=itemView.findViewById(R.id.rating1);
            cardviewww=itemView.findViewById(R.id.cardviewww);
            discount=itemView.findViewById(R.id.discount);
            spinner = (MaterialSpinner) itemView.findViewById(R.id.spinner);
        }
    }

    private void showdialog() {
//        mDialog = new Dialog(context);
//        mDialog.setContentView(R.layout.available_quantities_dialog_box);
//        mDialog.setCanceledOnTouchOutside(true);
//        mDialog.setCancelable(true);
//        mDialog.show();

//        AlertDialog.Builder builder=new AlertDialog.Builder(context);
//        final View customLayout =  LayoutInflater.from(context).inflate(R.layout.available_quantities_dialog_box, null);
//        builder.setSingleChoiceItems(arr, -1, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                data=arr[which];
//            }
//        });
//        builder.setPositiveButton("Add items", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.setView(customLayout);
//        builder.create();
//
//        builder.show();
    }
}

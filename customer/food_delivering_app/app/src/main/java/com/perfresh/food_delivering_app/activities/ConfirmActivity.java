package com.perfresh.food_delivering_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.perfresh.food_delivering_app.R;

import com.perfresh.food_delivering_app.adapter.OrderConfirmationAdapter;
import com.perfresh.food_delivering_app.fragments.MyDatabaseHelper;
import com.perfresh.food_delivering_app.fragments.orders_fragment;
import com.perfresh.food_delivering_app.model.CustomModel;
import com.perfresh.food_delivering_app.model.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cdflynn.android.library.checkview.CheckView;

import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_ID;
import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_NAME;
import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_PRICE;
import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_PRODUCTQUANTITY;
import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_QUANTITY;
import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_URL;
import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.TABLE_NAME;


public class ConfirmActivity extends AppCompatActivity {

    RecyclerView ordercomprec;
    ImageView ocback;
    String s,s2;
    ArrayList<CustomModel> sqlitelist = new ArrayList<>();
    CheckView checkView;
    Dialog mDialog3;
    String DeviceID;
    LinearLayout linearro;
    OrderConfirmationAdapter adapter;
    Dialog dialog;
    TextView cname, caddress, cmobileno,cdistance, topay,cuttedtopay,ordertotal,savedrs1;
    Button changedetails, vieworders, rohit;

    LinearLayout linearorder;
    ImageView emptycart;
    TextView empty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        ordercomprec = findViewById(R.id.ordercomprec);
        ocback = findViewById(R.id.Ocback);
        cname = findViewById(R.id.cname);
        caddress = findViewById(R.id.caddress);
        cmobileno = findViewById(R.id.ccontactno);
        ordertotal = findViewById(R.id.ordertotal);
        cdistance = findViewById(R.id.cdistance);
        savedrs1 = findViewById(R.id.savedrs1);
        changedetails = findViewById(R.id.changedetails);
        topay = findViewById(R.id.topay);
        cuttedtopay = findViewById(R.id.cuttedtopay);
        empty=findViewById(R.id.empty);
        emptycart=findViewById(R.id.emptycart);
        linearorder=findViewById(R.id.linearorder);
        rohit = findViewById(R.id.rohit);
        linearro=findViewById(R.id.linearro);


        ordertotal.setVisibility(View.GONE);


        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        init();

        getbio();
        getprice();

        rohit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Float.parseFloat(cdistance.getText().toString())<Float.parseFloat(MainActivity.appavailable)) {
                    if (Float.parseFloat(s.substring(1)) > 30) {
                        if (checkconnection()) {
                            confirmDialog();
                            send_data_to_firebase();
                            MyDatabaseHelper myDB = new MyDatabaseHelper(ConfirmActivity.this);
                            myDB.deleteAllData();
                            sqlitelist.clear();
                            adapter.notifyDataSetChanged();
                            linearorder.setVisibility(View.INVISIBLE);
                            emptycart.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
                            empty.setText("Your cart is empty!\n" + "Add items to it now.");
                            float total = Float.parseFloat(topay.getText().toString().substring(1)) * 10 + Float.parseFloat(ordertotal.getText().toString());
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("customer detail").child(DeviceID);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("no", "2");
                            hashMap.put("shares", "0");
                            hashMap.put("coinsmoney", "0");
                            if (Float.parseFloat(topay.getText().toString().substring(1)) > Float.parseFloat(orders_fragment.deliveryfee)) {
                                hashMap.put("orders", String.valueOf(total));
                            }

                            reference.updateChildren(hashMap);

                        } else {
                            Toast.makeText(ConfirmActivity.this, "check your Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ConfirmActivity.this, "Order amount is too low", Toast.LENGTH_LONG).show();

                    }
                }else{
                    showdialog();
                }
            }
        });



        ocback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmActivity.super.onBackPressed();
            }
        });
        changedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ConfirmActivity.this, MainActivity.class);
                i.putExtra("vieworderss", "3");
                startActivity(i);
            }
        });
        int save=Integer.parseInt(cuttedtopay.getText().toString().substring(1))-Integer.parseInt(topay.getText().toString().substring(1));
        savedrs1.setText("Saved\n"+"₹"+save);

        linearro.setVisibility(View.GONE);

    }

    private void showdialog() {
         mDialog3= new Dialog(this);
        mDialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog3.setContentView(R.layout.cantdeliverdialog);
        mDialog3.setCanceledOnTouchOutside(true);
        mDialog3.setCancelable(true);
        mDialog3.show();
    }

    private void send_data_to_firebase() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        int i=0;
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("TotalOrders").child(currentDate).child(currentDate+"  "+currentTime+" "+DeviceID);
        Map<String,Object> taskMap = new HashMap<String,Object>();
        for(CustomModel data : sqlitelist) {
            i++;
            rootRef.child("orders").child(String.valueOf(i)).setValue(data);
            taskMap.put("DeviceId", DeviceID);
            rootRef.child("orders").child(String.valueOf(i)).updateChildren(taskMap);
        }

        String st="";
        for(CustomModel data : sqlitelist) {
            st=st+data.getProductname()+"×"+data.getQuantity()+"  ";
        }
        Log.e("strrrr",st);
        getprice();

        rootRef.child("date").setValue(currentDate);
        rootRef.child("time").setValue(currentTime);
        rootRef.child("itemsordered").setValue(st);
        rootRef.child("topay").setValue(s);
        rootRef.child("deliveryboy").setValue("");
        rootRef.child("saved").setValue(savedrs1.getText().toString());
        rootRef.child("DeviceId").setValue(DeviceID);
        rootRef.child("status").setValue("pending");
        rootRef.child("completedtime").setValue("");
    }



    private void getbio() {
        String DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("customer detail").child(DeviceID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    user u = dataSnapshot.getValue(user.class);
                    cname.setText(u.getName());
                    caddress.setText(u.getAddress());
                    cmobileno.setText(u.getMobileno());
                    cdistance.setText(u.getDistance());
                    ordertotal.setText(u.getOrders());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("bgh", "onCancelled: called");
            }
        });
    }

    private void confirmDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_dialog_box);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        checkView = dialog.findViewById(R.id.check);
        vieworders = dialog.findViewById(R.id.vieworders);

        vieworders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmActivity.this, OrderCompleted.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        checkView.check();
        dialog.show();
    }

    private void getprice() {
        Intent in=getIntent();
        Bundle b=in.getExtras();
        assert b != null;
        s=b.getString("topay");
        s2=b.getString("cuttedtopay");
        assert s != null;
        topay.setText("₹"+Math.round(Float.parseFloat(s.substring(1))));
        cuttedtopay.setText("₹"+Math.round(Float.parseFloat(s2.substring(1))));
        cuttedtopay.setPaintFlags(cuttedtopay.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

    }



    private void init() {
        sqlitelist.clear();
        sqlitelist.addAll(storeDataInArrays());
        adapter = new OrderConfirmationAdapter(sqlitelist,getApplicationContext());
        ordercomprec.setAdapter(adapter);
        ordercomprec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.notifyDataSetChanged();
    }

    public List<CustomModel> storeDataInArrays() {
        ArrayList<CustomModel> sqlitelist = new ArrayList<>();
        MyDatabaseHelper databaseHelper = MyDatabaseHelper.getInstance(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "your Cart is Empty.\n    add items to it", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String price = cursor.getString(cursor.getColumnIndex(COLUMN_PRICE));
                String product_quantity = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTQUANTITY));
                String url = cursor.getString(cursor.getColumnIndex(COLUMN_URL));
                String quantity = cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY));


                sqlitelist.add(new CustomModel(url, id, name, price, product_quantity, quantity));
            }
        }
        return sqlitelist;
    }

    public boolean checkconnection(){
        ConnectivityManager manager=(ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork=manager.getActiveNetworkInfo();
        if(null!=activenetwork){
            if(activenetwork.getType()==ConnectivityManager.TYPE_WIFI){
                return true;
            }
            else if(activenetwork.getType()==ConnectivityManager.TYPE_MOBILE){
                return true;
            }
        }
        return false;
    }
}
package com.perfresh.food_delivering_app.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.perfresh.food_delivering_app.R;
import com.perfresh.food_delivering_app.activities.ConfirmActivity;
import com.perfresh.food_delivering_app.activities.MainActivity;
import com.perfresh.food_delivering_app.activities.OrderCompleted;
import com.perfresh.food_delivering_app.model.CustomModel;
import com.perfresh.food_delivering_app.model.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_CUTTEDPRICE;
import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_DISCOUNT;
import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_ID;
import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_NAME;
import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_PRICE;
import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_PRODUCTQUANTITY;
import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_QUANTITY;
import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_URL;
import static com.perfresh.food_delivering_app.fragments.MyDatabaseHelper.TABLE_NAME;


public class orders_fragment extends Fragment {
    RecyclerView recyclerView;
    public static ArrayList<String> customerlist = new ArrayList<String>();
    public static TextView empty,truefalse;
    public static ImageView bback,emptycart;
    public static String DeviceID;
    public static LinearLayout paymentdetail;
    public static TextView itemtotal,cuttedpricee,coindiscount,totalcoins,afterfreedel,coinsearned,orderbeyond,tenrsless;
    public static TextView deliverycharges, topay,cuttedtopay,savedrs,firstdiscountrupees,firsttopay,firstdiscount;
    public static Button ordernow,getcoinsdiscount;
    static int final_sum = 0;
    public static LinearLayout linear3;
    static int saving=0;
    public static int i=0;
    public static int k=0;
    public static int ro=0;
    SharedPreferences.Editor editor;
    public static float totalmoney=0;
    static int final_cuttedsum = 0;
    Button ordercompleted;
    public Dialog mdialogg;
    public static String deliveryfee = "100";
    public int grandTotal=0;
    public int cuttedgrandTotal=0;
    SharedPreferences prefs;
    public static String deliverycharge = "10";
    public static String freeunderdistance = "1000";
    public static String coindiscountdivision = "1000";
    public static String distance = "1000";
    public static String rsper200 = "10";
    Dialog mDialog11;
    public static String firsttimediscount = "25";
    public static boolean found ;
    public static boolean found1;


    ArrayList<CustomModel> sqlitelist = new ArrayList<>();
    public static CustomAdapter customAdapter;
    public orders_fragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getView() != null) {

                if(customAdapter!=null){
                    sqlitelistdata();
                    customAdapter.notifyDataSetChanged();
                }
                Log.e("sizzzzz", String.valueOf(sqlitelist.size()));

                if (sqlitelist.size() != 0) {
                    paymentdetail.setVisibility(View.VISIBLE);
                    emptycart.setVisibility(View.INVISIBLE);
                    empty.setVisibility(View.INVISIBLE);
                }else {
                    paymentdetail.setVisibility(View.INVISIBLE);
                    emptycart.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
                    empty.setText("Your cart is empty!\n" + "Add items to it now.");
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_fragment, container, false);
        empty = view.findViewById(R.id.empty);
        bback = view.findViewById(R.id.bback);
        emptycart = view.findViewById(R.id.emptycart);
        coindiscount = view.findViewById(R.id.coindiscount);
        afterfreedel = view.findViewById(R.id.afterfreedel);
        coinsearned = view.findViewById(R.id.coinsearned);
        tenrsless= view.findViewById(R.id.tenrsless);
        orderbeyond = view.findViewById(R.id.orderbeyond);
        paymentdetail = view.findViewById(R.id.paymentdetail);
        itemtotal = view.findViewById(R.id.itemtotal);
        cuttedpricee = view.findViewById(R.id.cuttedpricee);
        savedrs= view.findViewById(R.id.savedrs);
        truefalse=view.findViewById(R.id.truefalse);
        deliverycharges = view.findViewById(R.id.deliverycharges);
        ordercompleted=view.findViewById(R.id.ordercompleted);
        topay = view.findViewById(R.id.topay);
        getcoinsdiscount= view.findViewById(R.id.getcoinsdiscount);
        totalcoins = view.findViewById(R.id.totalcoins);
        firstdiscountrupees = view.findViewById(R.id.firstdiscountrupees);
        firstdiscount = view.findViewById(R.id.firstdiscount);
        linear3 = view.findViewById(R.id.linear3);
        firsttopay = view.findViewById(R.id.firsttopay);
        cuttedtopay= view.findViewById(R.id.cuttedtopay);
        ordernow = view.findViewById(R.id.ordernow);
        recyclerView = view.findViewById(R.id.cartrec);
        linear3.setVisibility(View.GONE);
        firsttopay.setVisibility(View.GONE);
        DeviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sqlitelistdata();
        customerlist = new ArrayList<>();

        gettotalsum();

        customerdetail();

        Log.e("mdfgdshhsdgf",deliverycharges.getText().toString());



        ordercompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrderCompleted.class);
                startActivity(intent);
            }
        });
        ordernow.setOnClickListener(new View.OnClickListener() {
            float totaltotal=0;
            @Override
            public void onClick(View v) {
//                Float.parseFloat(firstdiscountrupees.getText().toString().substring(4)
                Log.e("jhghjghgjhg",itemtotal.getText().toString()+"  "+deliverycharges.getText().toString().substring(1)+"  "
                        +coindiscount.getText().toString()+"  ");

                totaltotal=Float.parseFloat(itemtotal.getText().toString())+Float.parseFloat(deliverycharges.getText().toString().substring(1))
                        -Float.parseFloat(coindiscount.getText().toString());
                topay.setText("₹"+String.valueOf(totaltotal));
                if (Float.parseFloat(topay.getText().toString().substring(1)) < Float.parseFloat(deliveryfee)) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("You will not be rewarded with any coins")
                            .setMessage("Are you sure you want to Continue?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (found1 && i == 1) {
                                        Intent intent = new Intent(getContext(), ConfirmActivity.class);
                                        if (!firsttopay.getText().toString().equals("0")) {
                                            float firstt=totaltotal-Float.parseFloat(firstdiscountrupees.getText().toString().substring(4));
                                            firsttopay.setText("₹"+String.valueOf(firstt));
                                            intent.putExtra("topay", firsttopay.getText().toString());
                                            intent.putExtra("cuttedtopay", cuttedtopay.getText().toString());
                                        } else {
                                            intent.putExtra("topay", topay.getText().toString());
                                            intent.putExtra("cuttedtopay", cuttedtopay.getText().toString());
                                        }
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getContext(), "you have not registered yet", Toast.LENGTH_LONG).show();
                                        ((MainActivity) getActivity()).setCurrentItem(3, true);
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.coins)
                            .show();
                } else {
                    if (found1 && i == 1) {
                        Intent intent = new Intent(getContext(), ConfirmActivity.class);
                        if (!firsttopay.getText().toString().equals("0")) {
                            float firstt=totaltotal-Float.parseFloat(firstdiscountrupees.getText().toString().substring(4));
                            firsttopay.setText("₹"+String.valueOf(firstt));
                            intent.putExtra("topay", firsttopay.getText().toString());
                            intent.putExtra("cuttedtopay", cuttedtopay.getText().toString());
                        } else {
                            intent.putExtra("topay", topay.getText().toString());
                            intent.putExtra("cuttedtopay", cuttedtopay.getText().toString());
                        }
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "you have not registered yet", Toast.LENGTH_LONG).show();
                        ((MainActivity) getActivity()).setCurrentItem(3, true);
                    }
                }
            }
        });
        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setCurrentItem(0, true);
            }
        });
        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        coindiscountfunc();


        getcoinsdiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( truefalse.getText().toString().equals("true")) {
                    if (coindiscount.getText().toString().equals("0")) {
                        int remainingcoins = Integer.parseInt(totalcoins.getText().toString());


                        Log.e("coindiscountdivision", coindiscountdivision);
                        if (Integer.parseInt(coindiscountdivision) > 10 && remainingcoins > Integer.parseInt(coindiscountdivision)) {

                            ro=1;
                            int coinmoney = Integer.parseInt(coindiscount.getText().toString()) + Integer.parseInt(totalcoins.getText().toString()) / Integer.parseInt(coindiscountdivision);
                            remainingcoins = Integer.parseInt(totalcoins.getText().toString()) % Integer.parseInt(coindiscountdivision);
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("customer detail").child(DeviceID);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("shares", "0");
                            hashMap.put("orders", String.valueOf(remainingcoins));
                            hashMap.put("coinsmoney", String.valueOf(coinmoney));
                            reference.updateChildren(hashMap);


                            showdialog();
                            getcoinsdiscount.setVisibility(View.GONE);
                            k = 1;
                            ((MainActivity)getActivity()).setCurrentItem (0, true);

                        }
                    } else {
                        Toast.makeText(getContext(), "You can use coins only one time in one order use these coins in next order.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    ((MainActivity)getActivity()).setCurrentItem (3, true);
                    Toast.makeText(getContext(), "you have not registered yet", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cuttedtopay.setVisibility(View.VISIBLE);
        tenrsless.setText("Get ₹"+ deliverycharge+" less on delivery charge on\nbuying item greater than ₹"+deliveryfee+".");
        return view;
    }

    private void showdialog() {
         mDialog11 = new Dialog(getContext());
        mDialog11.setContentView(R.layout.faltu_dialog_box);
        mDialog11.setCanceledOnTouchOutside(true);
        mDialog11.setCancelable(true);
        mDialog11.show();
    }

    public static void coindiscountfunc() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("customer detail").child(DeviceID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user u = dataSnapshot.getValue(user.class);
                if (dataSnapshot.exists()) {
                    totalcoins.setText(String.valueOf(Math.round(Float.parseFloat(u.getShares()) * 500 + Float.parseFloat(u.getOrders()))));

                    if (u.getCoinsmoney() != null) {
                        coindiscount.setText(u.getCoinsmoney());
                    } else {
                        coindiscount.setText("0");
                    }
                    if (k == 1) {
                        if (Float.parseFloat(topay.getText().toString().substring(1)) <= Float.parseFloat(u.getCoinsmoney())) {
                            topay.setText("₹0");
                        } else {
                            float rupees = Float.parseFloat(topay.getText().toString().substring(1)) - Float.parseFloat(u.getCoinsmoney());
                            topay.setText("₹" + String.valueOf(rupees));
                        }
                        savedrs.setText("Saved\n" + "₹" + String.valueOf(Float.parseFloat(cuttedtopay.getText().toString().substring(1)) - Float.parseFloat(topay.getText().toString().substring(1))));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "onCancelled: called");
            }
        });
    }

    public void sqlitelistdata() {

        sqlitelist.clear();
        sqlitelist.addAll(storeDataInArrays());

        if (sqlitelist.size() == 0) {
            paymentdetail.setVisibility(View.INVISIBLE);
            emptycart.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
            empty.setText("Your cart is empty!\n" + "Add items to it now.");
        } else {
            customAdapter = new CustomAdapter(getContext(), sqlitelist);
            recyclerView.setAdapter(customAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            customAdapter.notifyDataSetChanged();
            Log.e("size", String.valueOf(sqlitelist.size()));
            grandTotal = 0;
            cuttedgrandTotal = 0;
            int size = sqlitelist.size();
            for (int i = 0; i < size; i++) {
                int price = Integer.parseInt(sqlitelist.get(i).getProductprice());
                int quantity = Integer.parseInt(sqlitelist.get(i).getQuantity());
                grandTotal += price * quantity;
            }
            itemtotal.setText(String.valueOf(grandTotal));
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
        }
    }
    public List<CustomModel> storeDataInArrays() {
        ArrayList<CustomModel> sqlitelist = new ArrayList<>();
        MyDatabaseHelper databaseHelper = MyDatabaseHelper.getInstance(getContext());
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "your Cart is Empty.\n    add items to it", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String price = cursor.getString(cursor.getColumnIndex(COLUMN_PRICE));
                String product_quantity = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTQUANTITY));
                String cuttedprice = cursor.getString(cursor.getColumnIndex(COLUMN_CUTTEDPRICE));
                String discount = cursor.getString(cursor.getColumnIndex(COLUMN_DISCOUNT));
                String url = cursor.getString(cursor.getColumnIndex(COLUMN_URL));
                String quantity = cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY));

                sqlitelist.add(new CustomModel(url, id, name, price, product_quantity, cuttedprice,discount,quantity));
            }
        }
        return sqlitelist;
    }
    public static void gettotalsum(){

        DatabaseReference query2 = FirebaseDatabase.getInstance().getReference("whole");
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    deliveryfee = dataSnapshot.child("deliveryfee").getValue().toString();
                    deliverycharge = dataSnapshot.child("deliverycharge").getValue().toString();
                    freeunderdistance = dataSnapshot.child("freeunderdistance").getValue().toString();
                    coindiscountdivision = dataSnapshot.child("coindiscountdivision").getValue().toString();
                    rsper200 = dataSnapshot.child("rsper200").getValue().toString();
                    firsttimediscount = dataSnapshot.child("firsttimediscount").getValue().toString();
                    firstdiscount.setText(firsttimediscount+"%");
                    afterfreedel.setText("above ₹"+deliveryfee);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });

        int total_sum=Integer.parseInt(itemtotal.getText().toString());
        int total_cuttedsum=Integer.parseInt(cuttedpricee.getText().toString());

        if(deliverycharge!=null && deliveryfee!=null) {
            if (total_sum <= Integer.parseInt(deliveryfee)) {
                final_sum = 0;
                final_cuttedsum=0;

                final_sum = total_sum + Integer.parseInt(deliverycharge);
                final_cuttedsum = total_cuttedsum + Integer.parseInt(deliverycharge);
                topay.setText("₹" + String.valueOf(final_sum));
                cuttedtopay.setText("₹" + String.valueOf(final_cuttedsum));
                deliverycharges.setText("₹" + deliverycharge);
                cuttedtopay.setPaintFlags(cuttedtopay.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                itemtotal.setText(String.valueOf(total_sum));
                cuttedpricee.setText("₹" +String.valueOf(total_cuttedsum));
                cuttedpricee.setPaintFlags(cuttedpricee.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                saving=final_cuttedsum-final_sum;
                savedrs.setText("Saved\n"+"₹"+saving);
            } else {
                deliverycharges.setText("₹" + "0");
                cuttedtopay.setText("₹" + String.valueOf(total_cuttedsum));
                topay.setText("₹" + String.valueOf(total_sum));
                cuttedtopay.setPaintFlags(cuttedtopay.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                itemtotal.setText(String.valueOf(total_sum));
                cuttedpricee.setText("₹" +String.valueOf(total_cuttedsum));
                cuttedpricee.setPaintFlags(cuttedpricee.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                saving=total_cuttedsum-total_sum;
                savedrs.setText("Saved\n"+"₹"+saving);

            }
            if(total_cuttedsum==total_sum){
                cuttedpricee.setVisibility(View.GONE);
                cuttedtopay.setVisibility(View.GONE);
                savedrs.setVisibility(View.GONE);
            }
        }
        if(ro==0){
            coindiscountfunc();
        }


    }
    public static void customerdetail() {
        DatabaseReference query = FirebaseDatabase.getInstance().getReference("customer detail").child(DeviceID);
        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                i=1;
                if(dataSnapshot.exists()){
                            if(dataSnapshot.getKey().equals(DeviceID)){
                                truefalse.setText("true");
                                found=true;
                                found1=true;
                                if(dataSnapshot.child("no").getValue().toString().equals("1")){
                                    found=false;
                                }
                                totalmoney=0;
                                distance=dataSnapshot.child("distance").getValue().toString();

                                String parts11 = deliverycharges.getText().toString().substring(1);

                                String parts21 = cuttedtopay.getText().toString().substring(1);

                                String parts31 = topay.getText().toString().substring(1);

                                float dist=Float.parseFloat(distance);
                                float freeunderdistance1=Float.parseFloat(freeunderdistance);

                                while (dist>freeunderdistance1){
                                    dist-=200;
                                    totalmoney+=Float.parseFloat(rsper200);
                                }
                                totalmoney=totalmoney*(3/2);
                                Log.e("dsfjhdsj",String.valueOf(totalmoney) );
                                float finaltotalmoney=0;
                                finaltotalmoney=Float.parseFloat(parts11)+totalmoney;
                                float finaltotalmoney1=0;
                                finaltotalmoney1=Float.parseFloat(parts21)+totalmoney;
                                float finaltotalmoney2=0;
                                finaltotalmoney2=Float.parseFloat(parts31)+totalmoney-Float.parseFloat(coindiscount.getText().toString());
                                deliverycharges.setText("₹"+finaltotalmoney);
                                cuttedtopay.setText("₹"+finaltotalmoney1);

                                float s=totalmoney*12+Float.parseFloat(deliveryfee);

                                if(finaltotalmoney2>=s ){
                                    finaltotalmoney2-=finaltotalmoney;
                                    deliverycharges.setText("₹"+0);
                                }
                                topay.setText("₹"+finaltotalmoney2);
                                if(Float.parseFloat(distance)>Float.parseFloat(freeunderdistance)){
                                    afterfreedel.setText("above ₹"+s);
                                }else{
                                    afterfreedel.setText("above ₹"+deliveryfee);
                                }


                                if(finaltotalmoney2<Float.parseFloat(deliveryfee)){
                                    coinsearned.setText("You will earn 0 coins from\nthis Order");
                                    orderbeyond.setVisibility(View.VISIBLE);
                                    orderbeyond.setText("Order beyond ₹"+deliveryfee+" will\ngive you "+Math.round(Float.parseFloat(deliveryfee)*10) +" coins and more.");
                                }else{
                                    coinsearned.setText("You will earn "+Math.round(finaltotalmoney2*10)+" coins from this Order");
                                    orderbeyond.setVisibility(View.GONE);
                                }

                            }

                }
                if(!found && i==1 ){

                    String part1 =topay.getText().toString().substring(1);

                    String part11234 = deliverycharges.getText().toString().substring(1);

                    int dis=Integer.parseInt(firsttimediscount);



                    String parts15 = cuttedtopay.getText().toString().substring(1);
                    float d=(Float.parseFloat(part1)-Float.parseFloat(part11234))*(dis)/100;
                    float l=Float.parseFloat(parts15)-Float.parseFloat(part1)+d;
                    firstdiscountrupees.setText(" - "+"₹"+d);
                    float r=0;
                    r=Float.parseFloat(part1)-d;
                    firsttopay.setText("₹"+r);
                    Log.e("fdsafdf",itemtotal.getText().toString()+" ");
                    firsttopay.setVisibility(View.VISIBLE);
                    getcoinsdiscount.setVisibility(View.INVISIBLE);
                    linear3.setVisibility(View.VISIBLE);
                    float k=l+Float.parseFloat(part11234);
                    savedrs.setText("Saved\n"+"₹"+k);
                    topay.setPaintFlags(topay.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    coinsearned.setVisibility(View.GONE);
                    orderbeyond.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());
            }

            });




    }

    @Override
    public void onResume() {
        super.onResume();
        sqlitelistdata();
        if(customAdapter!=null){
            customAdapter.notifyDataSetChanged();

        }
    }
}
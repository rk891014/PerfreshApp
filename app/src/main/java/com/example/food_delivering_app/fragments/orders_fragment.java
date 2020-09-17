package com.example.food_delivering_app.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivering_app.R;
import com.example.food_delivering_app.activities.ConfirmActivity;
import com.example.food_delivering_app.activities.MainActivity;
import com.example.food_delivering_app.activities.OrderCompleted;
import com.example.food_delivering_app.model.CustomModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;




public class orders_fragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> customerlist = new ArrayList<String>();
    public static TextView empty;
    public static ImageView bback,emptycart;
    public static CardView paymentdetail;
    public static TextView itemtotal;
    public static TextView deliverycharges, topay;
    Button ordernow;
    static int final_sum = 0;
    Button ordercompleted;
    public static String deliveryfee = "50";
    public int grandTotal=0;
    public static String deliverycharge = "10";
    boolean found = false;
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
        paymentdetail = view.findViewById(R.id.paymentdetail);
        itemtotal = view.findViewById(R.id.itemtotal);
        deliverycharges = view.findViewById(R.id.deliverycharges);
        ordercompleted=view.findViewById(R.id.ordercompleted);
        topay = view.findViewById(R.id.topay);
        ordernow = view.findViewById(R.id.ordernow);
        recyclerView = view.findViewById(R.id.cartrec);
        sqlitelistdata();
        customerlist = new ArrayList<>();
        gettotalsum();
        customerdetail();


        ordercompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrderCompleted.class);
                startActivity(intent);
            }
        });
        ordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchcustomer();
                if (customerlist.isEmpty()) {
                    Toast.makeText(getContext(), "wait a sec", Toast.LENGTH_LONG).show();
                } else {
                    if (found) {
                        Intent intent = new Intent(getContext(), ConfirmActivity.class);
                        intent.putExtra("topay",topay.getText().toString() );
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
        return view;
    }

    public void sqlitelistdata() {

        sqlitelist.clear();

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
            int size = sqlitelist.size();
            for (int i = 0; i < size; i++) {
                int price = Integer.parseInt(sqlitelist.get(i).getProductprice());
                int quantity = Integer.parseInt(sqlitelist.get(i).getQuantity());
                grandTotal += price * quantity;
            }
            itemtotal.setText(String.valueOf(grandTotal));
        }
    }

    public static void gettotalsum(){

        DatabaseReference query2 = FirebaseDatabase.getInstance().getReference("whole");
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    deliveryfee = dataSnapshot.child("deliveryfee").getValue().toString();
                    deliverycharge = dataSnapshot.child("deliverycharge").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });
        int total_sum=Integer.parseInt(itemtotal.getText().toString());

        if(deliverycharge!=null && deliveryfee!=null) {
            if (total_sum <= Integer.parseInt(deliveryfee)) {
                final_sum = 0;
                final_sum = total_sum + Integer.parseInt(deliverycharge);
                topay.setText("₹ " + String.valueOf(final_sum));
                deliverycharges.setText("₹ " + deliverycharge);
            } else {
                deliverycharges.setText("₹ " + "0");
                topay.setText("₹ " + String.valueOf(total_sum));
            }
        }

    }

    private void customerdetail() {
        DatabaseReference query = FirebaseDatabase.getInstance().getReference("customer detail");
        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    customerlist.clear();
                    customerlist = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            customerlist.add(postSnapshot.getKey());
                        }
                    }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());
            }
            });
    }
    private void searchcustomer() {
        customerdetail();

            final String DeviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            for (String ele : customerlist) {
                if (DeviceID.equals(ele)) {
                    found = true;
                }
        }
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
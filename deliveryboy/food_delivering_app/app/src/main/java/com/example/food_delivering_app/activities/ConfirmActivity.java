package com.example.food_delivering_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivering_app.OTP_Reciever;
import com.example.food_delivering_app.R;
import com.example.food_delivering_app.adapter.OrderConfirmationAdapter;
import com.example.food_delivering_app.fragments.MyDatabaseHelper;
import com.example.food_delivering_app.model.CustomModel;
import com.example.food_delivering_app.model.user;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
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
import java.util.concurrent.TimeUnit;

import cdflynn.android.library.checkview.CheckView;



public class ConfirmActivity extends AppCompatActivity {

    RecyclerView ordercomprec;

    ImageView ocback;
    FirebaseAuth fauth;
    String s;
    ArrayList<CustomModel> sqlitelist = new ArrayList<>();
    EditText codeEnter;
    CheckView checkView;
    String DeviceID;
    OrderConfirmationAdapter adapter;
    Dialog dialog;
    ProgressBar progressBar;
    TextView cname, caddress, cmobileno, topay;
    Button changedetails, nextBtn, vieworders, rohit;
    String verificaionid;
    LinearLayout linearorder;
    ImageView emptycart;
    TextView empty;
    PhoneAuthProvider.ForceResendingToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        ordercomprec = findViewById(R.id.ordercomprec);
        ocback = findViewById(R.id.Ocback);
        cname = findViewById(R.id.cname);
        caddress = findViewById(R.id.caddress);
        cmobileno = findViewById(R.id.ccontactno);
        changedetails = findViewById(R.id.changedetails);
        topay = findViewById(R.id.topay);
        empty=findViewById(R.id.empty);
        emptycart=findViewById(R.id.emptycart);
        linearorder=findViewById(R.id.linearorder);
        rohit = findViewById(R.id.rohit);

        requestpermission();
        fauth = FirebaseAuth.getInstance();
        codeEnter = findViewById(R.id.codeEnter);
        progressBar = findViewById(R.id.progressBar);
        nextBtn = findViewById(R.id.nextBtn);
        progressBar.setVisibility(View.GONE);
        codeEnter.setVisibility(View.GONE);

        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        getbio();
        getprice();

        rohit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
                send_data_to_firebase();
                MyDatabaseHelper myDB = new MyDatabaseHelper(ConfirmActivity.this);
                myDB.deleteAllData();
                sqlitelist.clear();
                adapter.notifyDataSetChanged();
                linearorder.setVisibility(View.INVISIBLE);
                emptycart.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
                empty.setText("Your cart is empty!\n" + "Add items to it now.");
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getbio();
                String phoneno = cmobileno.getText().toString();
                Log.e("phoneno", String.valueOf(phoneno.length()));
                if (phoneno.length() == 10) {
                    String phonenum = "+91" + phoneno;
                    progressBar.setVisibility(View.VISIBLE);
                    requestOtp(phonenum);
                    Toast.makeText(ConfirmActivity.this, phonenum, Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(ConfirmActivity.this, "Entered no is wrong", Toast.LENGTH_LONG).show();
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

    }

    private void send_data_to_firebase() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        int i=0;
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("TotalOrders").child(currentDate).child(currentDate+"  "+currentTime);
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
        rootRef.child("DeviceId").setValue(DeviceID);
        rootRef.child("status").setValue("pending");
        rootRef.child("completedtime").setValue("");
    }

    private void requestpermission() {
        String permission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;

            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }

    private void requestOtp(String phonenum) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenum, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressBar.setVisibility(View.GONE);
                codeEnter.setVisibility(View.VISIBLE);
                verificaionid = s;
                token = forceResendingToken;
                new OTP_Reciever().setEditText(codeEnter);
                codeEnter.setText(s);
                Log.e("sheebu", token.toString());

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Intent intent = new Intent(ConfirmActivity.this, OrderCompleted.class);
                startActivity(intent);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(ConfirmActivity.this, "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
        assert s != null;
        topay.setText(s.toString());
    }






}
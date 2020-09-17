package com.example.food_delivering_app.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import static android.Manifest.permission.CALL_PHONE;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivering_app.R;
import com.example.food_delivering_app.adapter.DeliveryboyAdapter;
import com.example.food_delivering_app.adapter.OrderCompletedAdapter;
import com.example.food_delivering_app.adapter.OrderConfirmationAdapter;
import com.example.food_delivering_app.fragments.CustomAdapter;
import com.example.food_delivering_app.fragments.MyDatabaseHelper;
import com.example.food_delivering_app.model.OrderCompletedmodel;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_ADDRESS;
import static com.example.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_COMPLETEDTIME;
import static com.example.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_CONTACT;
import static com.example.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_DATE;
import static com.example.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_DEVICE;
import static com.example.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_ID;
import static com.example.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_ITEMORDERED;
import static com.example.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_NAME;
import static com.example.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_STATUS;
import static com.example.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_TIME;
import static com.example.food_delivering_app.fragments.MyDatabaseHelper.COLUMN_TOPAY;
import static com.example.food_delivering_app.fragments.MyDatabaseHelper.TABLE_NAME;


public class Deliveryboy extends AppCompatActivity {
    public DeliveryboyAdapter adapter;
    RecyclerView recyclerView;
    Button deleteall;
    TextView listsize;
    ArrayList<OrderCompletedmodel> sqlitelist2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveryboy);
        recyclerView=findViewById(R.id.recyclerViewrohit);
        deleteall=findViewById(R.id.deleteall);
        listsize=findViewById(R.id.listsize);
        sqlitelistdata();

        deleteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });

    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(Deliveryboy.this);
                myDB.deleteAllData();
                Intent intent = new Intent(Deliveryboy.this, Deliveryboy.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    public void sqlitelistdata() {


        sqlitelist2.clear();
        sqlitelist2.addAll(storeDataInArrays());
        adapter = new DeliveryboyAdapter(sqlitelist2,getApplicationContext(),Deliveryboy.this);
        recyclerView.setAdapter(adapter);
        Collections.reverse(sqlitelist2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.notifyDataSetChanged();
            listsize.setText(String.valueOf(sqlitelist2.size()));
        }

    public ArrayList<OrderCompletedmodel> storeDataInArrays() {
        ArrayList<OrderCompletedmodel> sqlitelist2 = new ArrayList<>();
        MyDatabaseHelper databaseHelper = MyDatabaseHelper.getInstance(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "no item", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String device = cursor.getString(cursor.getColumnIndex(COLUMN_DEVICE));
                String  address= cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS));
                String contactno = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT));
                String itemsordered = cursor.getString(cursor.getColumnIndex(COLUMN_ITEMORDERED));
                String status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS));
                String topay = cursor.getString(cursor.getColumnIndex(COLUMN_TOPAY));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                String completedtime = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLETEDTIME));
                sqlitelist2.add(new OrderCompletedmodel(id,device,name,address,contactno ,  itemsordered,  status,  topay,  date,  time,  completedtime));

            }
        }
        return sqlitelist2;
    }
}
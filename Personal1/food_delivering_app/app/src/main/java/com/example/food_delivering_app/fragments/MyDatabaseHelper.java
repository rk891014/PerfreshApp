package com.example.food_delivering_app.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static MyDatabaseHelper databaseHelper;
    private Context context;
    public static final String DATABASE_NAME = "AkkuMMM";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "orders";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_PRODUCTQUANTITY = "productquantity";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_QUANTITY = "quantity";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    public static synchronized MyDatabaseHelper getInstance(Context context){
        if(databaseHelper==null){
            databaseHelper = new MyDatabaseHelper(context);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " integer primary key, " +
                COLUMN_NAME + " TEXT UNIQUE, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_PRODUCTQUANTITY + " TEXT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_QUANTITY + " INTEGER);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addBook(String id, String name, String price,String productquantity,String url, int quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_PRODUCTQUANTITY, productquantity);
        cv.put(COLUMN_URL, url);
        cv.put(COLUMN_QUANTITY, quantity);
        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Toast.makeText(context, "Already Added", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id, String quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_QUANTITY, quantity);

        long result = db.update(TABLE_NAME, cv, "_id="+row_id,null);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

}
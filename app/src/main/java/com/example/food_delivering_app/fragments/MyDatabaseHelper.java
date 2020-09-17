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
    public static final String DATABASE_NAME = "Deliveryyy";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "orders";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DEVICE = "device";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_CONTACT = "contact";
    public static final String COLUMN_ITEMORDERED = "itemorderd";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TOPAY = "topay";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_COMPLETEDTIME = "completedtime";

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
                COLUMN_NAME + " TEXT, " +
                COLUMN_DEVICE + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_CONTACT + " TEXT, " +
                COLUMN_ITEMORDERED + " TEXT, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_TOPAY + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
        COLUMN_COMPLETEDTIME + " TEXT);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addBook(String name,String device,String address, String contactno,String itemsordered, String status, String topay, String date, String time, String completedtime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DEVICE, device);
        cv.put(COLUMN_ADDRESS, address);
        cv.put(COLUMN_CONTACT, contactno);
        cv.put(COLUMN_ITEMORDERED, itemsordered);
        cv.put(COLUMN_STATUS, status);
        cv.put(COLUMN_TOPAY, topay);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_TIME, time);
        cv.put(COLUMN_COMPLETEDTIME, completedtime);
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

    public void updateData(String row_id, String completedtime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STATUS, "Completed");
        cv.put(COLUMN_COMPLETEDTIME, completedtime);

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
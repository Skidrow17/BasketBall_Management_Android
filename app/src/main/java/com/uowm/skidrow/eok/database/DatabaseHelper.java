package com.uowm.skidrow.eok.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Users.db";
    public static final String TABLE_NAME = "user";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "SAFETY";
    public static final String COL_4 = "USERNAME";
    public static final String COL_5 = "PASSWORD";
    public static final String COL_9 = "DB_ID";
    public static final String COL_10 = "N_O_M";
    public static final String COL_11 = "N_O_A";
    public static final String COL_12 = "POLLING_TIME";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME TEXT,PASSWORD TEXT,DB_ID TEXT,N_O_M INTEGER,SAFETY TEXT,N_O_A INTEGER,POLLING_TIME INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String id,String username,String password,String db_id,int n_o_m,String safety,int n_o_a,int polling_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_4,username);
        contentValues.put(COL_5,password);
        contentValues.put(COL_9,db_id);
        contentValues.put(COL_10,n_o_m);
        contentValues.put(COL_2,safety);
        contentValues.put(COL_11,n_o_a);
        contentValues.put(COL_12,polling_time);

        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public boolean updateData(String id,String username,String password,String db_id,int n_o_m,String safety,int n_o_a,int polling_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_4,username);
        contentValues.put(COL_5,password);
        contentValues.put(COL_9,db_id);
        contentValues.put(COL_10,n_o_m);
        contentValues.put(COL_2,safety);
        contentValues.put(COL_11,n_o_a);
        contentValues.put(COL_12,polling_time);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }

    public void clearUserTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }
}
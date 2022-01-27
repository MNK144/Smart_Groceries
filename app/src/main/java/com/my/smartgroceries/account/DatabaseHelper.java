package com.my.smartgroceries.account;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Database.db";

    //Firebase Auth's UID is used for session and account management and referencing
    public static final String TABLE_USER = "table_user";
    public static final String USER_COL_UID = "user_col_uid";
    public static final String DB_NULL = "NULL";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_USER +" ("+USER_COL_UID +" TEXT)");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_USER +" ("+USER_COL_UID +" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public String getUser()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT "+USER_COL_UID+" FROM "+TABLE_USER,null);
        if(cr.getCount()==0)
            return DB_NULL;
        cr.moveToFirst();
        String uid = cr.getString(0);
        cr.close();
        return uid;
    }

    public boolean setUser(String user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_COL_UID,user);
        long res = db.insert(TABLE_USER,null,cv);
        if(res==-1)
            return false;
        else
            return true;
    }
    public void deleteUser()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER,null,null);
    }
}

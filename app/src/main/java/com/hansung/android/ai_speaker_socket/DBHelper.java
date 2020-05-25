package com.hansung.android.ai_speaker_socket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {
    final static String TAG="SQLiteDBTest";


    public DBHelper(Context context) {
        super(context, DBContact.DB_NAME, null, DBContact.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,getClass().getName()+".onCreate()");
        db.execSQL(DBContact.DBcontact.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG,getClass().getName() +".onUpgrade()");
        db.execSQL(DBContact.DBcontact.DELETE_TABLE);
        onCreate(db);
    }


    public long insertPhotoByMethod(String memberName,String image) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContact.DBcontact.MEMBER_NAME, memberName);
        values.put(DBContact.DBcontact.KEY_IMAGE, image);

        return db.insert(DBContact.DBcontact.MEMBER_NAME,null,values);
    }

    public Cursor getAllPhotosByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(DBContact.DBcontact.Member,null,null,null,null,null,null);
    }


}

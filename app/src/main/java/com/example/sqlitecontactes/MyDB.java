package com.example.sqlitecontactes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

public class MyDB {

    public final String TABLE = "Contacte"; // name of table
    public final String ID = "_id";
    public final String NAME = "name";
    public final String NUMBER ="number";
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase database;
    public MyDB(Context context) {
        dbHelper = new MyDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }


    public long createRecords(String id, String name, String number) {
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(NAME, name);
        values.put(NUMBER,number);
        return database.insert(TABLE, null, values);
    }

    public Cursor selectContacts() {
        String[] cols = new String[]{ID, NAME, NUMBER};
        Cursor mCursor = database.query(true, TABLE, cols, null
                , null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    public int getCount() {
        int count = (int) DatabaseUtils.queryNumEntries(database, TABLE);
        return count;
    }

    public boolean deleteContact(String id) {
        return database.delete(TABLE, ID + "=" + id, null) > 0;
    }

    public void updateContact(String id, String number){
        ContentValues values = new ContentValues();
        values.put(NUMBER,number);
        database.update(TABLE,values,ID+"=?",new String[]{id});
    }

    public String getId(int index){
        Cursor c= selectContacts();
        c.move(index-1);
        return String.valueOf(c.getString(0));
    }
}


package com.example.zhangkai.gpstraker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * Created by zhangkai on 2016/2/23.
 */
public class DataBase extends SQLiteOpenHelper{
    public static final String TABLE_NAME = "reportedmissinglocation";
    public static final String COLUMN_NAME_ENTRY_ID = "entryid";
    public static final String COLUMN_NAME_LOCATION = "location";
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE "+ TABLE_NAME +"( " +
        COLUMN_NAME_ENTRY_ID +" INTERGER PRIMARY KEY ASC," +
        COLUMN_NAME_LOCATION + " TEXT );";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS reportedmissinglocation";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/gpstraker.db";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}

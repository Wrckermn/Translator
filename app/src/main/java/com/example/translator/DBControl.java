package com.example.translator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBControl extends SQLiteOpenHelper {
    static abstract class MyColumns implements BaseColumns {
        static final String NamaTabel = "user";
        static final String id = "id";
        static final String spn1 = "spn1";
        static final String spn2 = "spn2";
        static final String kata1 = "kata1";
        static final String kata2 = "kata2";
    }

    private static final String db_name ="tranlator.db";
    private static final int db_version=1;

    private static final String db_create = "CREATE TABLE "
            +MyColumns.NamaTabel+ "("
            +MyColumns.id+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +MyColumns.spn1+ " TEXT NOT NULL, "
            +MyColumns.spn2+ " TEXT NOT NULL, "
            +MyColumns.kata1+ " TEXT NOT NULL, "
            +MyColumns.kata2+ " TEXT NOT NULL );";

    private static final String db_delete = "DROP TABLE IF EXISTS "+MyColumns.NamaTabel;

    public DBControl(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(db_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(db_delete);
        onCreate(db);
    }
}

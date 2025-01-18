package com.example.silakurt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MedicineTracker.db"; // Veritabanı adı
    private static final int DATABASE_VERSION = 2;                   // Versiyon numarası

    private static final String TABLE_NAME = "medicines";            // Tablo adı
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";                // İlaç ismi
    private static final String COLUMN_DOSE = "dose";                // Doz bilgisi
    private static final String COLUMN_TIME = "time";                // Zaman bilgisi

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DOSE + " TEXT, " +
                COLUMN_TIME + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertMedicine(String name, String dose, String time) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_DOSE, dose);
            values.put(COLUMN_TIME, time);
            long result = db.insert(TABLE_NAME, null, values);
            return result != -1;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public Cursor getAllMedicines() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            return cursor;
        } catch (Exception e) {
            e.printStackTrace(); // Hata mesajını yazdırmak için
            return null;
        }
    }
}

package com.example.asm_ad.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ReportDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ASM_CampusExpense_Manager1";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_REPORT = "Report";

    // Report table columns
    public static final String COLUMN_REPORT_ID = "_id_report";
    public static final String COLUMN_REPORT_NAME = "report_name";
    public static final String COLUMN_FEED_BACK = "feed_back"; // Corrected typo: COULMN_FEED_BACK -> COLUMN_FEED_BACK
    public static final String COLUMN_USER_ID = "user_id"; // Foreign Key to users table
    public static final String COLUMN_DATE_REPORT = "date_report";

    // User table information (for foreign key)
    public static final String TABLE_USERS = "users"; // Assuming this is the table name in UserDatabaseHelper
    public static final String USER_COLUMN_ID = "_id"; // Assuming this is the primary key column in UserDatabaseHelper

    public ReportDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Enable foreign key support
        db.execSQL("PRAGMA foreign_keys=ON");

        // Create the Report table
        String CREATE_REPORT_TABLE = "CREATE TABLE " + TABLE_REPORT + "("
                + COLUMN_REPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_REPORT_NAME + " TEXT,"
                + COLUMN_FEED_BACK + " TEXT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_DATE_REPORT + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + USER_COLUMN_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_REPORT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Enable foreign key support
        db.execSQL("PRAGMA foreign_keys=ON");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT);
        // Create tables again
        onCreate(db);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        return db;
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase db = super.getReadableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        return db;
    }
}
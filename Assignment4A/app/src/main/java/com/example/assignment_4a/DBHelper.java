package com.example.assignment_4a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "CollegeDB.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "students";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_COURSE = "course";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_NAME + " TEXT, " +
                COL_COURSE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // CREATE
    public boolean insertStudent(String id, String name, String course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ID, id);
        values.put(COL_NAME, name);
        values.put(COL_COURSE, course);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    // READ
    public Cursor getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // UPDATE
    public boolean updateStudent(String id, String name, String course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (!name.isEmpty()) {
            values.put(COL_NAME, name);
        }
        if (!course.isEmpty()) {
            values.put(COL_COURSE, course);
        }

        if (values.size() == 0) {
            return false;
        }

        int rowsAffected = db.update(TABLE_NAME, values, COL_ID + "=?", new String[]{id});
        return rowsAffected > 0;
    }

    // DELETE
    public boolean deleteStudent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME, COL_ID + "=?", new String[]{id});
        return rowsDeleted > 0;
    }
}
package com.example.rakshalink;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RakshaLink.db";
    private static final int DATABASE_VERSION = 2; // Incremented version to add users table

    // Table: Hospitals
    public static final String TABLE_HOSPITALS = "hospitals";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_AREA = "area";
    public static final String COL_PHONE = "phone";
    public static final String COL_ICU = "icu_available";
    public static final String COL_RATING = "rating";
    public static final String COL_DISTANCE = "distance_km";

    // Table: Users
    public static final String TABLE_USERS = "users";
    public static final String COL_U_ID = "user_id";
    public static final String COL_U_NAME = "full_name";
    public static final String COL_U_EMAIL = "email";
    public static final String COL_U_PHONE = "phone";
    public static final String COL_U_PASSWORD = "password";
    public static final String COL_U_ROLE = "role"; // "User" or "Admin"

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Hospitals Table
        String createHospitals = "CREATE TABLE " + TABLE_HOSPITALS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_AREA + " TEXT NOT NULL, "
                + COL_PHONE + " TEXT NOT NULL, "
                + COL_ICU + " INTEGER DEFAULT 0, "
                + COL_RATING + " REAL DEFAULT 0.0, "
                + COL_DISTANCE + " REAL DEFAULT 0.0);";
        db.execSQL(createHospitals);

        // Create Users Table
        String createUsers = "CREATE TABLE " + TABLE_USERS + " ("
                + COL_U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_U_NAME + " TEXT NOT NULL, "
                + COL_U_EMAIL + " TEXT UNIQUE NOT NULL, "
                + COL_U_PHONE + " TEXT NOT NULL, "
                + COL_U_PASSWORD + " TEXT NOT NULL, "
                + COL_U_ROLE + " TEXT NOT NULL);";
        db.execSQL(createUsers);

        seedFiftyMumbaiHospitals(db);
        seedDefaultAccounts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOSPITALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void seedDefaultAccounts(SQLiteDatabase db) {
        // Pre-create an admin and a user so you can log in immediately
        registerUserInternal(db, "System Admin", "admin@rakshalink.com", "9820011223", "admin123", "Admin");
        registerUserInternal(db, "Rahul Sharma", "rahul@gmail.com", "9876543210", "user123", "User");
    }

    private void registerUserInternal(SQLiteDatabase db, String name, String email, String phone, String password, String role) {
        ContentValues cv = new ContentValues();
        cv.put(COL_U_NAME, name);
        cv.put(COL_U_EMAIL, email.toLowerCase().trim());
        cv.put(COL_U_PHONE, phone);
        cv.put(COL_U_PASSWORD, password);
        cv.put(COL_U_ROLE, role);
        db.insert(TABLE_USERS, null, cv);
    }

    // --- USER AUTHENTICATION METHODS ---

    public boolean registerUser(String name, String email, String phone, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_U_NAME, name);
        cv.put(COL_U_EMAIL, email.toLowerCase().trim());
        cv.put(COL_U_PHONE, phone);
        cv.put(COL_U_PASSWORD, password);
        cv.put(COL_U_ROLE, role);

        long result = db.insert(TABLE_USERS, null, cv);
        return result != -1;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_U_ID + " FROM " + TABLE_USERS + " WHERE " + COL_U_EMAIL + " = ?",
                new String[]{email.toLowerCase().trim()});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Returns the role ("Admin" or "User") if password matches, or null if invalid
    public String authenticateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_U_ROLE + " FROM " + TABLE_USERS
                        + " WHERE " + COL_U_EMAIL + " = ? AND " + COL_U_PASSWORD + " = ?",
                new String[]{email.toLowerCase().trim(), password});

        String role = null;
        if (cursor.moveToFirst()) {
            role = cursor.getString(0);
        }
        cursor.close();
        return role;
    }

    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_U_NAME + " FROM " + TABLE_USERS + " WHERE " + COL_U_EMAIL + " = ?",
                new String[]{email.toLowerCase().trim()});
        String name = "User";
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }

    // --- HOSPITAL METHODS ---

    public int getHospitalCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_HOSPITALS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public Cursor getAllHospitals() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_HOSPITALS + " ORDER BY " + COL_DISTANCE + " ASC", null);
    }

    public boolean insertHospital(String name, String area, String phone, int icu, float rating, double distance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_AREA, area);
        cv.put(COL_PHONE, phone);
        cv.put(COL_ICU, icu);
        cv.put(COL_RATING, rating);
        cv.put(COL_DISTANCE, distance);
        long res = db.insert(TABLE_HOSPITALS, null, cv);
        return res != -1;
    }

    private void seedFiftyMumbaiHospitals(SQLiteDatabase db) {
        Object[][] data = {
                {"KEM Hospital", "Parel", "02224107000", 1, 4.6f, 2.5},
                {"Tata Memorial Hospital", "Parel", "02224177000", 1, 4.9f, 2.7},
                {"Global Hospital", "Parel", "02267670101", 1, 4.5f, 2.9},
                {"Wadia Children Hospital", "Parel", "02224197000", 1, 4.3f, 3.0},
                {"Sir H.N. Reliance Foundation Hospital", "Girgaon", "02261305000", 1, 4.8f, 5.2},
                {"Saifee Hospital", "Charni Road", "02267570111", 1, 4.7f, 5.6},
                {"Bombay Hospital", "Marine Lines", "02222067676", 1, 4.5f, 6.4},
                {"Jaslok Hospital", "Pedder Road", "02266573333", 1, 4.6f, 6.8},
                {"Breach Candy Hospital", "Bhulabhai Desai Rd", "02223667788", 1, 4.7f, 7.1},
                {"St. George Hospital", "Fort", "02222620344", 0, 3.8f, 7.8},
                {"G.T. Hospital", "Dhobi Talao", "02222621464", 0, 3.7f, 6.9},
                {"Nair Hospital", "Mumbai Central", "02223027000", 1, 4.2f, 4.1},
                {"Hinduja Hospital", "Mahim", "02224451515", 1, 4.7f, 3.8},
                {"S.L. Raheja Hospital", "Mahim", "02266529999", 1, 4.4f, 4.2},
                {"Lilavati Hospital", "Bandra West", "02226751000", 1, 4.8f, 5.0},
                {"Bhabha Hospital", "Bandra West", "02226422775", 0, 3.6f, 5.4},
                {"Holy Family Hospital", "Bandra West", "02262670555", 1, 4.5f, 5.8},
                {"Guru Nanak Hospital", "Bandra East", "02242227777", 1, 4.1f, 6.1},
                {"Nanavati Max Super Speciality", "Vile Parle West", "02226265000", 1, 4.7f, 8.2},
                {"Criticare Asia Hospital", "Juhu", "02268360000", 1, 4.3f, 9.1},
                {"Arogya Nidhi Hospital", "Juhu", "02226207070", 0, 3.9f, 9.4},
                {"Cooper Hospital", "Juhu", "02226207254", 1, 4.0f, 9.5},
                {"Kokilaben Dhirubhai Ambani Hospital", "Andheri West", "02242696969", 1, 4.9f, 10.8},
                {"Criticare Multispeciality", "Andheri West", "02268360000", 1, 4.2f, 10.2},
                {"Bellevue Multispeciality Hospital", "Andheri West", "02266860000", 0, 4.0f, 10.5},
                {"SevenHills Hospital", "Andheri East", "02267676767", 1, 4.3f, 11.5},
                {"Holy Spirit Hospital", "Andheri East", "02228248500", 1, 4.4f, 12.0},
                {"Sanjeevani Hospital", "Malad East", "02228893895", 0, 3.9f, 14.8},
                {"Suchak Hospital", "Malad East", "02228892408", 0, 4.1f, 15.1},
                {"Thunga Hospital", "Malad West", "02240888888", 1, 4.2f, 15.6},
                {"Zen Multispeciality Hospital", "Malad West", "02228800055", 0, 3.8f, 15.9},
                {"Apex Multispeciality Hospital", "Borivali East", "02262825000", 1, 4.3f, 18.2},
                {"Karuna Hospital", "Borivali West", "02261590200", 1, 4.4f, 19.0},
                {"Bhagwati Municipal Hospital", "Borivali West", "02228932461", 0, 3.5f, 19.5},
                {"Sushrut Hospital", "Chembur", "02225265500", 1, 4.2f, 7.5},
                {"Zen Multispeciality Hospital", "Chembur", "02235100100", 1, 4.5f, 8.0},
                {"Surana Sethia Hospital", "Chembur", "02233783378", 0, 4.0f, 8.3},
                {"Kohinoor Hospital", "Kurla West", "02267556755", 1, 4.3f, 6.8},
                {"Parakh Hospital", "Ghatkopar East", "02261056666", 1, 4.1f, 9.8},
                {"Godrej Memorial Hospital", "Vikhroli East", "02266417000", 1, 4.6f, 11.2},
                {"Dr. L H Hiranandani Hospital", "Powai", "02225763300", 1, 4.8f, 12.6},
                {"Fortis Hospital", "Mulund West", "02267994444", 1, 4.7f, 16.5},
                {"MT Agarwal Municipal Hospital", "Mulund West", "02225605728", 0, 3.6f, 16.9},
                {"Jupiter Hospital", "Thane West", "02221725555", 1, 4.7f, 19.2},
                {"Bethany Hospital", "Thane West", "02221725111", 1, 4.5f, 20.4},
                {"Kaushalya Medical Foundation", "Thane West", "02221723456", 0, 4.0f, 19.8},
                {"Chhatrapati Shivaji Maharaj Hospital", "Kalwa, Thane", "02225372483", 0, 3.7f, 21.0},
                {"Fortis Hiranandani Hospital", "Vashi, Navi Mumbai", "02239199222", 1, 4.6f, 17.5},
                {"MGM New Bombay Hospital", "Vashi, Navi Mumbai", "02261526666", 1, 4.3f, 18.0},
                {"Apollo Hospitals", "CBD Belapur, Navi Mumbai", "02233503350", 1, 4.8f, 24.5}
        };

        db.beginTransaction();
        try {
            for (Object[] row : data) {
                ContentValues cv = new ContentValues();
                cv.put(COL_NAME, (String) row[0]);
                cv.put(COL_AREA, (String) row[1]);
                cv.put(COL_PHONE, (String) row[2]);
                cv.put(COL_ICU, (Integer) row[3]);
                cv.put(COL_RATING, (Float) row[4]);
                cv.put(COL_DISTANCE, (Double) row[5]);
                db.insert(TABLE_HOSPITALS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
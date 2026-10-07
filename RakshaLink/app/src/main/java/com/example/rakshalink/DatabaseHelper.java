package com.example.rakshalink;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RakshaLink.db";
    private static final int DATABASE_VERSION = 3; // Incremented for GPS coordinate schema

    // Table: Hospitals
    public static final String TABLE_HOSPITALS = "hospitals";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_AREA = "area";
    public static final String COL_PHONE = "phone";
    public static final String COL_ICU = "icu_available";
    public static final String COL_RATING = "rating";
    public static final String COL_DISTANCE = "distance_km";
    public static final String COL_LAT = "latitude";
    public static final String COL_LNG = "longitude";

    // Table: Users
    public static final String TABLE_USERS = "users";
    public static final String COL_U_ID = "user_id";
    public static final String COL_U_NAME = "full_name";
    public static final String COL_U_EMAIL = "email";
    public static final String COL_U_PHONE = "phone";
    public static final String COL_U_PASSWORD = "password";
    public static final String COL_U_ROLE = "role";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createHospitals = "CREATE TABLE " + TABLE_HOSPITALS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_AREA + " TEXT NOT NULL, "
                + COL_PHONE + " TEXT NOT NULL, "
                + COL_ICU + " INTEGER DEFAULT 0, "
                + COL_RATING + " REAL DEFAULT 0.0, "
                + COL_DISTANCE + " REAL DEFAULT 0.0, "
                + COL_LAT + " REAL DEFAULT 0.0, "
                + COL_LNG + " REAL DEFAULT 0.0);";
        db.execSQL(createHospitals);

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
        registerUserInternal(db, "System Admin", "admin@rakshalink.com", "9820011223", "admin123", "Admin");
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
        String name = "Citizen";
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }

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
        return db.rawQuery("SELECT * FROM " + TABLE_HOSPITALS, null);
    }

    public boolean insertHospital(String name, String area, String phone, int icu, float rating, double distance, double lat, double lng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_AREA, area);
        cv.put(COL_PHONE, phone);
        cv.put(COL_ICU, icu);
        cv.put(COL_RATING, rating);
        cv.put(COL_DISTANCE, distance);
        cv.put(COL_LAT, lat);
        cv.put(COL_LNG, lng);
        long res = db.insert(TABLE_HOSPITALS, null, cv);
        return res != -1;
    }

    private void seedFiftyMumbaiHospitals(SQLiteDatabase db) {
        // [Name, Area, Phone, ICU, Rating, BenchmarkDist, Lat, Lng]
        Object[][] data = {
                {"KEM Hospital", "Parel", "02224107000", 1, 4.6f, 2.5, 19.0026, 72.8427},
                {"Tata Memorial Hospital", "Parel", "02224177000", 1, 4.9f, 2.7, 19.0048, 72.8432},
                {"Global Hospital", "Parel", "02267670101", 1, 4.5f, 2.9, 18.9968, 72.8385},
                {"Wadia Children Hospital", "Parel", "02224197000", 1, 4.3f, 3.0, 19.0033, 72.8415},
                {"Sir H.N. Reliance Foundation Hospital", "Girgaon", "02261305000", 1, 4.8f, 5.2, 18.9567, 72.8189},
                {"Saifee Hospital", "Charni Road", "02267570111", 1, 4.7f, 5.6, 18.9525, 72.8183},
                {"Bombay Hospital", "Marine Lines", "02222067676", 1, 4.5f, 6.4, 18.9392, 72.8298},
                {"Jaslok Hospital", "Pedder Road", "02266573333", 1, 4.6f, 6.8, 18.9716, 72.8094},
                {"Breach Candy Hospital", "Bhulabhai Desai Rd", "02223667788", 1, 4.7f, 7.1, 18.9734, 72.8055},
                {"St. George Hospital", "Fort", "02222620344", 0, 3.8f, 7.8, 18.9405, 72.8361},
                {"G.T. Hospital", "Dhobi Talao", "02222621464", 0, 3.7f, 6.9, 18.9442, 72.8315},
                {"Nair Hospital", "Mumbai Central", "02223027000", 1, 4.2f, 4.1, 18.9722, 72.8222},
                {"Hinduja Hospital", "Mahim", "02224451515", 1, 4.7f, 3.8, 19.0332, 72.8387},
                {"S.L. Raheja Hospital", "Mahim", "02266529999", 1, 4.4f, 4.2, 19.0436, 72.8465},
                {"Lilavati Hospital", "Bandra West", "02226751000", 1, 4.8f, 5.0, 19.0514, 72.8295},
                {"Bhabha Hospital", "Bandra West", "02226422775", 0, 3.6f, 5.4, 19.0558, 72.8329},
                {"Holy Family Hospital", "Bandra West", "02262670555", 1, 4.5f, 5.8, 19.0583, 72.8275},
                {"Guru Nanak Hospital", "Bandra East", "02242227777", 1, 4.1f, 6.1, 19.0601, 72.8512},
                {"Nanavati Max Super Speciality", "Vile Parle West", "02226265000", 1, 4.7f, 8.2, 19.0968, 72.8407},
                {"Criticare Asia Hospital", "Juhu", "02268360000", 1, 4.3f, 9.1, 19.1042, 72.8299},
                {"Arogya Nidhi Hospital", "Juhu", "02226207070", 0, 3.9f, 9.4, 19.1098, 72.8284},
                {"Cooper Hospital", "Juhu", "02226207254", 1, 4.0f, 9.5, 19.1085, 72.8361},
                {"Kokilaben Dhirubhai Ambani Hospital", "Andheri West", "02242696969", 1, 4.9f, 10.8, 19.1311, 72.8252},
                {"Criticare Multispeciality", "Andheri West", "02268360000", 1, 4.2f, 10.2, 19.1245, 72.8315},
                {"Bellevue Multispeciality Hospital", "Andheri West", "02266860000", 0, 4.0f, 10.5, 19.1362, 72.8291},
                {"SevenHills Hospital", "Andheri East", "02267676767", 1, 4.3f, 11.5, 19.1215, 72.8804},
                {"Holy Spirit Hospital", "Andheri East", "02228248500", 1, 4.4f, 12.0, 19.1298, 72.8682},
                {"Sanjeevani Hospital", "Malad East", "02228893895", 0, 3.9f, 14.8, 19.1862, 72.8595},
                {"Suchak Hospital", "Malad East", "02228892408", 0, 4.1f, 15.1, 19.1841, 72.8542},
                {"Thunga Hospital", "Malad West", "02240888888", 1, 4.2f, 15.6, 19.1912, 72.8385},
                {"Zen Multispeciality Hospital", "Malad West", "02228800055", 0, 3.8f, 15.9, 19.1884, 72.8412},
                {"Apex Multispeciality Hospital", "Borivali East", "02262825000", 1, 4.3f, 18.2, 19.2294, 72.8621},
                {"Karuna Hospital", "Borivali West", "02261590200", 1, 4.4f, 19.0, 19.2392, 72.8465},
                {"Bhagwati Municipal Hospital", "Borivali West", "02228932461", 0, 3.5f, 19.5, 19.2312, 72.8498},
                {"Sushrut Hospital", "Chembur", "02225265500", 1, 4.2f, 7.5, 19.0552, 72.8941},
                {"Zen Multispeciality Hospital", "Chembur", "02235100100", 1, 4.5f, 8.0, 19.0581, 72.8995},
                {"Surana Sethia Hospital", "Chembur", "02233783378", 0, 4.0f, 8.3, 19.0521, 72.9012},
                {"Kohinoor Hospital", "Kurla West", "02267556755", 1, 4.3f, 6.8, 19.0684, 72.8795},
                {"Parakh Hospital", "Ghatkopar East", "02261056666", 1, 4.1f, 9.8, 19.0832, 72.9095},
                {"Godrej Memorial Hospital", "Vikhroli East", "02266417000", 1, 4.6f, 11.2, 19.1021, 72.9265},
                {"Dr. L H Hiranandani Hospital", "Powai", "02225763300", 1, 4.8f, 12.6, 19.1172, 72.9126},
                {"Fortis Hospital", "Mulund West", "02267994444", 1, 4.7f, 16.5, 19.1678, 72.9372},
                {"MT Agarwal Municipal Hospital", "Mulund West", "02225605728", 0, 3.6f, 16.9, 19.1721, 72.9452},
                {"Jupiter Hospital", "Thane West", "02221725555", 1, 4.7f, 19.2, 19.2045, 72.9698},
                {"Bethany Hospital", "Thane West", "02221725111", 1, 4.5f, 20.4, 19.2152, 72.9785},
                {"Kaushalya Medical Foundation", "Thane West", "02221723456", 0, 4.0f, 19.8, 19.1985, 72.9721},
                {"Chhatrapati Shivaji Maharaj Hospital", "Kalwa, Thane", "02225372483", 0, 3.7f, 21.0, 19.2001, 73.0012},
                {"Fortis Hiranandani Hospital", "Vashi, Navi Mumbai", "02239199222", 1, 4.6f, 17.5, 19.0715, 72.9985},
                {"MGM New Bombay Hospital", "Vashi, Navi Mumbai", "02261526666", 1, 4.3f, 18.0, 19.0682, 72.9954},
                {"Apollo Hospitals", "CBD Belapur, Navi Mumbai", "02233503350", 1, 4.8f, 24.5, 19.0215, 73.0382}
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
                cv.put(COL_LAT, (Double) row[6]);
                cv.put(COL_LNG, (Double) row[7]);
                db.insert(TABLE_HOSPITALS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}